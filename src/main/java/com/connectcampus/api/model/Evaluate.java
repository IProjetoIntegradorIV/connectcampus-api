package com.connectcampus.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "evaluates")
public class Evaluate{

    @Id
    private String id;
    private String userId;
    private String productId;
    private float rating;

    public Evaluate(String userId, String productId, float rating) {
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public String toString(){
        return "Id: " + id +
                ", userID: " + userId +
                ", ProductId: " + productId +
                ", rating: " + rating;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj.getClass() != this.getClass())
            return false;

        Evaluate e = (Evaluate) obj;

        if(e.id != this.id)
            return false;
        if(e.productId != this.productId)
            return false;
        if(e.userId != this.userId)
            return false;
        if(e.rating != this.rating)
            return false;

        return true;
    }

    @Override
    public int hashCode(){
        int ret = 1;
        ret = ret * 11 + id.hashCode();
        ret = ret * 11 + userId.hashCode();
        ret = ret * 11 + productId.hashCode();
        ret = ret * 11 + Float.hashCode(rating);

        if(ret < 0)
            ret = -ret;

        return ret;
    }
}
