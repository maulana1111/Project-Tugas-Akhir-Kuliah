package com.example.projectta.Response;

import com.example.projectta.model.DonaturModel;

import java.util.ArrayList;
import java.util.List;

public class ResponseDonaturFilter {

    private int status_code;
    private String status_message, status_key;
    private List<DonaturModel> data = new ArrayList<>();

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

    public String getStatus_key() {
        return status_key;
    }

    public void setStatus_key(String status_key) {
        this.status_key = status_key;
    }

    public List<DonaturModel> getData() {
        return data;
    }

    public void setData(List<DonaturModel> data) {
        this.data = data;
    }
}
