package FA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FiniteAutomata {
    private List<String> allStates;
    private List<String> alphabet;
    private List<String> finalStates;
    private List<Transitions> transitions;
    private String initialState;
    private String fileName;

    public FiniteAutomata(String fileName) {
        this.allStates = new ArrayList<String>();
        this.alphabet = new ArrayList<String>();
        this.finalStates = new ArrayList<String>();
        this.transitions = new ArrayList<Transitions>();
        this.initialState = "";
        this.fileName = fileName;
    }

    public void readFile() {
        File file = new File(this.fileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            //initial state
            String str = reader.readLine();//this will be the line containing "INITIAL STATE'
            //System.out.println(str);
            this.initialState = reader.readLine();
            //System.out.println(initialState);

            //final states
            str = reader.readLine(); // this will be the line containing "FINAL STATE"
            //System.out.println(str);
            this.finalStates = Arrays.asList(reader.readLine().split(","));
            //System.out.println(finalStates);

            //all states
            str = reader.readLine(); // this will be the line containing "STATES"
            //System.out.println(str);
            this.allStates = Arrays.asList(reader.readLine().split(","));
            //System.out.println(allStates.toString());

            //alphabet
            str = reader.readLine(); //this will be the line containing "ALPHABET"
            //System.out.println(str);
            this.alphabet = Arrays.asList(reader.readLine().split(","));
            //System.out.println(alphabet);

            //transitions
            str = reader.readLine(); //this will be the line containing "TRANSITIONS"
            //System.out.println(str);
            String readTransition = reader.readLine();
            while (readTransition != null) {
                List<String> separaredTransition = Arrays.asList(readTransition.split(","));
                Transitions currentTransition = new Transitions();
                currentTransition.setInitialState(separaredTransition.get(0));
                currentTransition.setValue(separaredTransition.get(1));
                List<String> finalStates = new ArrayList<String>();
                for (int i = 2; i < separaredTransition.size(); i++) {
                    finalStates.add(separaredTransition.get(i));
                }
                currentTransition.setFinalStates(finalStates);
                this.transitions.add(currentTransition);
                readTransition = reader.readLine();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public List<String> getAllStates() {
        return allStates;
    }

    public List<String> getAlphabet() {
        return alphabet;
    }

    public List<String> getFinalStates() {
        return finalStates;
    }

    public List<Transitions> getTransitions() {
        return transitions;
    }

    public String getInitialState() {
        return initialState;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isDFA() {
        for (Transitions t : transitions) {
            if (t.getFinalStates().size() > 1) {
                return false;
            }
        }
        return true;
    }

    public boolean isAccepted(String seq) {
        String currentState = this.initialState;
        String[] sequence = seq.split("");
        for (String character : sequence) {
            //System.out.println("in is accepted " + character);
            String nextState = nextState(currentState, character);

            if (nextState.equals("no-state-found"))
                return false;

            currentState = nextState;

        }
        return this.finalStates.contains(currentState);
    }

    public String nextState(String initialState, String value) {
        for (Transitions t : this.transitions) {
            if (t.getInitialState().equals(initialState) && t.getValue().equals(value)) {
                if (t.getFinalStates().size() == 1) {
                    return t.getFinalStates().get(0);
                }
            }
        }
        return "no-state-found";
    }

}
