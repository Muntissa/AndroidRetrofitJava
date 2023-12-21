package ru.pin120.androidjava.REST;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RESTHelper {
    private Retrofit retrofit;

    public RESTHelper() {
        initializeREST();
    }

    private void initializeREST() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.50.145:8080")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
