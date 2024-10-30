package com.connectcampus.api.repository;

import com.connectcampus.api.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByEstablishmentId(String establishmentId);
}
