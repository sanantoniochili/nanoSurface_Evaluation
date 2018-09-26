package ngramgraphs;

import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.shortestpath.BidirectionalDijkstraShortestPath;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.io.*;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.io.StringReader;
import java.util.Set;

public class JGraphTWrapper {

    Graph G;

    public Graph convertString(String graph) throws ImportException {
        // convert DOT string to JGraphT object
        VertexProvider<String> vp = (label, attrs) -> label;
        EdgeProvider<String, DefaultEdge> ep = (f, t, l, attrs) -> new DefaultEdge();
        ComponentUpdater<String> cu = (v, attrs) -> {
        };
        DOTImporter<String, DefaultEdge> importer = new DOTImporter<>(vp, ep, cu);

        DirectedPseudograph<String, DefaultEdge> jgraph =
                new DirectedPseudograph<>(DefaultEdge.class);
        importer.importGraph(jgraph, new StringReader(graph));

        this.G = jgraph;

        return jgraph;

    }
}
