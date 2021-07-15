package com.appsinventiv.mrapplianceadmin.Servicemen;

public class ServicemanModel {
    String id,name,username,password,mobile,role;
    boolean active,deleted;
    int age;
    long cnic;
    String imageUrl;
    String fcmKey;
    String skills;
    int salary;
    double latitude,longitude;

    public ServicemanModel() {
    }

    public ServicemanModel(String id, String name, String username, String password, String mobile, String role, boolean active, boolean deleted,
                           int age, long cnic, String imageUrl, int salary, String skills) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.mobile = mobile;
        this.role = role;
        this.salary = salary;
        this.skills = skills;
        this.active = active;
        this.deleted = deleted;
        this.age = age;
        this.cnic = cnic;
        this.imageUrl = imageUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getFcmKey() {
        return fcmKey;
    }

    public void setFcmKey(String fcmKey) {
        this.fcmKey = fcmKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getCnic() {
        return cnic;
    }

    public void setCnic(long cnic) {
        this.cnic = cnic;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
