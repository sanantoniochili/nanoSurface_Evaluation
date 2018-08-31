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
        Encode encoder = new Encode(SpacesNo, reader.STable.get(0));


    }
}
