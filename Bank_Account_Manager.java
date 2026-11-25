import java.util.*;
import java.io.*;

class BankAccount {
    String accountId;
    String name;
    double balance;

    BankAccount(String accountId, String name, double balance) {
        this.accountId = accountId;
        this.name = name;
        this.balance = balance;
    }
}

public class Bank_Account_Manager {

    static ArrayList<BankAccount> accounts = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    static final String FILE_NAME = "accounts.txt";
    static int nextAccountNumber = 101;

    // Load accounts from file
    static void loadAccounts() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|", 3);

                if (data.length == 3) {
                    String id = data[0];
                    String name = data[1];
                    double balance = Double.parseDouble(data[2]);

                    accounts.add(new BankAccount(id, name, balance));

                    int number = Integer.parseInt(id.substring(1));

                    if (number >= nextAccountNumber) {
                        nextAccountNumber = number + 1;
                    }
                }
            }

            System.out.println("Account data loaded successfully.");

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading account data: " + e.getMessage());
        }
    }

    // Save accounts to file
    static void saveAccounts() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (BankAccount account : accounts) {
                bw.write(account.accountId + "|" +
                         account.name + "|" +
                         account.balance);
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving account data: " + e.getMessage());
        }
    }

    // Find account using Account ID
    static BankAccount findAccount(String id) {

        for (BankAccount account : accounts) {
            if (account.accountId.equalsIgnoreCase(id)) {
                return account;
            }
        }

        return null;
    }

    // Create a new account
    static void createAccount() {

        System.out.print("Enter account holder name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty() || name.contains("|")) {
            System.out.println("Invalid account holder name.");
            return;
        }

        double balance = readAmount("Enter opening balance: ");

        if (balance < 0) {
            System.out.println("Opening balance cannot be negative.");
            return;
        }

        String id = "A" + nextAccountNumber++;

        BankAccount account = new BankAccount(id, name, balance);
        accounts.add(account);

        System.out.println("\nAccount created successfully!");
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Balance: " + formatBalance(balance));
    }

    // Deposit money
    static void depositMoney() {

        System.out.print("Enter Account ID: ");
        String id = sc.nextLine().trim();

        BankAccount account = findAccount(id);

        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        double amount = readAmount("Enter deposit amount: ");

        if (amount <= 0) {
            System.out.println("Deposit amount must be positive.");
            return;
        }

        account.balance += amount;

        System.out.println("Deposited " + formatBalance(amount)
                + ". New balance: " + formatBalance(account.balance));
    }

    // Withdraw money
    static void withdrawMoney() {

        System.out.print("Enter Account ID: ");
        String id = sc.nextLine().trim();

        BankAccount account = findAccount(id);

        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        double amount = readAmount("Enter withdrawal amount: ");

        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return;
        }

        if (amount > account.balance) {
            System.out.println("Insufficient balance. Current balance: "
                    + formatBalance(account.balance));
            return;
        }

        account.balance -= amount;

        System.out.println("Withdrawn " + formatBalance(amount)
                + ". New balance: " + formatBalance(account.balance));
    }

    // Search account by holder name using Linear Search
    static void searchAccount() {

        System.out.print("Enter account holder name: ");
        String searchName = sc.nextLine().trim();

        boolean found = false;

        for (BankAccount account : accounts) {

            if (account.name.equalsIgnoreCase(searchName)) {

                System.out.println("\nAccount Found!");
                displayAccount(account);
                found = true;
            }
        }

        if (!found) {
            System.out.println("Account not found.");
        }
    }

    // Display accounts sorted by balance (High to Low)
    // Using Bubble Sort
    static void displaySortedAccounts() {

        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }

        ArrayList<BankAccount> sortedAccounts =
                new ArrayList<>(accounts);

        int n = sortedAccounts.size();

        for (int i = 0; i < n - 1; i++) {

            for (int j = 0; j < n - i - 1; j++) {

                if (sortedAccounts.get(j).balance <
                    sortedAccounts.get(j + 1).balance) {

                    BankAccount temp = sortedAccounts.get(j);

                    sortedAccounts.set(j, sortedAccounts.get(j + 1));

                    sortedAccounts.set(j + 1, temp);
                }
            }
        }

        System.out.println("\n--- ACCOUNTS SORTED BY BALANCE ---");

        for (int i = 0; i < sortedAccounts.size(); i++) {

            System.out.print((i + 1) + ". ");
            displayAccount(sortedAccounts.get(i));
        }
    }

    // Display one account
    static void displayAccount(BankAccount account) {

        System.out.println("ID: " + account.accountId
                + " | Name: " + account.name
                + " | Balance: " + formatBalance(account.balance));
    }

    // Read and validate numeric input
    static double readAmount(String message) {

        while (true) {

            System.out.print(message);
            String input = sc.nextLine().trim();

            try {
                double amount = Double.parseDouble(input);

                if (Double.isFinite(amount)) {
                    return amount;
                }

                System.out.println("Please enter a valid amount.");

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number.");
            }
        }
    }

    // Format balance
    static String formatBalance(double amount) {

        if (amount == Math.floor(amount)) {
            return String.format("%.0f", amount);
        }

        return String.format("%.2f", amount);
    }

    // Main method
    public static void main(String[] args) {

        loadAccounts();

        while (true) {

            System.out.println("\n================================");
            System.out.println("     SIMPLE BANK ACCOUNT MANAGER");
            System.out.println("================================");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Search Account by Name");
            System.out.println("5. Display Accounts Sorted by Balance");
            System.out.println("6. Exit");
            System.out.println("================================");

            System.out.print("Enter your choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {

                case "1":
                    createAccount();
                    break;

                case "2":
                    depositMoney();
                    break;

                case "3":
                    withdrawMoney();
                    break;

                case "4":
                    searchAccount();
                    break;

                case "5":
                    displaySortedAccounts();
                    break;

                case "6":
                    saveAccounts();
                    System.out.println("Account data saved successfully.");
                    System.out.println("Thank you for using Bank Manager!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}