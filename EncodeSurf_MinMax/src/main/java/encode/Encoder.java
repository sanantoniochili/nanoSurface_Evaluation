package encode;

import operations.Linspace;
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

public class Encoder {

    MinMaxSurf SortedSurf;
    int AvgElementNo; // average number of points of surface per zone
    int Remain;       // remaining number of points
    
    double[] bounds;
    /*
    * SpaceNo: number of spaces to split [minH,maxH] into
    * S: Object of class Surface
     */
    Encoder(int SpaceNo, Surface S) {
        AvgElementNo = S.get_totalNo() / SpaceNo; // initialize average size of space
        Remain = S.get_totalNo() % SpaceNo;
        SortedSurf = new MinMaxSurf(S);

        // creating array of zones' boundaries beginning from minH and maxH
        // initializing array on both ends
        // assuming heights tend to accumulate around zero
        bounds = new double[SpaceNo+1]; // initialize array of zones' boundaries
        double beginLow = SortedSurf.minH; // beginning from lowest height
        double beginHigh = SortedSurf.maxH; // beginning from highest height

        // splitting spaces by counting elements
        // boundaries are mean value of space [last_zone_height,next_height]
        // and next existing height
        int CountElems = AvgElementNo-1;
        int Index = 0; // Index of boundaries' array
        int flag = 1; // change between high and low boundaries

        while( Index<=SpaceNo/2 ) {
            // creating zones beginning from lowest height values
            // changing between lower and higher zones in each turn

            if( flag>0 ) { // new lower zone
                bounds[Index] = beginLow;
                // find mean height between point.height[CountElems] and point.height[CountElems+1]
                double sumPrevNext = SortedSurf.points.get(CountElems).getValue() + SortedSurf.points.get(CountElems+1).getValue();
                beginLow = sumPrevNext / (double) 2;

                if( Index==SpaceNo/2 ) break; // no actions needed when half array index reached

            } else { // new upper zone
                bounds[SpaceNo-Index] = beginHigh;
                // find mean height between point.height[N-1-CountElems] and point.height[N-1-CountElems-1]
                double sumPrevNext = SortedSurf.points.get(SortedSurf.TotalElementNo-1-CountElems).getValue() + SortedSurf.points.get(SortedSurf.TotalElementNo-2-CountElems).getValue();
                beginHigh = sumPrevNext / (double) 2;

                CountElems += (AvgElementNo-1); // index of upper zone's last height
                Index++;
            }
            flag *= -1;
        }

    }

}

