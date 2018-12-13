import os
import subprocess
import sys

# 10 projects per command (to test)
BATCH_SIZE = 100

JAR_FILE = 'collections-explorer-0.0.1-SNAPSHOT-jar-with-dependencies.jar'

DIRECTORY = 'dataset/'

def chunks(l, n):
    """Yield successive n-sized chunks from l."""
    for i in range(0, len(l), n):
        yield l[i:i + n]


if __name__ == '__main__':

    # Current dir
    dirs = os.listdir(DIRECTORY)

    # Sort dirs
    dirs.sort(key=lambda x: int(x.split('_')[0]))

    # Subdivide in batches of size BATCH_SIZE
    batches = chunks(dirs, BATCH_SIZE)

    for idx, b in enumerate(batches):

        cmd = ['java', '-jar', JAR_FILE]

        formatted_dirs = [str(DIRECTORY + i) for i in b]

        cmd.extend(formatted_dirs)
        cmd.extend(['-out', 'batch-%d' % idx])
        cmd.extend(['-filter', '".*List<.*>|.*List|.*Set<.*>|.*Set|.*Map<.*>|.*Map"'])
        cmd = " ".join(cmd)
        print('Executing the command= %s' % cmd)
        #ret = subprocess.call(cmd, shell=True)










