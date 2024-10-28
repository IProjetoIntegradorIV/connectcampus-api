package com.connectcampus.api.model;

public class LoginResponse {
    private String message;
    private boolean success;

    public LoginResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public LoginResponse() {}

    // Getters
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
}
