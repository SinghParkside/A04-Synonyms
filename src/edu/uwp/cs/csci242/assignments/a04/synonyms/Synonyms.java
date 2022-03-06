package edu.uwp.cs.csci242.assignments.a04.synonyms;
/**
 * Name:  Ishmeet Singh
 * Course:  CSCI-242
 * Section: 2342
 * Assignment: A04-synonyms
 *Professor : Timothy Knautz
 * Project/Class Description :
 *                          This project will make me write a program which will contain concept of
 * generics and collections in Java. To accomplish this, you will implement a program that uses semantic
 * similarity to answer questions about synonyms. In this assignment you will build an intelligent
 * program that can learn to answer questions. In order to do that, your program will approximate the
 * semantic similarity of any pair of words.The semantic similarity between two words is the measure
 * of the closeness of their meanings. This project will also make you learn how Hashmap works, also you
 * will learn about how many methods there are to use Hashmaps and more about Hashmaps.
 * .
 *
 * Known Bugs:  NONE!!!
 */
/**
 * Import statements needed for this program:
 */

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Synonyms {
    /**
     * A handy implementation of the semantic descriptor vector is to use
     * a HashMap
     */
    private  HashMap<String, HashMap<String, Integer>> descriptor ;

    /**
     * The Synonyms constructor which new up the descriptor hashmap.
     */
    private Synonyms()
    {
        descriptor = new HashMap<>();
    }

    /**
     * The constructor which will test the full corpus. Corpus will contain large text of data
     * in forms of URLs and this constructor will be called in order to test the full corpus.
     * @param corpus corpus (plural corpora) or text corpus is a large and structured set of texts
     * that are used to do statistical analysis and testing, checking occurrences or validating.
     *  linguistic rules within a specific language. The large set of text give us enough data to
     *  do real statistical analysis such as cosine similarity. We will put hardcoded websites to test
     *  corpus
     */
    private Synonyms(URL [] corpus) {
        this();
        parseCorpus(corpus);
    }

    /**
     * Getter method for descriptor inorder to test if the numbers of semantic similarity are getting assgined
     * @return
     */
    private HashMap<String, HashMap<String, Integer>> getDescriptor() {

        return descriptor;
    }

    /**
     * The parameter of this method is corpus, which  will consist of a set of web pages of books at Project Gutenberg.
     * The corpus will be used to build the HashMap.
     * @param corpus to test all the web URLs we will use corpus .
     */
    private void parseCorpus(URL [] corpus) {
        String file = "";
        // Need to use corpus to read from the web pages
        for (URL net : corpus){
            // Print line parsing each URL
            System.out.println("Parsing  URL : " + net);
            try{
                // Stream all the web  pages
                Scanner s = new Scanner(net.openStream());
                // For reading a sentence one sentence at a time, we need to use
                // the delimiter of your scanner with useDelimiter
                s.useDelimiter("\\Z");
                file = s.next();

            } catch (IOException e) {
                // Exception if the URL is not working it will throw an exception
                System.out.println("Cant read from the url : " + e);
            }
            // Convert the String file to lower case
            file = file.toLowerCase();
            //Split the document into sentences
            String [] sentence = file.trim().split("[\\.\\?\\!]");
            //go through the sentence
            for (String sen : sentence){
                //split sentence to words.
                String [] words = sen.trim().split("\\s+");
                // increment each word of the descriptor by using a private method and call it.
                for (String tempword : words){
                    //method call to increment the descriptor
                    this.incrementDescriptor(tempword,words);
                }
            }
        }
    }

    /**
     * This method will increment the descriptor. This method will also retrieve the descriptor
     * for the current word and then it will look for the rest of the sentence and it will
     * increment the descriptor.
     * @param word each word
     * @param words words in the sentences
     */
    private void incrementDescriptor(String word, String [] words){
        //clean the word using replace all
        word = word.replaceAll("\\W+","").trim();
        //Retrive the descriptor for the current word; in this full descriptor is the one which can be provided with the
        // in this local protocol
        
        HashMap<String,Integer> innerMap = descriptor.get(word);
        if(innerMap == null){
            //This is a new word the old descriptor is fine
            innerMap = new HashMap<String,Integer>();
            descriptor.put(word,innerMap);
        }
        // Look at the other words in the sentence and increment the descriptor.
        for(String otherWord : words){
            otherWord = otherWord.replaceAll( "\\W+","");
            if (!word.equals(otherWord) && !otherWord.equals("")){
                int count = innerMap.getOrDefault(otherWord,0);
                innerMap.put(otherWord,count +1);

                int x = 0;
                float y = 0;

                int sum = (int) (y +x);
            }
        }
    }

    /**
     * This is a helper method for cosine similarity. Use collection class as a parameter.
     * @param vec to find vector.
     * @return incremented descriptor
     */
    private double sum (Collection<Integer> vec){
        double doubleSum = 0.0;
        //go through the vec length
        for (int count : vec){
            //multiply the count * count to double sum
            doubleSum += count * count;
        }
        //return Square root of double sum.
        return Math.sqrt(doubleSum);
    }

    /**
     *This method will calculate the semantic similarity. There many ways of measuring
     * semantic similarity. this method will use  a fairly straightforward method known
     * as the cosine similarity.
     * @param wordOne first word
     * @param wordTwo second word
     * @return cosine similarity.
     */

    private double cosineSimilarity ( String wordOne , String wordTwo){
        double vecDot = 0.0;
        HashMap<String , Integer > a = descriptor.get(wordOne);
        HashMap<String, Integer> b = descriptor.get(wordTwo);

        // Check if the maps are not null to avoid Null pointer exception
        if (a != null && b != null){
            double keyA = sum(a.values());
            double keyB = sum(b.values());
        // Go through all the keys and values in a

            for (Map.Entry<String , Integer> aOne : a.entrySet()){
                //try to get corresponding entry from b
                //if it doesn't exist return 0 instead of null this will avoid NPE
                int var = b.getOrDefault(aOne.getKey(), 0);
                vecDot += (aOne.getValue() * var);
            }
            return  vecDot / (keyA * keyB);
        }
        else {
            return -1;
        }

    }

    /** This is th main method in which you will test all your methods and your descriptors.
     *  The main will also ask the user enters the word on the first line of input and the list of choices on the second line. It then calculates the best match and prints it.
     * @param args
     */
    public static void main(String[] args){
        //Create the Scanner to read from the system
        Scanner scan = new Scanner (System.in);
        try {
            //update the corpus to test and fill the corpus with URLS(web pages we want go through)
            URL [] corpus = {
                    // Pride and Prejudice, by Jane Austen
                    new URL("https://www.gutenberg.org/files/1342/1342-0.txt"),
                    // The Adventures of Sherlock Holmes, by A. Conan Doyle
                    new URL("https://f5-studio.com/articles/what-is-web-development"),
            };

            // Create a Synonyms class level object that you can use the object in the class method.
            Synonyms sime = new Synonyms(corpus);
            // Enter a word which will be used as wordOne for cosine similarity
            System.out.println("Enter a word : ");
            // Use Scanner to read
            String word = scan.nextLine();
            // Enter the choices
            System.out.println("Enter the Choices : ");
            // Use the scanner to enter the choices
            String wordTwo = scan.nextLine();
            String [] array;
            array = wordTwo.split(("\\s+"));
            // Go through the array and print cosine similarity in given formatted order.
            for (String s : array) {
                System.out.println( s + " " + sime.cosineSimilarity(word, s));
            }
            // The code below is compare the highest cosine similarity and prints it.
            double variable = 0.0;
            String obj = " ";
            for (String s : array) {
                double tempCosine = sime.cosineSimilarity(word, s);
                if (variable < tempCosine) {
                    variable = tempCosine;
                    obj = s;
                }
            }
            //Print out the synonyms of given choices
            System.out.println("Synonym of " + word + " is : " + obj);
        } catch (Exception e) {
            System.out.println("Error reading the URL !! " + e);
        }
    }
}






