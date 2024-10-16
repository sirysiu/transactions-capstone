package com.plurualsight.transaction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Reports {
    private static ArrayList<Ledger> ledgers = new ArrayList<>();

  public Reports() {
      loadTransaction();
  }
    public void generateReports() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("""
                1) Month to Date
                2) Previous Month
                3) Year to Date
                4) Previous Year
                5) Search by Vendor
                0) Back to Reports
                
                
                
                """);

        int input = scanner.nextInt();
        scanner.nextLine();

        switch (input) {
            case 1:
                monthToDate();
                break;
            case 2:
                generatePreviousMonth();
                break;
            case 3:
                generateYearToDate();
                break;
            case 4:
                generatePreviousYear();
                break;
            case 5:

                break;
            case 0:
            default:


        }
    }


    private static void monthToDate() {
        double total = 0.0;
        LocalDate now = LocalDate.now();
        System.out.println("Transactions for the current month (" + now.getMonth() + "):");

        for (Ledger ledger : ledgers) {
            if (ledger.getDate().startsWith(now.getYear() + "-" + String.format("%02d", now.getMonthValue()))) {
                System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: %.2f\n",
                        ledger.getDate(), ledger.getTime(), ledger.getDescription(), ledger.getVendor(), ledger.getAmount());
                total += ledger.getAmount();
            }
        }

        System.out.printf("Month-to-Date Total for %s: %.2f\n", now.getMonth(), total);
    }

    private void generatePreviousMonth() {
        double total = 0.0;
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        System.out.println("Transactions for the previous month (" + lastMonth.getMonth() + "):");

        for (Ledger ledger : ledgers) {
            if (ledger.getDate().startsWith(lastMonth.getYear() + "-" + String.format("%02d", lastMonth.getMonthValue()))) {
                System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: %.2f\n",
                        ledger.getDate(), ledger.getTime(), ledger.getDescription(), ledger.getVendor(), ledger.getAmount());
                total += ledger.getAmount();
            }
        }

        System.out.printf("Total for Previous Month (%s): %.2f\n", lastMonth.getMonth(), total);
    }

    private void generateYearToDate() {
        double total = 0.0;
        int currentYear = LocalDate.now().getYear();
        System.out.println("Transactions for the current year (" + currentYear + "):");

        for (Ledger ledger : ledgers) {
            if (ledger.getDate().startsWith(String.valueOf(currentYear))) {
                System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: %.2f\n",
                        ledger.getDate(), ledger.getTime(), ledger.getDescription(), ledger.getVendor(), ledger.getAmount());
                total += ledger.getAmount();
            }
        }

        System.out.printf("Year-to-Date Total for %d: %.2f\n", currentYear, total);
    }

    public void generatePreviousYear() {
        double total = 0.0;
        int previousYear = LocalDate.now().getYear() - 1;
        System.out.println("Transactions for the previous year (" + previousYear + "):");

        for (Ledger ledger : ledgers) {
            if (ledger.getDate().startsWith(String.valueOf(previousYear))) {
                System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: %.2f\n",
                        ledger.getDate(), ledger.getTime(), ledger.getDescription(), ledger.getVendor(), ledger.getAmount());
                total += ledger.getAmount();
            }
        }

        System.out.printf("Total for Previous Year (%d): %.2f\n", previousYear, total);
  }

    private static void loadTransaction() {

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


}
