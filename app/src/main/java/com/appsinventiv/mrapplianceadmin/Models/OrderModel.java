package com.appsinventiv.mrapplianceadmin.Models;

import java.util.ArrayList;

public class OrderModel {
    long orderId;
    long time;
    User user;
    ArrayList<ServiceCountModel> countModelArrayList;
    ArrayList<ServiceCountModel> newCountModelList;
    long totalPrice;
    String instructions;
    String date, chosenTime;
    String orderStatus;
    String orderAddress, googleAddress;
    double lat, lon;
    long billNumber;
    String buildingType;
    long totalHours;
    String serviceName;
    String assignedTo;
    String assignedToName;
    long materialBill;
    long jobEndTime;
    long jobStartTime;
    float rating;
    String couponCode;
    int discount;
    long serviceCharges;
    String cancelReason;
    int tax;
    String serviceId;
    double endJourneyLat, endJourneyLng, startJourneyLat, startJourneyLng;
    ArrayList<String> orderImages;


    boolean assigned;
    boolean jobDone;
    boolean modifiedOrderConfirmed;
    boolean arrived;
    boolean jobFinish;
    boolean jobStarted;
    boolean customOrder;
    boolean commercialBuilding;
    boolean cancelled;
    boolean couponApplied;
    boolean rated;


    public OrderModel(long orderId,
                      long time,
                      User user,
                      ArrayList<ServiceCountModel> countModelArrayList,
                      long totalPrice,
                      String date,
                      String chosenTime,
                      String orderStatus,
                      String orderAddress,
                      String buildingType,
                      String serviceName,
                      String serviceId, boolean customOrder, double lat, double lon) {
        this.orderId = orderId;
        this.time = time;
        this.user = user;
        this.countModelArrayList = countModelArrayList;
        this.totalPrice = totalPrice;
        this.date = date;
        this.chosenTime = chosenTime;
        this.lat = lat;
        this.lon = lon;
        this.orderStatus = orderStatus;
        this.orderAddress = orderAddress;
        this.serviceId = serviceId;
        this.buildingType = buildingType;
        this.serviceName = serviceName;
        this.customOrder = customOrder;


    }


    public ArrayList<String> getOrderImages() {
        return orderImages;
    }

    public void setOrderImages(ArrayList<String> orderImages) {
        this.orderImages = orderImages;
    }

    public boolean isCustomOrder() {
        return customOrder;
    }

    public void setCustomOrder(boolean customOrder) {
        this.customOrder = customOrder;
    }

    public double getEndJourneyLat() {
        return endJourneyLat;
    }

    public void setEndJourneyLat(double endJourneyLat) {
        this.endJourneyLat = endJourneyLat;
    }

    public double getEndJourneyLng() {
        return endJourneyLng;
    }

    public void setEndJourneyLng(double endJourneyLng) {
        this.endJourneyLng = endJourneyLng;
    }

    public double getStartJourneyLat() {
        return startJourneyLat;
    }

    public void setStartJourneyLat(double startJourneyLat) {
        this.startJourneyLat = startJourneyLat;
    }

    public double getStartJourneyLng() {
        return startJourneyLng;
    }

    public void setStartJourneyLng(double startJourneyLng) {
        this.startJourneyLng = startJourneyLng;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public boolean isCommercialBuilding() {
        return commercialBuilding;
    }

    public void setCommercialBuilding(boolean commercialBuilding) {
        this.commercialBuilding = commercialBuilding;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public long getServiceCharges() {
        return serviceCharges;
    }

    public void setServiceCharges(long serviceCharges) {
        this.serviceCharges = serviceCharges;
    }

    public boolean isCouponApplied() {
        return couponApplied;
    }

    public void setCouponApplied(boolean couponApplied) {
        this.couponApplied = couponApplied;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public OrderModel() {
    }

    public boolean isArrived() {
        return arrived;
    }

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setArrived(boolean arrived) {
        this.arrived = arrived;
    }

    public long getMaterialBill() {
        return materialBill;
    }

    public void setMaterialBill(long materialBill) {
        this.materialBill = materialBill;
    }

    public long getJobEndTime() {
        return jobEndTime;
    }

    public void setJobEndTime(long jobEndTime) {
        this.jobEndTime = jobEndTime;
    }

    public boolean isJobDone() {
        return jobDone;
    }

    public void setJobDone(boolean jobDone) {
        this.jobDone = jobDone;
    }

    public boolean isJobFinish() {
        return jobFinish;
    }

    public void setJobFinish(boolean jobFinish) {
        this.jobFinish = jobFinish;
    }

    public boolean isJobStarted() {
        return jobStarted;
    }

    public void setJobStarted(boolean jobStarted) {
        this.jobStarted = jobStarted;
    }

    public long getJobStartTime() {
        return jobStartTime;
    }

    public void setJobStartTime(long jobStartTime) {
        this.jobStartTime = jobStartTime;
    }

    public boolean isModifiedOrderConfirmed() {
        return modifiedOrderConfirmed;
    }

    public void setModifiedOrderConfirmed(boolean modifiedOrderConfirmed) {
        this.modifiedOrderConfirmed = modifiedOrderConfirmed;
    }

    public ArrayList<ServiceCountModel> getNewCountModelList() {
        return newCountModelList;
    }

    public void setNewCountModelList(ArrayList<ServiceCountModel> newCountModelList) {
        this.newCountModelList = newCountModelList;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getAssignedToName() {
        return assignedToName;
    }

    public void setAssignedToName(String assignedToName) {
        this.assignedToName = assignedToName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }


    public long getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(long totalHours) {
        this.totalHours = totalHours;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<ServiceCountModel> getCountModelArrayList() {
        return countModelArrayList;
    }

    public void setCountModelArrayList(ArrayList<ServiceCountModel> countModelArrayList) {
        this.countModelArrayList = countModelArrayList;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChosenTime() {
        return chosenTime;
    }

    public void setChosenTime(String chosenTime) {
        this.chosenTime = chosenTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
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


    public long getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(long billNumber) {
        this.billNumber = billNumber;
    }

    public String getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }
}
