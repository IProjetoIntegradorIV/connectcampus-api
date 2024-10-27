package com.connectcampus.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.connectcampus.api.repository")
public class ConnectcampusApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConnectcampusApiApplication.class, args);
    }
}
