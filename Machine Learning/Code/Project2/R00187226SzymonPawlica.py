"""
Author: Szymon Pawlica

Title: Machine Learning Assignment 2: Supervised Learning
"""

from sklearn import neighbors, svm, tree, metrics, linear_model, model_selection
from statistics import mean
import time
import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import random

dataset = pd.read_csv("C:/Users/szymo/Documents/College/Year-4/Machine Learning/Code/Project2/product_images.csv")


def preprocessing_and_visualisation():
    # Get the labels and values of the dataset
    labels = dataset['label'].to_numpy()
    values = dataset.drop('label', axis=1).to_numpy()

    # Count the amount of sneakers / ankle boots
    sneakers = (labels == 0).sum()
    ankle_boots = (labels == 1).sum()
    print(f"There are {sneakers} samples of sneakers.")
    print(f"There are {ankle_boots} samples of ankle boots.")

    # Plot a random sneaker
    plt.figure()
    plt.title("Sneaker")
    index = random.choice(np.where(labels == 0)[0])
    plt.imshow(values[index].reshape(28, 28))

    # Plot a random ankle boot
    plt.figure()
    plt.title("Ankle Boot")
    index = random.choice(np.where(labels == 1)[0])
    plt.imshow(values[index].reshape(28, 28))

    return values, labels


def evaluation_procedure(data, target):
    # Use different samples sizes as % of full dataset
    sizes = [.1, .2], [.2, .4], [.3, .6], [.33, .67]
    classifiers = {"perceptron": {},
                   "SVM": {},
                   "k_nearest_neighbour": {},
                   "decision_trees": {}
                   }
    plt_titles = []
    plt_train_times = []
    plt_predict_times = []

    # Run for each classifier
    for classifier in classifiers:
        # Place to hold sample sizes
        classifiers[classifier]['samples'] = {}

        # Run for each sample size
        for test, train in sizes:
            classifier_accuracies, classifier_train_times, classifier_prediction_times = [], [], []
            train_data, test_data, train_target, test_target = model_selection.train_test_split(
                data, target, test_size=test, train_size=train)
            sample_size = round((test + train) * 100)

            # Place to hold accuracies and times for current sample size
            classifiers[classifier]['samples'][sample_size] = {}
            print(f"\n> Using {sample_size:.1f}% of Samples <")
            kf = model_selection.KFold(n_splits=3, shuffle=True)

            for k in range(1, 2):
                print(f"Classifier {classifier}: K {k}")
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

                    # Get training time
                    training_tic = time.time()
                    clf.fit(train_data[train_index], train_target[train_index])
                    training_toc = time.time()
                    training_time = training_toc - training_tic
                    training_times.append(training_time)

                    # Get prediction time
                    prediction_tic = time.time()
                    prediction = clf.predict(test_data)
                    prediction_toc = time.time()
                    prediction_time = prediction_toc - prediction_tic
                    prediction_times.append(prediction_time)

                    print(f"Split {split}: Processing Time required for Training {training_time:.4f}s")
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
                mean_ttime = mean(training_times)
                print(f"Minimum Processing Time Required for Training {min(training_times):.4f}s")
                print(f"Maximum Processing Time Required for Training {max(training_times):.4f}s")
                print(f"Average Processing Time Required for Training {mean_ttime:.4f}s")
                classifier_train_times.append(mean_ttime)
                mean_ptime = mean(prediction_times)
                print(f"Minimum Processing Time Required for Prediction {min(prediction_times):.4f}s")
                print(f"Maximum Processing Time Required for Prediction {max(prediction_times):.4f}s")
                print(f"Average Processing Time Required for Prediction {mean_ptime:.4f}s")
                classifier_prediction_times.append(mean_ptime)

            # Find which K has the most accurate mean prediction
            best_k = classifier_accuracies.index(max(classifier_accuracies)) + 1
            print(f"Best K = {best_k}")

            # Find average accuracy of all K's
            print(f"Average Accuracy Score of All K's - {mean(classifier_accuracies):.4f}")

            # Add the mean accuracy, training and prediction times to current sample size in dictionary
            classifiers[classifier]['samples'][sample_size]['accuracy'] = mean(classifier_accuracies)
            classifiers[classifier]['samples'][sample_size]['train_time'] = mean(classifier_train_times)
            classifiers[classifier]['samples'][sample_size]['predict_time'] = mean(classifier_prediction_times)

            # Add data to use for plotting the sample size > runtime relationship chart
            plt_titles.append(classifier[0].upper() + " " + str(sample_size) + "%")
            plt_train_times.append(classifiers[classifier]['samples'][sample_size]['train_time'])
            plt_predict_times.append(classifiers[classifier]['samples'][sample_size]['predict_time'])

    plt.figure()
    plt.title("Classifier sample-size: runtime relationship")
    width = 0.25
    br1 = np.arange(len(plt_titles))
    br2 = [x + width for x in br1]
    plt.bar(br1, plt_train_times, width=width, label='Train Time')
    plt.bar(br2, plt_predict_times, width=width, label='Predict Time')
    plt.xticks([r + width for r in range(len(plt_titles))], plt_titles)
    plt.legend()
    plt.xlabel("Sample Size (use Initial of Each Classifier)")
    plt.ylabel("Runtime (in Seconds)")


def main():
    data, target = preprocessing_and_visualisation()
    evaluation_procedure(data, target)
    plt.show()


main()
