package gr.demokritos.ssimple.encode;

import javafx.util.Pair;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class Surface {

    int TotalElementNo; // total number of surface points
    int N; // number of surface points (along square side)

    double rms;
    double clx;
    double cly;

    Vector<Pair<Integer,Double>> points; // save points as pairs of <index,height>

    public Surface(double rms, double clx, double cly, int total, int side) {
        this.rms = rms;
        this.clx = clx;
        this.cly = cly;
        this.TotalElementNo = total;
        this.N = side;

        points = new Vector<>(total);
    }

    public Surface(Surface S) {
        this.rms = S.rms;
        this.clx = S.clx;
        this.cly = S.cly;
        this.TotalElementNo = S.TotalElementNo;
        this.N = S.N;

        points = new Vector<>(S.points);
    }

    public void add_height(int i, Double z) {
        Pair p = new Pair(i,z);
        points.addElement(p);
    }

    public void sort_heights() {
        Collections.sort(points, new Comparator<Pair<Integer, Double>>() {
            @Override
            public int compare(final Pair<Integer, Double> o1, final Pair<Integer, Double> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
    }

    public void print_heights() {
        for(int i=0; i < TotalElementNo; i++){
            System.out.println("value: "+points.get(i).getValue()+" index: "+points.get(i).getKey());
        }
    }

    public int get_totalNo() {
        return TotalElementNo;
    }


}
