package com.appsinventiv.mrapplianceadmin.Activities.OrderLogs;

public class SolutionTrackingModel {
    String id, text;
    long time;

    public SolutionTrackingModel(String id, String text, long time) {
        this.id = id;
        this.text = text;
        this.time = time;
    }

    public SolutionTrackingModel() {
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
