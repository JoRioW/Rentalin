package com.example.finalprojectmobprog.model;

public class MsCar {
    private int id;
    private String name;
    private String model;
    private int year;
    private String licensePlate;
    private int carSeats;
    private String transmission;
    private double pricePerDay;
    private boolean status;

    public MsCar() {}

    public MsCar(int id, String name, String model, int year, String licensePlate, int carSeats, String transmission, double pricePerDay, boolean status) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.year = year;
        this.licensePlate = licensePlate;
        this.carSeats = carSeats;
        this.transmission = transmission;
        this.pricePerDay = pricePerDay;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getCarSeats() {
        return carSeats;
    }

    public void setCarSeats(int carSeats) {
        this.carSeats = carSeats;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
