package main.java.com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String> dictionary = Arrays.asList("abc", "acd", "bcd", "acdf", "afews", "acqwrw", "ac");
        Autocomplete autocomplete = new Autocomplete(dictionary);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        autocomplete.
            getWordsForPrefix(br.readLine()).
            stream().
            forEach(System.out::println);
    }
}
