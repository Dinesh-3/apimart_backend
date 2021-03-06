package com.codingmart.api_mart.repository;

import com.codingmart.api_mart.utils.MongoDBClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

@Repository
public class CollectionRepository {
    private final MongoDatabase mongoDBClient = MongoDBClient.getDatabase();
    @Autowired
    private Gson gson;
    public boolean saveCollection(String collectionName, List<Map<String, Object>> records) {
        MongoCollection<Document> collection = mongoDBClient.getCollection(collectionName);
        List<Document> documents = new ArrayList<>();

        for (Map record:records) {
            Document document = new Document();
            for (Object key : record.keySet()) {
                document.append(key.toString(), record.get(key));
            }
            documents.add(document);
        }
        collection.insertMany(documents);
        return true;
    }

    public List<Map<String, String>> getRecordsByCollection(String collectionName, Map<String,String> queryParams) throws IOException {

        MongoCollection<Document> collection = mongoDBClient.getCollection(collectionName);

//        for (Object key : queryParams.keySet()) {
//             filter = Filters.eq(key.toString(), queryParams.get(key));
//             break;
//        }

        Bson projection = Projections.fields(Projections.excludeId());



        Map<String, Integer> pageQuery = getPageQuery(queryParams);

        queryParams.remove("query_limit");
        queryParams.remove("query_page");

        Bson filter = Document.parse(gson.toJson(queryParams));
        FindIterable<Document> documents;
        if(pageQuery.containsKey("limit") && pageQuery.containsKey("page")) {
            int limit = pageQuery.get("limit");
            int page = pageQuery.get("page");
            documents = collection.find(filter).skip(limit * (page - 1)).limit(limit);
        }else {
            documents = collection.find(filter);
        }

        List<Map<String, String>> records = new ArrayList<>();
        for (Document item:documents) {
            Type type = new TypeToken<HashMap<String, String>>(){}.getType();
            String id = item.getObjectId("_id").toString();
            item.remove("_id");
            HashMap<String,String> map = gson.fromJson(item.toJson(), type);
            map.put("_id", id);
            records.add(map);
        }
        return records;
    }

    public Map<String, String> insertRecord(String collectionName, Map<String, String> requestBody) {
        MongoCollection<Document> collection = mongoDBClient.getCollection(collectionName);

        Document document = new Document();
        for (String key : requestBody.keySet()) {
            document.append(key, requestBody.get(key));
        }
        collection.insertOne(document);

        return requestBody;

    }


    public Map<String, String> updateRecord(String collectionName, Map<String,String> queryParams, Map<String, String> requestBody) {

        MongoCollection<Document> collection = mongoDBClient.getCollection(collectionName);
        if(collection.countDocuments() < 0) return null;

        Bson filter = Document.parse(gson.toJson(queryParams));

        Document document = new Document();
        for (String key : requestBody.keySet()) {
            document.append(key, requestBody.get(key));
        }
        UpdateOptions options = new UpdateOptions().upsert(false);
        collection.replaceOne(filter, document, options);
        return requestBody;
    }

    public boolean deleteRecord(String collectionName, Map<String,String> queryParams) {

        MongoCollection<Document> collection = mongoDBClient.getCollection(collectionName);
        if(collection.countDocuments() < 0) return false;

        Bson filter = Document.parse(gson.toJson(queryParams));

        DeleteResult updatedDocument = collection.deleteOne(filter);

        return true;
    }

    private boolean collectionExists(final String collectionName) {
        return mongoDBClient.listCollectionNames()
                .into(new ArrayList<>()).contains(collectionName);
    }

    private Map<String, Integer> getPageQuery(Map<String, String> query) {
        Map<String, Integer> hashMap = new HashMap<>();
        try {
            int limit = Integer.parseInt(query.get("query_limit"));
            int page = Integer.parseInt(query.get("query_page"));
            hashMap.put("limit", limit);
            hashMap.put("page", page);
        }catch (Exception e) {
            return hashMap;
        }
        return hashMap;
    }

}
