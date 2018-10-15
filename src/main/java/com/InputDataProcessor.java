package main.java.com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InputDataProcessor {

    public String pathname = "/home/varunu28/Documents/PrefixAutocomplete/src/resources/data.txt";

    public Map<String, Integer> getInputData() throws IOException {
        Map<String, Integer> words = new HashMap<>();

        File file = new File(pathname);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null) {
            String word = st.split(",")[0];
            int weight = Integer.parseInt(st.split(",")[1]);

            words.put(word, words.getOrDefault(word, 0) + weight);
        }

        return words;
    }
}
