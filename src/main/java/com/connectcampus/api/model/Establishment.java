package com.connectcampus.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "establishments")
public class Establishment {
    @Id
    private String id;
    private String name;
    private String description;
    private String openingHours;
    private String photo;
    private String ownerId;

    public Establishment() {}

    public Establishment(String name, String description, String openingHours, String photo, String ownerId) {
        this.name = name;
        this.description = description;
        this.openingHours = openingHours;
        this.photo = photo;
        this.ownerId = ownerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getOwnerId(){
        return ownerId;
    }

    public void setOwnerId(String ownerId){
        this.ownerId = ownerId;
    }
}
