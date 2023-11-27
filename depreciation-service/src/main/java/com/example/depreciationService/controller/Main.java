package com.example.depreciationService.controller;
import java.util.*;

class Example {
    String id;
    String SN;
    int value;

    public Example(String id, String SN, int value) {
        this.id = id;
        this.SN = SN;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSN() {
        return SN;
    }

    public void setSN(String SN) {
        this.SN = SN;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    // constructor, getters and setters
}

public class Main {
    public static void main(String[] args) {
        List<Example> list = new ArrayList<>();
        list.add(new Example("1","SN1",14));
        list.add(new Example("2","SN2",14));
        list.add(new Example("3","SN3",14));
        list.add(new Example("4","SN1",19));
        list.add(new Example("5","SN2",11));
        // add elements to list

        Map<String, Integer> map = new HashMap<>();
        for (Example example : list) {
            map.put(example.getSN(), map.getOrDefault(example.getSN(), 0) + example.getValue());
        }
        System.out.println(map);

    }
}
