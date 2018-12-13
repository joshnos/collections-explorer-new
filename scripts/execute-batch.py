import os
import subprocess
import sys

# 100 projects per command
BATCH_SIZE = 100

def chunks(l, n):
    """Yield successive n-sized chunks from l."""
    for i in range(0, len(l), n):
        yield l[i:i + n]


if __name__ == '__main__':

    # Current dir
    dirs = os.listdir()

    # Sort dirs
    dirs.sort(key=lambda x: int(x.split('_')[0]))

    # Subdivide in batches of size BATCH_SIZE
    batches = chunks(dirs, BATCH_SIZE)

    for idx, b in enumerate(batches):

        cmd = ['java', '-jar', 'collections-explorer.jar']
        cmd.extend(b)
        cmd.append(['-out', 'batch-%d' % idx])
        cmd.append(['-filter', '.*List<.*>|.*List|.*Set<.*>|.*Set.*|.*Map<.*>|.*Map'])

        ret = subprocess.call(cmd, shell=True)










