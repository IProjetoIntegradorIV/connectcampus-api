package com.connectcampus.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public class Product {
    @Id
    private String id;
    private String establishmentId;
    private String name;
    private String description;
    private String evaluation;
    private String photo;

    public Product() {}

    public Product(String establishmentId, String name, String description, String evaluation, String photo) {
        this.establishmentId = establishmentId;
        this.name = name;
        this.description = description;
        this.evaluation = evaluation;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEstablishmentId(){
        return establishmentId;
    }

    public void setEstablishmentId(String establishmentId){
        this.establishmentId = establishmentId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
