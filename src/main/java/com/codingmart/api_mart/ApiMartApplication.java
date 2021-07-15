package com.codingmart.api_mart;

import com.codingmart.api_mart.utils.MongoDBClient;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiMartApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ApiMartApplication.class, args);
		System.out.println("Server Started...");
	}

	@Override
	public void run(String... args) throws Exception {
		MongoDBClient.getMongoClient();
	}

}
