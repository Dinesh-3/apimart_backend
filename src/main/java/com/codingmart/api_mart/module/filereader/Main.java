package com.codingmart.api_mart.module.filereader;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        XlsMapParser parser = new XlsMapParser();
        parser.parseFile(new ArrayList<>(), "sample.csv");
    }
}
