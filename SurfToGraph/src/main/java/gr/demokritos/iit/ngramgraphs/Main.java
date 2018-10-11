package gr.demokritos.iit.ngramgraphs;

import gr.demokritos.iit.jinsect.documentModel.representations.DocumentNGramGraph;
import gr.demokritos.iit.jinsect.utils;
import gr.demokritos.iit.input_load.TextReader;
import org.jgrapht.io.ImportException;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] argv) throws IOException, ImportException {

        String in_filename = "";
        String out_filename = "";
        int out_flag = 0;

        for (int i=0 ; i<argv.length ; i++) {
            if( argv[i].equals("-in") ) {
                in_filename = argv[++i];
            }
            if( argv[i].equals("-out") ) {
                out_flag = 1;
                out_filename = argv[++i];
            }
        }
        if( in_filename==null ) {
            System.out.println("Please provide \".txt\" file.");
            return;
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
