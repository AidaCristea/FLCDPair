package domain;

import domain.Table.Table;
import util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecursiveDescendentParser {
    private String s;
    private Integer i;
    private List<String> alpha;
    private List<String> beta;
    private Grammar grammar;
    private List<String> w;
    private Table table;
    private boolean printOK;

    public RecursiveDescendentParser(Grammar grammar, String seq) {
        this.s = "q";
        this.i = 1;
        this.alpha = new ArrayList<>();
        this.beta = new ArrayList<>();
        this.w = new ArrayList<>();
        this.grammar = grammar;
        this.table = new Table();
        this.printOK = false;

        //INITIALIZATION
        this.beta.add(grammar.getStartingSymbol());
        //String w = ReadSeq(seq);
        this.w = ReadSeq(seq);

        System.out.println(this.w.toString());

        /*for (int i = 0; i < w.length(); i++) {
            String s = w.substring(i, i + 1);
            this.w.add(s);
        }*/
    }

    public List<String> ReadSeq(String seq) {
        List<String> elements = new ArrayList<>();

        try {
            // Create a Scanner to read from the file
            Scanner fileScanner = new Scanner(new File(seq));

            // Read every element from the file, splitting by a space
            while (fileScanner.hasNext()) {
                String element = fileScanner.next();
                elements.add(element);
            }

            // Close the file scanner
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }

        return elements;
    }

    public String Configuration() {
        return "(" + s + "," + i.toString() + "," + alpha.toString() + "," + beta.toString() + ")\n";
    }

    public void parse() throws InterruptedException {
        while (!s.equals("f") && !s.equals("e")) {
            Thread.sleep(10);
            /*if (CurrentOfW().trim() == "whileLoop") {
                this.printOK = true;
            }
            if (printOK) {
                System.out.println(Configuration());
            }*/
            System.out.println(Configuration());

            if (s.equals("q")) {
                if (i == w.size() + 1 && this.beta.isEmpty()) {
                    System.out.println("SUCCESS");
                    this.Success();
                } else {
                    //EXPAND when head of input stack (Beta) is nonterminal
                    System.out.println(HeadOfBeta());
                    if (grammar.getSetOfNonTerminals().contains(HeadOfBeta())) {
                        System.out.println("EXPAND");
                        this.Expand();
                    } else {
                        //ADVANCE when head of input stack (Beta) is a terminal=current symbol from input
                        //else MOMENTARY INSUCCESS
                        System.out.println("currentW: " + CurrentOfW());

                        System.out.println(grammar.getSetOfTerminals().contains(HeadOfBeta()));
                        if (grammar.getSetOfTerminals().contains(HeadOfBeta()) && HeadOfBeta().equals(CurrentOfW())) {
                            System.out.println("ADVANCE");
                            Advance();
                        } else {
                            System.out.println("MOMENTARY INSUCCESS");
                            MomentaryInsuccess();
                        }
                    }
                }
            } else {
                if (this.s.equals("b")) {
                    //BACK when head of working stack (alpha) is a terminal
                    if (grammar.getSetOfTerminals().contains(TailOfAlpha())) {
                        System.out.println("BACK");
                        Back();
                    } else {
                        System.out.println("ANOTHER TRY");
                        AnotherTry();
                    }
                }
            }
        }
        if (this.s.equals("e")) {
            System.out.println("ERROR");

        } else {
            System.out.println(Configuration());
            System.out.println("Sequence accepted");
            generateTable();
            System.out.println(table.toString());
            //System.out.println(BuildStringOfProd());
        }
    }

    //Generate table
    private Table generateTable() {

        //S1, S2, S3, S3
        List<String> productionNumbers = this.filterTerminals(alpha);
        System.out.println(productionNumbers);
        String initialState = grammar.getStartingSymbol();
        table.add(initialState, 0, 0);

        //this is so we know which nonTerminal we need to extend
        List<Pair<Integer, String>> nonTermsInSeq = new ArrayList<>();

        int startingSymbolIndex = 0;
        int numberOfCurrentProductionNumber = 0;

        int parentIndex = 0;


        //productions strings
        for (String productionNumber : productionNumbers) {
            numberOfCurrentProductionNumber++;
            String[] parts = separateStringAndNumber(productionNumber);
            String nonTerminal = parts[0];
            Integer index = Integer.parseInt(parts[1]);
            System.out.println(nonTerminal + ", " + index);

            List<String> production = grammar.getProductionsForNonTerminal(nonTerminal).get(index - 1);
            int i = 0;
            boolean enteredIf = false;
            for (String element : production) {
                System.out.println(element);
                //if i=0 this mean that this is the first element that we add from that production, so it will not have a left sibling
                if (i == 0) {
                    table.add(element, startingSymbolIndex, i);
                } else {
                    //we already added an element from the production, so we have a left sibling
                    table.add(element, startingSymbolIndex, table.getCurrentIdx() - 1);
                }

                if (grammar.getSetOfNonTerminals().contains(element)) {

                    //save the next parent that needs to be expanded
                    if (!enteredIf) {
                        parentIndex = table.getCurrentIdx() - 1;
                        enteredIf = true;
                    }
                    nonTermsInSeq.add(new Pair<>(table.getCurrentIdx() - 1, element));
                }
                i++;
            }

            if (numberOfCurrentProductionNumber == productionNumbers.size() - 1) {
                startingSymbolIndex = nonTermsInSeq.get(0).getFirst();
            } else {
                startingSymbolIndex = parentIndex;
            }

            System.out.println("Parent " + startingSymbolIndex);
            System.out.println(nonTermsInSeq);

            System.out.println(table.toString());

            //remove the pair from the list of pairs, because we are going to expand the nonterminal with that index
            if (nonTermsInSeq.size() > 0) {
                for (int p = 0; p < nonTermsInSeq.size(); p++) {
                    if (nonTermsInSeq.get(p).getFirst().equals(startingSymbolIndex)) {
                        System.out.println("in startsymb match first");
                        System.out.println(nonTermsInSeq);
                        nonTermsInSeq.remove(p);
                    }
                }
            }
            System.out.println("After if pairs");

            System.out.println(startingSymbolIndex);
            System.out.println(nonTermsInSeq);
            System.out.println();

        }
        //System.out.println(table.toString());

        return table;
    }


    private static String[] separateStringAndNumber(String input) {
        // Define a pattern to match the string and number parts
        Pattern pattern = Pattern.compile("([a-zA-Z]+)(\\d+)");
        Matcher matcher = pattern.matcher(input);

        // Check if the pattern matches
        if (matcher.matches()) {
            // Group 1 represents the string part, and Group 2 represents the number part
            String stringPart = matcher.group(1);
            String numberPart = matcher.group(2);

            return new String[]{stringPart, numberPart};
        } else {
            // Handle the case when the input doesn't match the pattern
            throw new IllegalArgumentException("Input does not match the expected pattern.");
        }
    }


    private List<String> filterTerminals(List<String> workingStack) {
        List<String> filteredWorkingStack = new ArrayList<>();
        //pattern for sequence of digits at the end of a string
        Pattern pattern = Pattern.compile("\\d+$");

        for (String s : workingStack) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                filteredWorkingStack.add(s);
            }
        }
        //System.out.println(filteredWorkingStack);
        return filteredWorkingStack;
    }

    //ALPHA AND BETA OPERATIONS
    private String HeadOfAlpha() {
        return this.alpha.get(0);
    }

    private String TailOfAlpha() {
        return this.alpha.get(this.alpha.size() - 1);
    }

    private void RemoveTailOfAlpha() {
        this.alpha.remove(this.alpha.size() - 1);
    }

    private void AddHeadOfBeta(String s) {
        this.beta.add(0, s);
    }

    private void AddTailOfAlpha(String s) {
        this.alpha.add(this.alpha.size(), s);
    }

    private void RemoveHeadOfBeta() {
        this.beta.remove(0);
    }

    private String HeadOfBeta() {
        return this.beta.get(0);
    }

    private String CurrentOfW() {
        if (this.i > this.w.size())
            return "";
        return this.w.get(this.i - 1);
    }

    // STEPS
    private void Success() {
        this.s = "f";

    }

    private void Back() {
        this.i -= 1;
        String lastFromAlpha = TailOfAlpha();
        /*String tailOfAlphaWithoutIndex = TailOfAlpha().substring(0, TailOfAlpha().length() - 1);

        Integer currentProductionIndex = Integer.parseInt(TailOfAlpha().substring(TailOfAlpha().length() - 1));


        for(int j =0;j<grammar.getProductionsForNonTerminal(tailOfAlphaWithoutIndex).get(currentProductionIndex).size();j++)
        {
            RemoveTailOfAlpha();
        }*/
        RemoveTailOfAlpha();
        AddHeadOfBeta(lastFromAlpha);
    }

    private void MomentaryInsuccess() {
        this.s = "b";
    }

    private void Advance() {
        this.i += 1;
        String headOfBeta = HeadOfBeta();
        RemoveHeadOfBeta();
        AddTailOfAlpha(headOfBeta);
    }

    private void Expand() {
        //(q,i,alpha,beta)->(q,i,alpha A1, prod1 beta)
        //where A-> prod1, prod2, prod3,...
        //      1=first prod of A

        //Extract the head of beta
        String headOfBeta = HeadOfBeta();

        //Remove the beta the first terminal
        RemoveHeadOfBeta();

        //Add to alpha A1
        AddTailOfAlpha(headOfBeta + "1");

        //Add to the first positions of beta the prod1
        List<String> firstProduction = grammar.getProductionsForNonTerminal(headOfBeta).get(0);
        for (int i = firstProduction.size() - 1; i >= 0; i--) {
            AddHeadOfBeta(firstProduction.get(i));
        }

    }

    private void AnotherTry() {

        String tailOfAlphaWithoutIndex = TailOfAlpha().substring(0, TailOfAlpha().length() - 1);
        //System.out.println(tailOfAlphaWithoutIndex);
        List<List<String>> productions = grammar.getProductionsForNonTerminal(tailOfAlphaWithoutIndex);
        String headOfAlpha = this.HeadOfAlpha();
        //System.out.println(headOfAlpha);
        //System.out.println(TailOfAlpha());
        //
        //System.out.println(TailOfAlpha().substring(TailOfAlpha().length() - 1));
        Integer currentProductionIndex = Integer.parseInt(TailOfAlpha().substring(TailOfAlpha().length() - 1));
        Integer nextProdIndex = currentProductionIndex + 1;
        if (nextProdIndex <= productions.size()) {
            this.s = "q";
            /*for(int j =0;j<grammar.getProductionsForNonTerminal(tailOfAlphaWithoutIndex).get(currentProductionIndex).size();j++)
            {
                RemoveTailOfAlpha();
            }*/


            RemoveTailOfAlpha();
            String newTailOfAlpha = tailOfAlphaWithoutIndex + nextProdIndex;
            this.alpha.add(newTailOfAlpha);

            for (int i = 0; i < productions.get(currentProductionIndex - 1).size(); i++) {
                //System.out.println("TO REMOVE : " + this.beta.get(0));
                this.beta.remove(0);
            }

            List<String> nextProduction = productions.get(nextProdIndex - 1);
            for (int i = nextProduction.size() - 1; i >= 0; i--) {
                //System.out.println("ADD");
                this.beta.add(0, nextProduction.get(i));
            }
        } else {
            //s1 -> s
            //listOfInteger1-> listOfInteger
            RemoveTailOfAlpha();

            for (int i = 0; i < productions.get(currentProductionIndex - 1).size(); i++) {
                RemoveHeadOfBeta();
            }

            //RemoveHeadOfBeta();
            AddHeadOfBeta(tailOfAlphaWithoutIndex);
        }
    }

    private String BuildStringOfProd() {
        return table.toString();
    }
}
