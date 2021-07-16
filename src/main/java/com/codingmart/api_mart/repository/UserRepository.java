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

import javax.print.Doc;
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
        FindIterable<Document> users = collection.find(filter);
        System.out.println("users = " + users.toString());
        for(Document item: users) {
            User findUser = new User(item.get("_id").toString(), item.getString("name"), item.getString("email"), item.getString("password"), item.get("created_at").toString());
            return findUser;
        }
        return null;
//        return new User("60ef23406d2dd577168dffeb", "Test", "test@gmail.com", "$2a$10$akNyrwr72B5x.mXm8JE1puE/Bels6xEhGPvIuxPIPxPBYvn8.uv4K", "2021-07-14T17:47:44.731+00:00");
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
//
//        BasicDBObject filterByName = new BasicDBObject("name", name);
//        BasicDBObject filterByEmail = new BasicDBObject("name", name);
//        System.out.println("filterByEmail = " + filterByEmail);
//        System.out.println("filterByEmail.toString() = " + filterByEmail.toString());
//        if( filterByEmail.isEmpty() && filterByName.isEmpty() ) return false;
//        return true;
    }

    public User findById(String id) {
        Bson filters = Filters.eq("_id", id);
        FindIterable<Document> users = collection.find(filters);
        for (Document item:users) {
            User findUser = new User(item.get("_id").toString(), item.getString("name"), item.getString("email"), item.getString("password"), item.get("created_at").toString());
            return findUser;
        }
        return null;
    }
}
