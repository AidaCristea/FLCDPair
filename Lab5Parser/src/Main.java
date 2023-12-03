import domain.Grammar;
import domain.RecursiveDescendentParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Grammar grammar = new Grammar("E://GitHub/LFTC_Pair/FLCDPair/Lab5Parser/src/input/g3");

        try {
            grammar.readFromFile();

            while (true) {
                display_menu();
                String command = "";
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Enter command: ");
                command = reader.readLine();

                switch (command) {
                    case "1":
                        System.out.println("Non-terminals: ");
                        System.out.println(grammar.getSetOfNonTerminals());
                        System.out.println("\n");
                        break;
                    case "2":
                        System.out.println("Terminals: ");
                        System.out.println(grammar.getSetOfTerminals());
                        System.out.println("\n");
                        break;
                    case "3":
                        System.out.println("Production: ");
                        System.out.println(grammar.getSetOfProductions());
                        System.out.println("\n");
                        break;
                    case "4":
                        System.out.println("Starting symbol: ");
                        System.out.println(grammar.getStartingSymbol());
                        System.out.println("\n");
                        break;
                    case "5":
                        System.out.println("Enter non-terminal: ");
                        Scanner input = new Scanner(System.in);
                        String non_terminal = input.next();
                        System.out.println(grammar.getProductionsForNonTerminal(non_terminal));
                        System.out.println("\n");
                        break;
                    case "6":
                        System.out.println("CFG check: ");
                        System.out.println(grammar.checkIfGrammarIsCFG());
                        System.out.println("\n");
                        break;
                    case "7":
                        System.out.println("Enter w= ");
                        input = new Scanner(System.in);
                        String w = input.next();
                        new RecursiveDescendentParser(grammar,w).parse();
                        System.out.println("\n");
                        break;
                    case "0":
                        System.exit(0);
                    default:
                        System.err.println("Unrecognized option");
                        break;
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    private static void display_menu() {
        System.out.println("1 - Show non-terminals");
        System.out.println("2 - Show terminals");
        System.out.println("3 - Show productions");
        System.out.println("4 - Show starting symbol");
        System.out.println("5 - Production for given non-terminal");
        System.out.println("6 - CFG check");
        System.out.println("7 - RECURSIVE DESCENDENT PARSER");
        System.out.println("0 - Exit \n");
    }
}