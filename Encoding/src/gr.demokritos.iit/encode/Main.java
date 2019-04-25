/** 
* Copyright 2018 Antonia Tsili
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package encode;
/*
 * Assuming that most heights vary between -100 and 100 nm among nanostructured surfaces.
 * Splitting space [-100nm,100nm] to even subspaces and  labelling them with Latin letters.
 */

//import gr.demokritos.ssimple.input_load.CSVRead;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import loadinput.CSVRead;

public class Main {
    static int SpacesNo = 1;
    static int Scale = 0;

    public static void main(String[] argv) throws IOException {

        String csvFile = "";
        String out_filename = "";
        int method = 0;
        int out_flag = 0;

        for (int i=0 ; i<argv.length ; i++) {
            if (argv[i].equals("-in")) {
                csvFile = argv[++i];
            }
            if (argv[i].equals("-z")) { // number of spaces to divide [-100nm,100nm] into
                SpacesNo = Integer.parseInt(argv[++i]);
               /* if( SpacesNo>26 && (SpacesNo%2 != 0) ){
                    System.out.println("Please provide different number of spaces");
                }*/

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
            if( argv[i].equals("-method") ){
                method = Integer.parseInt(argv[++i]);
            }

        }
        while( method==0 ){
            System.out.println("Please select method of encoding:");
            Scanner scanner = new Scanner(System.in);
            method = Integer.parseInt(scanner.next());
        }

        Encoder encoder = null;
        CSVRead reader = new CSVRead(csvFile,Scale);
        if( method==6 ) {
            encoder = new MinMaxRMSEncoder(SpacesNo, reader.SurfTable.get(0));
            for (int i=1; i<reader.SurfTable.size(); i++) { // for all surfaces in file

                //change values from heights to distance from c (rms)
                encoder.changeHeights(Scale);
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
            encoder.changeHeights(Scale);
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
        } else {
            if (method == 1)
                encoder = new SimpleEncoder(SpacesNo, reader.SurfTable.get(0));
            else if (method == 4)
                encoder = new MinMaxEncoder(SpacesNo, reader.SurfTable.get(0));
            for (int i = 1; i < reader.SurfTable.size(); i++) { // for all surfaces in file

                // encode in text and print surface
                encoder.InText();
                if (out_flag == 0) { // standard output
                    encoder.printText();
                } else { // file
                    try {
                        FileWriter writer = new FileWriter(out_filename, true);
                        encoder.printText(writer);
                    } catch (IOException ex) {
                        System.out.println("There was a problem creating/writing to the file");
                        ex.printStackTrace();
                    }
                }

                encoder.changeSurface(reader.SurfTable.get(i)); // next surface to encode
            }
            // print last surface
            encoder.InText();
            if (out_flag == 0) { // standard output
                encoder.printText();
            } else { // file
                try {
                    FileWriter writer = new FileWriter(out_filename, true);
                    encoder.printText(writer);
                } catch (IOException ex) {
                    System.out.println("There was a problem creating/writing to the file");
                    ex.printStackTrace();
                }
            }

            System.out.println();
        }

    }
}
