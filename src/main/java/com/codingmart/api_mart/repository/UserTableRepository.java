package com.codingmart.api_mart.repository;

import com.codingmart.api_mart.model.Table;
import com.codingmart.api_mart.model.User;
import com.codingmart.api_mart.utils.MongoDBClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserTableRepository {
    private MongoDatabase mongoDBClient = MongoDBClient.getDatabase();
    private MongoCollection<Document> collection = mongoDBClient.getCollection("table");

    public List<Table> getAll() {
        List<Table> response = new ArrayList<>();
        for (Document item:collection.find()) {
            Table table = new Table(item.get("_id").toString(), item.getString("user"), item.getString("fileName"), item.getString("collection"), item.getString("created_at"));
            response.add(table);
        }
        return response;
    }

    public List<Table> findByUserAndCollection(String user, String table) {
        Bson filter = Filters.and(Filters.eq("user", user), Filters.eq("collection", table));
        FindIterable<Document> userTables = collection.find(filter);
        List<Table> tables = new ArrayList<>();
        for(Document item: userTables) {
            Table updatedTable = new Table(item.get("_id").toString(), item.getString("user"), item.getString("fileName"), item.getString("collection"), item.get("created_at").toString());
            tables.add(updatedTable);
        }
        return tables;
    }

    public List<Table> findByUser(String user) {
        Bson filter = Filters.eq("user", user);
        FindIterable<Document> userTables = collection.find(filter);
        List<Table> tables = new ArrayList<>();
        for(Document item: userTables) {
            Table updatedTable = new Table(item.get("_id").toString(), item.getString("user"), item.getString("fileName"), item.getString("collection"), item.get("created_at").toString());
            tables.add(updatedTable);
        }
        return tables;
    }

    public boolean isUserAndCollectionExists(String user, String table) {
        Bson filter = Filters.and(Filters.eq("user", user), Filters.eq("collection", table));
        FindIterable<Document> userTables = collection.find(filter);
        for(Document item: userTables) {
//            Table updatedTable = new Table(item.get("_id").toString(), item.getString("user"), item.getString("collection"), item.get("created_at").toString());
            return true;
        }
        return false;
    }

    public boolean save(Table table) {
        Document userDocument = new Document();
        userDocument.append("user", table.getUser());
        userDocument.append("fileName", table.getFileName());
        userDocument.append("collection", table.getCollection());
        userDocument.append("created_at", table.getCreated_at());
        try {
            collection.insertOne(userDocument);
            return true;
        }catch (Exception error) {
            return false;
        }
    }
}
