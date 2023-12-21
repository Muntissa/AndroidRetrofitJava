package ru.pin120.androidjava.Entities;

import java.util.List;

public class Services {
    private long id;
    private String name;
    private String description;
    private int price;
    private List<Apartaments> apartaments;
    private List<Bookings> bookings;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Apartaments> getApartaments() {
        return apartaments;
    }

    public void setApartaments(List<Apartaments> apartaments) {
        this.apartaments = apartaments;
    }

    public List<Bookings> getBookings() {
        return bookings;
    }

    public void setBookings(List<Bookings> bookings) {
        this.bookings = bookings;
    }
}
