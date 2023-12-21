package ru.pin120.androidjava.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ru.pin120.androidjava.Entities.Apartaments;
import ru.pin120.androidjava.Entities.Clients;

public interface ApartamentsApi {
    @GET("/apartaments/get-all")
    Call<List<Apartaments>> getAllApartaments();

    @POST("/apartaments/save")
    Call<Apartaments> save(@Body Apartaments apartament);

    @POST("/apartaments/find")
    Call<Apartaments> findById(@Body long Id);

    @POST("/apartaments/edit")
    Call<Apartaments> update(@Body Apartaments apartament);

    @POST("/apartaments/delete")
    Call<Apartaments> delete(@Body Apartaments apartament);
}
