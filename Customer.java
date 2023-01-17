package net.cps.common.entities;

import net.cps.common.utils.AbstractUser;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer extends AbstractUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "INT NOT NULL AUTO_INCREMENT")
    private Integer id;
    @Column(name = "email", unique = true, columnDefinition = "VARCHAR(100) UNIQUE NOT NULL")
    private String email;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "password_salt")
    private String passwordSalt;
    @Column(name = "password_hash")
    private String passwordHash;
    @Column(name = "is_active")
    private Boolean isActive;
    @OneToMany(mappedBy = "customer")
    @Column(name = "vehicles")
    private List<Vehicle> vehicles;
    @OneToMany(mappedBy = "customer")
    @Column(name = "subscriptions")
    private List<Subscription> subscriptions;
    @Column(name = "balance")
    private Double balance;
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public Customer () {
        super();
    }
    
    public Customer (String email, String firstName, String lastName, String password) {
        super();
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordSalt = generateSalt();
        this.passwordHash = hashPassword(password, passwordSalt);
        
        this.isActive = false;
        this.vehicles = new ArrayList<>();
        this.subscriptions = new ArrayList<>();
        this.balance = 0.0;
    }
    
    
    /* ----- Getters and Setters ------------------------------------ */
    
    public Integer getId () {
        return id;
    }
    
    public void setId (Integer id) {
        this.id = id;
    }
    
    @Override
    public String getEmail () {
        return email;
    }
    
    public void setEmail (String email) {
        this.email = email;
    }
    
    public String getFirstName () {
        return firstName;
    }
    
    public void setFirstName (String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName () {
        return lastName;
    }
    
    public void setLastName (String lastName) {
        this.lastName = lastName;
    }
    
    public String getFullName () {
        return firstName + " " + lastName;
    }
    
    @Override
    public String getPasswordSalt () {
        return passwordSalt;
    }
    
    public void setPasswordSalt (String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }
    
    @Override
    public String getPasswordHash () {
        return passwordHash;
    }
    
    public void setPasswordHash (String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public void setPassword (String password) {
        this.passwordHash = hashPassword(password, passwordSalt);
    }
    
    public Boolean getIsActive () {
        return isActive;
    }
    
    public void setIsActive (Boolean active) {
        isActive = active;
    }
    
    public List<Vehicle> getVehicles () {
        return vehicles;
    }
    
    public void setVehicles (List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
    
    public void setVehicles (Vehicle... vehicles) {
        Collections.addAll(this.vehicles, vehicles);
    }
    
    public List<Subscription> getSubscriptions () {
        return subscriptions;
    }
    
    public void setSubscriptions (List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }
    
    public Double getBalance () {
        return balance;
    }
    
    public void setBalance (Double balance) {
        this.balance = balance;
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    public void addVehicle (Vehicle vehicle) {
        this.vehicles.add(vehicle);
    }
    
    public void removeVehicle (Vehicle vehicle) {
        this.vehicles.remove(vehicle);
    }
    
    public void addSubscription (Subscription subscription) {
        this.subscriptions.add(subscription);
    }
    
    public void removeSubscription (Subscription subscription) {
        this.subscriptions.remove(subscription);
    }
    
    public void creditBalance (Double amount) {
        this.balance += amount;
    }
    
    public void chargeBalance (Double amount) {
        this.balance -= amount;
    }
    
    @Override
    public String toString () {
        return "Customer {" +
                "id: " + id +
                ", email: '" + email + "'" +
                ", firstName: '" + firstName + "'" +
                ", lastName: '" + lastName + "'" +
                ", passwordHash: '" + passwordHash + "'" +
                ", passwordSalt: '" + passwordSalt + "'" +
                ", isActive: " + isActive +
                ", vehicles: " + vehicles +
                ", subscriptions: " + subscriptions +
                ", balance: " + balance +
                "}";
    }
}
