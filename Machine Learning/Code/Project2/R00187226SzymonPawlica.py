"""
Author: Szymon Pawlica

Title: Machine Learning Assignment 2: Supervised Learning
"""

from sklearn import neighbors, svm, tree, metrics, linear_model, model_selection
from statistics import mean
import time
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
    plt.title("Sneaker")
    index = random.choice(np.where(labels == 0)[0])
    plt.imshow(values[index].reshape(28, 28))

    plt.figure()
    plt.title("Ankle Boot")
    index = random.choice(np.where(labels == 1)[0])
    plt.imshow(values[index].reshape(28, 28))

    return values, labels


def evaluation_procedure(data, target):
    train_data, test_data, train_target, test_target = model_selection.train_test_split(data, target, test_size=0.33)

    classifiers = ["perceptron", "SVM", "k_nearest_neighbour", "decision_trees"]
    kf = model_selection.KFold(n_splits=5, shuffle=True)
    for classifier in classifiers:
        classifier_accuracies = []
        for k in range(1, 11):
            print(f"\nClassifier {classifier}: K {k}")
            print("-------------------------------------")
            accuracies, training_times, prediction_times = [], [], []
            split, prediction = 0, 0
            for train_index, test_index in kf.split(train_data, train_target):
                split += 1
                if classifier == "perceptron":
                    clf = linear_model.Perceptron()
                elif classifier == "SVM":
                    clf = svm.SVC(kernel='rbf')
                elif classifier == "k_nearest_neighbour":
                    clf = neighbors.KNeighborsClassifier(n_neighbors=k)
                else:
                    clf = tree.DecisionTreeClassifier()

                training_tic = time.time()
                clf.fit(train_data[train_index], train_target[train_index])
                training_toc = time.time()

                prediction_tic = time.time()
                prediction = clf.predict(test_data)
                prediction_toc = time.time()

                training_time = training_toc - training_tic
                training_times.append(training_time)
                print(f"Split {split}: Processing Time required for Training {training_time:.4f}s")
                prediction_time = prediction_toc - prediction_tic
                prediction_times.append(prediction_time)
                print(f"Split {split}: Processing Time required for Prediction {prediction_time:.4f}s")
                accuracy = metrics.accuracy_score(test_target, prediction)
                accuracies.append(accuracy)
                print(f"Split {split}: Accuracy Score - {accuracy:.4f}")

            print()
            print("Confusion Matrix")
            confusion = metrics.confusion_matrix(test_target, prediction)
            print(f"True Positive: {(confusion[0, 0] / np.sum(confusion)) * 100:.4f}")
            print(f"False Positive: {(confusion[0, 1] / np.sum(confusion)) * 100:.4f}")
            print(f"True Negative: {(confusion[1, 1] / np.sum(confusion)) * 100:.4f}")
            print(f"False Negative: {(confusion[1, 0] / np.sum(confusion)) * 100:.4f}")
            print()
            mean_acc = mean(accuracies)
            print(f"Minimum Accuracy Score - {min(accuracies):.4f}")
            print(f"Maximum Accuracy Score - {max(accuracies):.4f}")
            print(f"Average Accuracy Score - {mean_acc:.4f}")
            classifier_accuracies.append(mean_acc)
            print(f"Minimum Processing Time Required for Training {min(training_times):.4f}s")
            print(f"Maximum Processing Time Required for Training {max(training_times):.4f}s")
            print(f"Average Processing Time Required for Training {mean(training_times):.4f}s")
            print(f"Minimum Processing Time Required for Prediction {min(prediction_times):.4f}s")
            print(f"Maximum Processing Time Required for Prediction {max(prediction_times):.4f}s")
            print(f"Average Processing Time Required for Prediction {mean(prediction_times):.4f}s")

        best_k = classifier_accuracies.index(max(classifier_accuracies)) + 1
        print(f"Best K = {best_k}")
        # TODO Plot relationship between input data size and runtime for the optimal classifier
        # Does this even go here?!?!?!?

        print(f"Average Accuracy Score of All K's - {mean(classifier_accuracies):.4f}")


def main():
    data, target = preprocessing_and_visualisation()
    evaluation_procedure(data, target)
    plt.show()


main()
