package com.example.AgaiFullkoiasyzba;

import java.util.ArrayList;

public class Test extends Question {
    private String[] options;
    private int numOfOptions;
    private ArrayList<Character> labels;

    public void setOptions(String[] options){
        this.options = options;
    }

    public String getOptionAt(int numOfOptions){
        return options[numOfOptions];
    }

    public String[] getOptions(){
        return options;
    }

}

