package java.com.varun.policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LRUCache {

  Map<String, Node> map;
  Node head, tail;
  int capacity;
  int count;

  public LRUCache(int capacity) {
    map = new HashMap<>();
    this.capacity = capacity;

    head = new Node();
    head.previous = null;

    tail = new Node();
    tail.next = null;

    head.next = tail;
    tail.previous = head;

    this.count = 0;
  }

  public List<String> get(String key) {
    Node node = map.get(key);

    if (node == null) {
      return new ArrayList<>();
    }

    moveToHead(node);

    return node.topKWords;
  }

  private void moveToHead(Node node) {
    removeNode(node);
    addNode(node);
  }

  private void addNode(Node node) {
    node.previous = head;
    node.next = head.next;

    head.next.previous = node;
    head.next = node;
  }

  private Node popTail() {
    Node res = tail.previous;
    removeNode(res);

    return res;
  }

  private void removeNode(Node node) {
    Node pre = node.previous;
    Node post = node.next;

    pre.next = post;
    post.previous = pre;
  }

  public void put(String key, List<String> value) {
    Node node = map.get(key);

    if (node == null) {
      Node newNode = new Node();
      newNode.prefix = key;
      newNode.topKWords = value;

      map.put(key, newNode);
      addNode(newNode);

      count++;

      if (count > capacity) {
        Node tail = popTail();
        map.remove(tail.prefix);
        count--;
      }
    } else {
      node.topKWords = value;
      moveToHead(node);
    }
  }

  private class Node {

    Node previous;
    Node next;
    String prefix;
    List<String> topKWords;
  }
}
