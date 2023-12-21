package ru.pin120.androidjava.Entities;

import java.util.List;

public class Facilities {
    private long id;
    private String name;
    private List<Apartaments> apartaments;

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

    public List<Apartaments> getApartaments() {
        return apartaments;
    }

    public void setApartaments(List<Apartaments> apartaments) {
        this.apartaments = apartaments;
    }
}
