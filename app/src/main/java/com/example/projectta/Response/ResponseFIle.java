package com.example.projectta.Response;

import com.example.projectta.model.ModelFile;

import java.util.ArrayList;
import java.util.List;

public class ResponseFIle {
    private int status_code;
    private String status_message;
    private List<ModelFile> data = new ArrayList<>();
    private String execution_time;

    public String getExecution_time() {
        return execution_time;
    }

    public void setExecution_time(String execution_time) {
        this.execution_time = execution_time;
    }

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

    public List<ModelFile> getData() {
        return data;
    }

    public void setData(List<ModelFile> data) {
        this.data = data;
    }
}
