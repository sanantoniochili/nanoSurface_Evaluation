package encode;

import gr.demokritos.iit.jinsect.documentModel.representations.DocumentNGramGraph;
import javafx.util.Pair;
import utils.BinaryTree;
import utils.Linspace;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

public class Encoder {

    // surface
    Surface S;
    // each pair contains the point's index (number/position) and the matched character
    Vector<Pair<Integer,Character>> Text;
    // each pair matches a letter to space ( <lowest_pair_value>,<highest_pair_value> ] for positive
    // or [ <lowest_pair_value>,<highest_pair_value> ) for negative
    // zero is included in [ 0,<value> ]
    Vector<Pair<Character,Double>> STable;


    Encoder(int spacesNo, Surface surface) {
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

    void changeSurface(Surface S) {
        this.S = S;
    }

    void InText() {

        // building a tree out of pairs <Character,BoundOfSpace>
        BinaryTree tree = new BinaryTree();
        tree.init(tree.sortedArrayToBST(STable, 0, STable.size() - 1, null));


        for (Pair<Integer,Double> point: S.points) {
            Double height = point.getValue();

            // building character array
            // one character per surface point
            Character ch = tree.search(new Double(height)); // finding corresponding character
            Text.addElement(new Pair(point.getKey(),ch));

        }
    }

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


}

