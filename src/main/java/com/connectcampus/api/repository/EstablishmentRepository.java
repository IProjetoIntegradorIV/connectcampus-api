package com.connectcampus.api.repository;

import com.connectcampus.api.model.Establishment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface EstablishmentRepository extends MongoRepository<Establishment, String> {
    Optional<Establishment> findByOwnerId(String ownerId);
    List<Establishment> findByNameContainingIgnoreCase(String name);
}
