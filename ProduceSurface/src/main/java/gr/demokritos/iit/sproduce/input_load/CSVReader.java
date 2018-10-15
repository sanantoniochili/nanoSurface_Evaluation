package gr.demokritos.iit.sproduce.input_load;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CSVReader {

    public void test(String csvFile) {

        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            FileWriter writer = new FileWriter("test.txt");
            while ((line = br.readLine()) != null) {

                // use comma as separator
                StringBuilder sb = new StringBuilder();
                String[] country = line.split(cvsSplitBy);

                for (int i=0; i<country.length; i++) {
                    sb.append(country[i]+"\n");
                }
                writer.append(sb.toString());

            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
