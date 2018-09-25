
package generator;
/*
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.canvas.CanvasAWT;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] argv) throws Exception{
        if( argv.length<4 ){
            System.out.println("Error in argument passing.");
            return;
        }
        String filename= new String();
        double[] args_ = new double[argv.length];
        int y_flag 	   = 0;
        int out_flag   = 0;
        for (int i=0 ; i<argv.length ; i++) {
            if( argv[i].equals("-N") )
                args_[0] = Double.parseDouble(argv[++i]);
            if( argv[i].equals("-rL") )
                args_[1] = Double.parseDouble(argv[++i]);
            if( argv[i].equals("-h") )
                args_[2] = Double.parseDouble(argv[++i]);
            if( argv[i].equals("-clx") )
                args_[3] = Double.parseDouble(argv[++i]);
            if( argv[i].equals("-cly") ){
                args_[4] = Double.parseDouble(argv[++i]);
                y_flag = 1;
            }
            if( argv[i].equals("-out") ){
                filename = new String(argv[++i]);
                out_flag = 1;
            }
        }

        RandomGaussSurfaceGenerator RG;
        if( y_flag==0 )
            RG = new RandomGaussSurfaceGenerator(args_); // isotropic
        else
            RG = new RandomGaussSurfaceGenerator(args_,args_[4]); // non-isotropic,last argument is cly


        if( out_flag==0 ){
            PrintStream ps = new PrintStream(System.out); // standard output
            RG.printArray(RG.Surf,ps);
        } else {
            try{
                File outFile = new File(filename);
                FileOutputStream fout = new FileOutputStream(outFile);
                PrintStream ps = new PrintStream(fout); // output file <filename>
                RG.printArray(RG.Surf,ps);
                fout.close();
            } catch (IOException ex){
                System.out.println("There was a problem creating/writing to the file");
                ex.printStackTrace();
            }
        }

        System.setOut(System.out);
        // Build a polygon list
        double[][] distDataProp = RG.Surf;

        List<Polygon> polygons = new ArrayList<Polygon>();
        for(int i = 0; i < distDataProp.length -1; i++){
            for(int j = 0; j < distDataProp[i].length -1; j++){
                Polygon polygon = new Polygon();
                polygon.add(new Point( new Coord3d(i, j, distDataProp[i][j]) ));
                polygon.add(new Point( new Coord3d(i, j+1, distDataProp[i][j+1]) ));
                polygon.add(new Point( new Coord3d(i+1, j+1, distDataProp[i+1][j+1]) ));
                polygon.add(new Point( new Coord3d(i+1, j, distDataProp[i+1][j]) ));
                polygons.add(polygon);
            }
        }

        // Creates the 3d object
        Shape surface = new Shape(polygons);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new org.jzy3d.colors.Color(1,1,1,1f)));
        surface.setWireframeDisplayed(false);

        Chart chart = new AWTChartComponentFactory().newChart(Quality.Advanced, "awt");;
        chart.getScene().getGraph().add(surface);
        ChartLauncher.openChart(chart);
        File image = new File("surface.png");
        chart.screenshot(image);

    }

}
*/