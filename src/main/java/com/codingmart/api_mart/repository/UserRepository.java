package com.codingmart.api_mart.repository;

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
public class UserRepository {

    private MongoDatabase mongoDBClient = MongoDBClient.getDatabase();
    private MongoCollection<Document> collection = mongoDBClient.getCollection("user");
    public List<User> getAll() {

        List<User> response = new ArrayList<>();
        for (Document item:collection.find()) {
            User user = new User(item.get("_id").toString(), item.getString("name"), item.getString("email"), item.getString("password"), item.get("created_at").toString());
            response.add(user);
        }
        return response;
    }

    public User findByEmail(String email) {

        Bson filter = Filters.eq("email", email);
        Document user = collection.find(filter).first();

        User findUser = new User(user.get("_id").toString(), user.getString("name"), user.getString("email"), user.getString("password"), user.get("created_at").toString());

        return findUser;
    }

    public User findByName(String name) {
        Bson filter = Filters.eq("name", name);
        Document item = collection.find(filter).first();
        User findUser = new User(item.get("_id").toString(), item.getString("name"), item.getString("email"), item.getString("password"), item.get("created_at").toString());
        return findUser;
    }

    public boolean save(User user) {
        Document userDocument = new Document();
        userDocument.append("name", user.getName());
        userDocument.append("email", user.getEmail());
        userDocument.append("password", user.getPassword());
        userDocument.append("created_at", user.getCreated_at());
        try {
            collection.insertOne(userDocument);
            return true;
        }catch (Exception error) {
            return false;
        }
    }

    public boolean isEmailOrNameExists(String name, String email) {
        Bson filters = Filters.or(Filters.eq("name", name), Filters.eq("email", email));

        FindIterable<Document> users = collection.find(filters);

        for (Document user: users) {
            return true;
        }
        return false;
    }

    public User findById(String id) {
        Bson filters = Filters.eq("_id", id);
        Document user = collection.find(filters).first();
        User findUser = new User(user.get("_id").toString(), user.getString("name"), user.getString("email"), user.getString("password"), user.get("created_at").toString());
        return findUser;
    }
}
