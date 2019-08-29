/*
 * Fast Fourier Transform 2D
 *
 * In order to compute 2-dimensional transforms
 * of X we need to implement fft(fft(X).').' or ifft(ifft(X).').',
 * namely, performing a 1D FFT or iFFT (respectively) on each row of X
 * then do a 1D FFT or IFFT (respectively) on each column of the previous results.
 *
 */

package gr.demokritos.iit.sproduce.utils;
import edu.princeton.cs.algs4.FFT;
import edu.princeton.cs.algs4.Complex;
import edu.princeton.cs.algs4.StdOut;


/**
 * Two dimensional Fast Fourier Transformation
 */
public class FastFourier {

    /**
     * Simple Fast Fourier Transform instance
     */
    FFT fft_inst;
    /**
     * Dimension N
     */
    protected int N;
    /**
     * Dimension M
     */
    protected int M;

    /**
     * <p>Initialization</p>
     *
     * @param dim1
     * @param dim2
     */
    public FastFourier(int dim1, int dim2) {
        this.N = dim1;
        this.M = dim2;
    }

    /**
     * <p>Transform real to complex</p>
     *
     * @param in    Array of real numbers
     * @return      Result
     */
    public Complex[][] double2Complex(double[][] in) {
        Complex[][] X = new Complex[N][M];
        for (int i=0 ; i<N ; i++) {
            for (int j=0 ; j<M ; j++) {
                X[i][j] = new Complex(in[i][j], 0);
            }
        }
        return X;

    }

    /**
     * <p>Two dimensional Fast Fourier Transformation</p>
     *
     * @param X_cox     Array of Complex numbers
     * @return          Transformed matrix
     */
    public Complex[][] FTransform(Complex[][] X_cox) {
        Complex[][] temp = new Complex[N][M]; // result of 1D FFT on each row of X
        Complex[][] res = new Complex[N][M];

        for (int i=0 ; i<N ; i++) { // for each row
            temp[i] = fft_inst.fft(X_cox[i]);
        }
        for (int j=0 ; j<M ; j++) { // for each column
            Complex[] seq = new Complex[N];
            for (int i=0 ; i<N ; i++) { // for each row
                seq[i] = temp[i][j];
            }
            Complex[] res_col = fft_inst.fft(seq); // 1D array column of result
            for (int i=0 ; i<N ; i++) {
                res[i][j] = res_col[i]; // save sequence to result column
            }
        }
        return res;

    }

    /**
     * <p>Two dimensional inverse Fast Fourier Transformation</p>
     *
     * @param X_cox     Array of Complex numbers
     * @return          Transformed matrix
     */
    public Complex[][] iFTransform(Complex[][] X_cox) {
        Complex[][] temp = new Complex[N][M]; // result of 1D iFFT on each row of X
        Complex[][] res = new Complex[N][M];

        for (int i=0 ; i<N ; i++) { // for each row
            temp[i] = fft_inst.ifft(X_cox[i]);
        }
        for (int j=0 ; j<M ; j++) { // for each column
            Complex[] seq = new Complex[N];
            for (int i=0 ; i<N ; i++) { // for each row
                seq[i] = temp[i][j];
            }
            Complex[] res_col = fft_inst.ifft(seq); // 1D array column of result
            for (int i=0 ; i<N ; i++) {
                res[i][j] = res_col[i]; // save sequence to result column
            }
        }
        return res;

    }

    /**
     * Matrix multiplication (implementing fft2(GF).*fft2(RRS)
     *
     * @param X     Complex 2 dim Matrix multiplier
     * @param Y     Complex 2 dim Matrix multiplier
     * @param Out   Result
     * @return
     */
    public boolean ComplexArray_mult(Complex[][] X, Complex[][] Y, Complex[][] Out) {
        if( X.length != Y.length )
            return false;
        if( X[0].length != Y[0].length )
            return false;
        if( Out.length != X.length )
            return false;
        if( Out[0].length != X[0].length )
            return false;

        for (int i=0 ; i<N ; i++) {
            for (int j=0 ; j<M ; j++) {
                Out[i][j] = X[i][j].times(Y[i][j]);
            }
        }
        return true;

    }

    public void printArray(Complex[][] X) {
        for (int i=0 ; i<N ; i++) {
            for (int j=0 ; j<M ; j++) {
                StdOut.print(X[i][j]+" ");
            }
            System.out.println(" ");
        }
        System.out.println(" ");
    }
}