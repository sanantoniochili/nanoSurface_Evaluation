
package generator;

import java.util.*;
import java.io.*;

import operations.*;

public class Main {

    public static void main(String[] argv) throws Exception{	
    	if( argv.length<4 ){
    		System.out.println("Error in argument passing.");
    		return;
    	}
    	String filename= new String();
    	double[] args_ = new double[argv.length];
    	int y_flag 	   = 0;
    	int out_flag   = 0;
        for (int i=0 ; i<argv.length ; i++) {
            if( argv[i].equals("-N") )
            	args_[0] = Double.parseDouble(argv[++i]);
            if( argv[i].equals("-rL") )
            	args_[1] = Double.parseDouble(argv[++i]);
            if( argv[i].equals("-h") )
            	args_[2] = Double.parseDouble(argv[++i]);
            if( argv[i].equals("-clx") )
            	args_[3] = Double.parseDouble(argv[++i]);
            if( argv[i].equals("-cly") ){
            	args_[4] = Double.parseDouble(argv[++i]);
            	y_flag = 1;
            }
            if( argv[i].equals("-out") ){
    			filename = new String(argv[++i]);
    			out_flag = 1;
            }
        }
    	
        RandomGaussSurfaceGenerator RG;
        if( y_flag==0 )
       		RG = new RandomGaussSurfaceGenerator(args_); // isotropic
       	else
       		RG = new RandomGaussSurfaceGenerator(args_,args_[4]); // non-isotropic,last argument is cly

       	if( out_flag==0 ){
       		PrintStream ps = new PrintStream(System.out); // standard output
       		RG.printArray(RG.Surf,ps);
       	} else {
       		File outFile = new File(filename);
  		    FileOutputStream fout = new FileOutputStream(outFile);
  		    PrintStream ps = new PrintStream(fout); // output file <filename>
  		    RG.printArray(RG.Surf,ps);

       	}
    }

}