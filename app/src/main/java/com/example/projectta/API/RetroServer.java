package com.example.projectta.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroServer {

    private static final String baseURL = "http://192.168.18.45/AdminKrcV2/api/";
//    private static final String baseURL = "http://192.168.18.6/AdminKrcV2/api/";
    private static Retrofit retrofit;


    public static Retrofit connectRetrofit()
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

}
