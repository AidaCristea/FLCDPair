import domain.Grammar;
import domain.RecursiveDescendentParser;
import domain.Table.Table;
import scanner.PIF;
import util.FileCleaner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        /*FileCleaner fileCleaner = new FileCleaner();
        fileCleaner.cleaner("PIF3.out");
        fileCleaner.cleaner("PIF3Seq.out");
        fileCleaner.cleaner("ST3.out");


        PIF pif = new PIF();
        pif.readProblem("p3.txt", "PIF3.out", "ST3.out");
        System.out.println();
        String seq =pif.sequence("PIF3.out", "PIF3Seq.out");
        String pifseq = "PIF3Seq.out";
        //System.out.println(seq);*/


        /*FileCleaner fileCleaner2 = new FileCleaner();
        fileCleaner2.cleaner("PIF1.out");
        fileCleaner2.cleaner("PIFSeq.out");
        fileCleaner2.cleaner("ST1.out");


        PIF pif1 = new PIF();
        pif1.readProblem("p1.txt", "PIF1.out", "ST1.out");
        System.out.println();
        String seq1 =pif1.sequence("PIF1.out", "PIFSeq.out");
        String pifseq = "PIFSeq.out";*/


        FileCleaner fileCleaner3 = new FileCleaner();
        fileCleaner3.cleaner("PIF2.out");
        fileCleaner3.cleaner("PIF2Seq.out");
        fileCleaner3.cleaner("ST2.out");


        PIF pif2 = new PIF();
        pif2.readProblem("p2.txt", "PIF2.out", "ST2.out");
        System.out.println();
        String seq2 =pif2.sequence("PIF2.out", "PIF2Seq.out");
        String pifseq = "PIF2Seq.out";



        Grammar grammar = new Grammar("D:\\FACULTATE\\Materiale facultate 2023-2024\\LFTC\\Labs\\Lab5\\FLCDPair\\FLCDPair\\Lab5Parser\\src\\input\\g2");
        grammar.readFromFile();


        //new RecursiveDescendentParser(grammar,"PIF3Seq.out").parse();




        //Grammar grammar = new Grammar("D:\\FACULTATE\\Materiale facultate 2023-2024\\LFTC\\Labs\\Lab5\\FLCDPair\\FLCDPair\\Lab5Parser\\src\\input\\g3");
        //String seq_txt = "D:\\FACULTATE\\Materiale facultate 2023-2024\\LFTC\\Labs\\Lab5\\FLCDPair\\FLCDPair\\Lab5Parser\\src\\input\\seq_g3.txt";


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
                        new RecursiveDescendentParser(grammar,pifseq).parse();
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