/*
* credits: https://www.mkyong.com/
*/
package input_load;

import encode.Surface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class CSVRead {

    public Vector<Surface> STable;

    public CSVRead(String csvFile) {
        String line = "";
        String csvSplit1 = ",";
        String csvSplit2 = ":";

        STable = new Vector<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            line = br.readLine();
            //while ((line = br.readLine()) != null) { // each line is a sequence of heights from a surface

                // first column is parameters with colon separator
                // rest columns are heights with comma separator
                String[] heights = line.split(csvSplit1); // read line with parameters and heights
                String[] params = heights[0].split(csvSplit2); // split parameter values

                double rms = Double.parseDouble(params[1]);
                double clx = Double.parseDouble(params[3]);
                double cly = Double.parseDouble(params[5]);

                Surface data = new Surface(rms,clx,cly,heights.length-1);
                for (int i=1; i<heights.length; i++) {
                    data.add_height(i,new Double(heights[i]));
                }

                STable.addElement(data); // add to vector of surfaces


           // }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
