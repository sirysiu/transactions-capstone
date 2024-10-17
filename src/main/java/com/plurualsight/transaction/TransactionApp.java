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
        loadTransaction(); // load the transaction.csv
        boolean isRunning = true;


        while (isRunning) {
            // The users first prompt
            System.out.println("""
                    
                    ╔═══════════════════════════════╗
                    ║          Home Screen          ║
                    ╠═══════════════════════════════╣
                    ║ D) Add Deposit                ║
                    ║ P) Make Payment (Debit)       ║
                    ║ L) Ledger                     ║
                    ║ X) Exit                       ║
                    ╚═══════════════════════════════╝
                    """);
            String input = scanner.nextLine();

            switch (input) {
                case "d", "D":
                    addingDeposit(); // When user input d they will be able to add any deposits
                    break;
                case "l", "L":
                    displayTransaction(); // When user input L display all transactions
                    break;
                case "p", "P":
                    makePayment(); // When user input p they will be allowed to add a payment
                    break;
                case "x", "X":
                    System.out.println("exiting..."); // If x is called that will let the loop close out
                    isRunning = false;
            }

        }
    }

    private static void loadTransaction() {
        try (FileReader fileReader = new FileReader("./src/main/resources/transactions.csv")) { // Using the try with in resources statement
                                                                                                        // will allow the file to close instead of using the buffered.close()
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine(); // Skip the header so the while loop us able to read the data type

            String input;
            while ((input = bufferedReader.readLine()) != null) {
                String[] ledgerParts = input.split("\\|");
                if (ledgerParts.length < 5) continue; // Skips invalid entries

                String date = ledgerParts[0];
                String time = ledgerParts[1];
                String description = ledgerParts[2];
                String vendor = ledgerParts[3];
                double amount = Double.parseDouble(ledgerParts[4]);

                Transaction transaction = new Transaction(date, time, description, vendor, amount);
                transactions.add(transaction); // Create a new Transaction object and add to the arrays list

            }

        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    private static void displayTransaction() throws FileNotFoundException {
        for (Transaction transaction : transactions) { //Grab all the object from the array list and put in a loop to print out all list of transaction
            System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: %.2f\n",
                    transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
        }
// ******** This will Prompt the user to be able to filter by payment of deposits only
        System.out.println("""
                
                ╔════════════════════════════════════╗
                ║           Filter Options           ║
                ╠════════════════════════════════════╣
                ║ [D] Deposit                        ║
                ║ [P] Payments                       ║
                ║ [R] Reports                        ║
                ╚════════════════════════════════════╝
                """);
        String ledgerInput = scanner.nextLine();

        switch (ledgerInput) {
            case "d", "D":
                displayDeposits(); // displaying amount that are positive
                break;
            case "p", "P":
                displayPayments(); // display in amount that are negative
                break;
            case "r", "R":
                Reports report = new Reports();
                report.generateReports(); // allow user to view the transaction by dates
                break;
            default:
                System.out.println("Invalid return to main");
        }


    }

    private static void displayDeposits() {
        System.out.println("Deposits:");
        for (Transaction transaction : transactions) { // calling the arraylist
            if (transaction.getAmount() > 0) {          // to find amount that are more than 0 or positive will only print out deposits
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
        String formattedTime = today.format(timefmt); // format the date and time of when user will add any new objects
       // prompt the user to add their deposit here
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline


        Transaction newTransaction = new Transaction(formattedDate, formattedTime, description, vendor, amount);
        transactions.add(newTransaction);  // will add new object made in addingDeposits to the Arraylist
        System.out.println("Deposit added");


        saveTransactions(newTransaction); // this will allow the new user input to be saved in the csv file

    }

    private static void makePayment() throws FileNotFoundException {

        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(fmt);
        DateTimeFormatter timefmt = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = today.format(timefmt);
        // prompt uesr to add new payment objects
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
