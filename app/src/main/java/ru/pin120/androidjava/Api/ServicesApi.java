package ru.pin120.androidjava.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ru.pin120.androidjava.Entities.Facilities;
import ru.pin120.androidjava.Entities.Services;

public interface ServicesApi {
    @GET("/services/get-all")
    Call<List<Services>> getAllServices();

    @POST("/services/save")
    Call<Services> save(@Body Services service);

    @POST("/services/find")
    Call<Services> findById(@Body long Id);

    @POST("/services/edit")
    Call<Services> update(@Body Services service);

    @POST("/services/delete")
    Call<Services> delete(@Body Services service);
}
