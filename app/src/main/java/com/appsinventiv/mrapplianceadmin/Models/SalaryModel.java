package com.appsinventiv.mrapplianceadmin.Models;

import com.appsinventiv.mrapplianceadmin.Servicemen.ServicemanModel;

public class SalaryModel {
    String id;
    int amount;
    int deduction;
    int total;
    int day, month, year;
    ServicemanModel serviceman;

    public SalaryModel(String id, int amount, int deduction, int total, int day, int month, int year, ServicemanModel serviceman) {
        this.id = id;
        this.amount = amount;
        this.deduction = deduction;
        this.total = total;
        this.day = day;
        this.month = month;
        this.year = year;
        this.serviceman = serviceman;
    }

    public SalaryModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDeduction() {
        return deduction;
    }

    public void setDeduction(int deduction) {
        this.deduction = deduction;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ServicemanModel getServiceman() {
        return serviceman;
    }

    public void setServiceman(ServicemanModel serviceman) {
        this.serviceman = serviceman;
    }
}
