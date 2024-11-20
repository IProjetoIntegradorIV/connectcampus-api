package com.connectcampus.api.model;

public class LoginResponse{
    private String message;
    private boolean success;

    public LoginResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public LoginResponse() {}

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString(){
        return "message: " + message +
                ", sucess: " + success;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj.getClass() != this.getClass())
            return false;

        LoginResponse l = (LoginResponse) obj;

        if(l.message != this.message)
            return false;
        if(l.success != this.success)
            return false;

        return true;
    }

    @Override
    public int hashCode(){
        int ret = 1;
        ret = ret * 11 + message.hashCode();
        ret = ret * 11 + Boolean.hashCode(success);

        if(ret < 0)
            ret = -ret;

        return ret;
    }
}
