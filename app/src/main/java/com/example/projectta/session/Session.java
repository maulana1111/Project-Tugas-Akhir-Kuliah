package com.example.projectta.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    SharedPreferences preferences ;

    public Session(Context context)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setId(int id)
    {
        preferences.edit().putInt("adminId", id).commit();
    }

    public int getId()
    {
        int adminId = preferences.getInt("adminId", 0);
        return adminId;
    }

    public void setNamaAdmin(String nama)
    {
        preferences.edit().putString("namaAdmin", nama).commit();
    }

    public String getNamaAdmin()
    {
        String namaAdmin = preferences.getString("namaAdmin", "");
        return namaAdmin;
    }

    public void setLevelAdmin(String levelAdmin)
    {
        preferences.edit().putString("levelAdmin", levelAdmin).commit();
    }

    public String getLevelAdmin()
    {
        String levelAdmin = preferences.getString("levelAdmin", "");
        return levelAdmin;
    }

    public void setToken(String token)
    {
        preferences.edit().putString("token", token).commit();
    }

    public String getToken()
    {
        String token = preferences.getString("token", "");
        return token;
    }

    public void setKey(String key)
    {
        preferences.edit().putString("key", key).commit();
    }

    public String getKey()
    {
        String key = preferences.getString("key", "");
        return key;
    }

    public void setLoggedIn(Boolean loggedIn)
    {
        preferences.edit().putBoolean("userLoggedIn", loggedIn).commit();
    }

    public Boolean getLoggedIn()
    {
        Boolean userLoggedIn = preferences.getBoolean("userLoggedIn", false);
        return userLoggedIn;
    }

    public void logout()
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

}
