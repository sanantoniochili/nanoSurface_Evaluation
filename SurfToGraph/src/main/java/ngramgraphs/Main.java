package ngramgraphs;

import gr.demokritos.iit.jinsect.documentModel.representations.DocumentNGramGraph;
import gr.demokritos.iit.jinsect.utils;
import input_load.TextReader;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.io.*;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.io.IOException;
import java.io.StringReader;

public class Main {
    public static void main(String[] argv) throws IOException, ImportException {
        TextReader reader = new TextReader("../EncodeSimple/small_results_SurfText1.txt");
        String surf = "";
        //do{ // turn all surface-text to string
            surf = reader.SurfToString();

            // The default document n-gram graph
            // with min n-gram size and max n-gram size set to 3, and the dist parameter set to 3.
            DocumentNGramGraph dngGraph = new DocumentNGramGraph();

            // Create the graph
            dngGraph.setDataString(surf);

             /* The following command gets the first n-gram graph level (with the minimum n-gram
            size) and renders it, using the utils package, as a DOT string */
            String graph =  utils.graphToDot(dngGraph.getGraphLevel(0), true);
    // System.out.println(graph);

            // convert DOT string to JGraphT object
            VertexProvider<String> vp = (label, attrs) -> label;
            EdgeProvider<String, DefaultEdge> ep = (f, t, l, attrs) -> new DefaultEdge();
            ComponentUpdater<String> cu = (v, attrs) -> {
            };
            DOTImporter<String, DefaultEdge> importer = new DOTImporter<>(vp, ep, cu);

            DirectedPseudograph<String, DefaultEdge> jgraph =
                    new DirectedPseudograph<>(DefaultEdge.class);
            importer.importGraph(jgraph, new StringReader(graph));

    // System.out.println(jgraph.toString());

            DijkstraShortestPath dpath = new DijkstraShortestPath(jgraph);

        //}while( surf!=null ); // for all surface-texts
    }
}
