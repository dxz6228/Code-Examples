"""
file: wordData.py
language: python3
author: Denis Zhenilov
description: "WordData" program which defines YearCount and WordTrend classes and maps words to lists of YearCount structures, initially processing the files.
"""

from rit_lib import *

class YearCount(struct):
    """
    This is the class with two slots - for a year and for how many times the word occurred that year.
    input: integers
    """
    _slots=((int,"year"),(int,"count"))

class WordTrend(struct):
    """
    This is the class with two slots - for a word, and for its relative "trend" value - how often it occurred compared to other words.
    input: string, float
    """
    _slots=((str,"word"),(float,"trend"))

def readWordFile(fileName):
    """
    This is the function which processes the data file and creates a dictionary of words, mapping them to the lists of YearCount structures.
    input: string
    output: dictionary
    """
    outp={}
    for line in open('data/'+fileName):
        for el in line.split():
            if el[0]>="a" and el[0]<="z":
                wor=el
                outp[wor]=[]
            else:
                year=""
                yrcon=""
                switch=False
                for symb in el:
                    if symb!=",":
                        if switch==False:
                            year=year+symb
                        elif switch==True:
                            yrcon=yrcon+symb
                    elif symb==",":
                        switch=True
                outp[wor]=outp[wor]+[YearCount(int(year),int(yrcon))]
    return outp

def totalOccurrences(word, words):
    """
    A little function that calculates the total ammount of times a word has occurred throughout the years in a given dataset.
    Input: string, dictionary
    Output: integer
    """
    count=0
    if not (word in words):
        return 0
    else:
        for yc in words[word]:
            count=count+yc.count
        return count

def main():
    """
    A "main" function which prompts the user for the name of the file to be processed and for the word for which user wants to find the total occurrence. It then calls readWordFile to create a dictionary of words and totalOccurrences to calculate the total occurrence of the word;
    it then prints the total occurrence.
    """
    filename=input("Enter word file: ")
    word=input("Enter word: ")
    dict=readWordFile(filename)
    count=totalOccurrences(word,dict)
    print("Total occurrences of",word,":",count)

if __name__ == '__main__':
    main()