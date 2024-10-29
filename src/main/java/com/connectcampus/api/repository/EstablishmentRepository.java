package com.connectcampus.api.repository;

import com.connectcampus.api.model.Establishment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EstablishmentRepository extends MongoRepository<Establishment, String> {
}
