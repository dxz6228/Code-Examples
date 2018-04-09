"""
file: printedWords.py
language: python3
author: Denis Zhenilov
description: this program calculates the total amount of printed words per year and proceeds to create a graph plotting the trend for the amount of printed words.
"""


import wordData
from rit_lib import *

def printedWords(words):
    """
    This function creates a dictionary, mapping years to the amount of words in them. It then uses the dictionary to produce a list of YearCount classes.
    input: dictionary
    output: list
    """
    wordsPerYear={}
    for yearData in words.values():
        for year in yearData:
            if not (year.year in wordsPerYear):
                wordsPerYear[year.year]=wordData.YearCount(year.year,year.count)
            else:
                wordsPerYear[year.year].count=wordsPerYear[year.year].count+year.count
    yearlist=[]
    year=1900
    while year<=2008:
        if (year in wordsPerYear):
            yearlist=yearlist+[wordsPerYear[year]]
            year=year+1
        else:
            year=year+1
    return yearlist

def wordsForYear(year, yearlist):
    """
    This function returns the amount of words that were printed in a given year. If the year is not present in the list of years, it returns 0.
    Input: integer, list
    output: integer
    """
    for el in yearlist:
        if el.year==year:
            return el.count
    return 0

def main():
    """
    This is the "main" function which prompts the user for the name of the word file and the year and outputs the total amount of printed words in a given year.
    It then constructs a trend graph, showing the amount of printed words, over the whole period of time.
    """
    wordfile=input("Enter word file: ")
    year=int(input("Enter year: "))
    yrlist=printedWords(wordData.readWordFile(wordfile))
    total=wordsForYear(year,yrlist)
    print("Total printed words in",year,":",total)
    import simplePlot
    labels = 'Year', 'Total Words'
    plot = simplePlot.plot2D('Number of printed words over time', labels)
    for yc in yrlist:
        point = yc.year, yc.count
        plot.addPoint(point)
    plot.display()


if __name__ == '__main__':
    main()