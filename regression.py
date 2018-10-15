import csv
import numpy as np
from sklearn import linear_model
from sklearn.metrics import r2_score, mean_absolute_error
from sklearn.model_selection import KFold

trait_columns = int (input("Enter number of graph traits used for predictions\n"))  # e.g. 6
in_file = input("Enter name of file with input data\n") # e.g."Vectors_simple_10-10-2018.csv"
res_filename = input("Enter name of file to write regression metrics\n") # e.g."Regression_results.txt"
yesno = input("Do you want to overwrite?(y or n):")
while( yesno!="n" and yesno!="y" ): yesno = input("Do you want to overwrite?(y or n):")
if( yesno=="yes" ): open(res_filename, 'w').close()
pred_file = input("Enter name of file to write predicted values\n") # e.g."Predictions.txt"
enc_no = int (input("Enter type of surface encoding {1(Simple), 2(MinMax)}:"))

encoding = {
    1:"__Simple__",
    2:"__Minmax__"
}


"""
in_files = []
in_files.append('Vectors_simple_10-10-2018.csv')
in_files.append('Vectors_minmax_10-10-2018.csv')

pred_files = []
pred_files.append("Predictions_simple.txt")
pred_files.append("Predictions_minmax.txt")
"""

class Printable:
    def printIndices(train_index, test_index):
        outfile.write("TRAIN:")
        for item in train_index:
            outfile.write("%s" % item)
        outfile.write("\nTEST:")
        for item in test_index:
            outfile.write("%s" % item)
        outfile.write("\n")

    def printResults(y_test, predictions, outfile):
        outfile.write("R2 Score: %.2f\n" % r2_score(y_test, predictions))
        # The mean squared error
        outfile.write("Mean absolute error: %.2f\n\n" % mean_absolute_error(y_test, predictions))


models = []
models.append(('\t--Linear--', linear_model.LinearRegression(fit_intercept=True, normalize=False, copy_X=True)))
models.append(('\t--Ridge--', linear_model.Ridge()))
models.append(('\t--Lasso--', linear_model.Lasso()))
for name, reg in models:
    with open(in_file) as csvfile:
        reader = csv.reader(csvfile, delimiter=',')
        # get header from first row
        headers = next(reader)
        # get all the rows as a list
        data = list(reader)
        # transform data into numpy array
        data = np.array(data).astype(float)

    rows, cols = data.shape
    X = data[:, 0:trait_columns]
    y = data[:, trait_columns:cols]

    kf = KFold(n_splits=5)
    kf.get_n_splits(X)

    open(pred_file, 'w').close()
    with open(res_filename, "a") as outfile:
        outfile.write(name + '\n')
        outfile.write(encoding[enc_no] + '\n')
        for train_index, test_index in kf.split(X):
            Printable.printIndices(train_index, test_index)
            X_train, X_test = X[train_index], X[test_index]
            y_train, y_test = y[train_index], y[test_index]

            reg.fit(X_train, y_train)
            predictions = reg.predict(X_test)
            with open(pred_file, "a") as f:
                f.write("True Values:\n")
                np.savetxt(f, y_test)
                f.write("Predicted Values:\n")
                np.savetxt(f, predictions)

            Printable.printResults(y_test, predictions, outfile)
