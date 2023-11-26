package domain;

import java.util.List;

public class Production {
    private String key;
    private List<String> value;

    public Production(){}

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }


    public String toString(){
        return key + " -> " + value;
    }
}
