package com.codingmart.api_mart.repository;

import com.codingmart.api_mart.model.User;
import com.codingmart.api_mart.utils.MongoDBClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class CollectionRepository {
    private MongoDatabase mongoDBClient = MongoDBClient.getDatabase();

    public boolean saveCollection(String collectionName, List<Map<String, String>> records) {
        if(collectionName == "user") return false;
        MongoCollection<Document> collection = mongoDBClient.getCollection(collectionName);
        List<Document> documents = new ArrayList<>();

        for (Map record:records) {
            Document document = new Document();
            for (Object key : record.keySet()) {
                document.append(key.toString(), record.get(key));
            }
            documents.add(document);
        }

        try {
            collection.insertMany(documents);
            return true;
        }catch (Exception error) {
            return false;
        }
    }
}
