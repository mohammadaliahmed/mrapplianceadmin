package com.appsinventiv.mrapplianceadmin.Models;

public class InvoiceItemModel {
    String description;
    int quantity;
    int price;
    String itemId;

    public InvoiceItemModel() {
    }

    public InvoiceItemModel(String itemId, String description, int quantity, int price) {
        this.itemId = itemId;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
