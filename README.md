# nanoSurfaceEval
Nanostructured surfaces' infrastructure evaluation using N Gram Graphs

## Produce_Surface

Use the following flags:

* -N \<number of surface points (along square side)>
* -rL \<length of surface (along square side)>
* -h \<rms height>
* -clx (-cly)  \<correlation length in x (and y)>
* -out \<output filename>

[INPUT]

Provide the parameters needed (N, rL, h, clx are compulsory).
Omitting cly makes the surface isotropic.
Number N must be power of 2.

[OUTPUT]

The results are a matrix of heights corresponding to the surface points.

## EncodeSimple

Use the following flags:

* -in \<input filename>
* -z \<number of spaces to split [-100nm,100nm] into
* -scale \<n (heights measured in nanometres*10^n)>
* -out \<output filename>

[INPUT]

A csv file containing surface characteristics. First column contains parameters in a form <parameter>:<value> and every other column contains the height of the surface's points (column per height).

[OUTPUT]

A text denoting a height-zone [x,y], -100nm<=x<y<=100nm for every letter.
