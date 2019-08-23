/** 
* Copyright 2018 Antonia Tsili NCSR Demokritos
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

package gr.demokritos.iit.encode;

import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import gr.demokritos.iit.loadinput.CSVRead;

/**
 *
 * <p>Application which performs the conversion of a file of numbers
 * to a file of text using a particular given method (<i>-m</i>) of encoding.
 * The input (<i>-in</i>) and output (<i>-out</i>) files have particular format.</p>
 *
 * <p>Assuming that most heights vary between -100 and 100 nm among nanostructured surfaces.
 * <br>Splitting space [-100nm,100nm] to a number of even subspaces (<i>-z</i>) and  labelling them with Latin letters.
 * <br>Numbers are thought to measure nanometres*10^Scale (<i>-s</i>)"</p>
 *
 * @author  Antonia Tsili
 * @version 1.0
 * @since   2018-08
 *
 */
public class Conversion {

    /**
     * Number of spaces to split range of heights into
     */
    static int SpacesNo = 1;
    /**
     * Relation to nanometres
     */
    static int Scale = 0;

    /**
     *
     * @param argv input_file,number_of_spaces,scale,method_of_encoding,output_file*
     * @throws IOException
     *
     * <p><i>Asterisk (*) denotes optional argument.</i>
     * <br><i>Input file(.cvs) format:
     * input file(.cvs) format: rms:,clx:&lt;value&gt;,cly:&lt;value&gt;,N:&lt;value&gt;,(&lt;height&gt;,)*&lt;height&gt; surface per line
     *
     * <ul>
     *     <li>&lt;parameter&gt;:&lt;value&gt; for first column</li>
     *     <li>height per column for rest of columns</li>
     * </ul>
     * </p>
     *
     * <p><i>Note: When using methods 4 or 6 (from "Encodings.pdf") it is discouraged to use flag -z with a value greater than 26</i></p>
     */
    public static void main(String[] argv) throws IOException {

        String csvFile = "";
        String out_filename = "";
        int method = 0;
        int out_flag = 0;

        Options options = new Options();

        Option input = new Option("in", "input", true, "input file");
        input.setRequired(true);
        options.addOption(input);

        Option spaces = new Option("z", "spaces", true, "number of spaces to split into");
        spaces.setRequired(true);
        options.addOption(spaces);

        Option scale = new Option("s", "scale", true, "heights measured in nanometres*10^n");
        scale.setRequired(true);
        options.addOption(scale);

        Option method_ = new Option("m", "method", true, "method of encoding");
        method_.setRequired(true);
        options.addOption(method_);

        Option output = new Option("out", "output", true, "output file");
        output.setRequired(false);
        options.addOption(output);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, argv);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

        csvFile = cmd.getOptionValue("in");
        SpacesNo = Integer.parseInt((String) cmd.getOptionValue("z")); // number of spaces to divide [-100nm,100nm] into
        Scale = Integer.parseInt((String) cmd.getOptionValue("s")); // in case input is not measured in nm

        if( cmd.hasOption("out") ) {  // print text to output file
            out_filename = cmd.getOptionValue("out");
            out_flag = 1;
        }

        File f = new File(out_filename);  // check if file exists
        if(f.exists() && !f.isDirectory()) { // erase content if exists
            FileWriter writer = new FileWriter(out_filename);
            writer.write("");
            writer.close();
        }

        method = Integer.parseInt(cmd.getOptionValue("m")); // code of preferred method of encoding

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
