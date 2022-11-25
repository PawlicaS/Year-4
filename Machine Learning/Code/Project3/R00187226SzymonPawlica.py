"""
Author: Szymon Pawlica

Title: Machine Learning Assignment 3: Regression & optimisation
"""

import pandas as pd
import numpy as np

dataset = pd.read_csv("C:/Users/szymo/Documents/College/Year-4/Machine Learning/Code/Project3/energy_performance.csv")


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


def polynomial_model_function(degree, features, coefficients):
    print()


def determine_correct_size(degree):
    print()


def main():
    features, targets = input_data()


main()
