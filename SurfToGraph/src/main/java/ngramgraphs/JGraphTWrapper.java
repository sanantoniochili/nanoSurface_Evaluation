package ngramgraphs;

import javafx.util.Pair;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.clique.BronKerboschCliqueFinder;
import org.jgrapht.alg.connectivity.BiconnectivityInspector;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.io.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;


public class JGraphTWrapper {

    Graph G;
    String[] params;
    Vector<Pair<String,Double>> vector;
    int paramNo;

    public JGraphTWrapper(String[] params) {
        this.paramNo = params.length/2;
        this.params = params;
    }

    public Graph<String, DefaultEdge> convertString(String graph) throws ImportException {
        // convert DOT string to JGraphT object
        VertexProvider<String> vp = (label, attrs) -> label;
        EdgeProvider<String, DefaultEdge> ep = (f, t, l, attrs) -> new DefaultEdge();
        ComponentUpdater<String> cu = (v, attrs) -> {
        };
        DOTImporter<String, DefaultEdge> importer = new DOTImporter<>(vp, ep, cu);

        DirectedPseudograph<String, DefaultEdge> jgraph = new DirectedPseudograph<>(DefaultEdge.class);
        importer.importGraph(jgraph, new StringReader(graph));

        this.G = jgraph;

        return jgraph;
    }

    public Vector<Pair<String,Double>> vectorExtract() {

        Vector<Pair<String,Double>> vector = new Vector<>(6+paramNo);

        // first graph properties
        vector.addElement(new Pair<>("V", (double) G.vertexSet().size()));
        vector.addElement(new Pair<>("E", (double) G.edgeSet().size()));
       /*ok up till here*/ vector.addElement(new Pair<>("avgDegree", avgDegree()));
        vector.addElement(new Pair<>("avgPath",avgPath()));
        //vector.addElement(new Pair<>("maxCliques", (double) cliques()));
        //vector.addElement(new Pair<>("connectedComponents", (double) connectedComponents()));

        // then add surface parameters
        for (int i=0; i+1<params.length; i+=2) {
            // add name of parameter and value
            vector.addElement(new Pair<>(params[i],Double.parseDouble(params[i+1])));
        }

        this.vector = vector;
        return vector;
    }

    Double avgPath() {
        FloydWarshallShortestPaths fw = new FloydWarshallShortestPaths(this.G);

        String source = "";
        Iterator<String> it = G.vertexSet().iterator();
        if( it.hasNext() )
            source = it.next(); // get first vertex
        else
            return -1.0;

        Double AvgLength = 0.0;
        Double total = 0.0; // number of paths from source to rest
        String vertex = "";
        // compute shortest paths to all other vertices
        while( it.hasNext() ) {
            vertex = it.next();
            GraphPath Gpath = fw.getPath(source,vertex);
            if( Gpath!=null ) {
                AvgLength += Gpath.getLength();
                total++;
            }
        }
        AvgLength /= total;
    /*
        // compute shortest paths to all other vertices
        ShortestPathAlgorithm.SingleSourcePaths pathG =  fw.getPaths(source);
        String root = (String) pathG.getSourceVertex();

        Double AvgLength = 0.0;
        Double total = new Double(pathG.getGraph().vertexSet().size()-1); // number of paths from source to rest
        for (Object v: pathG.getGraph().vertexSet()) { // for each vertex
            if( !root.equals((String)v) ) {            // except source
                AvgLength += ( pathG.getPath(v).getLength()/total );
            }
        }
    */
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
        // construct new graph similar to G
        // new graph is simple
        SimpleGraph<Object, DefaultEdge> NG= new SimpleGraph<>(DefaultEdge.class);

        for (Object v: G.vertexSet()) { // for each vertex
            NG.addVertex(v);
        }
        for (Object e: G.edgeSet()) { // for each vertex
            String source = (String) G.getEdgeSource(e);
            String target = (String) G.getEdgeTarget(e);
            if( !(source.equals(target))  ) { // skip self loops
                if( !NG.containsEdge(source,target) ) { // skip parallel edges
                    NG.addEdge(source,target);
                }
            }
        }

        // find cliques on new simple graph
        BronKerboschCliqueFinder<Object, DefaultEdge> cf = new BronKerboschCliqueFinder<>(NG);

        int i = 0;
        Iterator<Set<Object>> it = cf.iterator();
        while(  it.hasNext() ) {
            it.next();
            i++;
        }

        return i;
    }

    int connectedComponents() {
        BiconnectivityInspector cin = new BiconnectivityInspector(G);

        int i = 0;
        Set CC = cin.getConnectedComponents();

        return CC.size();
    }

    void printVNames(FileWriter writer) throws IOException {
        StringBuilder sb = new StringBuilder();

        for (int i=0; i<vector.size(); i++) {
            Pair<String,Double> p = vector.get(i);
            sb.append(p.getKey());
            if( i<vector.size()-1 ) sb.append(",");
        }
        sb.append("\n");
        writer.append(sb.toString());
        writer.close();
    }

    void printVNames() throws IOException {
        for (int i=0; i<vector.size(); i++) {
            Pair<String,Double> p = vector.get(i);
            System.out.print(p.getKey());
            if( i<vector.size()-1 ) System.out.print(",");
        }
        System.out.println();
    }

    void printVector(FileWriter writer) throws IOException {
        StringBuilder sb = new StringBuilder();

        for (int i=0; i<vector.size(); i++) {
            Pair<String,Double> p = vector.get(i);
            sb.append(p.getValue());
            if( i<vector.size()-1 ) sb.append(",");
        }
        sb.append("\n");
        writer.append(sb.toString());
        writer.close();
    }

    void printVector() throws IOException {
        for (int i=0; i<vector.size(); i++) {
            Pair<String,Double> p = vector.get(i);
            System.out.print(p.getValue());
            if( i<vector.size()-1 ) System.out.print(",");
        }
        System.out.println();
    }

}
