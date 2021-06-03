package com.appsinventiv.mrapplianceadmin.Models;

import java.util.HashMap;

public class User {
    String firstname, lastname, username, password, email, mobile, phone, address, fcmKey;
    long time;
    boolean numberVerified;
    String fullName;
    String googleAddress;
    double lat, lon;
    HashMap<String, String> orders;

    long totalOrder, totalPayment;

    public User(String firstname, String lastname, String username, String password,
                String email, String mobile, String phone,
                String address, String fcmKey, long time, boolean numberVerified) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.mobile = mobile;
        this.phone = phone;
        this.address = address;
        this.fcmKey = fcmKey;
        this.time = time;
        this.numberVerified = numberVerified;
    }

    public User() {
    }


    public long getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(long totalOrder) {
        this.totalOrder = totalOrder;
    }

    public long getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(long totalPayment) {
        this.totalPayment = totalPayment;
    }

    public HashMap<String, String> getOrders() {
        return orders;
    }

    public void setOrders(HashMap<String, String> orders) {
        this.orders = orders;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGoogleAddress() {
        return googleAddress;
    }

    public void setGoogleAddress(String googleAddress) {
        this.googleAddress = googleAddress;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getFullName() {
        return firstname + " " + lastname;
    }


    public boolean isNumberVerified() {
        return numberVerified;
    }

    public void setNumberVerified(boolean numberVerified) {
        this.numberVerified = numberVerified;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFcmKey() {
        return fcmKey;
    }

    public void setFcmKey(String fcmKey) {
        this.fcmKey = fcmKey;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
