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

import javafx.util.Pair;
import org.jgrapht.Graph;
import org.jgrapht.alg.clique.PivotBronKerboschCliqueFinder;
import org.jgrapht.alg.connectivity.BlockCutpointGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.graph.Pseudograph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.io.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.TimeUnit;


/**
 * This class is needed in order to translate OpenJGraph to JGraphT graph.
 * <br>The former is transformed into DOT string and eventually is read into the needed kind of graph.
 */
public class JGraphTWrapper {

    /**
     * Directed graph
     */
    Graph digraph;
    /**
     * Simple graph
     */
    Graph graph;
    /**
     * Surface Parameters
     */
    String[] params;
    /**
     * Surface encoded text
     */
    Vector<Pair<String,Double>> vector;
    int paramNo;

    /**
     * <p>Initialization of class object<p>
     *
     * @param params        Parameters of surface
     */
    public JGraphTWrapper(String[] params) {
        this.paramNo = params.length/2;
        this.params = params;
    }

    /**
     * <p>Convert DOT string to JGraphT object</p>
     *
     * @param digraph           Directed graph
     * @param graph             Simple graph
     * @return                  JGraphT result
     * @throws ImportException
     * @throws IOException
     */
    public Graph<String, DefaultEdge> convertString(String digraph, String graph) throws ImportException, IOException {
        // convert DOT string to JGraphT object
        VertexProvider<String> vp = (label, attrs) -> label;
        EdgeProvider<String, DefaultEdge> ep = (f, t, l, attrs) -> new DefaultEdge();
        ComponentUpdater<String> cu = (v, attrs) -> {
        };
        DOTImporter<String, DefaultEdge> importer = new DOTImporter<>(vp, ep, cu);
        DirectedPseudograph<String, DefaultEdge> jdigraph = new DirectedPseudograph<>(DefaultEdge.class);
        Pseudograph jgraph = new Pseudograph(DefaultEdge.class);

        importer.importGraph(jdigraph, new StringReader(digraph));
        importer.importGraph(jgraph, new StringReader(graph));

        // make jgraph simple
        SimpleGraph jsimple = new SimpleGraph(DefaultEdge.class);
        for (Object v: jgraph.vertexSet()) {
            jsimple.addVertex(v);
        }
        for (Object e: jgraph.edgeSet()) { // for each edge
            String source = (String) jgraph.getEdgeSource(e);
            String target = (String) jgraph.getEdgeTarget(e);
            if( !source.equals(target)  ) { // skip self loops
                if( !jsimple.containsEdge(e) )
                    jsimple.addEdge(source,target,e);
            }
        }

        this.digraph = jdigraph;
        this.graph = jsimple;

// for testing purposes
if( jgraph.vertexSet().size() > 1000 )
    return null;

        return jgraph;
    }

    /**
     * <p>After the creation of graph, we extract a vector of its characteristics,
     * describing it through basic Graph Theory concepts</p>
     *
     * @return      Characteristics' vector
     */
    public Vector<Pair<String,Double>> vectorExtract() {

        Vector<Pair<String,Double>> vector = new Vector<>(6+paramNo);

        // first graph properties
        vector.addElement(new Pair<>("V", (double) digraph.vertexSet().size()));
        vector.addElement(new Pair<>("E", (double) digraph.edgeSet().size()));
        vector.addElement(new Pair<>("avgDegree", avgDegree())); // ok up till here
        vector.addElement(new Pair<>("avgPath",avgPath()));
        vector.addElement(new Pair<>("maxCliques", (double) cliques()));
        vector.addElement(new Pair<>("Blocks", (double) Blocks()));

        // then add surface parameters
        for (int i=0; i+1<params.length; i+=2) {
            // add name of parameter and value
            vector.addElement(new Pair<>(params[i],Double.parseDouble(params[i+1])));
        }

        this.vector = vector;
        return vector;
    }

    /**
     * @return      Average length of shortest paths
     */
    Double avgPath() {
        FloydWarshallShortestPaths fw = new FloydWarshallShortestPaths(this.digraph);

        String source = "";
        Iterator<String> it = digraph.vertexSet().iterator();
        if( it.hasNext() )
            source = it.next(); // get first vertex
        else
            return -1.0;

        // compute shortest paths to all other vertices and save to graph <pathG>
        ShortestPathAlgorithm.SingleSourcePaths pathG =  fw.getPaths(source);

        Double AvgLength = 0.0;
        // number of paths from source to rest
        Double total = new Double(pathG.getGraph().vertexSet().size()-1);
        for (Object v: pathG.getGraph().vertexSet()) { // for each vertex
            if( !source.equals((String)v) ) {            // except source
                AvgLength += ( pathG.getPath(v).getLength() / total );
            }
        }

        return AvgLength;
    }

    /**
     * @return      Average degree of the graph's nodes
     */
    Double avgDegree() {

        Double AvgD = 0.0; // avg sum of degrees
        Double total = new Double(digraph.vertexSet().size()); // total number of vertices
        for (Object v: digraph.vertexSet()) { // for each vertex
            AvgD += ( digraph.degreeOf(v)/total );
        }

        return AvgD;
    }

    /**
     * @return      Number of cliques found in the graph
     */
    int cliques() {
        // find cliques on simple graph
        PivotBronKerboschCliqueFinder<Object, DefaultEdge> cf = new PivotBronKerboschCliqueFinder<>(graph,60, TimeUnit.SECONDS);

        int i = 0;
        Iterator<Set<Object>> it = cf.maximumIterator();
        while(  it.hasNext() ) {
            it.next();
            i++;
        }

        return i;
    }

    /**
     * @return      Number of blocks found in graph
     */
    int Blocks() {
        BlockCutpointGraph BC = new BlockCutpointGraph(graph);

        return BC.getBlocks().size();
    }

    /**
     * <p>Printing characteristics' names to file</p>
     *
     * @param writer        Object that writes to the resulting file
     * @throws IOException
     */
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

    /**
     * <p>Printing characteristics' vector to standard output</p>
     *
     * @throws IOException
     */
    void printVNames() throws IOException {
        for (int i=0; i<vector.size(); i++) {
            Pair<String,Double> p = vector.get(i);
            System.out.print(p.getKey());
            if( i<vector.size()-1 ) System.out.print(",");
        }
        System.out.println();
    }

    /**
     * <p>Printing characteristics' vector to file</p>
     *
     * @param writer        Object that writes to the resulting file
     * @throws IOException
     */
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

    /**
     * <p>Printing characteristics' vector to standard output</p>
     *
     * @throws IOException
     */
    void printVector() throws IOException {
        for (int i=0; i<vector.size(); i++) {
            Pair<String,Double> p = vector.get(i);
            System.out.print(p.getValue());
            if( i<vector.size()-1 ) System.out.print(",");
        }
        System.out.println();
    }

}
