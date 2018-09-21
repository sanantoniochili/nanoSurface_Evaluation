/*
* Assuming that most heights vary between -100 and 100 nm among nanostructured surfaces.
* Splitting space [-100nm,100nm] to even subspaces and  labelling them with Latin letters.
*/

package encode;

import input_load.CSVRead;
import javafx.util.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.Iterator;

public class Main {
    static int SpacesNo = 1;
    static int Scale = 0;
    public static void main(String[] argv) throws IOException {
        
        String csvFile = "";
        String out_filename = "";
        int out_flag = 0;

        for (int i=0 ; i<argv.length ; i++) {
            if (argv[i].equals("-in")) {
                csvFile = argv[++i];
            }
            if (argv[i].equals("-z")) {
                SpacesNo = Integer.parseInt(argv[++i]);
                if( SpacesNo>26 && (SpacesNo%2 != 0) ){
                    System.out.println("Please provide different number of spaces");
                }

            }
            // in case input is not measured in nm
            // e.g. to multiply all by 10^1: -scale 1
            if (argv[i].equals("-scale")) {
                Scale = Integer.parseInt(argv[++i]);
            }
            // print text to output file
            if( argv[i].equals("-out") ){
                out_filename = argv[++i];
                out_flag = 1;

                // check if file exists
                File f = new File(out_filename);
                // erase content if exists
                if(f.exists() && !f.isDirectory()) {
                    FileWriter writer = new FileWriter(out_filename);
                    writer.write("");
                    writer.close();
                }
            }
        }
        CSVRead reader = new CSVRead(csvFile,Scale);
        Encoder encoder = new Encoder(SpacesNo, reader.STable.get(0));
        encoder.InText();

        if( out_flag==0 ){ // standard output
            encoder.printText();
        } else {
            try{
                FileWriter writer = new FileWriter(out_filename,true);
                encoder.printText(writer);
            } catch (IOException ex){
                System.out.println("There was a problem creating/writing to the file");
                ex.printStackTrace();
            }
        }
    }
}
