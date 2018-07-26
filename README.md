# nanoSurfaceEval
Nanostructured surfaces' infrastructure evaluation using N Gram Graphs

## Produce_Surface

```
./RUNME.sh
```

Use the following flags:

* -N <number of surface points (along square side)>
* -rL <length of surface (along square side)>
* -h <rms height>
* -clx (-cly)  <correlation length in x (and y)>
* -out <output filename>

[INPUT]

Provide the parameters needed (N, rL, h, clx are compulsory).
Omitting cly makes the surface isotropic.
Number N must be power of 2.

[OUTPUT]

The results are a matrix of heights corresponding to the surface points.
