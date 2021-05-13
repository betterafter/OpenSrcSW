# 11주차 실습 - #7 : wc

import sys

reader = open(sys.argv[2]).readlines()
 
lineCount = 0
wordCount = 0
for line in reader:
    lineCount += 1
    word = line.split()
    wordCount += len(word)


print("라인 수 : ", lineCount)
print("단어 수 : ", wordCount)

