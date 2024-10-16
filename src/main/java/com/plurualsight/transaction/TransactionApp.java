package com.plurualsight.transaction;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class TransactionApp {

    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Transaction> transactions = new ArrayList<>();

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
                Transaction transaction = new Transaction(date, time, description, vendor, amount);
                transactions.add(transaction);

            }
           // bufferedReader.close();
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    private static void displayTransaction() throws FileNotFoundException {
        for (Transaction transaction : transactions) {
            System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: %.2f\n",
                    transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
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
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: %.2f\n",
                        transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
            }
        }
    }

    private static void displayPayments() {
        System.out.println("Payments:");
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: %.2f\n",
                        transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
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


        Transaction newTransaction = new Transaction(formattedDate, formattedTime, description, vendor, amount);
        transactions.add(newTransaction);
        System.out.println("Deposit added");


            saveTransactions(newTransaction);

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


        Transaction newTransaction = new Transaction(formattedDate, formattedTime, description, vendor, -amount);
        transactions.add(newTransaction);
        System.out.println("Payment added");


            saveTransactions(newTransaction);

    }

    private static void saveTransactions(Transaction transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/resources/transactions.csv", true))) {
            writer.write(String.format("%s|%s|%s|%s|%.2f%n",
                    transaction.getDate(), transaction.getTime(),
                    transaction.getDescription(), transaction.getVendor(), transaction.getAmount()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
