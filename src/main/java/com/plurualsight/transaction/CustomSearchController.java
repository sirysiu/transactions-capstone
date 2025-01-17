package com.plurualsight.transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomSearchController {
    ArrayList<Transaction> ledger;

    public CustomSearchController(ArrayList<Transaction> ledger) {
        this.ledger = ledger;
    }

    public ArrayList<Transaction> search(String vendor, String startDate, String endDate, String description, double amount ) {
        ArrayList<Transaction> newLedger = new ArrayList<>();
        /*
        vendor
        date
        description
        amount
         */
        ledger.stream().filter(t -> vendor.isEmpty() || t.getVendor().contains(vendor))
                .filter((t -> startDate.isEmpty() || LocalDate.parse(t.getDate()).isAfter(LocalDate.parse(startDate))))
                .filter(t -> endDate.isEmpty() || LocalDate.parse(t.getDate()).isBefore(LocalDate.parse(endDate)))
                .filter(t -> description.isEmpty() || t.getDescription().contains(description))
                .filter(t-> amount ==  0|| t.getAmount() == amount)
                .forEach(t -> newLedger.add(t));

        return newLedger;
    }
}