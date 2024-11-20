package com.connectcampus.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "establishments")
public class Establishment{
    @Id
    private String id;
    private String name;
    private String description;
    private String openingHours;
    private String photo;
    private String ownerId;

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

    @Override
    public String toString(){
        return "Id: " + id +
                ", name: " + name +
                ", description: " + description +
                ", openingHours: " + openingHours +
                ", photo url: " + photo +
                ", ownerId: " + ownerId;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj.getClass() != this.getClass())
            return false;

        Establishment e = (Establishment) obj;

        if(e.id != this.id)
            return false;
        if(e.name != this.name)
            return false;
        if(e.description != this.description)
            return false;
        if(e.openingHours != this.openingHours)
            return false;
        if(e.photo != this.photo)
            return false;
        if(e.ownerId != this.ownerId)
            return false;

        return true;
    }

    @Override
    public int hashCode(){
        int ret = 1;
        ret = ret * 11 + id.hashCode();
        ret = ret * 11 + name.hashCode();
        ret = ret * 11 + description.hashCode();
        ret = ret * 11 + openingHours.hashCode();
        ret = ret * 11 + photo.hashCode();
        ret = ret * 11 + ownerId.hashCode();

        if(ret < 0)
            ret = -ret;

        return ret;
    }
}
