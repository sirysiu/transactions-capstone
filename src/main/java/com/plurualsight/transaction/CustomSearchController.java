package com.plurualsight.transaction;

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomSearchController {
    Scanner scan = new Scanner(System.in);
    ArrayList<Transaction> ledger;

    public CustomSearchController(ArrayList<Transaction> ledger) {
        this.ledger = ledger;
    }

    public void search(String vendor,String startDate, String endDate, String description, float decimal ) {

        /*
        vendor
        date
        description
        amount
         */
        ledger.stream().filter(t -> vendor == null || t.getVendor().contains(vendor));
    }
}