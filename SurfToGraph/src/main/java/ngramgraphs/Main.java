package ngramgraphs;

import gr.demokritos.iit.jinsect.documentModel.representations.DocumentNGramGraph;
import gr.demokritos.iit.jinsect.utils;
import input_load.TextReader;
import org.jgrapht.Graph;
import org.jgrapht.io.ImportException;

import java.io.IOException;
import java.io.StringReader;

public class Main {
    public static void main(String[] argv) throws IOException, ImportException {

        String in_filename = "";
        for (int i=0 ; i<argv.length ; i++) {
            if (argv[i].equals("-in")) {
                in_filename = argv[++i];
            } else {
                System.out.println("Please provide \".txt\" file.");
            }
        }
        TextReader reader = new TextReader(in_filename);
        String surf = "";
        //do{ // turn all surface-text to string
            surf = reader.SurfToString(1);

            // The default document n-gram graph
            // with min n-gram size and max n-gram size set to 3, and the dist parameter set to 3.
            DocumentNGramGraph dngGraph = new DocumentNGramGraph();

            // Create the graph
            dngGraph.setDataString(surf);

             /* The following command gets the first n-gram graph level (with the minimum n-gram
            size) and renders it, using the utils package, as a DOT string */
            String graph =  utils.graphToDot(dngGraph.getGraphLevel(0), true);

            JGraphTWrapper jgraph = new JGraphTWrapper();

            jgraph.convertString(graph);
            jgraph.vectorExtract();

        //}while( surf!=null ); // for all surface-texts
    }
}
