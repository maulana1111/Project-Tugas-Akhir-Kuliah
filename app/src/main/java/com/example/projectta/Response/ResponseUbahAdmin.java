package com.example.projectta.Response;

public class ResponseUbahAdmin {

    private int status_code;
    private String status_message, status_token;

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public String getStatus_token() {
        return status_token;
    }

    public void setStatus_token(String status_token) {
        this.status_token = status_token;
    }
}
