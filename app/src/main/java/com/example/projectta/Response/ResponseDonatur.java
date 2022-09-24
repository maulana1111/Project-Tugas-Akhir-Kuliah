package com.example.projectta.Response;

import com.example.projectta.model.DonaturModel;

import java.util.ArrayList;
import java.util.List;

public class ResponseDonatur {

    private int status_code;
    private String status_message;
    private List<DonaturModel> data_diterima = new ArrayList<>();
    private List<DonaturModel> data_proses = new ArrayList<>();
    private List<DonaturModel> data_ditolak = new ArrayList<>();

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

    public List<DonaturModel> getData_diterima() {
        return data_diterima;
    }

    public void setData_diterima(List<DonaturModel> data_diterima) {
        this.data_diterima = data_diterima;
    }

    public List<DonaturModel> getData_proses() {
        return data_proses;
    }

    public void setData_proses(List<DonaturModel> data_proses) {
        this.data_proses = data_proses;
    }

    public List<DonaturModel> getData_ditolak() {
        return data_ditolak;
    }

    public void setData_ditolak(List<DonaturModel> data_ditolak) {
        this.data_ditolak = data_ditolak;
    }
}
