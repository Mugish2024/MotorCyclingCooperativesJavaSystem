
package model;

public class Cooperatives {
   
    private int id;
    private String name;
    private String location;
    private double interestRate;
    // Add constructor, getters, and setters

    public Cooperatives() {
    }

    public Cooperatives(int id, String name, String location, double interestRate) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.interestRate = interestRate;
    }

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }


}
    
