package com.codingmart.api_mart.repository;

import com.codingmart.api_mart.utils.MongoDBClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CollectionRepository {
    private MongoDatabase mongoDBClient = MongoDBClient.getDatabase();

    public boolean saveCollection(String collectionName, List<Map<String, Object>> records) {
        if(collectionName == "user" || collectionName == "table") return false;
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

    public List<HashMap<String, String>> getRecordsByCollection(String collectionName, Map<String,String> queryParams) throws IOException {
        if(collectionName == "user" || collectionName == "table") return null;
        MongoCollection<Document> collection = mongoDBClient.getCollection(collectionName);
        Bson filter = Document.parse(new Gson().toJson(queryParams));
//        for (Object key : queryParams.keySet()) {
//             filter = Filters.eq(key.toString(), queryParams.get(key));
//             break;
//        }

        Bson projection = Projections.fields(Projections.excludeId());
        FindIterable<Document> documents = collection.find(filter).projection(projection);
        List<HashMap<String, String>> records = new ArrayList<>();
        for (Document item:documents) {
            Type type = new TypeToken<HashMap<String, String>>(){}.getType();
            HashMap<String,String> map = new Gson().fromJson(item.toJson(), type);

            records.add(map);
        }
        return records;
    }

   private Map<String, String> getHashMap(){ return new HashMap<String, String>();}

}
