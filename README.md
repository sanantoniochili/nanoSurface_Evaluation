# nanoSurfaceEval
Nanostructured surfaces' infrastructure evaluation using N Gram Graphs

## Produce_Surface

Use the following flags:

* -in \<input filename>
* -N \<number of surface points (along square side)>
* -rL \<length of surface (along square side)>
* -h \<rms height>
* -clx (-cly)  \<correlation length in x (and y)>
* -out \<output filename>

[INPUT]

input file(.cvs) format: \",Rms,clx,cly,Skewness,Kurtosis,Area" as a header 
                         \<ID>,<Rms>,<clx>,<cly>,<Skewness>,<Kurtosis>,<Area> per line

Provide the parameters needed (N, rL, h, clx are compulsory).
Omitting cly makes the surface isotropic.
Number N must be power of 2.

[OUTPUT]

output file(.cvs) format: rms:<value>,clx:<value>,cly:<value>,N:<value>,(<height>,)*<height> surface per line

The results are a matrix of heights corresponding to the surface points.
Use standard input to use 3D surface plotter.

## EncodeSimple

Use the following flags:

* -in \<input filename>
* -z \<number of spaces to split [-100nm,100nm] into
* -scale \<n (heights measured in nanometres*10^n)>
* -out \<output filename>

[INPUT]

input file(.cvs) format: rms:<value>,clx:<value>,cly:<value>,N:<value>,(<height>,)*<height> surface per line

A ".csv" file containing surface characteristics. First column contains parameters in a form <parameter>:<value> and every other column contains the height of the surface's points (column per height).

[OUTPUT]

output file(.txt) format: ((<letter>)*<blankline>(<letter>)*)*

A text denoting a height-zone [x,y], -100nm<=x<y<=100nm for every letter.

## SurfToGraph

[INPUT]

input file(.txt) format: ((<letter>)*<blankline>(<letter>)*)*

A ".txt" file containing texts that correspond to encoded surfaces. Texts are separated by a blank line (one text per surface).

[OUTPUT]

N gram graphs produced by the texts provided.
