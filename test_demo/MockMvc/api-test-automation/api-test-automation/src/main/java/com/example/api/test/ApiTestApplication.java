package com.example.api.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ApiTestApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiTestApplication.class, args);
		System.out.println("API Test Automation Application Started!");
		System.out.println("Swagger UI: http://localhost:8080/swagger-ui.html");
		System.out.println("H2 Console: http://localhost:8080/h2-console");
	}
}