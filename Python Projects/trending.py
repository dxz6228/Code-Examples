"""
file: trending.py
language: python3
author: Denis Zhenilov
description: This program calculates, which words are the most and the least "trending" in a given period of time. It relies on wordData to work.
"""

import wordData

def trending(words, startYr, endYr):
    """
    This function creates a list of trends of the words which occurred 1000 times or more at a given start and end year.
    It then uses that list to produce another, sorted list, which is then passed on to main() to produce the output.
    input: dictionary, integer, integer
    output: list
    """
    trends=[]
    for word in words.keys():
        starter=None
        end=None
        for year in words[word]:
            if year.year==startYr and year.count>=1000:
                starter=year.count
            elif year.year==endYr and year.count>=1000:
                end=year.count
        if starter!=None and end!=None:
            trends=trends+[wordData.WordTrend(word,(end/starter))]
    totalCheck=len(trends)
    sortedTrends=[]
    checker=0
    while checker<totalCheck:
        findMax=trends[0]
        for el in trends:
            if el.trend>findMax.trend:
                findMax=el
        sortedTrends=sortedTrends+[findMax]
        checker=checker+1
        trends.remove(findMax)
    return sortedTrends

def main():
    """
    This is the "main" function that prompts the user for the name of the file, starting and ending year. It then calls trending function to get a list of word trends,
    and prints the top 10 and bottom 10 trending words, or as many as possible, for the given period.
    """
    wordfile=input("Enter word file: ")
    startYr=int(input("Enter starting year: "))
    endYr=int(input("Enter ending year: "))
    trends=trending(wordData.readWordFile(wordfile),startYr,endYr)
    if len(trends)>=10:
        print ("The top 10 trending words from",startYr,"to",endYr,":")
        print(trends[0].word)
        print(trends[1].word)
        print(trends[2].word)
        print(trends[3].word)
        print(trends[4].word)
        print(trends[5].word)
        print(trends[6].word)
        print(trends[7].word)
        print(trends[8].word)
        print(trends[9].word)
        print ("The bottom 10 trending words from",startYr,"to",endYr,":")
        print(trends[-1].word)
        print(trends[-2].word)
        print(trends[-3].word)
        print(trends[-4].word)
        print(trends[-5].word)
        print(trends[-6].word)
        print(trends[-7].word)
        print(trends[-8].word)
        print(trends[-9].word)
        print(trends[-10].word)
    elif len(trends)<10:
        print ("The top trending words from",startYr,"to",endYr,":")
        for el in trends:
            print(el.word)

if __name__ == '__main__':
    main()