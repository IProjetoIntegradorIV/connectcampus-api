package com.connectcampus.api.model;

public class GoogleLoginRequest{
    private String idToken;

    public GoogleLoginRequest(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    @Override
    public String toString(){
        return "IdToken: " + idToken;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj.getClass() != this.getClass())
            return false;

        GoogleLoginRequest g = (GoogleLoginRequest) obj;

        if(g.idToken != this.idToken)
            return false;

        return true;
    }

    @Override
    public int hashCode(){
        int ret = 1;
        ret = ret * 11 + idToken.hashCode();

        if(ret < 0)
            ret = -ret;

        return ret;
    }
}
