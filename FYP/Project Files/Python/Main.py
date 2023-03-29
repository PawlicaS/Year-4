"""
Author: Szymon Pawlica

Identification of anonymous authors using textual analysis and machine learning
"""
import warnings

import pandas as pd
import numpy as np
import torch
import re
import xgboost as xgb

from datasets import load_metric
from sklearn import model_selection
from sklearn.ensemble import RandomForestClassifier, VotingClassifier
from sklearn.feature_extraction.text import TfidfVectorizer, CountVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score
from sklearn.model_selection import train_test_split
from sklearn.naive_bayes import MultinomialNB
from sklearn.neural_network import MLPClassifier
from sklearn.preprocessing import LabelEncoder, StandardScaler
from sklearn.svm import SVC
from sklearn.tree import DecisionTreeClassifier
from torch.utils.data import Dataset
from transformers import TrainingArguments, Trainer, DistilBertForSequenceClassification, AutoTokenizer

warnings.filterwarnings("ignore")
DEFAULT_FILE = "../Data/articles1.csv"
FILE = "../Data/articles1.csv"  # Change to passed in arg

def check_file():
    try:
        print("Reading in passed dataset")
        df = pd.read_csv(FILE)
    except FileNotFoundError:
        default_df = input("Dataset not found, continue with default dataset?\n")
        if default_df == "y":
            print("Reading in default dataset")
            df = pd.read_csv(DEFAULT_FILE)
        else:
            exit(0)
    return df


def prepocessing(data):
    df = data[['author', 'content']]
    df = df.dropna()
    df = df.groupby('author').filter(lambda x: len(x) >= 250)
    df['author'] = df['author'].apply(lambda x: x.lower())
    df['author'] = df['author'].apply(lambda x: re.sub('[^a-zA-Z0-9\s]', '', x))
    df['author'] = df['author'].apply(lambda x: re.sub('\s+', ' ', x))
    df['content'] = df['content'].apply(lambda x: x.lower())
    df['content'] = df['content'].apply(lambda x: re.sub('[^a-zA-Z0-9\s]', '', x))
    df['content'] = df['content'].apply(lambda x: re.sub('\s+', ' ', x))

    encoder = LabelEncoder()
    labels = encoder.fit_transform(df['author'])

    # cv = CountVectorizer()
    # cv_data = cv.fit_transform(df['content'])
    # cv_df = pd.DataFrame(cv_data.toarray(), columns=cv.get_feature_names_out())
    # print(cv_df)

    tfidf = TfidfVectorizer(max_df=0.5, min_df=2, ngram_range=(1, 1))
    features = tfidf.fit_transform(df['content'])

    x_train, x_test, y_train, y_test = train_test_split(features, labels, test_size=0.2, random_state=42)

    return x_train, x_test, y_train, y_test


class CustomDataset(torch.utils.data.Dataset):
    def __init__(self, encodings, labels):
        self.encodings = encodings
        self.labels = labels

    def __getitem__(self, idx):
        item = {key: torch.tensor(val[idx]) for key, val in self.encodings.items()}
        item['labels'] = torch.tensor(self.labels[idx])
        return item

    def __len__(self):
        return len(self.labels)


metric = load_metric('accuracy')


def compute_metrics(eval_pred):
    predictions, labels = eval_pred
    predictions = np.argmax(predictions, axis=1)
    return metric.compute(predictions=predictions, references=labels)


def naive_bayes(x_train, x_test, y_train, y_test):
    nb = MultinomialNB()
    nb.fit(x_train, y_train)
    y_pred = nb.predict(x_test)

    accuracy = accuracy_score(y_test, y_pred)
    return "NB Accuracy:", accuracy


def decision_tree(x_train, x_test, y_train, y_test):
    dt = DecisionTreeClassifier()
    dt.fit(x_train, y_train)
    y_pred = dt.predict(x_test)

    accuracy = accuracy_score(y_test, y_pred)
    return "DT Accuracy:", accuracy


def svm(x_train, x_test, y_train, y_test):
    svm = SVC(kernel='linear', C=1, random_state=None)
    svm.fit(x_train, y_train)
    y_pred = svm.predict(x_test)

    accuracy = accuracy_score(y_test, y_pred)
    return "SVM Accuracy:", accuracy


def random_forest(x_train, x_test, y_train, y_test):
    rf = RandomForestClassifier(n_estimators=100, bootstrap=True, criterion='gini', min_samples_leaf=1, min_samples_split=2, random_state=None)
    rf.fit(x_train, y_train)
    y_pred = rf.predict(x_test)

    accuracy = accuracy_score(y_test, y_pred)
    return "RF Accuracy:", accuracy


def xgboost(x_train, x_test, y_train, y_test):
    xg = xgb.XGBClassifier(reg_lambda=1, reg_alpha=0.5)
    xg.fit(x_train, y_train)
    y_pred = xg.predict(x_test)

    accuracy = accuracy_score(y_test, y_pred)
    return "XGB Accuracy:", accuracy


def multilayer_perceptron(x_train, x_test, y_train, y_test):
    mlp = MLPClassifier(activation='relu', solver='adam', alpha=0.0001, max_iter=200, shuffle=True, verbose=False, random_state=None)
    mlp.fit(x_train, y_train)
    y_pred = mlp.predict(x_test)

    accuracy = accuracy_score(y_test, y_pred)
    return "MLP Accuracy:", accuracy


def logistic_regression(x_train, x_test, y_train, y_test):
    lr = LogisticRegression(verbose=0, max_iter=100, solver='lbfgs', C=1.0, penalty='l2', random_state=None)
    lr.fit(x_train, y_train)
    y_pred = lr.predict(x_test)

    accuracy = accuracy_score(y_test, y_pred)
    return "LR Accuracy:", accuracy


def ensemble(x_train, x_test, y_train, y_test):
    model1 = RandomForestClassifier(n_estimators=100, bootstrap=True, criterion='gini', min_samples_leaf=1, min_samples_split=2, random_state=None)
    model2 = xgb.XGBClassifier(reg_lambda=1, reg_alpha=0.5)
    model3 = MLPClassifier(activation='relu', solver='adam', alpha=0.0001, max_iter=200, shuffle=True, verbose=False, random_state=None)

    ensemble = VotingClassifier(estimators=[('rf', model1), ('xg', model2), ('mlp', model3)], voting='hard')
    ensemble.fit(x_train, y_train)
    y_pred = ensemble.predict(x_test)

    accuracy = accuracy_score(y_test, y_pred)
    return "ensemble Accuracy:", accuracy


def distilbert(data):
    df = data[['author', 'content']]
    df = df.dropna()
    df = df.groupby('author').filter(lambda x: len(x) >= 250)
    df['author'] = df['author'].apply(lambda x: x.lower())
    df['author'] = df['author'].apply(lambda x: re.sub('[^a-zA-Z0-9\s]', '', x))
    df['author'] = df['author'].apply(lambda x: re.sub('\s+', ' ', x))
    df['content'] = df['content'].apply(lambda x: x.lower())
    df['content'] = df['content'].apply(lambda x: re.sub('[^a-zA-Z0-9\s]', '', x))
    df['content'] = df['content'].apply(lambda x: re.sub('\s+', ' ', x))
    labels_count = df['author'].nunique()
    texts = df['content'].to_list()

    encoder = LabelEncoder()
    df['author_encoded'] = encoder.fit_transform(df['author'])
    labels = df['author_encoded'].to_list()

    train_ratio = 0.70
    test_ratio = 0.20
    validation_ratio = 0.10

    x_train, x_test, y_train, y_test = train_test_split(texts, labels, test_size=1 - train_ratio)
    x_val, x_test, y_val, y_test = train_test_split(x_test, y_test,
                                                    test_size=test_ratio / (test_ratio + validation_ratio))

    tokenizer = AutoTokenizer.from_pretrained('distilbert-base-uncased')

    train_encodings = tokenizer(x_train, truncation=True, padding=True)
    val_encodings = tokenizer(x_val, truncation=True, padding=True)
    test_encodings = tokenizer(x_test, truncation=True, padding=True)

    train_dataset = CustomDataset(train_encodings, y_train)
    val_dataset = CustomDataset(val_encodings, y_val)
    test_dataset = CustomDataset(test_encodings, y_test)

    args = TrainingArguments(
        output_dir='./results',  # output directory
        num_train_epochs=3,  # total number of training epochs
        per_device_train_batch_size=80,  # batch size per device during training
        per_device_eval_batch_size=80,  # batch size for evaluation
        learning_rate=5e-05,
        eval_accumulation_steps=4,
        gradient_accumulation_steps=4,
        logging_dir='./logs',
    )

    model = DistilBertForSequenceClassification.from_pretrained("distilbert-base-uncased", num_labels=labels_count)
    trainer = Trainer(
        model=model,
        args=args,
        train_dataset=train_dataset,
        eval_dataset=val_dataset,
        compute_metrics=compute_metrics
    )

    trainer.train()
    trainer.evaluate(test_dataset)
    pred = trainer.predict(test_dataset)
    return "DistilBERT Accuracy:", pred.metrics['test_accuracy']


def data_fusion():
    return 0


def main():
    data = check_file()
    x_train, x_test, y_train, y_test = prepocessing(data)
    accuracies = {}

    x, y = random_forest(x_train, x_test, y_train, y_test)
    accuracies[x] = y

    x, y = xgboost(x_train, x_test, y_train, y_test)
    accuracies[x] = y

    x, y = multilayer_perceptron(x_train, x_test, y_train, y_test)
    accuracies[x] = y

    x, y = logistic_regression(x_train, x_test, y_train, y_test)
    accuracies[x] = y

    x, y = ensemble(x_train, x_test, y_train, y_test)
    accuracies[x] = y

    x, y = distilbert(data)
    accuracies[x] = y

    print(accuracies)

    # naive_bayes(x_train, x_test, y_train, y_test)
    # decision_tree(x_train, x_test, y_train, y_test)
    # svm(x_train, x_test, y_train, y_test)


main()
