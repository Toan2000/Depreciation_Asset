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
        Map<String,Double> data = new HashMap<>();
        data.put("1",30.0);

        data.get("1");
        System.out.println(data);
        data.put("1",data.get("1")+40.0);
        System.out.println(data);
        System.out.println(data.get("2"));

    }
}
