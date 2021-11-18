package com.codingmart.api_mart.utils;

public class FileName {
    private final String name;
    private final String type;
    private final String fullName;

    public FileName(String fullName) {
        String[] splitName = fullName.split("[.]");
        this.fullName = fullName;
        this.name = splitName[0];
        this.type = splitName[1];
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return fullName;
    }
}
