package java.com.varun;

import java.com.varun.core.Autocomplete;
import java.com.varun.dataprocessor.InputDataProcessor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class Runner {

  public static void main(String[] args) throws IOException {

    Map<String, Integer> words = new InputDataProcessor().getInputData();
    Autocomplete autocomplete = new Autocomplete(words);
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    while (true) {
      autocomplete.
          getTopKWordsForPrefix(br.readLine()).
          stream().
          forEach(System.out::println);
    }
  }
}