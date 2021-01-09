package com.altimetrik.main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



@SpringBootApplication
@EntityScan(basePackages = "com.altimetrik.model")
@ComponentScan(basePackages = "com.altimetrik")
@EnableJpaRepositories(basePackages = "com.altimetrik.repo")
public class StallManagementApplication {
	public static void main(String[] args) {
		SpringApplication.run(StallManagementApplication.class, args);
		
	}
}
