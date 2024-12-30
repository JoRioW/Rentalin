package com.example.rentalin.model;

public class Users {
    private String username;
    private String phone;
    private String address;
    private String image;

    public Users() {}

    public Users(String username, String phone, String address, String image) {
        this.username = username;
        this.phone = phone;
        this.address = address;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
