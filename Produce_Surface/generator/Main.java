
package generator;

import java.util.*;
import java.io.*;

import operations.*;

public class Main {

    public static void main(String[] argv) throws Exception{
    	
    	double[] args_ = new double[argv.length];
    	int count = 0;
        for (String s: argv) {
            args_[count] = Double.parseDouble(s);
            count++;
        }
        RandomGaussSurfaceGenerator RG;
        if( args_.length==4 )
       		RG = new RandomGaussSurfaceGenerator(args_);
       	else if( args_.length==5 )
       		RG = new RandomGaussSurfaceGenerator(args_,args_[4]); // last argument is cly
       	else
       		System.out.println("Error in argument passing.");
    }
}