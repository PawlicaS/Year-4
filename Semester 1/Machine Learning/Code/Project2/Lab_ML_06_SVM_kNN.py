from sklearn import datasets
from sklearn import metrics
from sklearn import svm
from sklearn import neighbors
from sklearn import model_selection
import numpy as np
import matplotlib.pyplot as plt


def kernel_function(x,y):
    result = np.zeros((len(x),len(y)))
    for s in range(len(x)):
        for t in range(len(y)):
            result[s,t] = sum([(x[s][i]*y[t][i])**d for i in range(4) for d in range(1,4)])
    return result


def main():
    iris = datasets.load_iris()
    train_data, test_data, train_target, test_target = model_selection.train_test_split(iris.data, iris.target,
                                                                                        test_size=0.2)

    clf = svm.SVC(kernel=kernel_function)
    clf.fit(train_data, train_target)
    prediction = clf.predict(test_data)
    svm_score = metrics.accuracy_score(test_target, prediction)
    print("SVM score = ", svm_score)

    kf = model_selection.KFold()
    knn_scores = []
    for k in range(1,51):
        scores = []
        for train_index,test_index in kf.split(train_data):
            clf = neighbors.KNeighborsClassifier(n_neighbors=k)
            clf.fit(train_data[train_index], train_target[train_index])
            prediction = clf.predict(train_data[test_index])
            score = metrics.accuracy_score(train_target[test_index], prediction)
            scores.append(score)
        knn_scores.append(np.mean(scores))

    plt.plot(np.arange(1,51), knn_scores)

    best_k = np.argmax(knn_scores)+1
    print("best k = ",best_k)

    clf = neighbors.KNeighborsClassifier(n_neighbors=best_k)
    clf.fit(train_data, train_target)
    prediction = clf.predict(test_data)
    knn_score = metrics.accuracy_score(test_target, prediction)
    print("kNN score = ", knn_score)

    plt.show()
    return

main()
