"""
Author: Szymon Pawlica

Identification of anonymous authors using textual analysis and machine learning
"""
import os
import time
import warnings
import argparse

import pandas as pd
import numpy as np
import torch
import re
import xgboost as xgb

from datasets import load_metric
from matplotlib import pyplot as plt
from sklearn.ensemble import RandomForestClassifier, VotingClassifier
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score
from sklearn.model_selection import train_test_split
from sklearn.neural_network import MLPClassifier
from sklearn.preprocessing import LabelEncoder
from torch.utils.data import Dataset
from transformers import TrainingArguments, Trainer, DistilBertForSequenceClassification, AutoTokenizer, logging

warnings.filterwarnings("ignore")
logging.set_verbosity_error()
DEFAULT_FILE = "../Data/articles1.csv"
DEFAULT_SAMPLES = 500
DEFAULT_ALGORITHMS = ["rf", "xgb", "mlp", "lr", "ensemble", "distilbert"]
DEFAULT_BATCH_SIZE = 16

parser = argparse.ArgumentParser(description='Identification of anonymous authors using textual analysis and machine learning.')
parser.add_argument('--dataset', type=str,
                    help='IF LEFT BLANK = "../Data/articles1.csv". An optional string argument, pass the path to your dataset.')
parser.add_argument('--samples', type=int,
                    help='IF LEFT BLANK = 500. An optional integer argument, pass the minimum amount of samples for an author to be accounted for.')
parser.add_argument('--algorithms', type=str,
                    help='IF LEFT BLANK = "rf, xgb, mlp, lr, ensemble, distilbert". An optional string argument, pass any/all of these "rf", "xgb", "mlp", "lr", "ensemble", "distilbert".')
parser.add_argument('--batch_size', type=int,
                    help='IF LEFT BLANK = 16. An optional integer argument, pass the batch size to be used for the distilbert model.')
args = parser.parse_args()
FILE = args.dataset
SAMPLES = args.samples
ALGORITHMS = args.algorithms
BATCH_SIZE = args.batch_size


def check_args():
    print("=============================\n"
          "Checking Args")
    if FILE is None:
        dataset = DEFAULT_FILE
    else:
        dataset = FILE

    if SAMPLES is None:
        min_samples = DEFAULT_SAMPLES
    else:
        min_samples = SAMPLES

    if ALGORITHMS is None:
        algorithms = DEFAULT_ALGORITHMS
    else:
        algorithms = re.sub('\s+', '', ALGORITHMS)
        algorithms.split(',')

    if BATCH_SIZE is None:
        batch_size = DEFAULT_BATCH_SIZE
    else:
        batch_size = BATCH_SIZE

    print(f"Min Samples: {min_samples}\nAlgorithms: {algorithms}\nBatch Size: {batch_size}")

    try:
        print(f"Trying to read in {dataset} dataset\n")
        df = pd.read_csv(dataset)
    except FileNotFoundError and ValueError:
        default_df = input(f"Dataset at {dataset} not found, is the file .csv? \nContinue with default dataset? (y/n)\n")
        if default_df == "y":
            print(f"Reading in {DEFAULT_FILE} dataset\n")
            df = pd.read_csv(DEFAULT_FILE)
        else:
            print("Exiting")
            exit(0)

    return df, min_samples, algorithms, batch_size


def prepocessing(data, min_samples):
    print("=============================\n"
          "Preprocessing Data")
    st = time.time()
    df = data[['author', 'content']]
    df = df.dropna()
    df = df.groupby('author').filter(lambda x: len(x) >= min_samples)
    df['author'] = df['author'].apply(lambda x: x.lower())
    df['author'] = df['author'].apply(lambda x: re.sub('[^a-zA-Z0-9\s]', '', x))
    df['author'] = df['author'].apply(lambda x: re.sub('\s+', ' ', x))
    df['content'] = df['content'].apply(lambda x: x.lower())
    df['content'] = df['content'].apply(lambda x: re.sub('[^a-zA-Z0-9\s]', '', x))
    df['content'] = df['content'].apply(lambda x: re.sub('\s+', ' ', x))

    print(f"Number of authors: {df['author'].nunique()}")

    samples = df.groupby('author').size()
    largest_sample = df.groupby('author').size().max()

    oversampled_data = pd.DataFrame(columns=df.columns)
    for class_name, group_data in df.groupby('author'):
        if samples[class_name] < largest_sample:
            group_data_oversampled = group_data.sample(n=largest_sample, replace=True, random_state=42)
            oversampled_data = pd.concat([oversampled_data, group_data_oversampled])
        else:
            oversampled_data = pd.concat([oversampled_data, group_data])

    encoder = LabelEncoder()
    labels = encoder.fit_transform(oversampled_data['author'])

    tfidf = TfidfVectorizer(max_df=0.5, min_df=2, ngram_range=(1, 1))
    features = tfidf.fit_transform(oversampled_data['content'])

    x_train, x_test, y_train, y_test = train_test_split(features, labels, test_size=0.2, random_state=42)

    et = time.time()
    tt = et - st
    print("Finished Preprocessing\n"
          f"Took {tt:.3f}s")
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


def random_forest(x_train, x_test, y_train, y_test):
    print("=============================\n"
          "Running Random Forest")
    st = time.time()
    rf = RandomForestClassifier(n_estimators=100, bootstrap=True, criterion='gini', min_samples_leaf=1,
                                min_samples_split=2, random_state=None)
    rf.fit(x_train, y_train)
    y_pred = rf.predict(x_test)

    accuracy = accuracy_score(y_test, y_pred)
    et = time.time()
    tt = et - st
    print("-Finished\n"
          f"Took {tt:.3f}s")
    return "Random Forest", accuracy


def xgboost(x_train, x_test, y_train, y_test):
    print("=============================\n"
          "Running XGBoost")
    st = time.time()
    xg = xgb.XGBClassifier(reg_lambda=1, reg_alpha=0.5)
    xg.fit(x_train, y_train)
    y_pred = xg.predict(x_test)

    accuracy = accuracy_score(y_test, y_pred)
    et = time.time()
    tt = et - st
    print("-Finished\n"
          f"Took {tt:.3f}s")
    return "XGBoost", accuracy


def multilayer_perceptron(x_train, x_test, y_train, y_test):
    print("=============================\n"
          "Running Multilayer Perceptron")
    st = time.time()
    mlp = MLPClassifier(activation='relu', solver='adam', alpha=0.0001, max_iter=200, shuffle=True, verbose=False,
                        random_state=None)
    mlp.fit(x_train, y_train)
    y_pred = mlp.predict(x_test)

    accuracy = accuracy_score(y_test, y_pred)
    et = time.time()
    tt = et - st
    print("-Finished\n"
          f"Took {tt:.3f}s")
    return "Multilayer Perceptron", accuracy


def logistic_regression(x_train, x_test, y_train, y_test):
    print("=============================\n"
          "Running Logistic Regression")
    st = time.time()
    lr = LogisticRegression(verbose=0, max_iter=100, solver='lbfgs', C=1.0, penalty='l2', random_state=None)
    lr.fit(x_train, y_train)
    y_pred = lr.predict(x_test)

    accuracy = accuracy_score(y_test, y_pred)
    et = time.time()
    tt = et - st
    print("-Finished\n"
          f"Took {tt:.3f}s")
    return "Logistic Regression", accuracy


def ensemble(x_train, x_test, y_train, y_test):
    print("=============================\n"
          "Running Ensemble")
    st = time.time()
    model1 = RandomForestClassifier(n_estimators=100, bootstrap=True, criterion='gini', min_samples_leaf=1,
                                    min_samples_split=2, random_state=None)
    model2 = xgb.XGBClassifier(reg_lambda=1, reg_alpha=0.5)
    model3 = MLPClassifier(activation='relu', solver='adam', alpha=0.0001, max_iter=200, shuffle=True, verbose=False,
                           random_state=None)

    ensemble = VotingClassifier(estimators=[('rf', model1), ('xg', model2), ('mlp', model3)], voting='hard')
    ensemble.fit(x_train, y_train)
    y_pred = ensemble.predict(x_test)

    accuracy = accuracy_score(y_test, y_pred)
    et = time.time()
    tt = et - st
    print("-Finished\n"
          f"Took {tt:.3f}s")
    return "Ensemble", accuracy


def distilbert(data, min_samples, batch_size):
    print("=============================\n"
          "Running DistilBERT")
    st = time.time()
    df = data[['author', 'content']]
    df = df.dropna()
    df = df.groupby('author').filter(lambda x: len(x) >= min_samples)
    df['author'] = df['author'].apply(lambda x: x.lower())
    df['author'] = df['author'].apply(lambda x: re.sub('[^a-zA-Z0-9\s]', '', x))
    df['author'] = df['author'].apply(lambda x: re.sub('\s+', ' ', x))
    df['content'] = df['content'].apply(lambda x: x.lower())
    df['content'] = df['content'].apply(lambda x: re.sub('[^a-zA-Z0-9\s]', '', x))
    df['content'] = df['content'].apply(lambda x: re.sub('\s+', ' ', x))
    labels_count = df['author'].nunique()
    texts = df['content'].to_list()

    encoder = LabelEncoder()
    labels = encoder.fit_transform(df['author']).tolist()

    train_ratio = 0.70
    test_ratio = 0.20
    validation_ratio = 0.10

    x_train, x_test, y_train, y_test = train_test_split(texts, labels, test_size=1 - train_ratio, random_state=42)
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
        per_device_train_batch_size=batch_size,  # batch size per device during training
        per_device_eval_batch_size=batch_size,  # batch size for evaluation
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
    et = time.time()
    tt = et - st
    print("-Finished\n"
          f"Took {tt:.3f}s")
    return "DistilBERT", pred.metrics['test_accuracy']


def data_fusion():
    return 0


def save_data(accuracies, min_samples, algorithms):
    with open(f'results{algorithms}{min_samples}.txt', 'w') as file:
        file.write(f"{min_samples} min samples\nUsing {algorithms}\n")
        for i in accuracies:
            file.write(f"{i}, {accuracies[i]}\n")


def plot(accuracies):
    print("Plotting Graph")
    algorithms = accuracies.keys()
    accuracy_values = accuracies.values()

    plt.yticks(np.arange(0, 100, 5))
    plt.grid(axis='y', linestyle='--')
    plt.bar(algorithms, accuracy_values)
    plt.title('Accuracy Comparison')
    plt.xlabel('Algorithm')
    plt.ylabel('Accuracy %')

    plt.show()


def main():
    data, min_samples, algorithms, batch_size = check_args()
    x_train, x_test, y_train, y_test = prepocessing(data, min_samples)
    accuracies = {}

    if "rf" in algorithms:
        x, y = random_forest(x_train, x_test, y_train, y_test)
        accuracies[x] = y * 100

    if "xgb" in algorithms:
        x, y = xgboost(x_train, x_test, y_train, y_test)
        accuracies[x] = y * 100

    if "mlp" in algorithms:
        x, y = multilayer_perceptron(x_train, x_test, y_train, y_test)
        accuracies[x] = y * 100

    if "lr" in algorithms:
        x, y = logistic_regression(x_train, x_test, y_train, y_test)
        accuracies[x] = y * 100

    if "ensemble" in algorithms:
        x, y = ensemble(x_train, x_test, y_train, y_test)
        accuracies[x] = y * 100

    if "distilbert" in algorithms:
        x, y = distilbert(data, min_samples, batch_size)
        accuracies[x] = y * 100

    save_data(accuracies, min_samples, algorithms)
    plot(accuracies)


main()
