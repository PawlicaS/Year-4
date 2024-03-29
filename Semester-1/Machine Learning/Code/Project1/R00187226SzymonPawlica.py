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
from statistics import mean

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

    return training_data, training_labels, test_data, test_labels, training_positive_count, training_negative_count


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
        positive_word_occurrences[word] = (positive_reviews[word] + alpha) / (
                    positive_count + (alpha * len(positive_reviews)))

    for word in negative_reviews:
        negative_word_occurrences[word] = (negative_reviews[word] + alpha) / (
                    negative_count + (alpha * len(negative_reviews)))

    prior_positive = positive_count / total_count
    negative_chance = negative_count / total_count

    return positive_word_occurrences, prior_positive, negative_word_occurrences, negative_chance


def likelihood_classification(review, positive_word_occurrences, prior_positive, negative_word_occurrences,
                              prior_negative):
    # Based on the input 'review' calculate the chances of it being a positive or negative review
    positive = 0
    negative = 0

    for word in review:
        if word in positive_word_occurrences:
            positive += log(positive_word_occurrences[word])

        if word in negative_word_occurrences:
            negative += log(negative_word_occurrences[word])

    if exp(positive) - exp(negative) > prior_negative - prior_positive:
        prediction = 1
    else:
        prediction = 0

    return prediction


def evaluation_of_results(min_word_occurrence):
    training_data, training_labels, test_data, test_labels, positive_count, negative_count = split_reviews()

    accuracy_means = []
    splits_number = 6
    kf = model_selection.StratifiedKFold(n_splits=splits_number, shuffle=True)

    for k in range(1, 11):
        accuracies = []
        split = 0

        for train_index, test_index in kf.split(training_data, training_labels):
            split += 1
            prediction = []

            # Train the classifier, running Tasks 2-4
            words = count_words(training_data.iloc[train_index], k, min_word_occurrence)
            positive_reviews, negative_reviews = count_frequencies(
                (training_data.to_frame().join(training_labels)).iloc[train_index], words)
            positive_word_occurrences, prior_positive, negative_word_occurrences, prior_negative = calculate_feature_likelihoods(
                positive_reviews, positive_count, negative_reviews, negative_count)

            # Find the likelihood that the review is either negative or positive
            for review in training_data.iloc[test_index]:
                prediction.append(likelihood_classification(review, positive_word_occurrences, prior_positive,
                                                            negative_word_occurrences, prior_negative))

            accuracy = metrics.accuracy_score(training_labels.iloc[test_index], prediction)
            accuracies.append(accuracy)
            print(f"Split {split} - Using minimum word length of {k} the accuracy is: {accuracy:.4f}")
        accuracy_mean = mean(accuracies)
        accuracy_means.append(accuracy_mean)
        print(f"The mean accuracy for this split is {accuracy_mean:.4f}\n")
    highest_accuracy_word_length = accuracy_means.index(max(accuracy_means)) + 1
    print(
        f"Highest accuracy minimum word length: {highest_accuracy_word_length}, with a mean accuracy of: {max(accuracy_means):.4f}")
    print()

    prediction = []
    words = count_words(training_data, highest_accuracy_word_length, min_word_occurrence)
    positive_reviews, negative_reviews = count_frequencies((training_data.to_frame().join(training_labels)), words)
    positive_word_occurrences, prior_positive, negative_word_occurrences, prior_negative = calculate_feature_likelihoods(
        positive_reviews, positive_count, negative_reviews, negative_count)

    for review in test_data:
        prediction.append(
            likelihood_classification(review, positive_word_occurrences, prior_positive, negative_word_occurrences,
                                      prior_negative))

    confusion = metrics.confusion_matrix(test_labels, prediction)
    print(f"True Positive: {(confusion[0, 0] / np.sum(confusion)) * 100:.4f}")
    print(f"False Positive: {(confusion[0, 1] / np.sum(confusion)) * 100:.4f}")
    print(f"True Negative: {(confusion[1, 1] / np.sum(confusion)) * 100:.4f}")
    print(f"False Negative: {(confusion[1, 0] / np.sum(confusion)) * 100:.4f}")
    print()

    accuracy = metrics.accuracy_score(test_labels, prediction)
    print(f"Accuracy score: {accuracy:.4f}")


def main():
    # while True:
    #     min_word_length = input("What is the minimum length of a word: ")
    #     if min_word_length.isnumeric():
    #         if int(min_word_length) > 0:
    #             min_word_length = int(min_word_length)
    #             break

    while True:
        min_word_occurrence = input("What is the minimum word occurrence: ")
        if min_word_occurrence.isnumeric():
            if int(min_word_occurrence) > 0:
                min_word_occurrence = int(min_word_occurrence)
                break

    # review = input("Input a review here:\n")
    # review = re.sub(r"[^a-zA-Z0-9\s]", "", review).lower().split(" ")

    evaluation_of_results(min_word_occurrence)


main()
