package model;

import java.io.Serializable;



public class Admin implements Serializable {
    
    private int id;
    private String username;
    private String fullName;
    private String email;           // Required for email-related operations
    private String password;        // Required for login and password hashing
    private boolean isVerified;     // Required for email verification status
    private String verificationToken; // Required for verification process

    // Default constructor
    public Admin() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean isVerified) { this.isVerified = isVerified; }

    public String getVerificationToken() { return verificationToken; }
    public void setVerificationToken(String verificationToken) { this.verificationToken = verificationToken; }
}