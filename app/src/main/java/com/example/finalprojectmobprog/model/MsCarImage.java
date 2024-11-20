package com.example.finalprojectmobprog.model;

public class MsCarImage {
    private int id;
    private String imageUrl;
    private int carId;

    public MsCarImage(){}

    public MsCarImage(int id, String imageUrl, int carId) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.carId = carId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }
}
