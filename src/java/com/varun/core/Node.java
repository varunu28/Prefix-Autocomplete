package java.com.varun.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node {

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