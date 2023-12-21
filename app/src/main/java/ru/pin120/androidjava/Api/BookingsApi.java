package ru.pin120.androidjava.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ru.pin120.androidjava.Entities.Apartaments;
import ru.pin120.androidjava.Entities.Bookings;

public interface BookingsApi {
    @GET("/bookings/get-all")
    Call<List<Bookings>> getAllBookings();

    @POST("/bookings/save")
    Call<Bookings> save(@Body Bookings booking);

    @POST("/bookings/find")
    Call<Bookings> findById(@Body long Id);

    @POST("/bookings/edit")
    Call<Bookings> update(@Body Bookings booking);

    @POST("/bookings/delete")
    Call<Bookings> delete(@Body Bookings booking);
}
