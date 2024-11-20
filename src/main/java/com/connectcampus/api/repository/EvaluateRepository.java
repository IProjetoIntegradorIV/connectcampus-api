package com.connectcampus.api.repository;

import com.connectcampus.api.model.Evaluate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EvaluateRepository extends MongoRepository<Evaluate, String> {
    List<Evaluate> findByProductId(String productId);
}
