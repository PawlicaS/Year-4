import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn import svm

'''
Task 1
'''

text = "C:/Users/szymo/Documents/College/Machine Learning/LabFiles/Day.csv"
cols = ["holiday", "casual", "registered", "temp", "dteday"]


def readCSV():
    df = pd.read_csv(text, usecols=cols)
    holiday = df.loc[df['holiday'] == 1, ['casual', 'registered']]
    work = df.loc[df['holiday'] == 0, ['casual', 'registered']]

    print(
        "Comparison of the average number of casual rentals and registered rentals depending on if it is a holiday or not")
    print(
        f'Work Day - Casual {int(holiday["casual"].mean().round())} - Registered {int(holiday["registered"].mean().round())}')
    print(
        f'Off Day - Casual {int(work["casual"].mean().round())} - Registered {int(work["registered"].mean().round())}\n')

    print("Minimum and maximum temperature in the data set in Celsius")
    print(f'Minimum - {df["temp"].min()}\nMaximum - {df["temp"].max()}\n')

    print("On which days are there more casual than registered renters")
    print(f'{df.query("casual > registered")["dteday"]}\n')
    #
    # ax1 = df.plot.scatter(x="temp",
    #                       y="registered",
    #                       c="red")
    # ax2 = df.plot.scatter(x="temp",
    #                       y="casual",
    #                       c="blue",
    #                       ax=ax1)
    # ax1.legend(["Registered", "Casual"])
    # ax1.set_xlabel("Temperature")
    # ax1.set_ylabel("Rentals")
    #
    # plt.show()


'''
Task 2
'''

text = "C:/Users/szymo/Documents/College/Machine Learning/LabFiles/Titanic.csv"
cols = ["Survived", "Sex", "Fare"]


def readTitanic():
    df = pd.read_csv(text, usecols=cols)

    print("Percentage of passengers that survived")
    print(f'Passengers - {len(df)}\n'
          f'Survived - {len(df[df["Survived"] == 1])}, {len(df[df["Survived"] == 1]) / len(df)}\n'
          f'Died - {len(df[df["Survived"] == 0])}, {len(df[df["Survived"] == 0]) / len(df)}\n')

    print("Percentage of passengers that survived male/female")
    print(f'Male - {len(df[(df["Survived"] == 1) & (df["Sex"] == "male")]) / len(df[df["Sex"] == "male"])}')
    print(f'Female - {len(df[(df["Survived"] == 1) & (df["Sex"] == "female")]) / len(df[df["Sex"] == "female"])}\n')

    print("Average fare paid by survivors compared to non-survivors")
    print(f'Survived Fare - {df.loc[df["Survived"] == 1]["Fare"].mean()}\n'
          f'Died Fare - {df.loc[df["Survived"] == 0]["Fare"].mean()}\n')


'''
Task 3
'''

def generate():
    no_of_clusters = 3
    cluster_mean = np.random.rand(no_of_clusters, 2)

    data = np.array([[]])
    target = np.array([[]], dtype='int')

    points_per_cluster = 100
    sigma = 0.1
    for i in range(no_of_clusters):
        noise = sigma * np.random.randn(points_per_cluster, 2)
        cluster = cluster_mean[i, :] + noise
        data = np.append(data, cluster).reshape((i + 1) * points_per_cluster, 2)
        target = np.append(target, [i] * points_per_cluster)

    plt.figure()
    plt.scatter(data[:, 0], data[:, 1], c=target)
    decision_boundaries(data, target)


def decision_boundaries(data, target):
    clf = svm.SVC()
    clf.fit(data, target)
    x_min = min(data[:, 0])
    x_max = max(data[:, 0])
    y_min = min(data[:, 1])
    y_max = max(data[:, 1])
    granularity = 0.01
    x, y = np.meshgrid(np.arange(x_min, x_max, granularity), np.arange(y_min,
                                                                       y_max, granularity))
    xy = np.array([x.flatten(), y.flatten()]).transpose()
    prediction = clf.predict(xy)
    prediction = prediction.reshape(x.shape)
    plt.figure()
    plt.imshow(prediction, extent=(x_min, x_max, y_min, y_max), alpha=0.4,
               origin="lower")
    plt.scatter(data[:, 0], data[:, 1], c=target)
    plt.show()


# readCSV()
# readTitanic()
generate()
