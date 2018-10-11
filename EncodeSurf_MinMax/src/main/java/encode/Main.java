/* Project is not finished.
* Till now: Actions to split [minH,maxH] of surface to zones with same amount of points. Creating array of boundaries.
* Beginning from both ends and meeting in the middle.
* Probably: Find concentration to each zone's mean height and rearrange boundaries.
* Must: Encode points to letters based on input number of spaces.
* (Impl 4 of encodings)
 */

package encode;

import input_load.CSVRead;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    static int SpacesNo = 10;
    static int Scale = 1;
    public static void main(String[] argv) throws IOException {

        String csvFile = "";
        String out_filename = "";
        int out_flag = 0;

        for (int i=0 ; i<argv.length ; i++) {
            if (argv[i].equals("-in")) {
                csvFile = argv[++i];
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

        CSVRead reader = new CSVRead(csvFile,1);
        Encoder encoder = new Encoder(SpacesNo, reader.SurfTable.get(0));
        for (int i=1; i<reader.SurfTable.size(); i++) { // for all surfaces in file

            // encode in text and print surface
            encoder.InText();
            if( out_flag==0 ){ // standard output
                encoder.printText();
            } else { // file
                try{
                    FileWriter writer = new FileWriter(out_filename,true);
                    encoder.printText(writer);
                } catch (IOException ex){
                    System.out.println("There was a problem creating/writing to the file");
                    ex.printStackTrace();
                }
            }

            encoder.changeSurface(reader.SurfTable.get(i)); // next surface to encode
        }
        // print last surface
        encoder.InText();
        if( out_flag==0 ){ // standard output
            encoder.printText();
        } else { // file
            try{
                FileWriter writer = new FileWriter(out_filename,true);
                encoder.printText(writer);
            } catch (IOException ex){
                System.out.println("There was a problem creating/writing to the file");
                ex.printStackTrace();
            }
        }

        System.out.println();

    }
}
