package encode;

import operations.Linspace;
class MinMaxSurf extends Surface {
    double minH;
    double maxH;

    MinMaxSurf(Surface S) {
        super(S);
        sort_heights();

        this.minH = points.get(0).getValue();
        this.maxH = points.get(TotalElementNo-1).getValue();
    }
}

public class Encode {

    MinMaxSurf Surf;
    int AvgElementNo; // average number of points of surface per zone
    int Remain;       // remaining number of points

    Encode(int SpaceNo, Surface S) {
        AvgElementNo = S.get_totalNo() / SpaceNo;
        Remain = S.get_totalNo() % SpaceNo;
        Surf = new MinMaxSurf(S);

        if( Remain>0 ) {
            double[] means = new double[SpaceNo];
            double AvgSpacing = (double) (Surf.maxH - Surf.minH)/(double) SpaceNo;

            //for (int i=0; i<SpaceNo; i++) {

            //}

        }

    }
}
