import pandas as pd
import numpy as np
import math
from sklearn import metrics

def naive_bayes():
    titanic = pd.read_csv("titanic.csv")
    data = titanic[["Sex","Pclass"]]
    target = titanic["Survived"]

    train_index = np.arange(int(0.8*len(target)))    
    test_index = np.arange(int(0.8*len(target)), len(target))

    total = len(target[train_index])
    survivors = sum(target[train_index]==1)
    casulties = sum(target[train_index]==0)    
    print("Total number of passengers:",total)    
    print("Survivors:",survivors)
    print("Casulties:",casulties)

    prior_survivors = survivors / total
    prior_casulties = casulties / total
    print("Prior survivors:", prior_survivors)
    print("Prior casulties:", prior_casulties)

    male_survivors = sum((data.iloc[train_index]["Sex"]=="male") & (target[train_index]==1))
    female_survivors= sum((data.iloc[train_index]["Sex"]=="female") & (target[train_index]==1))
    male_casulties = sum((data.iloc[train_index]["Sex"]=="male") & (target[train_index]==0))
    female_casulties = sum((data.iloc[train_index]["Sex"]=="female") & (target[train_index]==0))
    print("Male survivors:", male_survivors)
    print("Female survivors:", female_survivors)
    print("Male casulties:", male_casulties)
    print("Female casulties:", female_casulties)

    class1_survivors = sum((data.iloc[train_index]["Pclass"]==1) & (target[train_index]==1))
    class2_survivors= sum((data.iloc[train_index]["Pclass"]==2) & (target[train_index]==1))
    class3_survivors= sum((data.iloc[train_index]["Pclass"]==3) & (target[train_index]==1))
    class1_casulties = sum((data.iloc[train_index]["Pclass"]==1) & (target[train_index]==0))
    class2_casulties = sum((data.iloc[train_index]["Pclass"]==2) & (target[train_index]==0))
    class3_casulties = sum((data.iloc[train_index]["Pclass"]==3) & (target[train_index]==0))
    print("Class 1 survivors:", class1_survivors)
    print("Class 2 survivors:", class2_survivors)
    print("Class 3 survivors:", class3_survivors)
    print("Class 1 casulties:", class1_casulties)
    print("Class 2 casulties:", class2_casulties)
    print("Class 3 casulties:", class3_casulties)

    alpha = 10

    likelihood_male_survivors = (male_survivors + alpha) / (survivors + 2*alpha)
    likelihood_female_survivors = (female_survivors + alpha) / (survivors + 2*alpha)
    print("Likelihood male/survivors:", likelihood_male_survivors)
    print("Likelihood female/survivors:", likelihood_female_survivors)

    likelihood_male_casulties = (male_casulties + alpha) / (casulties + 2*alpha)
    likelihood_female_casulties = (female_casulties + alpha) / (casulties + 2*alpha)
    print("Likelihood male/casulties:", likelihood_male_casulties)
    print("Likelihood female/casulties:", likelihood_female_casulties)

    likelihood_class1_survivors = (class1_survivors + alpha) / (survivors + 3*alpha)
    likelihood_class2_survivors = (class2_survivors + alpha) / (survivors + 3*alpha)
    likelihood_class3_survivors = (class3_survivors + alpha) / (survivors + 3*alpha)
    print("Likelihood class 1/survivors:", likelihood_class1_survivors)
    print("Likelihood class 2/survivors:", likelihood_class2_survivors)
    print("Likelihood class 3/survivors:", likelihood_class3_survivors)

    likelihood_class1_casulties = (class1_casulties + alpha) / (casulties + 3*alpha)
    likelihood_class2_casulties = (class2_casulties + alpha) / (casulties + 3*alpha)
    likelihood_class3_casulties = (class3_casulties + alpha) / (casulties + 3*alpha)
    print("Likelihood class 1/casulties:", likelihood_class1_casulties)
    print("Likelihood class 2/casulties:", likelihood_class2_casulties)
    print("Likelihood class 3/casulties:", likelihood_class3_casulties)

    prediction = []
    for i in test_index:
        logLikelihood_survivor = 0
        logLikelihood_casulty = 0
        
        if data.iloc[i]["Sex"]=="male":
            logLikelihood_survivor = logLikelihood_survivor + math.log(likelihood_male_survivors)
            logLikelihood_casulty = logLikelihood_casulty + math.log(likelihood_male_casulties)
        elif data.iloc[i]["Sex"]=="female":
            logLikelihood_survivor = logLikelihood_survivor + math.log(likelihood_female_survivors)
            logLikelihood_casulty = logLikelihood_casulty + math.log(likelihood_female_casulties)
        
        if data.iloc[i]["Pclass"]==1:
            logLikelihood_survivor = logLikelihood_survivor + math.log(likelihood_class1_survivors)
            logLikelihood_casulty = logLikelihood_casulty + math.log(likelihood_class1_casulties)
        elif data.iloc[i]["Pclass"]==2:
            logLikelihood_survivor = logLikelihood_survivor + math.log(likelihood_class2_survivors)
            logLikelihood_casulty = logLikelihood_casulty + math.log(likelihood_class2_casulties)
        elif data.iloc[i]["Pclass"]==3:
            logLikelihood_survivor = logLikelihood_survivor + math.log(likelihood_class3_survivors)
            logLikelihood_casulty = logLikelihood_casulty + math.log(likelihood_class3_casulties)
        
#        likelihood_survivor = math.exp(logLikelihood_survivor)
#        likelihood_casulty = math.exp(logLikelihood_casulty)
#        if likelihood_survivor/likelihood_casulty > prior_casulties/prior_survivors:
#            prediction.append(1)
#        else:
#            prediction.append(0)
        
        if logLikelihood_survivor - logLikelihood_casulty > math.log(prior_casulties) - math.log(prior_survivors):
            prediction.append(1)
        else:
            prediction.append(0)

    confusion = metrics.confusion_matrix(target[test_index], prediction)
    print(confusion)
    
    accuracy = metrics.accuracy_score(target[test_index], prediction)
    print(accuracy)

def main():
    naive_bayes()
    
main()
