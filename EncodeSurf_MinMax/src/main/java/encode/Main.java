/* Project is not finished.
* Till now: Actions to split [minH,maxH] to zones with same amount of points. Creating array of boundaries.
* Beginning from both ends and meeting in the middle.
* Probably: Find concentration to each zone's mean height and rearrange boundaries.
* Must: Encode points to letters based on input number of spaces.
 */

package encode;

import input_load.CSVRead;

public class Main {
    static int SpacesNo = 10;
    public static void main(String[] argv) {
        
        String csvFile = "";
        for (int i=0 ; i<argv.length ; i++) {
            if (argv[i].equals("-in")) {
                csvFile = argv[++i];
            }
        }
        CSVRead reader = new CSVRead(csvFile);
        Encoder encoder = new Encoder(SpacesNo, reader.STable.get(0));


    }
}
