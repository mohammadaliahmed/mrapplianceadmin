package com.appsinventiv.mrapplianceadmin.Activities.Appointments;

public class AppointmentModel {
    String location,brand,model,status,report,payment,paymentType,customerName,phone,title,address,appointmentStatus;
    String staff,id;
    long time;
    String timeSelected,date;
    String invoiceId;

    public AppointmentModel(String id,String staff,String location, String brand, String model, String status, String report, String payment,
                            String paymentType, String customerName, String phone, String address, String appointmentStatus) {
        this.id = id;
        this.location = location;
        this.staff = staff;
        this.brand = brand;
        this.model = model;
        this.status = status;
        this.address = address;
        this.report = report;
        this.payment = payment;
        this.paymentType = paymentType;
        this.customerName = customerName;
        this.phone = phone;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AppointmentModel() {
    }

    public String getTimeSelected() {
        return timeSelected;
    }

    public void setTimeSelected(String timeSelected) {
        this.timeSelected = timeSelected;
    }

    public String getDate() {
        return date;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStaff() {
        return staff;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
