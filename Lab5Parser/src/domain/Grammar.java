package domain;

import util.Pair;

import java.io.File;
import java.util.*;

public class Grammar {

    private List<String> setOfNonTerminals;
    private List<String> setOfTerminals;
    private HashMap<String, List<String>> setOfProductions;
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

            if (checkCFG(productions.get(0))) {
                //System.out.println("prods" + productions);
                List<String> states = Arrays.asList(productions.get(1).split(" \\| "));
                //System.out.println("states" + states);

                Pair<String, List<String>> model = new Pair<>(productions.get(0), states);
                //System.out.println(model);
                this.setOfProductions.put(model.getFirst(), model.getSecond());
            } else {
                throw new Exception("The grammar is not CFG!");
            }


        }

        //Starting symbol
        this.startingSymbol = scanner.nextLine();
        //System.out.println(this.startingSymbol);

        scanner.close();

    }


    public boolean checkCFG(String nonTerms) {
        String[] extractedNonTerminals = nonTerms.split(",");
        return extractedNonTerminals.length <= 1;
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

    public HashMap<String, List<String>> getSetOfProductions() {
        return setOfProductions;
    }

    public void setSetOfProductions(HashMap<String, List<String>> setOfProductions) {
        this.setOfProductions = setOfProductions;
    }

    public List<String> getProductionsForNonTerminal(String nonterminal) {
        return this.setOfProductions.get(nonterminal);
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
