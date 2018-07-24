# nanoSurfaceEval
Nanostructured surfaces' infrastructure evaluation using N Gram Graphs

--Produce_Surface

In order to generate a new Random Gaussian Surface, run the bash script named "RUNME.sh" and follow the instructions:

N   - number of surface points (along square side) 

rL  - length of surface (along square side)

h   - rms height

clx (cly)  - correlation lengths (in x and y)

[INPUT]
Provide the parameters needed in the following order: N rL h clx (cly). 
Provision of cly will determine whether the surface will be isotropic or non-isotropic.
Number N must be power of 2.

[OUTPUT]
The results are a matrix of heights corresponding to the surface points.
