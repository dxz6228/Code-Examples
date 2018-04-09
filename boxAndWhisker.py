"""
file: boxAndWhisker.py
language: python3
author: Denis Zhenilov
description: this program produces a box-and-whisker plot of a given dataset
"""


from turtle import *

def boxAndWhisker(small, q1, med, q3, large):
    """
    This function produces a box-and-whisker plot for the given minimum, first quartile, median, third quartile and maximum values.
    It normalizes those values by dividing them by 100 and proceeds to construct a box-and-whisker plot using turtle commands.
    Input: integers
    """
    setworldcoordinates(-1,-1,1,1)
    setpos(0,0)
    smallNorm=small/100
    q1Norm=q1/100
    medNorm=med/100
    q3Norm=q3/100
    largeNorm=large/100
    up()
    backward((q1Norm-smallNorm))
    down()
    backward((q1Norm-smallNorm))
    left(90)
    forward(0.25/4)
    backward(0.5/4)
    forward(0.25/4)
    right(90)
    forward(q1Norm-smallNorm)
    left(90)
    forward(0.3/2)
    right(90)
    forward(medNorm-q1Norm)
    right(90)
    forward(0.6/2)
    right(90)
    forward(medNorm-q1Norm)
    right(90)
    forward(0.6/2)
    right(90)
    forward(medNorm-q1Norm)
    forward(q3Norm-medNorm)
    right(90)
    forward(0.6/2)
    right(90)
    forward(medNorm-q1Norm)
    forward(q3Norm-medNorm)
    backward(q3Norm-medNorm)
    backward(medNorm-q1Norm)
    right(180)
    left(90)
    forward(0.3/2)
    right(90)
    forward(largeNorm-q3Norm)
    left(90)
    forward(0.25/4)
    backward(0.5/4)
    forward(0.25/4)
    right(90)
    done()

def main():
    """
    This is the "main" function. It prompts the user for the minimum, first quartile, median, third quartile and maximum values and then uses those values to produce the boxAndWhisker plot.
    """
    min=int(input("Enter the minimum: "))
    q1=int(input("Enter the first quartile: "))
    med=int(input("Enter the median: "))
    q3=int(input("Enter the third quartile: "))
    max=int(input("Enter the maximum: "))
    boxAndWhisker(min,q1,med,q3,max)

if __name__ == '__main__':
    main()

