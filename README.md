# Prefix-Autocomplete
A command line autocomplete application using Java


```
    private void findAllChildWords(Node node, PriorityQueue<String> results) {
        if (node.isWord) {
            results.add(node.prefix);
            if (results.size() > SIZE) {
                results.poll();
            }
        }

        for (char c : node.childrens.keySet()) {
            findAllChildWords(node.childrens.get(c), results);
        }
    }
```