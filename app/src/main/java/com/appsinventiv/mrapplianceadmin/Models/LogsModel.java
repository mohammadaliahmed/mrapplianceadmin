package com.appsinventiv.mrapplianceadmin.Models;

public class LogsModel {
    String id, text;
    long time;
    String staffName,dateTime,assignTo,imageUrl;

    public LogsModel(String id, String text, long time) {
        this.id = id;
        this.text = text;
        this.time = time;
    }

    public LogsModel(String id, String text, long time, String dateTime, String staffName, String assignTo, String imageUrl) {
        this.id = id;
        this.text = text;
        this.time = time;
        this.assignTo = assignTo;
        this.dateTime = dateTime;
        this.staffName = staffName;
        this.imageUrl = imageUrl;
    }

    public LogsModel() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(String assignTo) {
        this.assignTo = assignTo;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
