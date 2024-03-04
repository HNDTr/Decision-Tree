# DecisionTree
Decision Tree Program


## Usage
---
### Compile the files

```
javac fileName.java
```

### Run the programs (with data)
```
java TestClassifier set1|set2 small|big
java TestClassifierHepatitis test-hepatitis.txt
```

## Datasets
Each dataset is split into two separate files: one for positive examples the other for negative
examples. Each training example has a value for each feature of the dataset. The features are
numbered starting from 0 (so for example, feature 3 is the 4th feature). Here are the first few lines
of the positive dataset for set1-small:
false false true false true true true true
false false false true true false true false
false true false true false false false true
...
The first line shows the values (either true or false) for each of the 8 features (feature 0 to feature
7) for example 1. The second line shows these values for example 2; the third line for example 3,
and so on.


## General Descriptions
A binary decision tree classifier. Since the tree is binary it can learn data when every feature has exactly two possible values (true or false).

## UCI Students' Dropout Dataset description
---

### A brief  description of the dataset including what the features are and what is being predicted 
The features are Daytime/evening attendance, Displaced, Educational special needs, 	Debtor,	Tuition fees up to date,	Gender,	Scholarship holder,	and International. We are predicting whether or not a candidate will be a graduate from college or higher education instition.<br/> 
### The number of examples in the dataset
- In the dataset, it had 4424 examples. However, we only selected the first 300 examples to use for testing and training. We took 200 examples used for training, and the other 100 for testing<br/>
### The number of features for each example
- Our dataset had 37 features, 30 of which were numerical. We took only 7 features to represent our datasets for simplicity
#### Analysis
- Based on our decision tree, we observe that it successfully classifies a significant portion of the examples, achieving accuracy for 76 out of 78 positive examples and 5 out of 22 negative examples. This suggests that the tree is effective in identifying positive cases, but it struggles with negative cases (this may be the case due to not enough negative test or data contains noise). Upon closer examination of the tree's structure, it becomes evident that feature 7 (international, yes/no) is the initial split point, indicating that this feature provides the highest information gain in distinguishing between positive and negative cases. International students have a stronger passion to study abroad, which allowed them to further exceed into their studies. Subsequently, the tree branches into feature 6 (scholarship holder, yes/no), followed by feature 4 (debtor, yes/no), and so on, indicating the order of importance in feature selection for classification. However, it's important to note that the decision tree appears larger and more complex than expected, which can be attributed to the presence of noise in the dataset. This noise introduces misclassifications and impurities in the data, making it harder for the tree to find a straightforward and compact set of rules for accurate classification. As a result, the tree might capture some of the noise in the data, leading to suboptimal branches and potentially overfitting the training data. In summary, the decision tree is effective at classifying positive cases, and certain features play a more significant role in the classification process. However, the presence of noise in the dataset complicates the tree's structure and leads to a larger and potentially less interpretable tree due to the introduction of misclassifications and impurities. This underscores the challenge of dealing with noisy data in decision tree learning and the need for techniques to mitigate the impact of noise on model performance.<br/>



## Acknowledgement 
---
### [RateItAll](https://archive.ics.uci.edu/ml/machine-learning-databases/hepatitis/hepatitis.names)https://archive.ics.uci.edu/ml/machine-learning-databases/hepatitis/hepatitis.names (Hepatitis Datasets)
### UCI Repository (Students' Dropout and Academic Success Datasets)
