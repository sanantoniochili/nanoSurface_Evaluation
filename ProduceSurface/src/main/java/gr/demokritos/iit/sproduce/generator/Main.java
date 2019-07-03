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

package gr.demokritos.iit.sproduce.generator;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] argv) throws Exception{

        String out_filename= "";
        String in_filename = "";
        String cvsSplitBy  = ",";
        double[] args_     = new double[5];
        int y_flag 	       = 0;
        int out_flag       = 0;
        int in_flag        = 0;

        for (int i=0 ; i<argv.length ; i++) {
            if( argv[i].equals("-in") ) {
                in_filename = argv[++i];
                in_flag = 1;
            }
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
                out_filename = argv[++i];
                out_flag = 1;

                // check if file exists
                File f = new File(out_filename);
                // erase content if exists
                if(f.exists() && !f.isDirectory()) {
                    FileWriter writer = new FileWriter(out_filename);
                    writer.write("");
                    writer.close();
                }
            }
        }
        // read from standard input
        if( in_flag==0 ) {
            RandomGaussSurfaceGenerator RG = produce(args_,y_flag,out_flag,out_filename);
            plot_surface(RG);

//            CSVReader reader = new CSVReader();
//            reader.test("stdin_results.csv");

        // read from csv file with multiple surface parameters
        } else {
            BufferedReader reader = null;
            String line       = "";
            y_flag            = 0;
            try {

                reader = new BufferedReader(new FileReader(in_filename));
                line = reader.readLine(); // get first line with names of parameters
                String[] all_params = line.split(cvsSplitBy);
                for (int i=0; i<all_params.length ; i++) {
                    if( all_params[i].equals("cly") )
                        y_flag = 1;
                }
                while ((line = reader.readLine()) != null) {
                    // use comma as separator
                    all_params = line.split(cvsSplitBy);

                    args_[1] = Math.sqrt(Double.parseDouble(all_params[6]));
                    args_[2] = Double.parseDouble(all_params[1]);
                    args_[3] = Double.parseDouble(all_params[2]);
                    if( y_flag==1 ) args_[4] = Double.parseDouble(all_params[3]);

                    RandomGaussSurfaceGenerator RG = produce(args_,y_flag,out_flag,out_filename);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

    }

    static private RandomGaussSurfaceGenerator produce(double[] args_, int y_flag, int out_flag, String out_filename) throws ImError, IOException {
        RandomGaussSurfaceGenerator RG;
        if( y_flag==0 )
            RG = new RandomGaussSurfaceGenerator(args_); // isotropic
        else
            RG = new RandomGaussSurfaceGenerator(args_,args_[4]); // non-isotropic,last argument is cly

        if( out_flag==0 ){ // standard output
            RG.printArray(RG.Surf);
        } else {
            try{
                FileWriter writer = new FileWriter(out_filename,true);
                RG.printArray(writer,RG.Surf);
            } catch (IOException ex){
                System.out.println("There was a problem creating/writing to the file");
                ex.printStackTrace();
            }
        }
        return RG;
    }

    static private void plot_surface(RandomGaussSurfaceGenerator RG) throws IOException {
        double[][] distDataProp = RG.Surf;

        // Build a polygon list
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

