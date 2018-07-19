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

	private double[][] RandomRoughSurf; 
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
    	RandomSurfaceH(); // init member RandomRoughSurf
        /*
        * correlation of surface including convolution (faltung), inverse
        * Fourier transform and normalizing prefactors
        */
        double[][] GF = GaussianFilter(cly);

        FFT_2D fft2 = new FFT_2D(N,N);
        
        Complex[][] GF_cox = fft2.double2Complex(GF);
        fft2.printArray(GF_cox);
        Complex[][] GF_Fourier = fft2.FTransform(GF_cox);
        fft2.printArray(GF_Fourier);

        /*Complex[][] RRS_cox = fft2.double2Complex(RandomRoughSurf);
        Complex[][] RRS_Fourier = fft2.FTransform(RRS_cox);

        Complex[][] MultOut = new Complex[N][N];
    	fft2.ComplexArray_mult(GF_Fourier,RRS_Fourier,MultOut);
        Complex[][] Res = fft2.iFTransform(MultOut);*/

    }

    // isotropic surface
    public RandomGaussSurfaceGenerator(int N, double Lr, double h, double clx) { //, double rL, dounble h, double clx, double cly) {
    	this.N   = N;
    	this.Lr  = Lr;
    	this.clx = clx;
    	meshGrid();       // init members meshGridX, meshGridY
    	RandomSurfaceH(); // init member RandomRoughSurf
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
    	RandomRoughSurf = new double[N][N];
    	Random rand = new Random();
    	for (int i=0 ; i<N ; i++) {
    		for (int j=0 ; j<N ; j++) {
    			RandomRoughSurf[i][j] = H*rand.nextGaussian(); //standard normal distribution
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
  
}