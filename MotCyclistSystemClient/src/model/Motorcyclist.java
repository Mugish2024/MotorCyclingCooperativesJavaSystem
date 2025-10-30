package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;



public class Motorcyclist implements Serializable {

 
    private int id;

    private String name;
    private String licenseNumber;
    private String phone;
    private Date dateOfBirth;

    
    private Cooperatives cooperative;

   
    private SavingsAccount savingsAccount;
    
    
    private List<Loan> loans;


    public Motorcyclist() {}

    // Getters and Setters...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Cooperatives getCooperative() {
        return cooperative;
    }

    public void setCooperative(Cooperatives cooperative) {
        this.cooperative = cooperative;
    }

    public SavingsAccount getSavingsAccount() {
        return savingsAccount;
    }

    public void setSavingsAccount(SavingsAccount savingsAccount) {
        this.savingsAccount = savingsAccount;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }
}
