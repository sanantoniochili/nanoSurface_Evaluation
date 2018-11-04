# nanoSurfaceEval
Nanostructured surfaces' infrastructure characterization using N Gram Graphs. Machine learning experiments based on the N Gram Graphs' representation of the surfaces.

## Produce_Surface

Application producing NxN heights corresponding to NxN points of a random Gaussian isotropic or non-isotropic surface. 
Use the following flags:

* -in \<input filename>
* -N \<number of surface points (along square side)>
* -rL \<length of surface (along square side)>
* -h \<rms height>
* -clx (-cly)  \<correlation length in x (and y)>
* -out \<output filename>

[INPUT]

input file(.cvs) format: \",Rms,clx,cly,Skewness,Kurtosis,Area" as a header 
                         \<ID>,\<Rms>,\<clx>,\<cly>,\<Skewness>,\<Kurtosis>,<Area> per line

Provide the parameters needed (N, rL, h, clx are compulsory).
Omitting cly makes the surface isotropic.
Number N must be power of 2.

[OUTPUT]

output file(.cvs) format: rms:<value>,clx:\<value>,cly:\<value>,N:\<value>,(\<height>,)*\<height> surface per line

The results are a matrix of heights corresponding to the surface points.
Use standard input to invoke 3D surface plotter.

## Encoding

Application to encode heights to letters of the Latin alphabet using the first oneof the methods suggested in file "Encodings.pdf".
Use the following flags:

* -in \<input filename>
* -z \<number of spaces to split [-100nm,100nm] into>
* -scale \<n (heights measured in nanometres*10^n)>
* -out \<output filename>

[INPUT]

input file(.cvs) format: rms:<value>,clx:\<value>,cly:\<value>,N:\<value>,(\<height>,)*\<height> surface per line

A ".csv" file containing surface characteristics. First column contains parameters in a form \<parameter>:\<value> and every other column contains the height of the surface's points (column per height).

[OUTPUT]

output file(.txt) format: ((\<letter>)*\<blankline>(\<letter>)*)*

A text denoting a height-zone [x,y], -100nm<=x<y<=100nm for every letter.

## SurfToGraph

Application to convert a text representing a Gaussian surface to a N Gram Graph. After the graph has been formed, we measure the graph's most important traits (degree, shortest paths etc.) in order to extract a feature vector.

[INPUT]

input file(.txt) format: ((\<letter>)\*\<blankline>(\<letter>)\*)*

A ".txt" file containing texts that correspond to encoded surfaces. Texts are separated by a blank line (one text per surface).

[OUTPUT]

N gram graphs produced by the texts provided.

## regression.py

Code to evaluate the result of machine learning algorithms which predict the parameters of the surface production. Input is file with feature vectors of the N Gram Graphs mentioned above along with the true values of the parameters. Using SKlearn library implementations for linear, ridge and lasso regression with cross validation.

[INPUT]

A ".csv" file with N columns and M lines. Each line is a surface. The first k values are the traits of the graphs to be used for the predictions and the rest N-k values are the parameters that characterize the surface.

[OUTPUT]

Two ".txt" files:
- Metrics of regression
- True and predicted values in arrays
