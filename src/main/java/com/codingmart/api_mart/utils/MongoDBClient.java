package com.codingmart.api_mart.utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

/**
 * Singleton Pattern usage
 */
public class MongoDBClient implements Cloneable, Serializable {
    private static volatile MongoClient mongoDBClient = null;
    static {
        setValue();
    }
    private MongoDBClient() {
        if(mongoDBClient != null) throw new Error("Instance creation not allowed");
    }
    public static MongoDatabase getDatabase(){
        MongoDatabase database = mongoDBClient.getDatabase("myFirstDatabase");
        return database;
    }

    public static MongoClient getMongoClient() {
        if(mongoDBClient == null) setValue();
        return mongoDBClient;
    }

    private static synchronized void setValue() {
        if(mongoDBClient == null) {
            MongoClientURI URI = new MongoClientURI("mongodb+srv://root:aZUnLWfdUMcB3my2@cluster0.obetp.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
            mongoDBClient = new MongoClient(URI);
        }
    }

    protected Object readResolve() { // For prevent from serialization
        return mongoDBClient;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning not allowed for Mongodb Object");
    }
}
