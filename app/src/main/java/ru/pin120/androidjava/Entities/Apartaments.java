package ru.pin120.androidjava.Entities;

import java.util.List;

public class Apartaments {
    private long id;
    private int number;
    private int area;
    private String photoPath;
    private Boolean reservation;
    private List<Facilities> facilities;
    private List<Services> services;
    private Tariffs tariff;

    public Apartaments(){}

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }

    public int getArea() {
        return area;
    }
    public void setArea(int area) {
        this.area = area;
    }

    public String getPhotoPath() {
        return photoPath;
    }
    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Boolean getReservation() {
        return reservation;
    }
    public void setReservation(Boolean reservation) {
        this.reservation = reservation;
    }

    public List<Facilities> getFacilities() {
        return facilities;
    }
    public void setFacilities(List<Facilities> facilities) {
        this.facilities = facilities;
    }

    public List<Services> getServices() {
        return services;
    }
    public void setServices(List<Services> services) {
        this.services = services;
    }

    public Tariffs getTariff() {
        return tariff;
    }
    public void setTariff(Tariffs tariff) {
        this.tariff = tariff;
    }

    @Override
    public String toString() {
        return "Номер: " + number;
    }
}
