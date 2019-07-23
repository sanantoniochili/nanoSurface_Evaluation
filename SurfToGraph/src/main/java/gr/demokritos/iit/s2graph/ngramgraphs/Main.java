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

package gr.demokritos.iit.s2graph.ngramgraphs;

import gr.demokritos.iit.jinsect.documentModel.representations.DocumentNGramGraph;
import gr.demokritos.iit.jinsect.utils;
import gr.demokritos.iit.s2graph.ngramgraphs.TextReader;

import org.apache.commons.cli.*;
import org.jgrapht.io.ImportException;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] argv) throws IOException, ImportException {

        String in_filename = "";
        String out_filename = "";
        int out_flag = 0;

        Options options = new Options();

        Option input = new Option("in", "input", true, "input file");
        input.setRequired(true);
        options.addOption(input);

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

        in_filename = cmd.getOptionValue("in");
        if( cmd.hasOption("out") ){
            out_filename = cmd.getOptionValue("out");
            out_flag = 1;
        }

        TextReader reader = new TextReader(in_filename);
        String surf = reader.SurfToString();
        int iter = 0;
        while( surf!=null ) { // for all surface-texts

            // The default document n-gram graph
            // with min n-gram size and max n-gram size set to 3, and the dist parameter set to 3.
            DocumentNGramGraph dngGraph = new DocumentNGramGraph();

            // Create the graph
            dngGraph.setDataString(surf);

             /* The following command gets the first n-gram graph level (with the minimum n-gram
            size) and renders it, using the utils package, as a DOT string.
             digraph := directed graph
             graph := simple graph */
            String digraph =  utils.graphToDot(dngGraph.getGraphLevel(0), true);
            String graph =  utils.graphToDot(dngGraph.getGraphLevel(0), false);

            JGraphTWrapper jgraph = new JGraphTWrapper(reader.getParams());
            if( jgraph.convertString(digraph, graph)!=null ) {
                jgraph.vectorExtract();

                if (out_flag == 0) { // standard output
                    if (iter == 0) { // first iteration
                        // print names of columns
                        jgraph.printVNames();
                        iter++;
                    }
                    jgraph.printVector();
                } else { // file
                    if (iter == 0) { // first iteration
                        // print names of columns in blank file
                        FileWriter writer = new FileWriter(out_filename, false);
                        jgraph.printVNames(writer);
                        iter++;
                    }
                    try {
                        FileWriter writer = new FileWriter(out_filename, true);
                        jgraph.printVector(writer);
                    } catch (IOException ex) {
                        System.out.println("There was a problem creating/writing to the file");
                        ex.printStackTrace();
                    }
                }
            }

            surf = reader.SurfToString();

        }
    }
}
