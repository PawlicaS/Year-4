"""
Author: Szymon Pawlica

Title: Machine Learning Assignment 1: Bayesian Classification
"""

import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import re

from math import log, exp
from sklearn import metrics
from sklearn import model_selection
from sklearn import neighbors

FILE = "movie_reviews.xlsx"
DF = pd.read_excel(FILE)
DF['Sentiment'] = DF['Sentiment'].map({'negative': 0, 'positive': 1})

# Get rid of any special characters and convert to lowercase
DF['Review'] = DF['Review'].replace("[^a-zA-Z0-9\s]", "", regex=True).str.lower().str.split()


def split_reviews():
    # Split the dataset into training and testing sets
    training_split = DF.loc[DF['Split'] == 'train']
    testing_split = DF.loc[DF['Split'] == 'test']

    training_data = training_split['Review']
    training_labels = training_split['Sentiment']
    test_data = testing_split['Review']
    test_labels = testing_split['Sentiment']

    training_positive_count = training_labels.value_counts()[1]
    training_negative_count = training_labels.value_counts()[0]
    print(f"Positive reviews in the training set: {training_positive_count}")
    print(f"Negative reviews in the training set: {training_negative_count}")
    print(f"Positive reviews in the testing set: {test_labels.value_counts()[1]}")
    print(f"Negative reviews in the testing set: {test_labels.value_counts()[0]}")

    return training_split, training_data, training_labels, testing_split, test_data, test_labels, training_positive_count, training_negative_count


def count_words(training_data, min_word_length, min_word_occurrence):
    # Find the occurrence of each word
    wordlist = {}
    meet_requirements = []

    for row in training_data.to_numpy():
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
    positive_sentiment = training_split.loc[training_split['Sentiment'] == 1]['Review']
    negative_sentiment = training_split.loc[training_split['Sentiment'] == 0]['Review']

    # Find in what reviews each word appears
    for word in words:
        positive_review_count[word], negative_review_count[word] = 0, 0

        for _, row in positive_sentiment.items():
            if row.count(word) > 0:
                positive_review_count[word] += 1

        for _, row in negative_sentiment.items():
            if row.count(word) > 0:
                negative_review_count[word] += 1

    return positive_review_count, negative_review_count

def calculate_feature_likelihoods(positive_reviews, positive_count, negative_reviews, negative_count):
    # Find what are the chances of a word occurring in a positive or negative review
    total_count = positive_count + negative_count
    alpha = 1
    positive_word_occurrences = {}
    negative_word_occurrences = {}

    for word in positive_reviews:
        positive_word_occurrences[word] = (positive_reviews[word] + alpha) / (positive_count + (alpha * len(positive_reviews)))

    for word in negative_reviews:
        negative_word_occurrences[word] = (negative_reviews[word] + alpha) / (negative_count + (alpha * len(negative_reviews)))

    prior_positive = positive_count / total_count
    negative_chance = negative_count / total_count

    return positive_word_occurrences, prior_positive, negative_word_occurrences, negative_chance

def likelihood_classification(review, positive_word_occurrences, prior_positive, negative_word_occurrences, prior_negative):
    # Based on the input 'review' calculate the chances of it being a positive or negative review
    positive = 0
    negative = 0

    for word in review:
        if word in positive_word_occurrences:
            positive += log(positive_word_occurrences[word])

        if word in negative_word_occurrences:
            negative += log(negative_word_occurrences[word])

    if exp(positive) - exp(negative) > prior_negative - prior_positive:
        prediction = "Positive"
    else:
        prediction = "Negative"

    print(f"It looks like your review is {prediction}")

def evaluation_of_results(test_split, test_data, test_labels, training_split, training_data, training_labels, positive_word_occurrences, prior_positive, negative_word_occurrences, prior_negative):
    data = DF['Review']
    target = DF['Sentiment']

    train_index = training_labels
    test_index = test_labels

    positive = len(data[target==1])
    negative = len(data[target==0])

    prediction = []
    for review in test_data:
        positive_likelihood = 0
        negative_likelihood = 0

        for word in review:
            if word in positive_word_occurrences:
                positive_likelihood += log(positive_word_occurrences[word])

            if word in negative_word_occurrences:
                negative_likelihood += log(negative_word_occurrences[word])

        if exp(positive_likelihood) - exp(negative_likelihood) > prior_negative - prior_positive:
            prediction.append(1)
        else:
            prediction.append(0)

    confusion = metrics.confusion_matrix(target[test_index], prediction)
    print(confusion)

    accuracy = metrics.accuracy_score(target[test_index], prediction)
    print(accuracy)

    #TODO k-fold
    kf = model_selection.StratifiedKFold(n_splits=min(positive, negative), shuffle=True)

    ROC_X = np.array([0])
    ROC_Y = np.array([0])

    for k in range(1, 10):
        true_negative = []
        true_positive = []
        false_negative = []
        false_positive = []
        for train_index, test_index in kf.split(data, target):
            clf = neighbors.KNeighborsClassifier(n_neighbors=k)
            clf.fit(data[train_index], target[train_index])
            predicted_labels = clf.predict(data[test_index])

            C = metrics.confusion_matrix(target[test_index], predicted_labels)

            true_negative.append(C[0, 0])
            true_positive.append(C[1, 1])
            false_negative.append(C[1, 0])
            false_positive.append(C[0, 1])

        print("k =", k)
        print("True Negative:", np.sum(true_negative))
        print("True Positive:", np.sum(true_positive))
        print("False Negative:", np.sum(false_negative))
        print("False Positive:", np.sum(false_positive))
        print()

        ROC_X = np.append(ROC_X, np.sum(false_positive))
        ROC_Y = np.append(ROC_Y, np.sum(true_positive))

    index = np.argsort(ROC_X)

    print(ROC_X)
    print(ROC_Y)
    print(index)

    plt.close('all')
    plt.figure()
    plt.plot(ROC_X[index], ROC_Y[index])
    plt.axis([0, np.max(ROC_X), 0, np.max(ROC_Y)])
    plt.show()

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

    review = input("Input a review here:\n")
    review = re.sub(r"[^a-zA-Z0-9\s]", "", review).lower().split(" ")

    training_split, training_data, training_labels, testing_split, test_data, test_labels, positive_count, negative_count = split_reviews()
    words = count_words(training_data, min_word_length, min_word_occurrence)
    positive_reviews, negative_reviews = count_frequencies(training_split, words)
    positive_word_occurrences, prior_positive, negative_word_occurrences, prior_negative = calculate_feature_likelihoods(positive_reviews, positive_count, negative_reviews, negative_count)
    likelihood_classification(review, positive_word_occurrences, prior_positive, negative_word_occurrences, prior_negative)
    evaluation_of_results(testing_split, test_data, test_labels, training_split, training_data, training_labels, positive_word_occurrences, prior_positive, negative_word_occurrences, prior_negative)


main()
