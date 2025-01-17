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

    public void monthToDate() {
        double total = 0.0; // The double data type will hold total amounts
        LocalDate now = LocalDate.now(); // get the current date
        System.out.println("Transactions for the current month (" + now.getMonth() + "):");

        for (Transaction transaction : ledger) { // find the transaction within the current month
            if (transaction.getDate().startsWith(now.getYear() + "-" + String.format("%02d", now.getMonthValue()))) {
                System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: %.2f\n",
                        transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
                total += transaction.getAmount(); // will add the total within the current month
            }
        }

        System.out.printf("Month-to-Date Total for %s: %.2f\n", now.getMonth(), total);
    }

    public void generatePreviousMonth() {
        double total = 0.0;
        LocalDate lastMonth = LocalDate.now().minusMonths(1);// Gets the date for previous month
        System.out.println("Transactions for the previous month (" + lastMonth.getMonth() + "):");

        for (Transaction transaction : ledger) {
            if (transaction.getDate().startsWith(lastMonth.getYear() + "-" + String.format("%02d", lastMonth.getMonthValue()))) {
                System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: %.2f\n",
                        transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
                total += transaction.getAmount();
            }
        }

        System.out.printf("Total for Previous Month (%s): %.2f\n", lastMonth.getMonth(), total);
    }

    public void generateYearToDate() {
        double total = 0.0;
        int currentYear = LocalDate.now().getYear();
        System.out.println("Transactions for the current year (" + currentYear + "):");

        for (Transaction transaction : ledger) {
            if (transaction.getDate().startsWith(String.valueOf(currentYear))) {
                System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: %.2f\n",
                        transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
                total += transaction.getAmount();
            }
        }

        System.out.printf("Year-to-Date Total for %d: %.2f\n", currentYear, total);
    }

    public void generatePreviousYear() {
        double total = 0.0;
        int previousYear = LocalDate.now().getYear() - 1;
        System.out.println("Transactions for the previous year (" + previousYear + "):");

        for (Transaction transaction : ledger) {
            if (transaction.getDate().startsWith(String.valueOf(previousYear))) {
                System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: %.2f\n",
                        transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
                total += transaction.getAmount();
            }
        }

        System.out.printf("Total for Previous Year (%d): %.2f\n", previousYear, total);
    }

    // method for user to search by vendor input
    public void search() {
        System.out.println("Search: ");
        String input = scan.nextLine();
        for (Transaction transaction : ledger) {

            if (input.equalsIgnoreCase(transaction.getVendor())) {
                System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: %.2f\n",
                        transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());

            } else if (input.equalsIgnoreCase(transaction.getDate())) {
                System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: %.2f\n",
                        transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());

            } else if (input.trim().equalsIgnoreCase(transaction.getDescription())) {
                System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: %.2f\n",
                        transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());

            }

        }

    }

    public ArrayList loadTransaction() {
        ArrayList<Transaction> ledger = new ArrayList<>();
        try (FileReader fileReader = new FileReader("./src/main/resources/transactions.csv")) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            bufferedReader.readLine();

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

                Transaction transaction = new Transaction(date, time, description, vendor, amount);
                ledger.add(transaction);

                return ledger;
            }


            bufferedReader.close();
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
        return ledger;
    }

}
