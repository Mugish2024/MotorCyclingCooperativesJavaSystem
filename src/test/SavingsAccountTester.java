package test;

import dao.SavingAccountDao;
import model.SavingsAccount;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class SavingsAccountTester {
    public static void main(String[] args) {
        SavingAccountDao dao = new SavingAccountDao();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n=== Savings Account Tester ===");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. Transfer Money");
            System.out.println("6. Apply Interest");
            System.out.println("7. View Accounts by Cooperative");
            System.out.println("8. View All Accounts");
            System.out.println("9. Find Account by Rider ID");
            System.out.println("10. Exit");
            System.out.print("Choose option: ");
            
            int choice = getValidInt(scanner, 1, 10);
            scanner.nextLine(); // Clear buffer
            
            switch (choice) {
              //  case 1: {
                    // Create Account
                //    SavingsAccount account = new SavingsAccount();
                  //  System.out.print("Enter Rider ID: ");
                 //   account.setRiderId(getValidInt(scanner));
                    
                  //  System.out.print("Enter Initial Balance: ");
                  //  account.setBalance(getValidDouble(scanner));
                    
                   //account.setLastInterestDate(new Date());
                    
                   // if (dao.create(riderId)) {
                      //  System.out.println("Account created! Account Number: " + account.getAccountNumber());
                   // } else {
                   //     System.out.println("Failed to create account!");
                   // }
                   // break;
                //}
                    
                case 2: {
                    // Deposit
                    System.out.print("Enter Account Number: ");
                    String accountNumber = scanner.nextLine();
                    
                    System.out.print("Enter Amount: ");
                    double amount = getValidDouble(scanner, 0.01);
                    
                    if (dao.deposit(accountNumber, amount)) {
                        System.out.printf("Successfully deposited %.2f to account %s%n", amount, accountNumber);
                    } else {
                        System.out.println("Deposit failed! Check account number or amount.");
                    }
                    break;
                }
                    
                case 3: {
                    // Withdraw
                    System.out.print("Enter Account Number: ");
                    String accountNumber = scanner.nextLine();
                    
                    System.out.print("Enter Amount: ");
                    double amount = getValidDouble(scanner, 0.01);
                    
                    if (dao.withdraw(accountNumber, amount)) {
                        System.out.printf("Withdrawn %.2f from account %s%n", amount, accountNumber);
                    } else {
                        System.out.println("Withdrawal failed! Check balance or account.");
                    }
                    break;
                }
                    
                case 4: {
                    // Check Balance
                    System.out.print("Enter Account Number: ");
                    String accountNumber = scanner.nextLine();
                    
                    double balance = dao.getBalance(accountNumber);
                    if (balance >= 0) {
                        System.out.printf("Balance for account %s: %.2f%n", accountNumber, balance);
                    } else {
                        System.out.println("Account not found!");
                    }
                    break;
                }
                    
                case 5: {
                    // Transfer
                    System.out.print("Enter Sender Account Number: ");
                    String fromAccount = scanner.nextLine();
                    
                    System.out.print("Enter Receiver Account Number: ");
                    String toAccount = scanner.nextLine();
                    
                    System.out.print("Enter Amount: ");
                    double amount = getValidDouble(scanner, 0.01);
                    
                    if (dao.transfer(fromAccount, toAccount, amount)) {
                        System.out.printf("Transferred %.2f from %s to %s%n", amount, fromAccount, toAccount);
                    } else {
                        System.out.println("Transfer failed! Check accounts and balances.");
                    }
                    break;
                }
                    
                case 6: {
                    // Apply Interest
                    System.out.print("Enter Cooperative ID: ");
                    int coopId = getValidInt(scanner);
                    
                    if (dao.applyMonthlyInterest(coopId)) {
                        System.out.println("Interest applied to cooperative " + coopId);
                    } else {
                        System.out.println("Failed to apply interest!");
                    }
                    break;
                }
                    
                case 7: {
                    // Accounts by Cooperative
                    System.out.print("Enter Cooperative ID: ");
                    int coopId = getValidInt(scanner);
                    
                    List<SavingsAccount> accounts = dao.getAccountsByCooperative(coopId);
                    
                    System.out.printf("\nAccounts in Cooperative %d:%n", coopId);
                    System.out.println("--------------------------------------------------");
                    System.out.printf("%-15s %-10s %-12s %-15s%n", 
                                    "Account Number", "Rider ID", "Balance", "Last Interest");
                    
                    for (SavingsAccount acc : accounts) {
                        System.out.printf("%-15s %-10d %-12.2f %-15s%n", 
                                        acc.getAccountNumber(), 
                                        acc.getRiderId(), 
                                        acc.getBalance(),
                                        acc.getLastInterestDate());
                    }
                    break;
                }
                    
                case 8: {
                    // All Accounts
                    List<SavingsAccount> accounts = dao.getAllAccounts();
                    
                    System.out.println("\nAll Savings Accounts:");
                    System.out.println("--------------------------------------------------");
                    System.out.printf("%-15s %-10s %-12s %-15s%n", 
                                    "Account Number", "Rider ID", "Balance", "Last Interest");
                    
                    for (SavingsAccount acc : accounts) {
                        System.out.printf("%-15s %-10d %-12.2f %-15s%n", 
                                        acc.getAccountNumber(), 
                                        acc.getRiderId(), 
                                        acc.getBalance(),
                                        acc.getLastInterestDate());
                    }
                    break;
                }
                    
                case 9: {
                    // Find by Rider ID
                    System.out.print("Enter Rider ID: ");
                    int riderId = getValidInt(scanner);
                    
                    SavingsAccount account = dao.findByRiderId(riderId);
                    if (account != null) {
                        System.out.println("\nAccount Details:");
                        System.out.println("----------------");
                        System.out.println("Account Number: " + account.getAccountNumber());
                        System.out.println("Rider ID: " + account.getRiderId());
                        System.out.printf("Balance: %.2f%n", account.getBalance());
                        System.out.println("Last Interest Date: " + account.getLastInterestDate());
                    } else {
                        System.out.println("No account found for rider " + riderId);
                    }
                    break;
                }
                    
                case 10:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
            }
        }
    }
    
    // Helper methods for input validation
    private static int getValidInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input! Please enter a number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }
    
    private static int getValidInt(Scanner scanner, int min, int max) {
        int input;
        do {
            input = getValidInt(scanner);
            if (input < min || input > max) {
                System.out.printf("Input must be between %d and %d: ", min, max);
            }
        } while (input < min || input > max);
        return input;
    }
    
    private static double getValidDouble(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            System.out.print("Invalid input! Please enter a number: ");
            scanner.next();
        }
        return scanner.nextDouble();
    }
    
    private static double getValidDouble(Scanner scanner, double min) {
        double input;
        do {
            input = getValidDouble(scanner);
            if (input < min) {
                System.out.printf("Input must be at least %.2f: ", min);
            }
        } while (input < min);
        return input;
    }
} 