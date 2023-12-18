package FA;

import java.util.List;

public class Transitions {
    private String initialState;
    private String value;
    private List<String> finalStates;

    public Transitions() {
    }

    public String getInitialState() {
        return initialState;
    }

    public void setInitialState(String initialState) {
        this.initialState = initialState;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getFinalStates() {
        return finalStates;
    }

    public void setFinalStates(List<String> finalStates) {
        this.finalStates = finalStates;
    }


    public String toString() {
        return "(" + initialState + "," + value + "," + finalStates + ")";
    }
}
