from sklearn import datasets
from sklearn import tree
from sklearn.tree import export_text
from sklearn import model_selection
from sklearn import metrics
import numpy as np
import matplotlib.pyplot as plt

def task1():
    iris = datasets.load_iris()
    print(len(iris.target))

    kf = model_selection.KFold(n_splits=len(iris.target))
    avg_acc_train = []
    avg_acc_test = []
    for d in range(1,6):
        clf = tree.DecisionTreeClassifier(max_depth=d)
        acc_train = []
        acc_test = []
        for train_index,test_index in kf.split(iris.data):
            clf.fit(iris.data[train_index], iris.target[train_index])
            prediction_train = clf.predict(iris.data[train_index])
            prediction_test = clf.predict(iris.data[test_index])
            acc_train.append(metrics.accuracy_score(iris.target[train_index], prediction_train))
            acc_test.append(metrics.accuracy_score(iris.target[test_index], prediction_test))
        avg_acc_train.append(np.mean(acc_train))
        avg_acc_test.append(np.mean(acc_test))

    plt.figure()
    plt.plot(np.arange(len(avg_acc_test))+1, avg_acc_test)
    plt.plot(np.arange(len(avg_acc_train))+1, avg_acc_train)

    best_depth = np.argmax(avg_acc_test)+1
    print("Best depth=",best_depth)

    return


def task2():
    iris = datasets.load_iris()
    for d in range(1,6):
        clf = tree.DecisionTreeClassifier(max_depth=d, random_state=0)
        clf.fit(iris.data, iris.target)

        print("Depth=",d)
        print(export_text(clf, feature_names=iris.feature_names))

        plt.figure()
        tree.plot_tree(clf, feature_names=iris.feature_names,
                       class_names=["Iris-Setosa",
                                    "Iris-Versicolour",
                                    "Iris-Virginica"],
                       filled=True)

    return


def task3():
    iris = datasets.load_iris()

    min_ = np.min(iris.data, axis=0)
    max_ = np.max(iris.data, axis=0)
    granularity = (max_ - min_) / 100
    g2, g3 = np.meshgrid(np.arange(min_[2], max_[2], granularity[2]), np.arange(min_[3], max_[3], granularity[3]))
    g0 = np.ones(g2.shape) * (min_[0] + max_[0]) / 2
    g1 = np.ones(g2.shape) * (min_[1] + max_[1]) / 2
    xy = np.array([g0.flatten(), g1.flatten(), g2.flatten(), g3.flatten()]).transpose()

    for d in range(1,6):
        clf = tree.DecisionTreeClassifier(max_depth=d, random_state=0)
        clf.fit(iris.data, iris.target)
        prediction = clf.predict(xy)
        prediction = prediction.reshape(g2.shape)

        plt.figure()
        plt.imshow(prediction, extent=(min_[2], max_[2], min_[3], max_[3]), alpha=0.4, origin="lower")
        plt.scatter(iris.data[:, 2], iris.data[:, 3], c=iris.target)

    return


def main():
    task1()
    task2()
    task3()
    plt.show()

main()
