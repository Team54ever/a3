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

# -------------------------- getSRCC() ---------------------------------------
def getSRCC(doc1, doc2):
    sum = 0
    for key in doc1:
        val = doc1[key] - doc2[key]
        val *= val
        sum += val

    result = 1 - ( (6*sum)/(len(doc1)*(len(doc1)*len(doc1) - 1)) )

    return result

# -----------------------------------------------------------------------
# --------------------------- main --------------------------------------
#usage()

#file1 = sys.argv[1]
#file2 = sys.argv[2]

file1 = "/Users/paul.yuan/Desktop/outputfile_custom.txt"
file2 = "/Users/paul.yuan/Desktop/outputfile.txt"
SRCC_list = []

query_id = 'to_make_life_easier'
is_first_line = 1

for line1 in file1.readlines():

    new_line1 = line1.split()

    if not line1.startswith(query_id):

        if is_first_line == 0:

            doc2 = {}
            for line2 in file2.readlines():
                if line2.startswith(query_id):
                    new_line2 = line2.split()
                    doc2[new_line2[2]] = new_line2[3]

            SRCC_list.append(getSRCC(doc1, doc2))

            query_id = new_line1[0]
            doc1[new_line1[2]] = new_line1[3]

        if is_first_line == 1:

            is_first_line = 0

            doc1 = {}
            query_id = new_line1[0]
            doc1[new_line1[2]] = new_line1[3]

    else:

        doc1[new_line1[2]] = new_line1[3]


print ("SRCC: " + str( sum(SRCC_list)/len(SRCC_list) ))

