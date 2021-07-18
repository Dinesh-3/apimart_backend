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
        collection.insertMany(documents);
        return true;
    }

    public List<HashMap<String, String>> getRecordsByCollection(String collectionName, Map<String,String> queryParams) throws IOException {
        if(collectionName == "user" || collectionName == "table") return null;

        MongoCollection<Document> collection = mongoDBClient.getCollection(collectionName);

//        for (Object key : queryParams.keySet()) {
//             filter = Filters.eq(key.toString(), queryParams.get(key));
//             break;
//        }

        Bson projection = Projections.fields(Projections.excludeId());



        Map<String, Integer> pageQuery = getPageQuery(queryParams);

        queryParams.remove("query_limit");
        queryParams.remove("query_page");

        Bson filter = Document.parse(new Gson().toJson(queryParams));

        FindIterable<Document> documents = null;
        if(pageQuery.containsKey("limit") && pageQuery.containsKey("page")) {
            int limit = pageQuery.get("limit");
            int page = pageQuery.get("page");
            documents = collection.find(filter).projection(projection).skip(limit * (page - 1)).limit(limit);
        }else {
            documents = collection.find(filter).projection(projection);
        }

        List<HashMap<String, String>> records = new ArrayList<>();
        for (Document item:documents) {
            Type type = new TypeToken<HashMap<String, String>>(){}.getType();
            HashMap<String,String> map = new Gson().fromJson(item.toJson(), type);

            records.add(map);
        }
        return records;
    }

    private Map<String, Integer> getPageQuery(Map<String, String> query) {
        Map<String, Integer> hashMap = new HashMap<>();
        try {
            int limit = Integer.parseInt(query.get("query_limit"));
            int page = Integer.parseInt(query.get("query_page"));
            hashMap.put("limit", limit);
            hashMap.put("page", page);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return hashMap;
    }

   private Map<String, String> getHashMap(){ return new HashMap<String, String>();}

}
