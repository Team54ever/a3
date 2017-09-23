import os
import sys
import numpy

# ----------- global variables -----------------------
usageMsg = '''

Usage: Score.py [runfile1] [runfile2] 

'''


# -------------------------- usage() ---------------------------------------
def usage():
    if len(sys.argv) != 3 or sys.argv[1] == "-h":
        print(usageMsg)
        exit(0)

# -------------------------- usage() ---------------------------------------
def readrunfile(doc1, doc2, file1, file2):
    file1 = sys.argv[1]
    file2 = sys.argv[2]

    for line in file1.readlines():
        new_line = line.split()


# -----------------------------------------------------------------------
# --------------------------- main --------------------------------------
