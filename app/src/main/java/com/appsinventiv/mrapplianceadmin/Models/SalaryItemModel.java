package com.appsinventiv.mrapplianceadmin.Models;

public class SalaryItemModel {
    String name;
    int amount;

    public SalaryItemModel(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public SalaryItemModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
