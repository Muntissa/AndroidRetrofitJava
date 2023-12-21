package ru.pin120.androidjava.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ru.pin120.androidjava.Entities.Facilities;
import ru.pin120.androidjava.Entities.Tariffs;

public interface TariffsApi {
    @GET("/tariffs/get-all")
    Call<List<Tariffs>> getAllTariffs();

    @POST("/tariffs/save")
    Call<Tariffs> save(@Body Tariffs tariff);

    @POST("/tariffs/find")
    Call<Tariffs> findById(@Body long Id);

    @POST("/tariffs/edit")
    Call<Tariffs> update(@Body Tariffs tariff);

    @POST("/tariffs/delete")
    Call<Tariffs> delete(@Body Tariffs tariffs);
}
