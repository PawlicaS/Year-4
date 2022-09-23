import numpy as np
import pandas as pd

'''
Task 1
'''

fiblist = []


def fibonacci(fib1, fib2):
    fib3 = fib1 + fib2
    fiblist.append(fib1)
    if len(fiblist) == 40:
        return fiblist
    fibonacci(fib2, fib3)


fibonacci(0, 1)
number = int(input("What number: "))
print(fiblist[number - 1])

'''
Task 2
'''

text = "C:/Users/szymo/Documents/College/Machine Learning/Week 2/Dracula.txt"
wordlist = {}


def readText(minWordLength, minWordOccurence):
    with open(text, 'r') as file:
        for line in file:
            for word in line.replace(",", " ").split():
                if len(word) >= minWordLength:
                    if word not in wordlist:
                        wordlist[word] = 0
                    else:
                        wordlist[word] += 1

    d = {word: count for (word, count) in wordlist.items() if count > minWordOccurence}
    return d


print(readText(5, 300))

'''
Test 3
'''

text = "C:/Users/szymo/Documents/College/Machine Learning/Week 2/Day.csv"
cols = ["weathersit", "cnt"]


def readCSV():
    df = pd.read_csv(text, usecols=cols)
    df = df.loc[df["weathersit"] == 1]
    print(f'Clear Days {len(df.index)} - Bikes {int(df["cnt"].mean().round())}')


readCSV()
