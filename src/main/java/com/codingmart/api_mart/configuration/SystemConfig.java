package com.codingmart.api_mart.configuration;

import java.util.HashSet;
import java.util.Set;

public class SystemConfig {
    private static final Set<String> reservedCollection = new HashSet<>();

    static {
        reservedCollection.add("user");
        reservedCollection.add("verification");
        reservedCollection.add("table");
    }

    public static Set<String> getReservedCollections(){
        return reservedCollection;
    }
}
