package com.appsinventiv.mrapplianceadmin.Models;

public class AdminModel {
    String adminNumber,fcmKey,providingServiceInCities,jobs;
    int tax;

    public AdminModel() {
    }

    public String getJobs() {
        return jobs;
    }

    public void setJobs(String jobs) {
        this.jobs = jobs;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public String getProvidingServiceInCities() {
        return providingServiceInCities;
    }

    public void setProvidingServiceInCities(String providingServiceInCities) {
        this.providingServiceInCities = providingServiceInCities;
    }

    public String getFcmKey() {
        return fcmKey;
    }

    public void setFcmKey(String fcmKey) {
        this.fcmKey = fcmKey;
    }

    public String getAdminNumber() {
        return adminNumber;
    }

    public void setAdminNumber(String adminNumber) {
        this.adminNumber = adminNumber;
    }
}
