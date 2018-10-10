package main.java.com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Autocomplete {

    private Node trie;

    public Autocomplete(List<String> dict) {
        trie = new Node("");
        for (String s : dict) {
            insertWord(s);
        }
    }

    private void insertWord(String s) {
        Node curr = trie;
        for (int i = 0; i < s.length(); i++) {
            if (!curr.childrens.containsKey(s.charAt(i))) {
                curr.childrens.put(s.charAt(i),
                        new Node(s.substring(0, i+1)));
            }

            curr = curr.childrens.get(s.charAt(i));

            if (i == s.length() - 1) {
                curr.isWord = true;
            }
        }
    }

    public List<String> getWordsForPrefix(String prefix) {
        List<String> results = new ArrayList<>();

        Node curr = trie;

        for (char c : prefix.toCharArray()) {
            if (curr.childrens.containsKey(c)) {
                curr = curr.childrens.get(c);
            }
            else {
                return results;
            }
        }

        findAllChildWords(curr, results);

        return results;
    }

    private void findAllChildWords(Node node, List<String> results) {
        if (node.isWord) {
            results.add(node.prefix);
        }

        for (char c : node.childrens.keySet()) {
            findAllChildWords(node.childrens.get(c), results);
        }
    }

    private static final class Node {
        String prefix;
        Map<Character, Node> childrens;
        boolean isWord;

        public Node(String prefix) {
            this.prefix = prefix;
            this.childrens = new HashMap<>();
        }
    }
}