package com.plurualsight.transaction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class ReportsController {
    ArrayList<Transaction> ledger = loadTransaction();
    Scanner scan = new Scanner(System.in);

    // Utility function to format the date and amount
    private String formatDate(String date) {
        return date.replace("-", "/"); // Convert date from yyyy-MM-dd to yyyy/MM/dd format
    }

    private String formatAmount(double amount) {
        return String.format("%.2f", amount); // Format the amount to 2 decimal places
    }

    // Print header row for transaction table
    private void printTransactionHeader() {
        System.out.printf("%-15s %-8s %-30s %-15s %-10s\n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-------------------------------------------------------------");
    }

    public void monthToDate() {
        double total = 0.0;
        LocalDate now = LocalDate.now();
        System.out.println("\nTransactions for the current month (" + now.getMonth() + "):");
        printTransactionHeader();

        for (Transaction transaction : ledger) {
            if (transaction.getDate().startsWith(now.getYear() + "-" + String.format("%02d", now.getMonthValue()))) {
                System.out.printf("%-15s %-8s %-30s %-15s %-10s\n",
                        formatDate(transaction.getDate()),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        formatAmount(transaction.getAmount()));
                total += transaction.getAmount();
            }
        }

        System.out.printf("\nMonth-to-Date Total for %s: %s\n", now.getMonth(), formatAmount(total));
    }

    public void generatePreviousMonth() {
        double total = 0.0;
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        System.out.println("\nTransactions for the previous month (" + lastMonth.getMonth() + "):");
        printTransactionHeader();

        for (Transaction transaction : ledger) {
            if (transaction.getDate().startsWith(lastMonth.getYear() + "-" + String.format("%02d", lastMonth.getMonthValue()))) {
                System.out.printf("%-15s %-8s %-30s %-15s %-10s\n",
                        formatDate(transaction.getDate()),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        formatAmount(transaction.getAmount()));
                total += transaction.getAmount();
            }
        }

        System.out.printf("\nTotal for Previous Month (%s): %s\n", lastMonth.getMonth(), formatAmount(total));
    }

    public void generateYearToDate() {
        double total = 0.0;
        int currentYear = LocalDate.now().getYear();
        System.out.println("\nTransactions for the current year (" + currentYear + "):");
        printTransactionHeader();

        for (Transaction transaction : ledger) {
            if (transaction.getDate().startsWith(String.valueOf(currentYear))) {
                System.out.printf("%-15s %-8s %-30s %-15s %-10s\n",
                        formatDate(transaction.getDate()),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        formatAmount(transaction.getAmount()));
                total += transaction.getAmount();
            }
        }

        System.out.printf("\nYear-to-Date Total for %d: %s\n", currentYear, formatAmount(total));
    }

    public void generatePreviousYear() {
        double total = 0.0;
        int previousYear = LocalDate.now().getYear() - 1;
        System.out.println("\nTransactions for the previous year (" + previousYear + "):");
        printTransactionHeader();

        for (Transaction transaction : ledger) {
            if (transaction.getDate().startsWith(String.valueOf(previousYear))) {
                System.out.printf("%-15s %-8s %-30s %-15s %-10s\n",
                        formatDate(transaction.getDate()),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        formatAmount(transaction.getAmount()));
                total += transaction.getAmount();
            }
        }

        System.out.printf("\nTotal for Previous Year (%d): %s\n", previousYear, formatAmount(total));
    }

    // method for user to search by vendor input
    public void customSearchView() {
        System.out.print("Enter vendor (Press enter to skip): ");
        String vendor = scan.nextLine();

        System.out.print("Enter start date (Press enter to skip): ");
        String startDate = scan.nextLine();

        System.out.print("Enter end date (Press enter to skip): ");
        String endDate = scan.nextLine();

        System.out.print("Enter description (Press enter to skip): ");
        String description = scan.nextLine();

        System.out.print("Enter amount (Press enter to skip): ");
        double amount = scan.nextDouble();
        scan.nextLine();

        double total = 0;
        ArrayList<Transaction> filteredLedger = new CustomSearchController(ledger).search(vendor, startDate, endDate, description, amount);
        for (Transaction transaction : filteredLedger) {

            System.out.printf("%-15s %-8s %-30s %-15s %-10s\n",
                    formatDate(transaction.getDate()),
                    transaction.getTime(),
                    transaction.getDescription(),
                    transaction.getVendor(),
                    formatAmount(transaction.getAmount()));
            total += transaction.getAmount();
        }
        System.out.printf("\nTotal for your search: %s\n", formatAmount(total));
    }

    public ArrayList<Transaction> loadTransaction() {
        ArrayList<Transaction> ledger = new ArrayList<>();
        try (FileReader fileReader = new FileReader("./src/main/resources/transactions.csv")) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            bufferedReader.readLine(); // Skip the header line, assuming there's one in the CSV file

            String input;
            while ((input = bufferedReader.readLine()) != null) {
                String[] ledgerParts = input.split("\\|");
                if (ledgerParts.length < 5) {
                    continue; // Skip invalid entries
                }
                String date = ledgerParts[0];
                String time = ledgerParts[1];
                String description = ledgerParts[2];
                String vendor = ledgerParts[3];
                double amount = Double.parseDouble(ledgerParts[4]);

                // Create and add the transaction to the ledger list
                Transaction transaction = new Transaction(date, time, description, vendor, amount);
                ledger.add(transaction);
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading the transactions file.", e);
        }

        // Return the populated ledger list after reading all lines
        return ledger;
    }


}
