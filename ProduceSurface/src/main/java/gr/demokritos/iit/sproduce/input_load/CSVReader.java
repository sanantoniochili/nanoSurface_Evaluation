/** 
* Copyright 2018 Antonia Tsili
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
