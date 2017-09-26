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
    tmp = 100
    for key in doc1:
        #print(doc1[key])
        #print(doc2[key])
        if key in doc2:
            val = int(doc1[key]) - int(doc2[key])
        else:
            tmp += 1
            val = tmp - int(doc1[key])
        val *= val
        sum += val

    if tmp > 100 :
        rank = 0
        for key in doc2:
            if key not in doc1:
                rank += int(doc2[key])
        tmp_sum = 0
        while tmp > 100 :
            tmp_sum += tmp
            tmp -= 1

        sum += tmp_sum - rank

    #print ("?1 " + str(len(doc1)))

    if len(doc1) == 1 :
        result = 1
    else :
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

runfile1 = open(file1)

for line1 in runfile1.readlines():

    new_line1 = line1.split()

    if not line1.startswith(query_id):

        if is_first_line == 0:

            doc2 = {}
            runfile2 = open(file2)

            for line2 in runfile2.readlines():
                if line2.startswith(query_id):
                    new_line2 = line2.split()
                    doc2[new_line2[2]] = new_line2[3]

            runfile2.close()

            SRCC_list.append(getSRCC(doc1, doc2))
            #getSRCC(doc1, doc2)

            #print(query_id)
            #print(doc1)
            #print(doc2)

            doc1 = {}

            query_id = new_line1[0]
            doc1[new_line1[2]] = new_line1[3]

        if is_first_line == 1:

            is_first_line = 0

            doc1 = {}
            query_id = new_line1[0]
            doc1[new_line1[2]] = new_line1[3]

    else:

        #print(new_line1[2])
        #print(new_line1[3])
        doc1[new_line1[2]] = new_line1[3]


runfile1.close()

#print(SRCC_list)
#print(sum(SRCC_list))
#print(len(SRCC_list))
print ("SRCC: " + str( float(sum(SRCC_list))/len(SRCC_list) ))

