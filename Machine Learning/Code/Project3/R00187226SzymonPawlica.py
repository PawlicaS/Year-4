"""
Author: Szymon Pawlica

Title: Machine Learning Assignment 3: Regression & optimisation
"""

import pandas as pd
import numpy as np
from matplotlib import pyplot as plt
from sklearn import model_selection

dataset = pd.read_csv("energy_performance.csv")


def input_data():
    # Get the labels and values of the dataset
    features = dataset.drop(['Heating load', 'Cooling load'], axis=1).to_numpy()
    targets = dataset[['Heating load', 'Cooling load']].to_numpy()

    # Find the minimum and maximum heating and cooling loads
    min_heating = targets.min(0)[0]
    max_heating = targets.max(0)[0]
    min_cooling = targets.min(0)[1]
    max_cooling = targets.max(0)[1]
    print(f"Minimum heating {min_heating}.")
    print(f"Maximum heating {max_heating}.")
    print(f"Minimum cooling {min_cooling}.")
    print(f"Maximum cooling {max_cooling}.")

    return features, targets


def num_coefficients_8(d):
    t = 0
    for n in range(d + 1):
        for i in range(n + 1):
            for j in range(n + 1):
                for k in range(n + 1):
                    for l in range(n + 1):
                        for m in range(n + 1):
                            for o in range(n + 1):
                                for p in range(n + 1):
                                    for q in range(n + 1):
                                        if i + j + k + l + m + o + p + q == n:
                                            t += 1
    return t


def calculate_model_function(deg, data, p):
    result = np.zeros(data.shape[0])
    t = 0
    for n in range(deg + 1):
        for i in range(n + 1):
            for j in range(n + 1):
                for k in range(n + 1):
                    for l in range(n + 1):
                        for m in range(n + 1):
                            for o in range(n + 1):
                                for q in range(n + 1):
                                    for r in range(n + 1):
                                        if i + j + k + l + m + o + q + r == n:
                                            result += p[t] * (
                                                     data[:, 0] ** i) * (
                                                     data[:, 1] ** j) * (
                                                     data[:, 2] ** k) * (
                                                     data[:, 3] ** l) * (
                                                     data[:, 4] ** m) * (
                                                     data[:, 5] ** o) * (
                                                     data[:, 6] ** q) * (
                                                     data[:, 7] ** r)
                                            t += 1
    return result


def linearize(deg, data, p0):

    # Calculate the model function at linearization point p0
    f0 = calculate_model_function(deg, data, p0)
    J = np.zeros((len(f0), len(p0)))
    epsilon = 1e-6
    for i in range(len(p0)):
        p0[i] += epsilon

        # Calculate the model function at linearization point p0 after adding small perturbation
        fi = calculate_model_function(deg, data, p0)
        p0[i] -= epsilon

        # The element of the Jacobian Matrix is the difference (with - without perturbation) divided by the perturbation
        di = (fi - f0) / epsilon
        J[:, i] = di
    return f0, J


def calculate_update(y, f0, J):
    l = 1e-2

    # Regularisation happens here, it's calculated by adding a small parameter update "l",
    # it slows down convergence and ensures matrix N can be inverted
    N = np.matmul(J.T, J) + l * np.eye(J.shape[1])

    # Residual vector is the target vector and the model function output
    r = y - f0
    n = np.matmul(J.T, r)
    dp = np.linalg.solve(N, n)
    return dp


def regression(deg, data, target):
    max_iter = 10

    # Parameter vector
    p0 = np.zeros(num_coefficients_8(deg))

    # The parameter vector is updated
    for i in range(max_iter):
        f0, J = linearize(deg, data, p0)
        dp = calculate_update(target, f0, J)
        p0 += dp

    return p0


def main():
    features, targets = input_data()
    splits = 5
    heating_target = targets[:, 0]
    cooling_target = targets[:, 1]

    kf = model_selection.KFold(n_splits=splits, shuffle=True)

    heating_load_absolute_difference_per_degree = []
    cooling_load_absolute_difference_per_degree = []

    for degree_of_polynomial in range(0, 3):
        split = 0

        heating_load_absolute_differences_for_all_folds = []
        cooling_load_absolute_differences_for_all_folds = []
        for train_index, test_index in kf.split(dataset):
            split += 1

            # Get train data
            train_features = features[train_index]
            heating_load_train_target = heating_target[train_index]
            cooling_load_train_target = cooling_target[train_index]

            # Get test data
            test_features = features[test_index]
            heating_load_test_target = heating_target[test_index]
            cooling_load_test_target = cooling_target[test_index]

            p0 = regression(degree_of_polynomial, train_features, heating_load_train_target)
            p1 = regression(degree_of_polynomial, train_features, cooling_load_train_target)

            # Find predicted loads for test data
            heating_load_predicted_testing = calculate_model_function(degree_of_polynomial, test_features, p0)
            cooling_load_predicted_testing = calculate_model_function(degree_of_polynomial, test_features, p1)

            # Find the absolute difference between the predicted load and actual load
            heating_load_absolute_difference = np.absolute(
                heating_load_predicted_testing - heating_load_test_target.astype('float64'))
            absolute_difference_cooling_load = np.absolute(
                cooling_load_predicted_testing - cooling_load_test_target.astype('float64'))

            # Find the absolute difference
            heating_load_absolute_differences_for_all_folds = np.concatenate(
                (heating_load_absolute_differences_for_all_folds, heating_load_absolute_difference), axis=0)
            cooling_load_absolute_differences_for_all_folds = np.concatenate(
                (cooling_load_absolute_differences_for_all_folds, absolute_difference_cooling_load), axis=0)

        # Find the mean absolute difference for each degree
        heating_load_absolute_difference_per_degree.append(np.mean(heating_load_absolute_differences_for_all_folds))
        cooling_load_absolute_difference_per_degree.append(np.mean(cooling_load_absolute_differences_for_all_folds))

    # Find the best degree
    heating_load_best_degree = heating_load_absolute_difference_per_degree.index(
        min(heating_load_absolute_difference_per_degree))
    cooling_load_best_degree = cooling_load_absolute_difference_per_degree.index(
        min(cooling_load_absolute_difference_per_degree))

    print(f"Best Degree for Heating Loads: {heating_load_best_degree}")
    print(f"Best Degree for Cooling Loads: {cooling_load_best_degree}")

    # Find the best prediction for the best degree
    p0 = regression(heating_load_best_degree, features, heating_target)
    p1 = regression(cooling_load_best_degree, features, cooling_target)
    heating_load_best_degree_predicted = calculate_model_function(heating_load_best_degree, features, p0)
    cooling_load_best_degree_predicted = calculate_model_function(cooling_load_best_degree, features, p1)

    print(f"Mean absolute difference between estimated Heating Loads {heating_load_absolute_difference_per_degree}")
    print(f"Mean absolute difference between estimated Cooling Loads {cooling_load_absolute_difference_per_degree}")

    # Plot Actual and Predicted Loads
    plt.figure()
    plt.scatter(heating_load_best_degree_predicted, heating_target, color='orange')
    plt.plot([min(heating_target), max(heating_target)], [min(heating_target), max(heating_target)], color='red')
    plt.xlabel("Predicted Heating Loads")
    plt.ylabel("Actual Heating Loads")

    plt.figure()
    plt.scatter(cooling_load_best_degree_predicted, cooling_target, color='cyan')
    plt.plot([min(cooling_target), max(cooling_target)], [min(cooling_target), max(cooling_target)], color='blue')
    plt.xlabel("Predicted Cooling Loads")
    plt.ylabel("Actual Cooling Loads")

    plt.show()


main()
