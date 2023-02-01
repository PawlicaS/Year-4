import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn import metrics
from sklearn import model_selection
from sklearn import neighbors

def cross_validation():
    titanic = pd.read_csv("titanic.csv")

    titanic["Sex"] = titanic["Sex"].map({"male":0,"female":1})    

    data = titanic[["Sex","Pclass"]].to_numpy()
    target = titanic["Survived"].to_numpy()
    
    survivors = len(data[target==1])
    casulties = len(data[target==0])
                
    kf = model_selection.StratifiedKFold(n_splits=min(survivors,casulties), shuffle=True)
    
    ROC_X = np.array([0])
    ROC_Y = np.array([0])
    
    for k in range(1,10):
        true_casulties = []
        true_survivors= []
        false_casulties = []
        false_survivors= []
        for train_index, test_index in kf.split(data, target):
            clf = neighbors.KNeighborsClassifier(n_neighbors=k)
            clf.fit(data[train_index,:], target[train_index])
            predicted_labels = clf.predict(data[test_index,:])
            
            C = metrics.confusion_matrix(target[test_index], predicted_labels)
            
            true_casulties.append(C[0,0])
            true_survivors.append(C[1,1])            
            false_casulties.append(C[1,0])
            false_survivors.append(C[0,1])
        
        print("k =",k)
        print("True casulties:", np.sum(true_casulties))
        print("True survivors:", np.sum(true_survivors))
        print("False casulties:", np.sum(false_casulties))
        print("False survivors:", np.sum(false_survivors))
        print()
                    
        ROC_X = np.append(ROC_X, np.sum(false_survivors))
        ROC_Y = np.append(ROC_Y, np.sum(true_survivors))
            
        
    index = np.argsort(ROC_X)

    print(ROC_X)
    print(ROC_Y)
    print(index)

    plt.close('all')
    plt.figure()
    plt.plot(ROC_X[index],ROC_Y[index])
    plt.axis([0,np.max(ROC_X),0,np.max(ROC_Y)])
    plt.show()
    
    
def main():
    cross_validation()
    
main()
