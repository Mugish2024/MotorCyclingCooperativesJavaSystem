package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;



public class SavingsAccount implements Serializable {

   
    private int accountId;

    
    private String accountNumber;

    private double balance;
    private Date lastInterestDate;

   
    private Motorcyclist motorcyclist;

    private List<AccountTransaction> transactions;

    public SavingsAccount() {}

    // Getters and Setters...

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Date getLastInterestDate() {
        return lastInterestDate;
    }

    public void setLastInterestDate(Date lastInterestDate) {
        this.lastInterestDate = lastInterestDate;
    }

    public Motorcyclist getMotorcyclist() {
        return motorcyclist;
    }

    public void setMotorcyclist(Motorcyclist motorcyclist) {
        this.motorcyclist = motorcyclist;
    }

    public List<AccountTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<AccountTransaction> transactions) {
        this.transactions = transactions;
    }
}