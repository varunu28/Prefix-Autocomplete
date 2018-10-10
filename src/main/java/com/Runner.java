package main.java.com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class Runner {
    public static void main(String[] args) throws IOException {

        Map<String, Integer> words = new InputDataProcessor().getInputData();

        Autocomplete autocomplete = new Autocomplete(words);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        long startTime = System.currentTimeMillis();

        autocomplete.
            getTopKWordsForPrefix(br.readLine()).
            stream().
            forEach(System.out::println);

        long endTime = System.currentTimeMillis();

        System.out.println(endTime - startTime);
    }
}
