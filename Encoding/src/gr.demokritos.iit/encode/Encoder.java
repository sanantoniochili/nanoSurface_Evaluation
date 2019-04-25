/** Copyright 2018 Antonia Tsili
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

import gr.demokritos.iit.jinsect.documentModel.representations.DocumentNGramGraph;
import javafx.util.Pair;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import utils.BinaryTree;
import utils.ContinuousSplitBT;
import utils.Linspace;
import utils.TwoDirectSplitBT;

abstract class Encoder {
    // surface
    Surface S;
    // each pair contains the point's index (number/position) and the matched character
    Vector<Pair<Integer,Character>> Text;
    // each pair matches a letter to space ( <lowest_pair_value>,<highest_pair_value> ] for positive
    // or [ <lowest_pair_value>,<highest_pair_value> ) for negative
    // zero is included in [ 0,<value> ]
    Vector<Pair<Character,Double>> STable;

    Encoder(int spacesNo, Surface surface) {}

    void changeSurface(Surface S) {
        this.S = S;
        this.Text.clear();
    }

    void InText() {}

    void printText() {
        Iterator it = Text.iterator();

        for (int i=1; i<=S.TotalElementNo; i++) {
            Pair<Integer,Character> p = (Pair<Integer, Character>) it.next();
            System.out.print(p.getValue());
            if( i>=S.N && i%(S.N)==0 ) { // reached end of side: N x d + 0 = i
                System.out.println();
            }
        }
        System.out.println();
    }

    void printText(FileWriter writer) throws IOException {
        StringBuilder sb = new StringBuilder();
        Iterator it = Text.iterator();

        sb.append("rms:").append(String.valueOf((S.rms))); // printing parameters in first column as: <param_name>:<param_value>
        sb.append(":clx:").append(String.valueOf(S.clx));
        sb.append(":cly:").append(String.valueOf(S.cly));
        sb.append(":N:").append(String.valueOf(S.N));
        sb.append('\n');
        for (int i=1; i<=S.TotalElementNo; i++) {
            Pair<Integer,Character> p = (Pair<Integer, Character>) it.next();
            sb.append(p.getValue());
            if( i>=S.N && i%(S.N)==0 ) { // reached end of side: N x d + 0 = i
                sb.append('\n');
            }
        }
        sb.append("\n");
        writer.append(sb.toString());
        writer.close();
    }

    DocumentNGramGraph toGraph(String sFilename) {
        DocumentNGramGraph dngGraph = new DocumentNGramGraph();
        // Load the data string from the file, also dealing with exceptions
        try {
            dngGraph.loadDataStringFromFile(sFilename);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return dngGraph;
    }

    void changeHeights(int Scale) {
        // convert rms scale before changing heights
        this.S.distance_heights(this.S.rms*Math.pow(10,Scale));
    }

    void printHeights() {
        this.S.print_heights();
    }
}

class SimpleEncoder extends Encoder {
    SimpleEncoder(int spacesNo, Surface surface) {
        super(spacesNo,surface);
        Linspace lin = new Linspace(-100,100,spacesNo+1);
        double[] bounds = lin.op(); // find boundaries of zones

        STable = new Vector<>(spacesNo+1);

        if( spacesNo%2 == 0 ){ // use both capital and small letters
            // small letters correspond to negative values
            int Index = 0;
            char A = (char) (97 + spacesNo/2-1); // begin from the last small character
            Double val;

            for (; Index < spacesNo/2 ; Index++) {
                val = bounds[Index];
                Pair p = new Pair(A,val);
                STable.addElement(p);
                A--;
            }
            STable.addElement(new Pair('A',new Double(0)));
            Index = spacesNo/2+1;
            A = 65;
            for (; Index<spacesNo+1; Index++) {
                val = bounds[Index];
                Pair p = new Pair(A, val);
                STable.addElement(p);
                A++;
            }
        }
        S = new Surface(surface);
        Text = new Vector<>(S.TotalElementNo); // we have to build this

    }
    void InText() {
        // building a tree out of pairs <Character,BoundOfSpace>
        TwoDirectSplitBT tree = new TwoDirectSplitBT();
        tree.init(tree.sortedArrayToBST(STable, 0, STable.size() - 1, null));


        for (Pair<Integer,Double> point: S.points) {
            Double height = point.getValue();

            // building character array
            // one character per surface point
            Character ch = tree.search(new Double(height)); // finding corresponding character
            Text.addElement(new Pair(point.getKey(),ch));

        }
    }
}

class MinMaxSurf extends Surface {
    double minH;
    double maxH;

    MinMaxSurf(Surface S) {
        super(S);
        sort_heights(); // sort heights of surface

        this.minH = points.get(0).getValue(); // get minimum and max height of vector of pairs
        this.maxH = points.get(TotalElementNo-1).getValue(); // pairs of <index,height>
    }
}


class MinMaxEncoder extends Encoder {
    MinMaxSurf SortedSurf;
    int AvgElementNo; // average number of points of surface per zone
    int Remain;       // remaining number of points
    double[] bounds;

    MinMaxEncoder(int spacesNo, Surface S) {
        super(spacesNo,S);

        /*
         * SpaceNo: number of spaces to split [minH,maxH] into
         * S: Object of class Surface
         */
        AvgElementNo = S.get_totalNo() / spacesNo; // initialize average size of space
        Remain = S.get_totalNo() % spacesNo;
        SortedSurf = new MinMaxSurf(S);

        // creating array of zones' boundaries beginning from minH and maxH
        // initializing array on both ends
        // assuming heights tend to accumulate around zero
        bounds = new double[spacesNo+1]; // initialize array of zones' boundaries
        double beginLow = SortedSurf.minH; // beginning from lowest height
        double beginHigh = SortedSurf.maxH; // beginning from highest height

        // splitting spaces by counting elements
        // boundaries are mean value of space [last_zone_height,next_height]
        // and next existing height
        int CountElems = AvgElementNo-1;
        int Index = 0; // Index of boundaries' array
        int flag = 1; // change between high and low boundaries

        while( Index<=spacesNo/2 ) {
        // creating zones beginning from lowest height values
        // changing between lower and higher zones in each turn
            if( flag>0 ) { // new lower zone
                bounds[Index] = beginLow;
                // find mean height between point.height[CountElems] and point.height[CountElems+1]
                double sumPrevNext = SortedSurf.points.get(CountElems).getValue() + SortedSurf.points.get(CountElems+1).getValue();
                beginLow = sumPrevNext / (double) 2;
                if( Index==spacesNo/2 ) break; // no actions needed when half array index reached

            } else { // new upper zone
                bounds[spacesNo-Index] = beginHigh;
                // find mean height between point.height[N-1-CountElems] and point.height[N-1-CountElems-1]
                double sumPrevNext = SortedSurf.points.get(SortedSurf.TotalElementNo-1-CountElems).getValue() + SortedSurf.points.get(SortedSurf.TotalElementNo-2-CountElems).getValue();
                beginHigh = sumPrevNext / (double) 2;

                CountElems += (AvgElementNo-1); // index of upper zone's last height
                Index++;
            }
            flag *= -1;
        }

        STable = new Vector<>(spacesNo+1);
        char A = 65;
        for (int i=0; i<spacesNo+1; i++) {
            STable.addElement(new Pair(A, bounds[i]));
            A++;
        }

        this.S = new Surface(S);
        this.Text = new Vector<>(S.TotalElementNo); // we have to build this

    }

    void InText() {
        // building a tree out of pairs <Character,BoundOfSpace>
        ContinuousSplitBT tree = new ContinuousSplitBT();
        tree.init(tree.sortedArrayToBST(STable, 0, STable.size() - 1, null));


        for (Pair<Integer,Double> point: S.points) {
            Double height = point.getValue();

            // building character array
            // one character per surface point
            Character ch = tree.search(new Double(height)); // finding corresponding character
            Text.addElement(new Pair(point.getKey(),ch));

        }
    }
}

class MinMaxRMS extends Surface {
    double minR;
    double maxR;

    MinMaxRMS(Surface S) {
        super(S);
        sort_heights(); // sort dr = abs(height - rms) distances

        this.minR = points.get(0).getValue(); // get minimum and max dr of vector of pairs
        this.maxR = points.get(TotalElementNo-1).getValue(); // pairs of <index,height>
    }
}

class MinMaxRMSEncoder extends Encoder {
    MinMaxRMS SortedSurf;
    int AvgElementNo; // average number of points of surface per zone
    int Remain;       // remaining number of points
    double[] bounds;

    MinMaxRMSEncoder(int spacesNo, Surface S) {
        super(spacesNo,S);

        /*
         * SpaceNo: number of spaces to split [minH,maxH] into
         * S: Object of class Surface
         */
        AvgElementNo = S.get_totalNo() / spacesNo; // initialize average size of space
        Remain = S.get_totalNo() % spacesNo;
        SortedSurf = new MinMaxRMS(S);

        // creating array of zones' boundaries beginning from minH and maxH
        // initializing array on both ends
        // assuming heights tend to accumulate around zero
        bounds = new double[spacesNo+1]; // initialize array of zones' boundaries
        double beginLow = SortedSurf.minR; // beginning from lowest height
        double beginHigh = SortedSurf.maxR; // beginning from highest height

        // splitting spaces by counting elements
        // boundaries are mean value of space [last_zone_height,next_height]
        // and next existing height
        int CountElems = AvgElementNo-1;
        int Index = 0; // Index of boundaries' array
        int flag = 1; // change between high and low boundaries

        while( Index<=spacesNo/2 ) {
            // creating zones beginning from lowest height values
            // changing between lower and higher zones in each turn
            if( flag>0 ) { // new lower zone
                bounds[Index] = beginLow;
                // find mean height between point.height[CountElems] and point.height[CountElems+1]
                double sumPrevNext = SortedSurf.points.get(CountElems).getValue() + SortedSurf.points.get(CountElems+1).getValue();
                beginLow = sumPrevNext / (double) 2;
                if( Index==spacesNo/2 ) break; // no actions needed when half array index reached

            } else { // new upper zone
                bounds[spacesNo-Index] = beginHigh;
                // find mean height between point.height[N-1-CountElems] and point.height[N-1-CountElems-1]
                double sumPrevNext = SortedSurf.points.get(SortedSurf.TotalElementNo-1-CountElems).getValue() + SortedSurf.points.get(SortedSurf.TotalElementNo-2-CountElems).getValue();
                beginHigh = sumPrevNext / (double) 2;

                CountElems += (AvgElementNo-1); // index of upper zone's last height
                Index++;
            }
            flag *= -1;
        }

        STable = new Vector<>(spacesNo+1);
        char A = 65;
        for (int i=0; i<spacesNo+1; i++) {
            STable.addElement(new Pair(A, bounds[i]));
            A++;
        }

        this.S = new Surface(S);
        this.Text = new Vector<>(S.TotalElementNo); // we have to build this

    }

    void InText() {
        // building a tree out of pairs <Character,BoundOfSpace>
        ContinuousSplitBT tree = new ContinuousSplitBT();
        tree.init(tree.sortedArrayToBST(STable, 0, STable.size() - 1, null));


        for (Pair<Integer,Double> point: S.points) {
            Double height = point.getValue();

            // building character array
            // one character per surface point
            Character ch = tree.search(new Double(height)); // finding corresponding character
            Text.addElement(new Pair(point.getKey(),ch));

        }
    }
}
