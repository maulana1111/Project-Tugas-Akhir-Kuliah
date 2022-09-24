package com.example.projectta.Response;

import com.example.projectta.model.ModelAdmin;

import java.util.ArrayList;
import java.util.List;

public class ResponseAdmins {

    private int status_code;
    private String status_message;
    private List<ModelAdmin> data = new ArrayList<>();

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

    public List<ModelAdmin> getData() {
        return data;
    }

    public void setData(List<ModelAdmin> data) {
        this.data = data;
    }
}
