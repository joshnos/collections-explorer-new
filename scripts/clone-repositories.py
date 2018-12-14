import pandas as pd
import os
import argparse
import requests
import json
from git import Repo

COUNT = 0

def clone_repository(df, repo_folder, user, pwd):
    global COUNT

    try:

        project_name = df['name']

        print('%d - PROJECT: %s' % (COUNT, project_name))
        print('\t\tAccessing the project %s with url %s' % (project_name, df.url))

        if user:
            response = requests.get(df.url, auth=(user, pwd))
        else:
            response = requests.get(df.url)

        print('\t\tResponse received')

        to_clone = json.loads(response.text)
        clone_url = to_clone['clone_url']
        print('\t\tCloning the project %s with GIT %s' % (project_name, clone_url))

        project_folder = os.path.join(repo_folder, '%d_%s_%s' % (COUNT, df.id, project_name))
        print('\t\tSaving the project into the folder = %s' % project_folder)

        Repo.clone_from(clone_url, project_folder)
        print('Cloned successfully\n')

    except KeyError as e:
        print('KeyError while processing the PROJECT %s' % df['name'])
        print(e)

    COUNT = COUNT + 1
    pass


def read_pandas(file):
    try:
        return pd.read_csv(file, sep=',', names=['id', 'url', 'owner_id', 'name', 'description', 'language',
                                                'created_at', 'forked_from', 'deleted', 'updated_at', 'n_watchers'])
    except Exception as e:
        raise argparse.ArgumentTypeError('not a valid result csv file - %s' % e)


def writable_dir(prospective_dir):
    if not os.path.isdir(prospective_dir):
        try:
            os.makedirs(prospective_dir)
        except:
            argparse.ArgumentTypeError("outputfolder:{0} is not a writable dir".format(prospective_dir))
    return prospective_dir


if __name__ == '__main__':

    parser = argparse.ArgumentParser(description='Clone a list og github repositories.')

    parser.add_argument('input', type=read_pandas,
                        help='A tabbed file exported from the GHTORRENT with the lists of projects to clone.')

    parser.add_argument('output', type=writable_dir, help='The output folder to export the cloned repositories')

    parser.add_argument('--start', type=int, help='Start cloning from the index.')
    parser.add_argument('--end', type=int, help='Finish cloning on this index.')

    parser.add_argument('--user', type=str, help='User to call authenticated requests.')
    parser.add_argument('--pwd', type=str, help='Password for the user. ')

    args = parser.parse_args()

    df = args.input

    COUNT = 0

    # Explicitly ask for None because 0 -> false
    if args.start is not None and args.end is not None:
        df = df[args.start : args.end]
        COUNT = args.start

    user = None
    pwd = None

    if args.user and args.pwd:
        user = args.user
        pwd = args.pwd
        print('Using authentication from user %s' % user)

    df.apply(clone_repository, axis=1, args=(args.output, user, pwd,))

    print('#################################################################')
    print('##################           DONE            ####################')
    print('#################################################################')
