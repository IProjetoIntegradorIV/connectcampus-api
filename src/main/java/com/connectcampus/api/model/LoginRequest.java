package com.connectcampus.api.model;

public class LoginRequest{
    private String email;
    private String password;

    public LoginRequest() {}

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

    @Override
    public String toString(){
        return "email: " + email +
                ", password: " + password;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj.getClass() != this.getClass())
            return false;

        LoginRequest g = (LoginRequest) obj;

        if(g.email != this.email)
            return false;
        if(g.password != this.password)
            return false;

        return true;
    }

    @Override
    public int hashCode(){
        int ret = 1;
        ret = ret * 11 + email.hashCode();
        ret = ret * 11 + password.hashCode();

        if(ret < 0)
            ret = -ret;

        return ret;
    }
}
