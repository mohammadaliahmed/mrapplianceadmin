package com.appsinventiv.mrapplianceadmin.Models;

import java.util.HashMap;

public class CustomInvoiceModel {
    String invoiceId;
    HashMap<String,InvoiceItemModel> invoiceItems;
    int total;
    String status;
    long paymentTime;
    long time;
    User user;
    HashMap<String,PaymentModel> payments;

    public CustomInvoiceModel() {
    }

    public CustomInvoiceModel(String invoiceId, HashMap<String,InvoiceItemModel> invoiceItems, int total, long time, String status, User user) {
        this.invoiceId = invoiceId;
        this.invoiceItems = invoiceItems;
        this.status = status;
        this.total = total;
        this.user = user;
        this.time = time;
    }



    public HashMap<String, PaymentModel> getPayments() {
        return payments;
    }

    public void setPayments(HashMap<String, PaymentModel> payments) {
        this.payments = payments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(long paymentTime) {
        this.paymentTime = paymentTime;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public HashMap<String, InvoiceItemModel> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(HashMap<String, InvoiceItemModel> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
