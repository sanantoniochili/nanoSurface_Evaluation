import java.util.*;
import java.io.*;
import java.lang.Math;

public class RandomGaussSurfaceGenerator {
/*
* generates a square 2-dimensional random rough surface f(x,y) with NxN 
* surface points. The surface has a Gaussian height distribution and 
* exponential autocovariance functions (in both x and y), where rL is the 
* length of the surface side, h is the RMS height and clx and cly are the 
* correlation lengths in x and y. Omitting cly makes the surface isotropic.
*/

	Integer N;  // number of surface points (along square side)
	double Lr;  // length of surface (along square side)
	double H;   // rms height
	double clx; // correlation length in x
	double cly; // correlation length in y

	private double[][] Z; 
	private double[][] meshGridX;
	private double[][] meshGridY;

	// non-isotropic surface
    public RandomGaussSurfaceGenerator(int N, double Lr, double h, double clx, double cly) { //, double rL, dounble h, double clx, double cly) {
    	this.N   = N;
    	this.Lr  = Lr;
    	this.clx = clx;
    	this.cly = cly;
    	this.H   = h;
    	meshGrid();		  // init members meshGridX, meshGridY
    	RandomSurfaceH(); // init member Z
        double[][] GF = GaussianFilter(cly);
        Complex[][] GF_Fourier = FTransform(GF);

    	
    }

    // isotropic surface
    public RandomGaussSurfaceGenerator(int N, double Lr, double h, double clx) { //, double rL, dounble h, double clx, double cly) {
    	this.N   = N;
    	this.Lr  = Lr;
    	this.clx = clx;
    	meshGrid();       // init members meshGridX, meshGridY
    	RandomSurfaceH(); // init member Z
        double[][] F = GaussianFilter();

    }

    /*
    * create matrices X,Y of absolute values where 
    * X: same vector in each row
    * Y: same vector in each column
    * of evenly spaced points between -Lr/2 and Lr/2
    */
    private void meshGrid() {	
    	double begin = -Lr/(double)2;
    	double end = Lr/(double)2;
  
    	meshGridX = new double[N][N];
    	meshGridY = new double[N][N];

    	double count = begin;
    	double step = ( end-begin ) / (double)N; //find space between points
		
		for (int j=0 ; j<N ; j++) {	
			for (int i=0 ; i<N ; i++) {
				meshGridX[i][j] = Math.abs(count); //all rows of same column with the same point
				if( count>end ){ 	   	           //don't exceed end point
					meshGridX[i][j] = Math.abs(end);
					break;
				}
			}
			count += step;	//next column with new point
		}
		count = begin;
		for (int i=0 ; i<N ; i++) {	
			for (int j=0 ; j<N ; j++) {
				meshGridY[i][j] = Math.abs(count); //all columns of same row with the same point
				if( count>end ){		           //don't exceed end point
					meshGridY[i][j] = Math.abs(end);
					break;
				}
			}
			count += step;	//next column with new point
		}
		/*for (int i =0;i<N ; i++) {
			for (int j=0;j<N ;j++ ) {
				System.out.print(meshGridX[i][j]+" ");
			}
			System.out.println(" ");
		}*/

    }

    /*
    * Create matrix NxN of random normal distributed values
    * multiplied by h (rms height)
    */
    private void RandomSurfaceH() {
    	Z = new double[N][N];
    	Random rand = new Random();
    	for (int i=0 ; i<N ; i++) {
    		for (int j=0 ; j<N ; j++) {
    			Z[i][j] = H*rand.nextGaussian(); //standard normal distribution
    		}
    	}

    }

    /*
    * Compute the Gaussian filter
    * of non-isotropic
    *
    * F is size N*N
    */
    private double[][] GaussianFilter(double arg) {
        double[][] F = new double[N][N];
        for (int i=0 ; i<N ; i++) {
            for (int j=0 ; j<N ; j++) {
                F[i][j] = Math.exp( -(Math.abs( meshGridX[i][j] )/(clx/2)+Math.abs( meshGridY[i][j] )/(cly/2)) );
            }
        }
        return F;

    }

    /*
    * Compute the Gaussian filter
    * of isotropic
    *
    * F is size N*N
    */
    private double[][] GaussianFilter() {
        double[][] F = new double[N][N];
        for (int i=0 ; i<N ; i++) {
            for (int j=0 ; j<N ; j++) {
                F[i][j] = Math.exp( -((Math.abs( meshGridX[i][j] )+Math.abs( meshGridY[i][j] ))/(clx/2)) );
            }
        }
        return F;

    }

    /* 
    * Correlation of non-isotropic surface 
    * including convolution (faltung), 
    * inverse Fourier transform and normalizing prefactors
    *
    * in order to compute 2-dimensional Fourier transform 
    * of F we need to implement fft(fft(X).').' ,namely
    * performing a 1D FFT on each row of X
    * then do a 1D FFT on each column of the results
    *
    */
    private Complex[][] FTransform(double[][] X) {
        Complex[][] temp = new Complex[N][N]; // result of 1D FFT on each row of X
        Complex[][] res = new Complex[N][N];
        FFT fft = new FFT();

        for (int i=0 ; i<N ; i++) { // for each row
            Complex[] seq = new Complex[N];
            for (int j=0 ; j<N ; j++) { // for each column
                seq[j] = new Complex(X[i][j], 0);
            }
            temp[i] = fft.fft(seq);
            //fft.show(temp[i], "temp = fft(seq)");
        }
        for (int j=0 ; j<N ; j++) { // for each column
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
  
}