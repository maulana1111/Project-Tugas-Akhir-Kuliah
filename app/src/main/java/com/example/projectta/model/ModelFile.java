package com.example.projectta.model;

public class ModelFile {
    private int id;
    private String file_name_source, final_file_name, file_path, file_size, tanggal_upload, tanggal_update, keterangan, status_file, status_enkrip;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFile_name_source() {
        return file_name_source;
    }

    public void setFile_name_source(String file_name_source) {
        this.file_name_source = file_name_source;
    }

    public String getFinal_file_name() {
        return final_file_name;
    }

    public void setFinal_file_name(String final_file_name) {
        this.final_file_name = final_file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    public String getTanggal_upload() {
        return tanggal_upload;
    }

    public void setTanggal_upload(String tanggal_upload) {
        this.tanggal_upload = tanggal_upload;
    }

    public String getTanggal_update() {
        return tanggal_update;
    }

    public void setTanggal_update(String tanggal_update) {
        this.tanggal_update = tanggal_update;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getStatus_file() {
        return status_file;
    }

    public void setStatus_file(String status_file) {
        this.status_file = status_file;
    }

    public String getStatus_enkrip() {
        return status_enkrip;
    }

    public void setStatus_enkrip(String status_enkrip) {
        this.status_enkrip = status_enkrip;
    }
}
