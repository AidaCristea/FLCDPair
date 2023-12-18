package symbolTbl;


import util.Pair;

import java.util.ArrayList;

public class HashTable {
    private final ArrayList<ArrayList<String>> hashtable;
    private final Integer size;

    public HashTable(Integer size) {
        this.size = size;
        this.hashtable = new ArrayList<>();

        for (int i = 0; i < size; i++)
            this.hashtable.add(new ArrayList<>());
    }

    public Integer asciiCode(String key) {
        //compute the ascii code of the key
        //compute the sum of ascii keys for each character

        int asciiCode = 0;
        for (char c : key.toCharArray()) {
            asciiCode += c;
        }
        return asciiCode;

    }

    public Integer hashFunction(String key) {
        //get the hashcode : asciiValue%size
        Integer asciiCode = asciiCode(key);
        return asciiCode % size;

    }

    public boolean search(String key) {
        //search the key in the hashtable
        //if it exists return true, otherwise return false
        Integer index = hashFunction(key);
        ArrayList<String> bucket = hashtable.get(index);

        for (String str : bucket) {
            if (str.equals(key))
                return true;
        }
        return false;
    }

    public void insert(String key) {
        //compute hashCode and insert in the proper ArrayList, if the key doesn't exist in the hashtable.
        if (!search(key)) {
            Integer index = hashFunction(key);
            ArrayList<String> elements = hashtable.get(index);
            elements.add(key);
        }
    }

   /* public String findValue(String key) {
        //return the index of the array and the position of that key
        Integer index = hashFunction(key);
        ArrayList<String> bucket = hashtable.get(index);

        for (int i = 0; i < bucket.size(); i++) {
            if (bucket.get(i).equals(key))
                return "The key is in the array with the index " + index + " on position " + i;
        }
        return null;
    }*/


    public Pair<Integer, Integer> findValue(String key) {
        //return the index of the array and the position of that key
        Integer index = hashFunction(key);
        ArrayList<String> bucket = hashtable.get(index);

        for (int i = 0; i < bucket.size(); i++) {
            if (bucket.get(i).equals(key))
                return new Pair<>(index, i);
        }
        return null;
    }

    public String toString() {
        return " { elements=" + this.hashtable + ", size = " + this.size + " }";
    }
}
