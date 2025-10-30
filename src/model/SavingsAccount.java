
package model;

import java.util.Date;

public class SavingsAccount {
    
    
    private int accountId;
    private int riderId;       // Links to Motorcyclist (who already links to Cooperative)
    private double balance;
    private Date lastInterestDate;
    private String accountNumber;
    // Add constructor, getters, setters

   
   
    public SavingsAccount() {
    }

    public SavingsAccount(int accountId, int riderId, double balance, Date lastInterestDate ,String accountNumber) {
        this.accountId = accountId;
        this.riderId = riderId;
        this.balance = balance;
        this.lastInterestDate = lastInterestDate;
        this.accountNumber = accountNumber;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getRiderId() {
        return riderId;
    }

    public void setRiderId(int riderId) {
        this.riderId = riderId;
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
     public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

}
    

