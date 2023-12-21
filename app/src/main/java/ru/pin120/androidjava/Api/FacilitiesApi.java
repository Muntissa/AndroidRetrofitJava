package ru.pin120.androidjava.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ru.pin120.androidjava.Entities.Clients;
import ru.pin120.androidjava.Entities.Facilities;

public interface FacilitiesApi {
    @GET("/facilities/get-all")
    Call<List<Facilities>> getAllFacilities();

    @POST("/facilities/save")
    Call<Facilities> save(@Body Facilities facility);

    @POST("/facilities/find")
    Call<Facilities> findById(@Body long Id);

    @POST("/facilities/edit")
    Call<Facilities> update(@Body Facilities facility);

    @POST("/facilities/delete")
    Call<Facilities> delete(@Body Facilities facility);
}
