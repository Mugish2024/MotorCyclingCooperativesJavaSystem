package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Loan")
public class Loan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int loanId;

    private double amount;
    private double interestRate;
    private Date issueDate;
    private Date dueDate;
    private boolean isRepaid;

    @ManyToOne
    @JoinColumn(name = "motorcyclist_id", nullable = false)
    private Motorcyclist motorcyclist;

    public Loan() {}

    public Loan(double amount, double interestRate, Date issueDate, Date dueDate, boolean isRepaid, Motorcyclist motorcyclist) {
        this.amount = amount;
        this.interestRate = interestRate;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.isRepaid = isRepaid;
        this.motorcyclist = motorcyclist;
    }

    // Getters and Setters...

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isIsRepaid() {
        return isRepaid;
    }

    public void setIsRepaid(boolean isRepaid) {
        this.isRepaid = isRepaid;
    }

    public Motorcyclist getMotorcyclist() {
        return motorcyclist;
    }

    public void setMotorcyclist(Motorcyclist motorcyclist) {
        this.motorcyclist = motorcyclist;
    }
}
