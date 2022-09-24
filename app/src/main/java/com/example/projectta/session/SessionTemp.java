package com.example.projectta.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SessionTemp {

    SharedPreferences preferences ;

    public SessionTemp(Context context)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setNoRekening(String noRekening)
    {
        preferences.edit().putString("noRekening", noRekening).commit();
    }

    public String getNorek()
    {
        String noRekening = preferences.getString("noRekening", "");
        return noRekening;
    }

    public void setNamaPemilik(String pemilik)
    {
        preferences.edit().putString("namaPemilik", pemilik).commit();
    }

    public String getNamaPemilik()
    {
        String namaPemilik = preferences.getString("namaPemilik", "");
        return namaPemilik;
    }

    public void setOrg(String org)
    {
        preferences.edit().putString("organisasi", org).commit();
    }

    public String getOrg()
    {
        String organisasi = preferences.getString("organisasi", "");
        return organisasi;
    }

    public void setJum(String jum)
    {
        preferences.edit().putString("jumlah", jum).commit();
    }

    public String getjum()
    {
        String jumlah = preferences.getString("jumlah", "");
        return jumlah;
    }

    public void setGmail(String gmail)
    {
        preferences.edit().putString("gmail", gmail).commit();
    }

    public String getGmail()
    {
        String gmail = preferences.getString("gmail", "");
        return gmail;
    }

    public void setPesan(String pesan)
    {
        preferences.edit().putString("pesan", pesan).commit();
    }

    public String getPesan()
    {
        String pesan = preferences.getString("pesan", "");
        return pesan;
    }

    public void setTanggal(String tanggal)
    {
        preferences.edit().putString("tanggal", tanggal).commit();
    }

    public String getTanggal()
    {
        String tanggal = preferences.getString("tanggal", "");
        return tanggal;
    }

    public void setkey_temp(String key_temp)
    {
        preferences.edit().putString("key_temp", key_temp).commit();
    }

    public String getkey_temp()
    {
        String key_temp = preferences.getString("key_temp", "");
        return key_temp;
    }

    public void setOpsi(String opsi)
    {
        preferences.edit().putString("opsi", opsi).commit();
    }

    public String getOpsi()
    {
        String opsi = preferences.getString("opsi", "");
        return opsi;
    }

    public void setKeterangan(String keterangan)
    {
        preferences.edit().putString("keterangan", keterangan).commit();
    }

    public String getKeterangan()
    {
        String keterangan = preferences.getString("keterangan", "");
        return keterangan;
    }

    public void hapus()
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("namaPemilik");
        editor.remove("noRekening");
        editor.remove("organisasi");
        editor.remove("jumlah");
        editor.remove("gmail");
        editor.remove("pesan");
        editor.remove("tanggal");
        editor.remove("key_temp");
        editor.remove("opsi");
        editor.remove("keterangan");
        editor.commit();
    }

}
