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

package gr.demokritos.iit.encode;

import javafx.util.Pair;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import static java.lang.Math.abs;

/**
 * Class that represents the bit of nano-surface to examine.
 * <br>Each Surface refers to a square part of physical surface that has gone through sampling of its heights.
 * Consequently, each object is defined by a number N of points on each square side and NxN in total.
 * <br>It also carries all necessary fields that characterize the height distribution of the surface's points.
 */
public class Surface {

    /**
     * Total number of surface points
     */
    int TotalElementNo;
    /**
     * Number of surface points (along square side)
     */
    int N;

    /**
     * Root Mean Square Height
     */
    double rms;
    /**
     * Correlation length on x axis
     */
    double clx;
    /**
     * Correlation length on y axis
     */
    double cly;

    /**
     * Save points as pairs of (<i>index</i>,<i>height</i>)
     */
    Vector<Pair<Integer,Double>> points;

    /**
     * <p>Initialization</p>
     *
     * @param rms       Root Mean Square Height
     * @param clx       Correlation length x
     * @param cly       Correlation length y (<i>optional</i>)
     * @param total     Total number of elements
     * @param side      Number of elements on square side
     */
    public Surface(double rms, double clx, double cly, int total, int side) {
        this.rms = rms;
        this.clx = clx;
        this.cly = cly;
        this.TotalElementNo = total;
        this.N = side;

        points = new Vector<>(total);
    }

    /**
     * <p>Copy surface</p>
     *
     * @param S         Surface instance
     */
    public Surface(Surface S) {
        this.rms = S.rms;
        this.clx = S.clx;
        this.cly = S.cly;
        this.TotalElementNo = S.TotalElementNo;
        this.N = S.N;

        points = new Vector<>(S.points);
    }

    /**
     * <p>Add a height point of the surface into the structure</p>
     *
     * @param i     Point id
     * @param z     Height of point on surface
     */
    public void add_height(int i, Double z) {
        Pair p = new Pair(i,z);
        points.addElement(p);
    }

    /**
     * <p>Sort heights of surface</p>
     */
    public void sort_heights() {
        Collections.sort(points, new Comparator<Pair<Integer, Double>>() {
            @Override
            public int compare(final Pair<Integer, Double> o1, final Pair<Integer, Double> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
    }

    /**
     * <p>Print saved heights of surface points</p>
     */
    public void print_heights() {
        for(int i=0; i < TotalElementNo; i++){
            System.out.println("value: "+points.get(i).getValue()+" index: "+points.get(i).getKey());
        }
    }

    /**
     * @return      Total number of points
     */
    public int get_totalNo() {
        return TotalElementNo;
    }

    // compute (+ or -)|height-c|
    public void distance_heights(Double c) {
        for (int i=0; i<this.points.size(); i++) {
            double height = points.get(i).getValue();
            double dr = abs(height - c);
            Pair p = null;
            if( height >= 0 )
                p = new Pair(points.get(i).getKey(),dr);
            else
                p = new Pair(points.get(i).getKey(),-dr);
            points.setElementAt(p,i);
        }
    }
}
