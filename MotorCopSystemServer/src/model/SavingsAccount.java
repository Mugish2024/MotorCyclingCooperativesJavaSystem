package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "savings_account")
public class SavingsAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountId;

    @Column(nullable = false)
    private String accountNumber;

    private double balance;
    private Date lastInterestDate;

    @OneToOne
    @JoinColumn(name = "rider_id")
    private Motorcyclist motorcyclist;

    @OneToMany(mappedBy = "savingsAccount", cascade = CascadeType.ALL)
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