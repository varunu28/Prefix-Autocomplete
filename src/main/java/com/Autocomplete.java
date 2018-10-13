package main.java.com;

import java.util.*;

public class Autocomplete {

    private Node trie;
    private Map<String, Integer> words;
    private LRUCache cache;
    private final int SIZE = 3;
    private final int CACHE_SIZE = 5;

    public Autocomplete(Map<String, Integer> words) {
        this.words = words;
        trie = new Node("");

        for (String word : words.keySet()) {
            insertWord(word);
        }

        cache = new LRUCache(CACHE_SIZE);
    }

    private void insertWord(String  s) {
        Node curr = trie;

        for (int i = 0; i < s.length(); i++) {
            if (!curr.childrens.containsKey(s.charAt(i))) {
                curr.childrens.put(s.charAt(i),
                        new Node(s.substring(0, i+1)));
            }

            curr = curr.childrens.get(s.charAt(i));
            curr.completions.add(s);

            if (i == s.length() - 1) {
                curr.isWord = true;
            }
        }
    }

    public List<String> getTopKWordsForPrefix(String prefix) {
        if (cache.get(prefix).size() > 0) {
            return cache.get(prefix);
        }

        List<String> topKWords = new ArrayList<>(getTopKSuggestions(prefix));
        cache.put(prefix, topKWords);

        return topKWords;
    }

    private PriorityQueue<String> getTopKSuggestions(String prefix) {
        PriorityQueue<String> results =
                new PriorityQueue<>(SIZE, (s1, s2) -> words.get(s2) - words.get(s1));

        Node curr = trie;

        for (char c : prefix.toCharArray()) {
            if (curr.childrens.containsKey(c)) {
                curr = curr.childrens.get(c);
            }
            else {
                return results;
            }
        }

//        findAllChildWords(curr, results);

        // Pre-saving all completions rather than traversing the tree to find the valid words
        List<String> completions = curr.completions;
        for (String word : completions) {
            results.add(word);
            if (results.size() > SIZE) {
                results.poll();
            }
        }

        return results;
    }

//    private void findAllChildWords(Node node, PriorityQueue<String> results) {
//        if (node.isWord) {
//            results.add(node.prefix);
//            if (results.size() > SIZE) {
//                results.poll();
//            }
//        }
//
//        for (char c : node.childrens.keySet()) {
//            findAllChildWords(node.childrens.get(c), results);
//        }
//    }

    private static final class Node {
        String prefix;
        Map<Character, Node> childrens;
        List<String> completions;

        boolean isWord;

        public Node(String prefix) {
            this.prefix = prefix;
            this.childrens = new HashMap<>();
            completions = new ArrayList<>();
        }
    }
}