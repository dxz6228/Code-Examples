"""
file: letterFreq.py
language: python3
author: Denis Zhenilov
description: this program calculates the frequency of occurrence for each letter of the alphabet and prints them out in its descending order. Relies on wordData to work.
"""

from rit_lib import *

import wordData

class ltr(struct):
    """
    A class definition for letter with a slot for a letter and its frequency of occurrence.
    Input: string, integer
    """
    _slots=((str,"letter"),(int,"frq"))

def letterFreq(words):
    """
    A program that creates a dictionary of letters, mapping them to the letter class structures. It then proceeds to use the dictionary to construct a string of letters sorted in the descending order of frequency of occurrence.
    input: dictionary
    output: string
    """
    a=ltr("a",0)
    b=ltr("b",0)
    c=ltr("c",0)
    d=ltr("d",0)
    e=ltr("e",0)
    f=ltr("f",0)
    g=ltr("g",0)
    h=ltr("h",0)
    i=ltr("i",0)
    j=ltr("j",0)
    k=ltr("k",0)
    l=ltr("l",0)
    m=ltr("m",0)
    n=ltr("n",0)
    o=ltr("o",0)
    p=ltr("p",0)
    q=ltr("q",0)
    r=ltr("r",0)
    s=ltr("s",0)
    t=ltr("t",0)
    u=ltr('u',0)
    v=ltr('v',0)
    w=ltr('w',0)
    x=ltr('x',0)
    y=ltr('y',0)
    z=ltr('z',0)
    alphabet={}
    alphabet["a"]=a
    alphabet['b']=b
    alphabet['c']=c
    alphabet['d']=d
    alphabet['e']=e
    alphabet['f']=f
    alphabet['g']=g
    alphabet['h']=h
    alphabet['i']=i
    alphabet['j']=j
    alphabet['k']=k
    alphabet['l']=l
    alphabet['m']=m
    alphabet['n']=n
    alphabet['o']=o
    alphabet['p']=p
    alphabet['q']=q
    alphabet['r']=r
    alphabet['s']=s
    alphabet['t']=t
    alphabet['u']=u
    alphabet['v']=v
    alphabet['w']=w
    alphabet['x']=x
    alphabet['y']=y
    alphabet['z']=z
    for word in words.keys():
        occurence=wordData.totalOccurrences(word,words)
        for letter in word:
            alphabet[letter].frq=alphabet[letter].frq+occurence
    checkMax=""
    maxFreq=0
    sortedAlphabet=""
    checker=0
    while checker<26:
        for let in alphabet.values():
            if let.frq>=maxFreq:
                maxFreq=let.frq
                checkMax=let.letter
        sortedAlphabet=sortedAlphabet+checkMax
        maxFreq=0
        del alphabet[checkMax]
        checker=checker+1
    return(sortedAlphabet)

def main():
    """
    The "main" function which prompts the user for the name of the word file and prints a string of letters sorted in the decreasing order of frequency.
    """
    wordfile=input("Enter word file: ")
    print("Letters sorted by decreasing frequency:",letterFreq(wordData.readWordFile(wordfile)))

if __name__ == '__main__':
    main()