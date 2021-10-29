package com.codingmart.api_mart.utils;

public class FileName {
    private String name;
    private String type;
    private String fullName;

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

    public String getFullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return fullName;
    }
}
