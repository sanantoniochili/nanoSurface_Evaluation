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

package loadinput;

/*
 * credits: https://www.mkyong.com/
 */

import encode.Surface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class CSVRead {

    public Vector<Surface> SurfTable;

    public CSVRead(String csvFile, int Scale) {
        String line = "";
        String csvSplit1 = ",";
        String csvSplit2 = ":";

        SurfTable = new Vector<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) { // each line is a sequence of heights from a surface

                // first column is parameters with colon separator
                // rest columns are heights with comma separator
                String[] heights = line.split(csvSplit1); // read line with parameters and heights
                String[] params = heights[0].split(csvSplit2); // split parameter values

                double rms = Double.parseDouble(params[1]);
                double clx = Double.parseDouble(params[3]);
                double cly = Double.parseDouble(params[5]);
                int N = Integer.parseInt(params[7]);

                if( N!=Math.sqrt(heights.length-1) ) {
                    System.out.println("Error in total number of points in input.");
                    return;
                }
                Surface data = new Surface(rms,clx,cly,heights.length-1,N);
                for (int i=1; i<heights.length; i++) {
                    data.add_height(i,new Double(heights[i])*Math.pow(10,Scale));
                }

                SurfTable.addElement(data); // add to vector of surfaces


            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
