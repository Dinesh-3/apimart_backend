package com.codingmart.api_mart.utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBClient {

    public static MongoDatabase getDatabase(){
        MongoClient mongoClient = getMongoClient();
        MongoDatabase database = mongoClient.getDatabase("myFirstDatabase");
        return database;
    }

    public static MongoClient getMongoClient() {
        MongoClientURI URI = new MongoClientURI("mongodb+srv://root:aZUnLWfdUMcB3my2@cluster0.obetp.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
        return new MongoClient(URI);
    }
}
