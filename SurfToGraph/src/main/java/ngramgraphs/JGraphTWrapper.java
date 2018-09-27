package ngramgraphs;

import org.jgrapht.Graph;
import org.jgrapht.alg.clique.PivotBronKerboschCliqueFinder;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.BidirectionalDijkstraShortestPath;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.io.*;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static javafx.scene.input.KeyCode.V;

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
    public void vectorExtract() {
        avgPath();
        G.vertexSet().size();
        G.edgeSet().size();



    }

    Double avgPath() {
        FloydWarshallShortestPaths fw = new FloydWarshallShortestPaths(this.G);

        String source = "";
        Iterator<String> it = G.vertexSet().iterator();
        if( it.hasNext() )
            source = it.next(); // get first vertex

        // compute shortest paths to all other vertices
        ShortestPathAlgorithm.SingleSourcePaths pathG =  fw.getPaths(source);
        String root = (String) pathG.getSourceVertex();

        Double AvgLength = 0.0;
        Double total = new Double(pathG.getGraph().vertexSet().size()-1); // number of paths from source to rest
        for (Object v: pathG.getGraph().vertexSet()) { // for each vertex
            if( !root.equals((String)v) ) { // except source                AvgLength += ( pathG.getPath(v).getLength()/total );
            }
        }

        return AvgLength;
    }

    Double avgDegree() {

        Double AvgD = 0.0; // avg sum of degrees
        Double total = new Double(G.vertexSet().size()); // total number of vertices
        for (Object v: G.vertexSet()) { // for each vertex
            AvgD += ( G.degreeOf(v)/total );
        }

        return AvgD;
    }

    int cliques() {
        PivotBronKerboschCliqueFinder cf = new PivotBronKerboschCliqueFinder(G);

        int i = 0;
        Iterator<Set> it = cf.iterator();
        while(  it.hasNext() ) {
            Set clique = it.next();
            i++;
        }

        return i;
    }
}
