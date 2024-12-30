package com.example.rentalin.model;

public class Booking {
    private String car_image;
    private String car_name;
    private int total_price;
    private String start_date, end_date;
    private String payment_method;

    public Booking() {}

    public Booking(String car_image, String car_name, int total_price, String start_date, String end_date, String payment_method) {
        this.car_image = car_image;
        this.car_name = car_name;
        this.total_price = total_price;
        this.start_date = start_date;
        this.end_date = end_date;
        this.payment_method = payment_method;
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

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }
}
