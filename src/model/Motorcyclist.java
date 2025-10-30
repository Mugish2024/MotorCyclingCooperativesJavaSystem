package model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

public class Motorcyclist {
    private int id;
    private String name;
    private String licenseNumber;
    private String phone;
    private Date dateOfBirth;  // New field added
    private int cooperativeId;
    private String cooperativeName;

    public Motorcyclist() {}

    // Updated constructor with dateOfBirth
    public Motorcyclist(int id, String name, String licenseNumber, String phone, 
                      Date dateOfBirth, int cooperativeId, String cooperativeName) {
        this.id = id;
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.cooperativeId = cooperativeId;
        this.cooperativeName = cooperativeName;
    }

    // All your existing getters and setters remain the same
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

    public int getCooperativeId() {
        return cooperativeId;
    }

    public void setCooperativeId(int cooperativeId) {
        this.cooperativeId = cooperativeId;
    }

    public String getCooperativeName() {
        return cooperativeName;
    }

    public void setCooperativeName(String cooperativeName) {
        this.cooperativeName = cooperativeName;
    }

    // New getter and setter for dateOfBirth
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

public int getAge() {
    if (this.dateOfBirth == null) {
        return 0;
    }
    
    Calendar dob = Calendar.getInstance();
    dob.setTime(this.dateOfBirth);
    Calendar today = Calendar.getInstance();
    
    int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
    
    // If birthday hasn't occurred yet this year, subtract 1
    if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
        age--;
    }
    
    return age;
}
}