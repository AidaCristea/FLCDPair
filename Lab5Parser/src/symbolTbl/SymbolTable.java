package symbolTbl;

import util.Pair;

public class SymbolTable {

    HashTable hashTable;

    public SymbolTable(HashTable hashTable) {
        this.hashTable = hashTable;
    }

    public int getHashCode(String key) {
        return hashTable.hashFunction(key);
    }

    public void insert(String key) {
        hashTable.insert(key);
    }

    public boolean search(String key) {
        return hashTable.search(key);
    }

    public Pair<Integer, Integer> find(String key) {
        return hashTable.findValue(key);
    }

    public String toString() {
        return "symbolTbl.SymbolTable " + hashTable.toString();
    }
}
