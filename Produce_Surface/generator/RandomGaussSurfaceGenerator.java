
package generator;

import java.util.*;
import java.io.*;
import java.lang.Math;
import java.math.BigDecimal;
import java.math.RoundingMode;

import operations.*;


public class RandomGaussSurfaceGenerator {
/*
* generates a square 2-dimensional random rough surface f(x,y) with NxN 
* surface points. The surface has a Gaussian height distribution and 
* exponential autocovariance functions (in both x and y), where rL is the 
* length of the surface side, h is the RMS height and clx and cly are the 
* correlation lengths in x and y. Omitting cly makes the surface isotropic.
*/

	Integer N;  // number of surface points (along square side)
	double rL;  // length of surface (along square side)
	double H;   // rms height
	double clx; // correlation length in x
	double cly; // correlation length in y

	private double[][] RandomRoughSurf; 
	private double[][] meshGridX;
	private double[][] meshGridY;

    public Double[][] Surf;    //height results

	// non-isotropic surface
    public RandomGaussSurfaceGenerator(double[] args, double cly) throws ImError{ 
    	this.N   = (int)args[0];
    	this.rL  = args[1];
    	this.H   = args[2];
        this.clx = args[3];
        this.cly = cly;

    	meshGrid();		  // init members meshGridX, meshGridY
    	RandomSurfaceH(); // init member RandomRoughSurf
        double[][] GF = GaussianFilter(cly);

        /*
        * correlation of surface including convolution (faltung), inverse
        * Fourier transform and normalizing prefactors
        */

        FFT_2D fft2 = new FFT_2D(N,N); // NxM matrix Fourier Transform
        
        // implementing ifft2(fft2(GF).*fft2(RRS)
        Complex[][] GF_cox = fft2.double2Complex(GF);
        Complex[][] GF_Fourier = fft2.FTransform(GF_cox);

        Complex[][] RRS_cox = fft2.double2Complex(RandomRoughSurf);
        Complex[][] RRS_Fourier = fft2.FTransform(RRS_cox);

        Complex[][] MultOut = new Complex[N][N];
    	fft2.ComplexArray_mult(GF_Fourier,RRS_Fourier,MultOut);
        Complex[][] Res = fft2.iFTransform(MultOut);
        // end

        Surf = new Double[N][N];

        for (int i=0 ; i<N ; i++) {
            for (int j=0 ; j<N ; j++) {
                if( round(Res[i][j].im(),10)!=0 ) // must be real number after rounding
                    throw new ImError();
                Surf[i][j] = new Double( 2*rL/N/Math.sqrt(clx*cly)*Res[i][j].re() );
            }
        }
        printArray(Surf);

    }

    // isotropic surface
    public RandomGaussSurfaceGenerator(double[] args) throws ImError{ 
    	this.N   = (int)args[0];
        this.rL  = args[1];
        this.H   = args[2];
        this.clx = args[3];

    	meshGrid();       // init members meshGridX, meshGridY
    	RandomSurfaceH(); // init member RandomRoughSurf
        double[][] GF = GaussianFilter();

        /*
        * correlation of surface including convolution (faltung), inverse
        * Fourier transform and normalizing prefactors
        */

        FFT_2D fft2 = new FFT_2D(N,N); // NxM matrix Fourier Transform

        // implementing ifft2(fft2(GF).*fft2(RRS)
        Complex[][] GF_cox = fft2.double2Complex(GF);
        Complex[][] GF_Fourier = fft2.FTransform(GF_cox);

        Complex[][] RRS_cox = fft2.double2Complex(RandomRoughSurf);
        Complex[][] RRS_Fourier = fft2.FTransform(RRS_cox);

        Complex[][] MultOut = new Complex[N][N];
        fft2.ComplexArray_mult(GF_Fourier,RRS_Fourier,MultOut);
        Complex[][] Res = fft2.iFTransform(MultOut);
        // end

        Surf = new Double[N][N];

        for (int i=0 ; i<N ; i++) {
            for (int j=0 ; j<N ; j++) {
                if( round(Res[i][j].im(),10)!=0 ) // must be real number after rounding
                    throw new ImError();
                Surf[i][j] = new Double( 2*rL/N/clx*Res[i][j].re() );
            }
        }
        printArray(Surf);

    }

    /*
    * create matrices X,Y of absolute values where 
    * X: same vector in each row
    * Y: same vector in each column
    * of evenly spaced points between -rL/2 and rL/2
    */
    private void meshGrid() {	
    	double begin = -rL/2;
    	double end = rL/2;
  
    	meshGridX = new double[N][N];
    	meshGridY = new double[N][N];

        Linspace linspace = new Linspace(begin,end,N);
        double[] L = linspace.op();
		
		for (int j=0 ; j<N ; j++) {	
			for (int i=0 ; i<N ; i++) {
				meshGridX[i][j] = Math.abs(L[j]); //all rows of same column with the same point
			}
		}
		for (int i=0 ; i<N ; i++) {	
			for (int j=0 ; j<N ; j++) {
				meshGridY[i][j] = Math.abs(L[i]); //all columns of same row with the same point
			}
		}
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
                F[i][j] = Math.exp( -( meshGridX[i][j]/(clx/2) + meshGridY[i][j]/(cly/2)) ); 
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
                F[i][j] = Math.exp( -( (meshGridX[i][j] + meshGridY[i][j])/(clx/2) ) );
            }
        }
        return F;

    }

    public void printArray(double[][] X) {
        for (int i=0 ; i<N ; i++) {
            for (int j=0 ; j<N ; j++) {
                System.out.print(X[i][j]+" ");
            }
            System.out.println(" ");
        }
        System.out.println(" ");
    }

    public void printArray(Double[][] X) {
        for (int i=0 ; i<N ; i++) {
            for (int j=0 ; j<N ; j++) {
                System.out.print(X[i][j]+" ");
            }
            System.out.println(" ");
        }
        System.out.println(" ");
    }

    /*
    * round double value to n decimals
    */ 
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
     
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
  
}