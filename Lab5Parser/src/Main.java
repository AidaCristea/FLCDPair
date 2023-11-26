import domain.Grammar;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Grammar grammar = new Grammar("D:\\FACULTATE\\Materiale facultate 2023-2024\\LFTC\\Labs\\Lab5\\Lab5Parser\\src\\input\\g1");
        grammar.readFromFile();


        System.out.println(grammar.checkCFG());

    }
}