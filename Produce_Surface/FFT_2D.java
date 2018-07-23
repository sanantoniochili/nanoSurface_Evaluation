/* 
* Fast Fourier Transform 2D
*
* In order to compute 2-dimensional transforms
* of X we need to implement fft(fft(X).').' or ifft(ifft(X).').',
* namely, performing a 1D FFT or iFFT (respectively) on each row of X
* then do a 1D FFT or IFFT (respectively) on each column of the previous results.
*
*/

import java.lang.Math;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class FFT_2D {

	private int N;
	private int M;

	public FFT_2D(int dim1, int dim2) {
		this.N = dim1;
		this.M = dim2;
	}

	public Complex[][] double2Complex(double[][] in) {
		Complex[][] X = new Complex[N][M];
		for (int i=0 ; i<N ; i++) {
			for (int j=0 ; j<M ; j++) {
				X[i][j] = new Complex(in[i][j], 0);
			}
		}
		return X;

	}

	public Complex[][] FTransform(Complex[][] X_cox) {
		Complex[][] temp = new Complex[N][M]; // result of 1D FFT on each row of X
        Complex[][] res = new Complex[N][M];
        FFT fft = new FFT();

        for (int i=0 ; i<N ; i++) { // for each row
            temp[i] = fft.fft(X_cox[i]);
        }
        for (int j=0 ; j<M ; j++) { // for each column
            Complex[] seq = new Complex[N];
            for (int i=0 ; i<N ; i++) { // for each row
                seq[i] = temp[i][j];
            }
            Complex[] res_col = fft.fft(seq); // 1D array column of result
            for (int i=0 ; i<N ; i++) {
                res[i][j] = res_col[i]; // save sequence to result column
            }
        }
        return res;

	}

	public Complex[][] iFTransform(Complex[][] X_cox) {
		Complex[][] temp = new Complex[N][M]; // result of 1D iFFT on each row of X
        Complex[][] res = new Complex[N][M];
        FFT fft = new FFT();

        for (int i=0 ; i<N ; i++) { // for each row
            temp[i] = fft.ifft(X_cox[i]);
        }
        for (int j=0 ; j<M ; j++) { // for each column
            Complex[] seq = new Complex[N];
            for (int i=0 ; i<N ; i++) { // for each row
                seq[i] = temp[i][j];
            }
            Complex[] res_col = fft.ifft(seq); // 1D array column of result
            for (int i=0 ; i<N ; i++) {
                res[i][j] = res_col[i]; // save sequence to result column
            }
        }
        return res;
        
	}

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

	private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
     
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
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