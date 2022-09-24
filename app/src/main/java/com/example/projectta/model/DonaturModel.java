package com.example.projectta.model;

public class DonaturModel {
    private int id;
    private String no_rekening, pemilik_rekening, organisasi, jumlah, gmail, pesan, status, bukti_transfer, status_enkrip, tanggal_donate, status_show;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNo_rekening() {
        return no_rekening;
    }

    public void setNo_rekening(String no_rekening) {
        this.no_rekening = no_rekening;
    }

    public String getPemilik_rekening() {
        return pemilik_rekening;
    }

    public void setPemilik_rekening(String pemilik_rekening) {
        this.pemilik_rekening = pemilik_rekening;
    }

    public String getOrganisasi() {
        return organisasi;
    }

    public void setOrganisasi(String organisasi) {
        this.organisasi = organisasi;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBukti_transfer() {
        return bukti_transfer;
    }

    public void setBukti_transfer(String bukti_transfer) {
        this.bukti_transfer = bukti_transfer;
    }

    public String getStatus_enkrip() {
        return status_enkrip;
    }

    public void setStatus_enkrip(String status_enkrip) {
        this.status_enkrip = status_enkrip;
    }

    public String getTanggal_donate() {
        return tanggal_donate;
    }

    public void setTanggal_donate(String tanggal_donate) {
        this.tanggal_donate = tanggal_donate;
    }

    public String getStatus_show() {
        return status_show;
    }

    public void setStatus_show(String status_show) {
        this.status_show = status_show;
    }
}
