package dao;

import model.Transaction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {
    private final String jdbcurl = "jdbc:mysql://localhost:3306/motor";
    private final String dbUsername = "root";
    private final String dbPassword = "12345";

    // 1. Log a new transaction
    public boolean logTransaction(Transaction transaction) {
        try (Connection con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword);
             PreparedStatement stmt = con.prepareStatement(
                 "INSERT INTO Transactions (account_number, transaction_type, amount, transaction_date, description) " +
                 "VALUES (?, ?, ?, ?, ?)")) {
            
            stmt.setString(1, transaction.getAccountNumber());
            stmt.setString(2, transaction.getTransactionType());
            stmt.setDouble(3, transaction.getAmount());
            stmt.setTimestamp(4, new Timestamp(transaction.getTransactionDate().getTime()));
            stmt.setString(5, transaction.getDescription());
            
            return stmt.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // 2. Get all transactions for an account
    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword);
             PreparedStatement stmt = con.prepareStatement(
                 "SELECT * FROM Transactions WHERE account_number = ? ORDER BY transaction_date DESC")) {
            
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Transaction t = new Transaction();
                t.setTransactionId(rs.getInt("transaction_id"));
                t.setAccountNumber(rs.getString("account_number"));
                t.setTransactionType(rs.getString("transaction_type"));
                t.setAmount(rs.getDouble("amount"));
                t.setTransactionDate(rs.getDate("transaction_date"));
                t.setDescription(rs.getString("description"));
                transactions.add(t);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return transactions;
    }

    // 3. Get all transactions (admin view)
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword);
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM Transactions ORDER BY transaction_date DESC");
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Transaction t = new Transaction();
                t.setTransactionId(rs.getInt("transaction_id"));
                t.setAccountNumber(rs.getString("account_number"));
                t.setTransactionType(rs.getString("transaction_type"));
                t.setAmount(rs.getDouble("amount"));
                t.setTransactionDate(rs.getDate("transaction_date"));
                t.setDescription(rs.getString("description"));
                transactions.add(t);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return transactions;
    }
}