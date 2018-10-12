/*
 * Generates n points
 * The spacing between the points is (arg2-arg1)/(n-1).
 */

package gr.demokritos.iit.sproduce.utils;

import java.lang.Math;
import java.math.BigDecimal;


public class Linspace {

    private int total;
    private final double end;
    private final double start;

    public Linspace(double start, double end, int totalCount) {
        this.total   = (int)Math.floor( (double)totalCount )-1;
        if( start>end ){ // check for wrong sequence of arguments
            this.end   = start;
            this.start = end;
        } else {
            this.end   = end;
            this.start = start;
        }

    }

    public double[] op() {
        // check intermediate value for appropriate treatment
        // throws exception at overflow
        try{
            BigDecimal.valueOf(Math.multiplyExact((long)(end-start),(long)total-1));
            double[] res = new double[total+1];
            if( end*start<0 ){
                for (int i=0 ; i<=total ; i++) {
                    res[i] = start + (end/total)*i - (start/total)*i;
                }
            } else {
                for (int i=0; i<=total ; i++) {
                    res[i] = start + i*( (end-start)/total );
                }
            }
            return res;
            // in case of overflow
        } catch (ArithmeticException e) {
            double[] res = new double[total+1];
            for (int i=0; i<=total ; i++) {
                res[i] = start + i*( (end-start)/total );
            }
            return res;
        }

    }

}