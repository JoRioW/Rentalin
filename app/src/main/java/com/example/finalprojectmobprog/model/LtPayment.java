package com.example.finalprojectmobprog.model;

import java.util.Date;

public class LtPayment {
    private int id;
    private Date paymentDate;
    private double amount;
    private String paymentMethod;
    private int rentalId;

    public LtPayment(){}

    public LtPayment(int id, Date paymentDate, double amount, String paymentMethod, int rentalId) {
        this.id = id;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.rentalId = rentalId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }
}
