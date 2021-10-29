package com.codingmart.api_mart.repository;

import com.codingmart.api_mart.ExceptionHandler.UserNotFoundException;
import com.codingmart.api_mart.model.User;
import com.codingmart.api_mart.utils.MongoDBClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    private final MongoDatabase mongoDBClient = MongoDBClient.getDatabase();
    private final MongoCollection<Document> collection = mongoDBClient.getCollection("user");
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
        if(user == null) throw new UserNotFoundException(HttpStatus.NOT_FOUND, String.format("User with email %s Not Found", email));
        return new User(user.get("_id").toString(), user.getString("name"), user.getString("email"), user.getString("password"), user.get("created_at").toString(), user.getString("user_id"));
    }

    public User findByName(String name) {
        Bson filter = Filters.eq("name", name);
        Document item = collection.find(filter).first();
        return new User(item.get("_id").toString(), item.getString("name"), item.getString("email"), item.getString("password"), item.get("created_at").toString());
    }

    public boolean save(User user) {
        Document userDocument = new Document();
        userDocument.append("name", user.getName());
        userDocument.append("email", user.getEmail());
        userDocument.append("password", user.getPassword());
        userDocument.append("created_at", user.getCreated_at());
        userDocument.append("user_id", user.getUser_id());
        try {
            collection.insertOne(userDocument);
            return true;
        }catch (Exception error) {
            return false;
        }
    }

    public boolean isEmailOrNameExists(String name, String email) {
        Bson filters = Filters.or(Filters.eq("name", name), Filters.eq("email", email));

        Document user = collection.find(filters).first();
        return user != null;
    }

    public User findById(String id) {
        Bson filter = Filters.or(Filters.eq("user_id", id), Filters.eq("_id", id));
        Document user = collection.find(filter).first();
        if(user == null) throw new UserNotFoundException(HttpStatus.NOT_FOUND, String.format("User with id %s Not Found", id));
        return new User(user.get("_id").toString(), user.getString("name"), user.getString("email"), user.getString("password"), user.get("created_at").toString(), user.getString("user_id"));
    }
}
