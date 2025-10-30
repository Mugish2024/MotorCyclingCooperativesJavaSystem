package model;

public class Admin {
    private int id;
    private String username;
    private String fullName;

    public Admin() {}

    public Admin(int id, String username, String fullName) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}