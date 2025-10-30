package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Cooperative")
public class Cooperatives implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    private String location;

    private double interestRate;

    @OneToMany(mappedBy = "cooperative", cascade = CascadeType.ALL)
    private List<Motorcyclist> motorcyclists;

    public Cooperatives() {}

    public Cooperatives(int id, String name, String location, double interestRate) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.interestRate = interestRate;
    }

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

    public List<Motorcyclist> getMotorcyclists() {
        return motorcyclists;
    }

    public void setMotorcyclists(List<Motorcyclist> motorcyclists) {
        this.motorcyclists = motorcyclists;
    }
}
