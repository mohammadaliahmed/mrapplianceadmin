package com.appsinventiv.mrapplianceadmin.Models;

public class PaymentModel {
    String id;
    int price;
    String date,paymentMode;

    public PaymentModel() {

    }

    public PaymentModel(String id, int price, String date, String paymentMode) {
        this.id = id;
        this.paymentMode = paymentMode;
        this.price = price;
        this.date = date;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
