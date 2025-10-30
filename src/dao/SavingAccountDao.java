package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.SavingsAccount;

public class SavingAccountDao {
    private final String jdbcurl = "jdbc:mysql://localhost:3306/motor";
    private final String dbUsername = "root";
    private final String dbPassword = "12345";

    // Generate unique account number
    private String generateAccountNumber() {
        return "ACC" + (100000 + (int)(Math.random() * 900000)); // ACC123456 format
    }

    // 1. Create account with generated account number
   // In the SavingAccountDao class, modify the create method to only require riderId

    public boolean riderHasAccount(int riderId) {
    try (Connection con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword);
         PreparedStatement stmt = con.prepareStatement(
             "SELECT COUNT(*) AS count FROM SavingsAccounts WHERE rider_id = ?")) {
        
        stmt.setInt(1, riderId);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            return rs.getInt("count") > 0;
        }
        return false;
    } catch (Exception ex) {
        ex.printStackTrace();
        return true; // Assume they have an account to prevent creation on error
    }
}
    public boolean create(int riderId) {
    // First check if rider already has an account
    if (riderHasAccount(riderId)) {
        return false;
    }
    
    String generatedNumber = generateAccountNumber();
    try (Connection con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword);
         PreparedStatement stmt = con.prepareStatement(
             "INSERT INTO SavingsAccounts (account_number, rider_id, balance, last_interest_date) VALUES (?, ?, 0, CURDATE())")) {
        
        stmt.setString(1, generatedNumber);
        stmt.setInt(2, riderId);
        
        int result = stmt.executeUpdate();
        return result > 0;
    } catch (Exception ex) {
        ex.printStackTrace();
        return false;
    }
}
    // 2. Deposit with amount validation
    public boolean deposit(String accountNumber, double amount) {
        if (amount <= 0) return false;
        
        try (Connection con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword);
             PreparedStatement stmt = con.prepareStatement(
                 "UPDATE SavingsAccounts SET balance = balance + ? WHERE account_number = ?")) {
            
            stmt.setDouble(1, amount);
            stmt.setString(2, accountNumber);
            return stmt.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // 3. Withdraw with balance check
    public boolean withdraw(String accountNumber, double amount) {
        if (amount <= 0) return false;
        
        try (Connection con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword)) {
            con.setAutoCommit(false);
            
            // Check balance first
            try (PreparedStatement checkStmt = con.prepareStatement(
                     "SELECT balance FROM SavingsAccounts WHERE account_number = ? FOR UPDATE")) {
                
                checkStmt.setString(1, accountNumber);
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next() && rs.getDouble("balance") >= amount) {
                    try (PreparedStatement updateStmt = con.prepareStatement(
                             "UPDATE SavingsAccounts SET balance = balance - ? WHERE account_number = ?")) {
                        
                        updateStmt.setDouble(1, amount);
                        updateStmt.setString(2, accountNumber);
                        updateStmt.executeUpdate();
                        con.commit();
                        return true;
                    }
                }
                con.rollback();
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // 4. Get balance by account number
    public double getBalance(String accountNumber) {
        try (Connection con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword);
             PreparedStatement stmt = con.prepareStatement(
                 "SELECT balance FROM SavingsAccounts WHERE account_number = ?")) {
            
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getDouble("balance") : -1; // -1 indicates not found
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    // 5. Transfer between accounts
    public boolean transfer(String fromAccount, String toAccount, double amount) {
        if (amount <= 0 || fromAccount.equals(toAccount)) return false;
        
        try (Connection con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword)) {
            con.setAutoCommit(false);
            
            // Verify sender balance
            try (PreparedStatement checkStmt = con.prepareStatement(
                     "SELECT balance FROM SavingsAccounts WHERE account_number = ? FOR UPDATE")) {
                
                checkStmt.setString(1, fromAccount);
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next() && rs.getDouble("balance") >= amount) {
                    // Withdraw
                    try (PreparedStatement withdrawStmt = con.prepareStatement(
                             "UPDATE SavingsAccounts SET balance = balance - ? WHERE account_number = ?")) {
                        
                        withdrawStmt.setDouble(1, amount);
                        withdrawStmt.setString(2, fromAccount);
                        withdrawStmt.executeUpdate();
                    }
                    
                    // Deposit
                    try (PreparedStatement depositStmt = con.prepareStatement(
                             "UPDATE SavingsAccounts SET balance = balance + ? WHERE account_number = ?")) {
                        
                        depositStmt.setDouble(1, amount);
                        depositStmt.setString(2, toAccount);
                        depositStmt.executeUpdate();
                    }
                    
                    con.commit();
                    return true;
                }
                con.rollback();
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // 6. Apply monthly interest
    public boolean applyMonthlyInterest(int cooperativeId) {
        try (Connection con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword)) {
            // Get interest rate
            try (PreparedStatement rateStmt = con.prepareStatement(
                     "SELECT interest_rate FROM Cooperatives WHERE cooperative_id = ?")) {
                
                rateStmt.setInt(1, cooperativeId);
                ResultSet rs = rateStmt.executeQuery();
                
                if (rs.next()) {
                    double rate = rs.getDouble("interest_rate") / 100.0;
                    
                    // Apply to all accounts in cooperative
                    try (PreparedStatement updateStmt = con.prepareStatement(
                             "UPDATE SavingsAccounts sa " +
                             "JOIN Motorcyclists m ON sa.rider_id = m.rider_id " +
                             "SET sa.balance = sa.balance * (1 + ?), " +
                             "sa.last_interest_date = CURDATE() " +
                             "WHERE m.cooperative_id = ?")) {
                        
                        updateStmt.setDouble(1, rate);
                        updateStmt.setInt(2, cooperativeId);
                        return updateStmt.executeUpdate() > 0;
                    }
                }
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // 7. Get account by rider ID
    public SavingsAccount findByRiderId(int riderId) {
        try (Connection con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword);
             PreparedStatement stmt = con.prepareStatement(
                 "SELECT * FROM SavingsAccounts WHERE rider_id = ?")) {
            
            stmt.setInt(1, riderId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                SavingsAccount account = new SavingsAccount();
                account.setAccountId(rs.getInt("account_id"));
                account.setAccountNumber(rs.getString("account_number"));
                account.setRiderId(rs.getInt("rider_id"));
                account.setBalance(rs.getDouble("balance"));
                account.setLastInterestDate(rs.getDate("last_interest_date"));
                return account;
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // 8. Get accounts by cooperative
    public List<SavingsAccount> getAccountsByCooperative(int cooperativeId) {
        List<SavingsAccount> accounts = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword);
             PreparedStatement stmt = con.prepareStatement(
                 "SELECT sa.* FROM SavingsAccounts sa " +
                 "JOIN Motorcyclists m ON sa.rider_id = m.rider_id " +
                 "WHERE m.cooperative_id = ?")) {
            
            stmt.setInt(1, cooperativeId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                SavingsAccount account = new SavingsAccount();
                account.setAccountId(rs.getInt("account_id"));
                account.setAccountNumber(rs.getString("account_number"));
                account.setRiderId(rs.getInt("rider_id"));
                account.setBalance(rs.getDouble("balance"));
                account.setLastInterestDate(rs.getDate("last_interest_date"));
                accounts.add(account);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return accounts;
    }

    // 9. Get all accounts
    public List<SavingsAccount> getAllAccounts() {
        List<SavingsAccount> accounts = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword);
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM SavingsAccounts");
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                SavingsAccount account = new SavingsAccount();
                account.setAccountId(rs.getInt("account_id"));
                account.setAccountNumber(rs.getString("account_number"));
                account.setRiderId(rs.getInt("rider_id"));
                account.setBalance(rs.getDouble("balance"));
                account.setLastInterestDate(rs.getDate("last_interest_date"));
                accounts.add(account);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return accounts;
    }
}