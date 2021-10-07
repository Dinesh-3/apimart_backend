package com.codingmart.api_mart;

import com.codingmart.api_mart.utils.MongoDBClient;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ApiMartApplication implements CommandLineRunner {

	@Value("${server.port:NA}")
	private String PORT;

	public static void main(String[] args) {
		SpringApplication.run(ApiMartApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		MongoClient mongoClient = MongoDBClient.getMongoClient();
		mongoClient.startSession();
		System.out.println("Server Successfully Started on Port: " + PORT);
	}
	@Configuration
	public class WebConfiguration implements WebMvcConfigurer {
		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**").allowedMethods("*");
		}
	}
}

