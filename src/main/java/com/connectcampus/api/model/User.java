package com.connectcampus.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User{
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private Boolean establishmentOwner;
    private String photo;

    public User(String name, String email, String password, Boolean establishmentOwner ,String photo) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.establishmentOwner = establishmentOwner;
        this.photo = photo;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEstablishmentOwner(){
        return establishmentOwner;
    }

    public void setEstablishmentOwner(Boolean establishmentOwner){
        this.establishmentOwner = establishmentOwner;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString(){
        return "Id: " + id +
                ", name: " + name +
                ", email: " + email +
                ", password: " + password +
                ", establishmentOwner: " + establishmentOwner +
                ", photo url: " + photo;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj.getClass() != this.getClass())
            return false;

        User u = (User) obj;

        if(u.id != this.id)
            return false;
        if(u.name != this.name)
            return false;
        if(u.email != this.email)
            return false;
        if(u.password != this.password)
            return false;
        if(u.photo != this.photo)
            return false;
        if(u.establishmentOwner != this.establishmentOwner)
            return false;

        return true;
    }

    @Override
    public int hashCode(){
        int ret = 1;
        ret = ret * 11 + id.hashCode();
        ret = ret * 11 + name.hashCode();
        ret = ret * 11 + email.hashCode();
        ret = ret * 11 + password.hashCode();
        ret = ret * 11 + photo.hashCode();
        ret = ret * 11 + Boolean.hashCode(establishmentOwner);

        if(ret < 0)
            ret = -ret;

        return ret;
    }

}
