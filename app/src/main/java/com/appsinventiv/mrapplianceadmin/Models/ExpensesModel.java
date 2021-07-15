package com.appsinventiv.mrapplianceadmin.Models;

public class ExpensesModel {
    String id, title, description, category, date, status, staffMember;
    int price;
    String imgUrl;

    public ExpensesModel(String id, String title, String description, String category, String date, String status, String staffMember, int price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.date = date;
        this.status = status;
        this.staffMember = staffMember;
        this.price = price;
    }

    public ExpensesModel() {

    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStaffMember() {
        return staffMember;
    }

    public void setStaffMember(String staffMember) {
        this.staffMember = staffMember;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
