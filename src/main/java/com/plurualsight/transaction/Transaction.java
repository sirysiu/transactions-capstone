package com.plurualsight.transaction;

import java.io.FileNotFoundException;

public class Transaction {
    private String date;
    private String time;
    private String description;
    private String vendor;
    private double amount;

    // Constructor to initialize a new Transaction object
    public Transaction(String date, String time, String description, String vendor, double amount) throws FileNotFoundException {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    //Getters
    public String getDate() {
        return date;
    }


    public String getTime() {
        return time;
    }


    public String getDescription() {
        return description;
    }


    public String getVendor() {
        return vendor;
    }


    public double getAmount() {
        return amount;
    }

}
