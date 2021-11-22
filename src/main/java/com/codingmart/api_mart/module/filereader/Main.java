package com.codingmart.api_mart.module.filereader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        XlsMapParser parser = new XlsMapParser();
        try {
            parser.parseFile(new ArrayList<>(), new File("sample.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
