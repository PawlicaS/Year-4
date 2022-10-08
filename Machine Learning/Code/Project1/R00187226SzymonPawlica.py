"""
Author: Szymon Pawlica

Title: Machine Learning Assignment 1: Bayesian Classification
"""

import pandas as pd
import numpy as np

FILE = "movie_reviews.xlsx"


def split_reviews():
    # Split the dataset into training and testing sets
    df = pd.read_excel(FILE)
    training_split = df.loc[df['Split'] == 'train']
    testing_split = df.loc[df['Split'] == 'test']

    # Get the training and testing - data and labels
    training_data = training_split['Review']
    training_labels = training_split['Sentiment']
    test_data = testing_split['Review']
    test_labels = testing_split['Sentiment']

    # Print positive and negative review counts in training and testing sets
    print(f"Positive reviews in the training set: {training_labels.value_counts()['positive']}")
    print(f"Negative reviews in the training set: {training_labels.value_counts()['negative']}")
    print(f"Positive reviews in the testing set: {test_labels.value_counts()['positive']}")
    print(f"Negative reviews in the testing set: {test_labels.value_counts()['negative']}")

    return training_split, training_data, training_labels, testing_split, test_data, test_labels


def extract_features(training_data, test_data):
    # Get rid of any special characters and convert to lowercase
    training_data = training_data.replace("[^a-zA-Z0-9\s]", "", regex=True).str.lower().str.split()
    return training_data


def count_words(training_data, min_word_length, min_word_occurrence):
    # Find the occurrence of each word
    wordlist = {}
    meet_requirements = []
    for _, row in training_data.items():
        for word in row:
            if len(word) >= min_word_length:
                if word not in wordlist:
                    wordlist[word] = 1
                else:
                    wordlist[word] += 1
    dict = {word: count for (word, count) in wordlist.items() if count > min_word_occurrence}
    for word in dict:
        meet_requirements.append(word)
    return meet_requirements


def count_frequencies(training_split, words):
    # Separate the training split into positive and negative reviews
    positive_review_count = {}
    negative_review_count = {}
    training_split['Review'] = training_split['Review'].replace("[^a-zA-Z0-9\s]", "", regex=True).str.lower()
    positive_sentiment = training_split.loc[training_split['Sentiment'] == 'positive']['Review']
    negative_sentiment = training_split.loc[training_split['Sentiment'] == 'negative']['Review']

    for word in words:
        positive_review_count[word], negative_review_count[word] = 0, 0
        for _, row in positive_sentiment.items():
            if row.count(word) > 0:
                positive_review_count[word] += 1
        for _, row in negative_sentiment.items():
            if row.count(word) > 0:
                negative_review_count[word] += 1

    return positive_review_count, negative_review_count


def main():
    while True:
        min_word_length = input("What is the minimum length of a word: ")
        if min_word_length.isnumeric():
            if int(min_word_length) > 0:
                min_word_length = int(min_word_length)
                break
    while True:
        min_word_occurrence = input("What is the minimum word occurrence: ")
        if min_word_occurrence.isnumeric():
            if int(min_word_length) > 0:
                min_word_occurrence = int(min_word_occurrence)
                break

    training_split, training_data, training_labels, testing_split, test_data, test_labels = split_reviews()
    training_data = extract_features(training_data, test_data)
    words = count_words(training_data, min_word_length, min_word_occurrence)
    count_frequencies(training_split, words)


main()
