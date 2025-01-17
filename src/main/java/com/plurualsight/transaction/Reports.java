package com.plurualsight.transaction;

import java.util.Scanner;


public class Reports {

    static Scanner scanner = new Scanner(System.in);
    ReportsController rc;

    public Reports() {
        rc = new ReportsController();

    }


    public void generateReports() {

// prompt will be shown after user input r in the transactionApp ledger prompt
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("""
                    
                    ╔═══════════════════════════════════╗
                    ║          Report Options           ║
                    ╠═══════════════════════════════════╣
                    ║ 1) Month to Date                  ║
                    ║ 2) Previous Month                 ║
                    ║ 3) Year to Date                   ║
                    ║ 4) Previous Year                  ║
                    ║ 5) Search More...                 ║
                    ║ 0) Back to Homepage               ║
                    ╚═══════════════════════════════════╝
                    """);

            int input = scanner.nextInt();
            scanner.nextLine();

            switch (input) {
                case 1:
                    rc.monthToDate(); // generate the current month
                    break;
                case 2:
                    rc.generatePreviousMonth();
                    break;
                case 3:
                    rc.generateYearToDate(); // generate the current year
                    break;
                case 4:
                    rc.generatePreviousYear();
                    break;
                case 5:
                    rc.search(); // allow the user to search by vendor
                    break;
                case 0:
                default:
                    isRunning = false;


            }
        }
    }
}