package com.connectcampus.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public class Product{
    @Id
    private String id;
    private String establishmentId;
    private String name;
    private String description;
    private String evaluation;
    private String photo;
    private String price;

    public Product(String establishmentId, String name, String description, String evaluation, String photo, String price) {
        this.establishmentId = establishmentId;
        this.name = name;
        this.description = description;
        this.evaluation = evaluation;
        this.photo = photo;
        this.price = price;
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

    public String getPrice(){
        return price;
    }

    public void setPrice(String price){
        this.price = price;
    }

    @Override
    public String toString(){
        return "Id: " + id +
                ", establishmentId: " + establishmentId +
                ", name: " + name +
                ", description: " + description +
                ", evaluation: " + evaluation +
                ", photo url: " + photo +
                ", price: " + price;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj.getClass() != this.getClass())
            return false;

        Product p = (Product) obj;

        if(p.id != this.id)
            return false;
        if(p.establishmentId != this.establishmentId)
            return false;
        if(p.name != this.name)
            return false;
        if(p.description != this.description)
            return false;
        if(p.evaluation != this.evaluation)
            return false;
        if(p.photo != this.photo)
            return false;
        if(p.price != this.price)
            return false;

        return true;
    }

    @Override
    public int hashCode(){
        int ret = 1;
        ret = ret * 11 + id.hashCode();
        ret = ret * 11 + establishmentId.hashCode();
        ret = ret * 11 + name.hashCode();
        ret = ret * 11 + description.hashCode();
        ret = ret * 11 + evaluation.hashCode();
        ret = ret * 11 + photo.hashCode();
        ret = ret * 11 + price.hashCode();

        if(ret < 0)
            ret = -ret;

        return ret;
    }
}
