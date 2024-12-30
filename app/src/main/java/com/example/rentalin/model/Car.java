package com.example.rentalin.model;

import java.io.Serializable;

public class Car implements Serializable {
    private String car_image;
    private String car_name;
    private String car_model;
    private int car_seats;
    private String transmission;
    private int price_per_day;
    private String description;
    private String city;

    public Car() {}

    public Car(String car_image, String car_name, String car_model, int car_seats, String transmission, int price_per_day, String description, String city) {
        this.car_image = car_image;
        this.car_name = car_name;
        this.car_model = car_model;
        this.car_seats = car_seats;
        this.transmission = transmission;
        this.price_per_day = price_per_day;
        this.description = description;
        this.city = city;
    }

    public String getCar_image() {
        return car_image;
    }

    public void setCar_image(String car_image) {
        this.car_image = car_image;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    public int getCar_seats() {
        return car_seats;
    }

    public void setCar_seats(int car_seats) {
        this.car_seats = car_seats;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public int getPrice_per_day() {
        return price_per_day;
    }

    public void setPrice_per_day(int price_per_day) {
        this.price_per_day = price_per_day;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
