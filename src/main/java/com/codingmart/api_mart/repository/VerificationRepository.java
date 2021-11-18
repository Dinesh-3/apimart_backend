package com.codingmart.api_mart.repository;

import com.codingmart.api_mart.ExceptionHandler.ResourceNotFoundException;
import com.codingmart.api_mart.model.User;
import com.codingmart.api_mart.model.Verification;
import com.codingmart.api_mart.utils.MongoDBClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

@Repository
public class VerificationRepository {
    private static final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    private static final ObjectMapper mapper  = new ObjectMapper();

    private final MongoDatabase mongoDBClient = MongoDBClient.getDatabase();
    private final MongoCollection<Document> collection = mongoDBClient.getCollection("verification");

    public boolean save(Verification verification) throws JsonProcessingException {
        String json = ow.writeValueAsString(verification);
        Document document = Document.parse(json);
        try {
            collection.insertOne(document);
            return true;
        }catch (Exception error) {
            return false;
        }
    }

    public Verification findById(String id) {
        Bson filters = Filters.eq("user_id", id);
        FindIterable<Document> documents = collection.find(filters);
        Document user = documents.first();
        if(user == null) throw new ResourceNotFoundException(String.format("User %s Not Found", id));
        user.remove("_id");
        try {
            return mapper.readValue(user.toJson(), Verification.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        throw new ResourceNotFoundException("Verification Resource Not Found");
    }

    public void findOneAndUpdate(String id, Verification verification){
        Bson filter = Filters.eq("user_id", id);
        String json = null;
        try {
            json = ow.writeValueAsString(verification);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Document document = Document.parse(json);
        document.remove("id");
        UpdateOptions options = new UpdateOptions().upsert(false);
        UpdateResult updatedDocument = collection.replaceOne(filter, document, options);
    }
}
