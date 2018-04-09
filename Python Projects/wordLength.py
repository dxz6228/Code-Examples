"""
file: wordLength.py
language: python3
author: Denis Zhenilov
description: this is a program which creates a dataset of word lengths and how often they occur throughout a given year, it then calculates the minimum, maximum, first quartile, median and third quartile values and creates a box-and-whisker plot.
It relies on wordData and boxAndWhisker to function.
"""


import wordData
import boxAndWhisker

def summaryFromWords(words, year):
    """
    This function processes a given dictionary and calculates how often each length occurs throughout a given year. It then proceeds to find the first occuring, last occuring, first quartile, median and third quartile values and constructs a box-and-whisker plot.
    input: dictionary, integer
    output: tuple
    """
    lengthsDic={}
    for wrd in words.keys():
        for years in words[wrd]:
            if years.year==year:
                if not (len(wrd) in lengthsDic):
                    lengthsDic[len(wrd)]=years.count
                else:
                    lengthsDic[len(wrd)]=lengthsDic[len(wrd)]+years.count
    totalLengths=0
    for length in lengthsDic.keys():
        totalLengths=totalLengths+lengthsDic[length]
    medInd=totalLengths//2
    medTracker=0
    lengthHolder=[]
    for length in lengthsDic.keys():
        lengthHolder=lengthHolder+[length]
    lengthHolder.sort()
    for length in lengthHolder:
        medTracker=medTracker+lengthsDic[length]
        if medTracker>=medInd:
                med=length
                break
    q1Tracker=0
    q1Ind=medInd//2
    for length in lengthHolder:
        q1Tracker=q1Tracker+lengthsDic[length]
        if q1Tracker>=q1Ind:
            q1=length
            break
    q3Tracker=0
    q3Ind=medInd+q1Ind
    for length in lengthHolder:
        q3Tracker=q3Tracker+lengthsDic[length]
        if q3Tracker>=q3Ind:
            q3=length
            break
    first=lengthHolder[0]
    last=lengthHolder[-1]
    outputTup=(first,q1,med,q3,last)
    return outputTup

def main():
    """
    This is the "main" function which prompts the user for the word file and a year, and calls summaryFromWords to find out the distribution of the word lengths. It then prints the minimum occuring word length, first quartile, median, third quartile and maximum occurring word length.
    """
    wordfile=input("Enter word file: ")
    year=int(input("Enter year: "))
    ret=summaryFromWords(wordData.readWordFile(wordfile),year)
    print("minimum:",ret[0])
    print("1st quartile:",ret[1])
    print("median:",ret[2])
    print("3rd quartile:",ret[3])
    print("maximum:",ret[4])
    boxAndWhisker.boxAndWhisker(ret[0],ret[1],ret[2],ret[3],ret[4])

if __name__ == '__main__':
    main()
