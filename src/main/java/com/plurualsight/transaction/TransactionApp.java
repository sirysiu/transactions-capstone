package com.plurualsight.transaction;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class TransactionApp {

    static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Ledger> ledgers = new ArrayList<>();
    // static Ledger ledger;

    public static void main(String[] args) throws FileNotFoundException {
        loadTransaction();
        boolean isRunning = true;


        while (isRunning) {
            System.out.println("""
                    
                    ==============
                    Home Screen
                    ==============
                    D) Add Deposit
                    P) Make Payment (Debit)
                    L) Ledger
                    X) Exit
                    """);
            String input = scanner.nextLine();

            switch (input) {
                case "d", "D":
                    addingDeposit();
                    break;
                case "l", "L":
                    displayTransaction();
                    break;
                case "p", "P":
                    makePayment();
                    break;
                case "x", "X":
                    System.out.println("exiting...");
                    isRunning = false;
            }

        }
    }

    private static void loadTransaction() {
        try (FileReader fileReader = new FileReader("./src/main/resources/transactions.csv")) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();

            String input;
            while ((input = bufferedReader.readLine()) != null) {
                String[] ledgerParts = input.split("\\|");
                if (ledgerParts.length < 5) continue; // Skip invalid entries

                String date = ledgerParts[0];
                String time = ledgerParts[1];
                String description = ledgerParts[2];
                String vendor = ledgerParts[3];
                double amount;
                try {
                    amount = Double.parseDouble(ledgerParts[4]);
                } catch (NumberFormatException e) {
                    continue;
                }
                Ledger ledger = new Ledger(date, time, description, vendor, amount);
                ledgers.add(ledger);

            }
            bufferedReader.close();
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    private static void displayTransaction() throws FileNotFoundException {
        for (Ledger ledger : ledgers) {
            System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: %.2f\n",
                    ledger.getDate(), ledger.getTime(), ledger.getDescription(), ledger.getVendor(), ledger.getAmount());
        }

        System.out.println("""
                
                Filter by [d] Deposit || Payments [p] || Reports [r]
                
                """);
        String ledgerInput = scanner.nextLine();

        switch (ledgerInput) {
            case "d", "D":
                displayDeposits();
                break;
            case "p", "P":
                displayPayments();
                break;
            case "r", "R":
                Reports report = new Reports();
                report.generateReports();
                break;
            default:
                System.out.println("Invalid return to main");
        }


    }

    private static void displayDeposits() {
        System.out.println("Deposits:");
        for (Ledger ledger : ledgers) {
            if (ledger.getAmount() > 0) {
                System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: %.2f\n",
                        ledger.getDate(), ledger.getTime(), ledger.getDescription(), ledger.getVendor(), ledger.getAmount());
            }
        }
    }

    private static void displayPayments() {
        System.out.println("Payments:");
        for (Ledger ledger : ledgers) {
            if (ledger.getAmount() < 0) {
                System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: %.2f\n",
                        ledger.getDate(), ledger.getTime(), ledger.getDescription(), ledger.getVendor(), ledger.getAmount());
            }
        }
    }

    private static void addingDeposit() throws FileNotFoundException {
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(fmt);
        DateTimeFormatter timefmt = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = today.format(timefmt);
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline


        Ledger newLedger = new Ledger(formattedDate, formattedTime, description, vendor, amount);
        ledgers.add(newLedger);
        System.out.println("Deposit added");


            saveTransactions(newLedger);

    }

    private static void makePayment() throws FileNotFoundException {

        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(fmt);
        DateTimeFormatter timefmt = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = today.format(timefmt);
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline


        Ledger newLedger = new Ledger(formattedDate, formattedTime, description, vendor, -amount);
        ledgers.add(newLedger);
        System.out.println("Payment added");


            saveTransactions(newLedger);

    }

    private static void saveTransactions(Ledger ledger) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/resources/transactions.csv", true))) {
           // for (Ledger ledger : ledgers ) {
            writer.write(String.format("%s|%s|%s|%s|%.2f%n",
                    ledger.getDate(), ledger.getTime(),
                    ledger.getDescription(), ledger.getVendor(), ledger.getAmount()));


           // }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
