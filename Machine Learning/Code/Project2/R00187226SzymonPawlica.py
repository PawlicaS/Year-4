"""
Author: Szymon Pawlica

Title: Machine Learning Assignment 2: Supervised Learning
"""
from statistics import mean

from sklearn import datasets, neighbors
from sklearn import metrics
from sklearn import linear_model
from sklearn import model_selection
# from time import clock
import matplotlib.pyplot as plt
import pandas as df
import numpy as np
import random

dataset = df.read_csv("product_images.csv")


def preprocessing_and_visualisation():
    labels = dataset['label'].to_numpy()
    values = dataset.drop('label', axis=1).to_numpy()
    sneakers = (labels == 0).sum()
    ankle_boots = (labels == 1).sum()
    print(f"There are {sneakers} samples of sneakers.")
    print(f"There are {ankle_boots} samples of ankle boots.")

    plt.figure()
    index = random.choice(np.where(labels == 0)[0])
    plt.imshow(values[index].reshape(28, 28))

    plt.figure()
    index = random.choice(np.where(labels == 1)[0])
    plt.imshow(values[index].reshape(28, 28))

    # plt.show()
    return values, labels


def evaluation_procedure(data, target):
    train_data, test_data, train_target, test_target = model_selection.train_test_split(data, target,
                                                                                        test_size=0.2)

    kf = model_selection.StratifiedKFold(n_splits=2, shuffle=True)
    for k in range(1, 3):
        accuracies, training_times, prediction_times = [], [], []
        split, prediction = 0, 0
        # training_tic = clock()
        for train_index, test_index in kf.split(train_data, train_target):
            split += 1
            print()
            print(f"K {k}, Split {split}")
            clf = linear_model.Perceptron()
            clf.fit(train_data[train_index], train_target[train_index])
            prediction = clf.predict(test_data)

            # training_toc = clock()
            # prediction_tic = clock()
            # prediction_toc = clock()
            # training_time = training_tic - training_toc
            # print(f"Processing Time required for Training {training_time}")
            # prediction_time = prediction_tic - prediction_toc
            # print(f"Processing Time required for Prediction {prediction_time}")
            accuracy = metrics.accuracy_score(test_target, prediction)
            accuracies.append(accuracy)
            print(f"Accuracy Score {accuracy:.4f}")

        print()
        print(f"K {k}")
        print("Confusion Matrix")
        confusion = metrics.confusion_matrix(test_target, prediction)
        print(f"True Positive: {(confusion[0, 0] / np.sum(confusion)) * 100:.4f}")
        print(f"False Positive: {(confusion[0, 1] / np.sum(confusion)) * 100:.4f}")
        print(f"True Negative: {(confusion[1, 1] / np.sum(confusion)) * 100:.4f}")
        print(f"False Negative: {(confusion[1, 0] / np.sum(confusion)) * 100:.4f}")
        print()
        print(f"Average Accuracy Score {mean(accuracies):.4f}")
        # print(f"Average Processing Time Required for Training {mean(training_times)}")
        # print(f"Average Processing Time Required for Prediction {mean(prediction_times)}")


def main():
    data, target = preprocessing_and_visualisation()
    evaluation_procedure(data, target)


main()
