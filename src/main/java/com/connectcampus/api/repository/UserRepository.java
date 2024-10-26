package com.connectcampus.api.repository;

import com.connectcampus.api.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}

