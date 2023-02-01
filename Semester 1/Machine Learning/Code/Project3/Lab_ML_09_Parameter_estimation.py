import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D


def num_coefficients_2(d):
    t = 0
    for n in range(d + 1):
        for i in range(n + 1):
            for j in range(n + 1):
                if i + j == n:
                    t = t + 1
    return t


def calculate_model_function(deg, data, p):
    result = np.zeros(data.shape[0])
    k = 0
    for n in range(deg + 1):
        for i in range(n + 1):
            result += p[k] * (data[:, 0] ** i) * (data[:, 1] ** (n - i))
            k += 1
    return result


def linearize(deg, data, p0):
    f0 = calculate_model_function(deg, data, p0)
    J = np.zeros((len(f0), len(p0)))
    epsilon = 1e-6
    for i in range(len(p0)):
        p0[i] += epsilon
        fi = calculate_model_function(deg, data, p0)
        p0[i] -= epsilon
        di = (fi - f0) / epsilon
        J[:, i] = di
    return f0, J


def calculate_update(y, f0, J):
    l = 1e-2
    N = np.matmul(J.T, J) + l * np.eye(J.shape[1])
    r = y - f0
    n = np.matmul(J.T, r)
    dp = np.linalg.solve(N, n)
    return dp


def main():
    bike = pd.read_csv("day.csv")
    data = np.array(bike[["temp", "windspeed"]])
    target = np.array(bike["casual"])

    plt.close("all")

    max_iter = 10
    for deg in range(5):
        p0 = np.zeros(num_coefficients_2(deg))
        for i in range(max_iter):
            f0, J = linearize(deg, data, p0)
            dp = calculate_update(target, f0, J)
            print(dp)
            p0 += dp

        x, y = np.meshgrid(np.arange(np.min(data[:, 0]), np.max(data[:, 0]), 0.1),
                           np.arange(np.min(data[:, 1]), np.max(data[:, 1]), 0.1))
        test_data = np.array([x.flatten(), y.flatten()]).transpose()
        test_target = calculate_model_function(deg, test_data, p0)

        fig = plt.figure()
        ax = fig.add_subplot(111, projection='3d')
        ax.scatter(data[:, 0], data[:, 1], target, c='r')
        ax.plot_surface(x, y, test_target.reshape(x.shape))

    plt.show()

main()
