package com.appsinventiv.mrapplianceadmin.Models;

public class MyMarker {
    private String mLabel;
    private String phone;
    private String picUrl;
    private String mIcon;
    private Double mLatitude;
    private Double mLongitude;

    public MyMarker(String label, String icon, Double latitude, Double longitude,String phone) {
        this.mLabel = label;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mIcon = icon;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getmLabel() {
        return mLabel;
    }

    public void setmLabel(String mLabel) {
        this.mLabel = mLabel;
    }

    public String getmIcon() {
        return mIcon;
    }

    public void setmIcon(String icon) {
        this.mIcon = icon;
    }

    public Double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(Double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public Double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(Double mLongitude) {
        this.mLongitude = mLongitude;
    }
}
