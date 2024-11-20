package com.connectcampus.api.model;

public class ResponseMessage{
    private String message;

    public ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString(){
        return "message: " + message;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj.getClass() != this.getClass())
            return false;

        ResponseMessage r = (ResponseMessage) obj;

        if(r.message != this.message)
            return false;

        return true;
    }

    @Override
    public int hashCode(){
        int ret = 1;
        ret = ret * 11 + message.hashCode();

        if(ret < 0)
            ret = -ret;

        return ret;
    }
}

