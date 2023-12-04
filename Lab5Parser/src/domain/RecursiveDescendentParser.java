package domain;

import domain.Table.Table;

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

    public RecursiveDescendentParser(Grammar grammar, String seq) {
        this.s = "q";
        this.i = 1;
        this.alpha = new ArrayList<>();
        this.beta = new ArrayList<>();
        this.w = new ArrayList<>();
        this.grammar = grammar;

        //INITIALIZATION
        this.beta.add(grammar.getStartingSymbol());
        String w = ReadSeq(seq);
        for (int i = 0; i < w.length(); i++) {
            String s = w.substring(i, i + 1);
            this.w.add(s);
        }
    }

    public String ReadSeq(String seq) {
        try {
            // Create a Scanner to read from the file
            String filePath;
            Scanner fileScanner = new Scanner(new File(seq));

            String w = "";
            // Read the value of w from the file
            if (fileScanner.hasNext()) {
                w += fileScanner.next();
            } else {
                System.out.println("File is empty.");
            }
            // Close the file scanner
            fileScanner.close();
            return w;
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            return "";

        }
    }

    public String Configuration() {
        return "(" + s + "," + i.toString() + "," + alpha.toString() + "," + beta.toString() + ")\n";
    }

    public void parse() {
        while (!s.equals("f") && !s.equals("e")) {
            System.out.println(Configuration());
            if (s.equals("q")) {
                if (i == w.size() + 1 && this.beta.isEmpty()) {
                    System.out.println("SUCCESS");
                    this.Success();
                } else {
                    //EXPAND when head of input stack (Beta) is nonterminal
                    if (grammar.getSetOfNonTerminals().contains(HeadOfBeta())) {
                        System.out.println("EXPAND");
                        this.Expand();
                    } else {
                        //ADVANCE when head of input stack (Beta) is a terminal=current symbol from input
                        //else MOMENTARY INSUCCESS
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
                    System.out.println("HEAD OF ALPHA " + HeadOfAlpha());
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
            System.out.println("Sequence accepted");
            generateTable();
            System.out.println(Configuration());
            //System.out.println(BuildStringOfProd());
        }
    }

    //Generate table
    private Table generateTable() {
        List<String> productionNumbers = this.filterTerminals(alpha);
        String initialState=grammar.getStartingSymbol();
        //productions strings
        for (String productionNumber : productionNumbers) {
            String nonTerminal = productionNumber.substring(0, productionNumber.length() - 1);
            Integer index = Integer.parseInt(productionNumber.substring(productionNumber.length() - 1));
            System.out.println(nonTerminal + ", " + index);
            List<String> production=grammar.getProductionsForNonTerminal(nonTerminal).get(index);
            
        }
        return table;
    }

    private List<String> filterTerminals(List<String> workingStack) {
        List<String> filteredWorkingStack = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+$");

        for (String s : workingStack) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                filteredWorkingStack.add(s);
            }
        }
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
        List<List<String>> productions = grammar.getProductionsForNonTerminal(tailOfAlphaWithoutIndex);
        String headOfAlpha = this.HeadOfAlpha();
        Integer currentProductionIndex = Integer.parseInt(TailOfAlpha().substring(headOfAlpha.length() - 1));
        Integer nextProdIndex = currentProductionIndex + 1;
        if (nextProdIndex <= productions.size()) {
            this.s = "q";
            RemoveTailOfAlpha();
            String newTailOfAlpha = tailOfAlphaWithoutIndex + nextProdIndex;
            this.alpha.add(newTailOfAlpha);

            for (int i = 0; i < productions.get(currentProductionIndex - 1).size(); i++) {
                this.beta.remove(0);
            }

            List<String> nextProduction = productions.get(nextProdIndex - 1);
            for (int i = nextProduction.size() - 1; i >= 0; i--) {
                System.out.println("ADD");
                this.beta.add(0, nextProduction.get(i));
            }
        } else {
            //s1 -> s
            //listOfInteger1-> listOfInteger
            RemoveTailOfAlpha();
            RemoveHeadOfBeta();
            AddHeadOfBeta(tailOfAlphaWithoutIndex);
        }
    }

    private String BuildStringOfProd() {
        return table.toString();
    }
}
