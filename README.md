# Prefix-Autocomplete
A command line autocomplete application using Java

## What it does?
A prefix autocomplete which can match words associated with a prefix typical to an SQL editor.
So for a list of words such as below:
- reversal
- reverse
- reverent
- first_name_reversal

If we provide a prefix of ```rev```, it would pick up all the four words while performing an autocomplete.

## How to run it?

 - Open the project in any IDE (Preferably IntelliJ)
 - I have a sample file in src/resources called ```data.txt``` containing 2000 words with associated weights. If you want to change it, then upload your file with same name. If you change the filename then you would have to make changes in ```InputDataProcessor.java```
 - Run the ```Runner.java``` and start adding prefixes. You will be getting top 10 suggestions for the prefix (If there are at least 10 words starting with that prefix or else you will get less number of suggestions)
 - For changing cache size(```CACHE_SIZE```) or suggestions limit (```SIZE```) change the corresponding values in ```Autocomplete.java```
 - Open an issue if you find any difficulty while running the code
  
## Design
When we hear autocomplete, the first thing that comes to our mind is ```trie```. That one data structure which gets overshadowed by complex tree traversals :stuck_out_tongue: 

So when I started working on this project, I had a slight idea on how to approach this problem as there is a specific data structure assigned for this task in computer science. I created a a POC and then moved on to optimize my approach in order to make the task of getting top suggestions as fast as possible. 

I have divided my approach for this project below in form of multiple stages this project went through and tried to explain that the end result was not the first solution that I designed but an iteration of improvements which I implemented on the code base.

#### Initial POC

I started by fulfilling the basic criteria an autocomplete should fulfil. So if there are words given like the ones below:
- reversal
- reverse
- reverent
- first_name_reversal
- hello

And the user adds a prefix of ```rev``` then they should get back the first three words which start with the prefix. I am right now not considering any weight associated to these words which actually happen in real life searches eg: Once we type ```do``` in Google search, chances of getting ```Donald Trump``` are higher than ```dollar```. I used a ```trie``` data structure for this where the ```trie``` node looked like below:
```
    private static final class Node {
        String prefix;
        Map<Character, Node> childrens;
        boolean isWord;
        
         public Node(String prefix) {
            this.prefix = prefix;
            this.childrens = new HashMap<>();
        }
    }
```

Here my approach was to create a data structure of ```trie``` nodes with the given database of words with each character of the word and children of that node as the remaining characters of that word.
This gives me the expected results as when a user types a prefix of ```rev```, I look up through the existing nodes which I created using the input database of words and start narrowing down my search character-by-character of the given prefix. If I reach a point where a character from the prefix does not have any children in the stored data structure then that means that there is no word present starting with that prefix and we return an empty result.

#### Weights associated with words
Now that I was getting the results for an input prefix, my next goal was to bring weights associated with the word into play and for that I needed to narrow down the suggestion list to 10 results based on the weights of the words. For this I used two different data structures. First being a ```HashMap``` where I stored the word and its associated weight. I populate this ```HashMap``` when I read the input file of words which contains all the words and their associated weights. 

Now that I had all the suggestions for a prefix and weights associated with these words, I had to choose top 10 words based on their weights. A data structure that does it in an efficient manner is a ```PriorityQueue``` in which I use a comparision based on the weights of words which I get from the ```HashMap```
```$xslt
PriorityQueue<String> results =
                new PriorityQueue<>(SIZE, Comparator.comparingInt(s -> words.get(s)));
```
Limiting the size of ```PriorityQueue``` to 10 gives the top 10 suggestions for a prefix.

#### Caching 
A basic LRU caching really helps to optimize the operations of getting the suggestions, if user repeats certain prefix query frequently. The code right now checks if the prefix entered by user is already present in cache and returns the result associated with prefix directly from cache rather than doing the complete computation. Cache gets modified in a ```LRU``` manner and the size of cache can be increased based on the use case.

#### Pre-computations
Currently, for a given prefix, the code goes down every character of prefix and creates a list of words. An optimization to that is to pre-compute a list of completions on each prefix so that we don't have to traverse the complete word list again.

For this, a ```list``` of completions is added to the ```trie``` node to store completions while creating initial data structure from input database. Now for each prefix, we just check if that is a valid prefix and if that is then we just return a list of associated words back as a list which is converted to a ```PriorityQueue```.

A trade-off which we are making here is to increase storage space as compared to computation time.


#### Autocomplete with underscore
For this autocomplete to be used in an SQL editor, it has to pass the test of matching words where prefix is not just in the starting of word but also in case the prefix is after an underscore.

 - first_name_appoint
 - apple
 - first_name_class_appoint_verb

In the above example, a prefix of ```app``` should match all the three words as the word ```appoint``` is present immediately after a prefix. 

To solve this problem, I updated the logic of adding the word. So if I get the word ```first_name_appoint```, while inserting the word into database, I would insert three separate words i.e. ```first, name, appoint``` and put ```first_name_appoint``` into the list of their completions so that I can get it later. I add a check while putting words for a given prefix into the ```PriorityQueue``` that if that word was part of the ```HashMap``` which I created while reading the input word list. This is to avoid adding words which are not part of the actual word list to the suggestions.

#### Optimization for searching in O(1) time
Right now the code does a computation till the length of prefix for list of words associated with it. I creared a ```prefixMap``` in order to store all the computations associated with a prefix into a ```HashMap``` so that now we don't need to do go down the complete prefix character-by-character as we have pre-stored the completions which can be directly fed to ```PriorityQueue```.

*A further optimization to this can be storing top K suggestions in a map so that we no longer need to do the PriorityQueue operations.*

#### Serialization
As we have stored the prefix and associated words in a map, it becomes a ```Key-Value``` structure which can be transferred to a database such as ```Redis``` and then we can query the database to get the list associated with a prefix to be used in a ```PriorityQueue```. 

## What can be improved?

 - A fuzzy search functionality so that it can pick up partial words without an underscore
 - Code for updating and writing to a ```Redis``` database and wrapper around database operations to make ```CRUD``` operations on database easier.
 - Code to wipe out existing database of words and upload new word-list using a text file or an S3 bucket
 - Dockerize the application

#### PR/Issues are welcome