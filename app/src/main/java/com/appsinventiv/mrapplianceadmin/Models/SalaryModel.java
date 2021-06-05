package com.appsinventiv.mrapplianceadmin.Models;

import com.appsinventiv.mrapplianceadmin.Servicemen.ServicemanModel;

import java.util.ArrayList;
import java.util.List;

public class SalaryModel {
    String id;
    int grossSalary;
    int total;
    int day, month, year;
    ServicemanModel serviceman;
    ArrayList<SalaryItemModel> deductions;
    ArrayList<SalaryItemModel> allowances;

    public SalaryModel(String id, int grossSalary, ArrayList<SalaryItemModel> allowances, ArrayList<SalaryItemModel> deductions , int total, int day, int month, int year, ServicemanModel serviceman) {
        this.id = id;
        this.grossSalary = grossSalary;
        this.allowances = allowances;
        this.deductions = deductions;
        this.total = total;
        this.day = day;
        this.month = month;
        this.year = year;
        this.serviceman = serviceman;
    }

    public SalaryModel() {
    }

    public int getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(int grossSalary) {
        this.grossSalary = grossSalary;
    }

    public ArrayList<SalaryItemModel> getDeductions() {
        return deductions;
    }

    public void setDeductions(ArrayList<SalaryItemModel> deductions) {
        this.deductions = deductions;
    }

    public ArrayList<SalaryItemModel> getAllowances() {
        return allowances;
    }

    public void setAllowances(ArrayList<SalaryItemModel> allowances) {
        this.allowances = allowances;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ServicemanModel getServiceman() {
        return serviceman;
    }

    public void setServiceman(ServicemanModel serviceman) {
        this.serviceman = serviceman;
    }
}
