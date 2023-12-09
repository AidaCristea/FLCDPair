package domain;

import util.Pair;

import java.io.File;
import java.util.*;

public class Grammar {

    private List<String> setOfNonTerminals;
    private List<String> setOfTerminals;
    private HashMap<List<String>, List<List<String>>> setOfProductions;
    private String startingSymbol;
    private String fileName;


    public Grammar(String fileName) {
        this.setOfNonTerminals = new ArrayList<>();
        this.setOfTerminals = new ArrayList<>();
        this.setOfProductions = new HashMap<>();
        this.startingSymbol = "";
        this.fileName = fileName;
    }


    public void readFromFile() throws Exception {
        File file = new File(this.fileName);
        Scanner scanner = new Scanner(file);

        //Set of non-terminals
        String nonTerminalsTxt = scanner.nextLine();
        String setOfNonTerminals = scanner.nextLine();
        this.setOfNonTerminals = Arrays.asList(setOfNonTerminals.split(","));
        //System.out.println(this.setOfNonTerminals);


        //Set of terminals
        String terminalsTxt = scanner.nextLine();
        String setOfTerminals = scanner.nextLine();
        this.setOfTerminals = Arrays.asList(setOfTerminals.split(","));
        //System.out.println(this.setOfTerminals);


        //Productions
        String productionsTxt = scanner.nextLine();
        String production = "";

        while (true) {
            production = scanner.nextLine();
            if (production.equals("STARTING SYMBOL"))
                break;


            List<String> productions = Arrays.asList(production.split(" -> "));
            List<String> left = Arrays.asList(productions.get(0).split(" "));
            List<String> states = Arrays.asList(productions.get(1).split(" \\| "));
            List<List<String>> right = new ArrayList<>();
            for (String s : states) {
                right.add(Arrays.asList(s.split(" ")));
            }
            Pair<List<String>, List<List<String>>> model = new Pair<>(left, right);
            this.setOfProductions.put(model.getFirst(), model.getSecond());


        }

        //Starting symbol
        this.startingSymbol = scanner.nextLine();
        //System.out.println(this.startingSymbol);

        scanner.close();

    }


    public boolean checkCFG(List<String> nonTerms) {
        return nonTerms.size() <= 1;
    }

    public boolean checkIfGrammarIsCFG() {
        for (List<String> key : setOfProductions.keySet()) {
            if (key.size() != 1) {
                System.out.println("in check size not 1");
                return false;
            }
            else {
                if(!setOfNonTerminals.contains(key.get(0)))
                {
                    System.out.println(setOfNonTerminals);
                    System.out.println(key);
                    return false;
                }

            }
        }

        return true;
    }

    public List<String> getSetOfNonTerminals() {
        return setOfNonTerminals;
    }

    public void setSetOfNonTerminals(List<String> setOfNonTerminals) {
        this.setOfNonTerminals = setOfNonTerminals;
    }

    public List<String> getSetOfTerminals() {
        return setOfTerminals;
    }

    public void setSetOfTerminals(List<String> setOfTerminals) {
        this.setOfTerminals = setOfTerminals;
    }

    public HashMap<List<String>, List<List<String>>> getSetOfProductions() {
        return setOfProductions;
    }

    public void setSetOfProductions(HashMap<List<String>, List<List<String>>> setOfProductions) {
        this.setOfProductions = setOfProductions;
    }

    public List<List<String>> getProductionsForNonTerminal(String nonterminal) {
        for (Map.Entry<List<String>, List<List<String>>> entry : setOfProductions.entrySet()) {
            List<String> key = entry.getKey();
            if (key.contains(nonterminal)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public String getStartingSymbol() {
        return startingSymbol;
    }

    public void setStartingSymbol(String startingSymbol) {
        this.startingSymbol = startingSymbol;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
