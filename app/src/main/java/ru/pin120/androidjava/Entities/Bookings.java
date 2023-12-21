package ru.pin120.androidjava.Entities;

import java.time.LocalDate;
import java.util.List;

public class Bookings {
    private long id;
    private String startTime;
    private String endTime;
    private int price;
    private boolean isActive = true;
    private Clients client;
    private Apartaments apartament;
    private List<Services> services;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Services> getServices() {
        return services;
    }

    public void setServices(List<Services> services) {
        this.services = services;
    }

    public Clients getClient() {
        return client;
    }

    public void setClient(Clients client) {
        this.client = client;
    }

    public Apartaments getApartament() {
        return apartament;
    }

    public void setApartament(Apartaments apartament) {
        this.apartament = apartament;
    }
}
