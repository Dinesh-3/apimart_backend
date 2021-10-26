package com.codingmart.api_mart;

import com.codingmart.api_mart.service.MailService;
import com.codingmart.api_mart.utils.MongoDBClient;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeExceptionMapper;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ApiMartApplication implements CommandLineRunner {

	@Value("${server.port:NA}")
	private String PORT;

	@Value("${spring.data.mongodb.uri}")
	private String URI;

	public static void main(String[] args) {
		SpringApplication.run(ApiMartApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		MongoClient mongoClient = MongoDBClient.getMongoClient();
		mongoClient.startSession();
		System.out.println("Server Successfully Started on Port: " + PORT);
	}

//	@Override
//	public int getExitCode() {
//		return 0; // Process finished with exit code 0 : for success
//	}

//	@Bean
//	ExitCodeExceptionMapper exitCodeExceptionMapper(){
//		return exception -> {
//			if(exception.getCause() instanceof NullPointerException) return 10;
//			if(exception.getCause() instanceof NumberFormatException) return 11;
//			return 0;
//		};
//	}

	@Bean
	@Primary
	MailService getMailService() {
		return new MailService();
	}

//	@Bean
//	CommandLineRunner runner() {
//		return args -> {
//			String name = null;
//			name.charAt(0); // Process finished with exit code 10
//		};
//	}

	@Configuration
	public class WebConfiguration implements WebMvcConfigurer {
		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**").allowedMethods("*");
		}
	}
}

