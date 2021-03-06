package java.com.varun.core;

import java.com.varun.policy.LRUCache;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Autocomplete {

  private final int SIZE = 10;
  private final int CACHE_SIZE = 5;
  private Node trie;
  private Map<String, Integer> words;
  private LRUCache cache;
  private Map<String, List<String>> prefixMap;

  public Autocomplete(Map<String, Integer> words) {
    this.words = words;
    trie = new Node("");
    prefixMap = new HashMap<>();

    for (String word : words.keySet()) {
      if (word.indexOf('_') != -1) {
        String[] split = word.split("_");
        for (String s : split) {
          insertWord(s, word, true);
        }
      } else {
        insertWord(word, "", false);
      }
    }

    cache = new LRUCache(CACHE_SIZE);
  }

  private void insertWord(String s, String completeSplitWord, boolean hasSplit) {
    Node curr = trie;
    for (int i = 0; i < s.length(); i++) {
      if (!curr.childrens.containsKey(s.charAt(i))) {
        curr.childrens.put(s.charAt(i),
            new Node(s.substring(0, i + 1)));
      }

      curr = curr.childrens.get(s.charAt(i));

      // Pre-saving all completions rather than traversing the tree to find the valid words
      curr.completions.add(s);

      if (hasSplit) {
        curr.completions.add(completeSplitWord);
      }

      prefixMap.putIfAbsent(curr.prefix, curr.completions);

      if (i == s.length() - 1) {
        curr.isWord = true;
      }
    }
  }

  public List<String> getTopKWordsForPrefix(String prefix) {
    if (cache.get(prefix).size() > 0) {
      return cache.get(prefix);
    }

    PriorityQueue<String> results =
        new PriorityQueue<>(SIZE, Comparator.comparingInt(s -> words.get(s)));
    List<String> wordsForPrefix = prefixMap.getOrDefault(prefix, new ArrayList<>());

    for (String word : wordsForPrefix) {
      if (words.containsKey(word)) {
        results.add(word);
      }

      if (results.size() > SIZE) {
        results.poll();
      }
    }

    List<String> topKWords = new ArrayList<>(results);
    cache.put(prefix, topKWords);

    return topKWords;
  }
}