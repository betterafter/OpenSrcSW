# 11주차 실습 - #6 : cp

import sys

reader = open(sys.argv[2]).readlines()

writer = open(sys.argv[3], 'w')
for line in reader:
    writer.write(line)


