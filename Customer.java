package org.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer extends AbstractUser implements Serializable {

    private String email;
    private String id;
    private String firstName;
    private String lastName;
    private String passwordSalt;
    private String passwordHash;
    private Boolean isActive;
    private List<Vehicle> vehicles;
    private List<Subscription> subscriptions;
    private Double balance;


    /* ----- Constructors ------------------------------------------- */

    public Customer () {
        super();
    }

    public Customer (String email, String id, String firstName, String lastName, String password) {
        super();
        this.email = email;
        this.id = id;
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

    public String getEmail () {
        return email;
    }
    public void setEmail (String email) {
        this.email = email;
    }

    public String getId () {
        return id;
    }
    public void setId (String id) {
        this.id = id;
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

    public String getPasswordSalt () {
        return passwordSalt;
    }
    public void setPasswordSalt (String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getPasswordHash () {
        return passwordHash;
    }
    public void setPasswordHash (String passwordHash) {
        this.passwordHash = passwordHash;
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

    public String toString () {
        return "Customer {" +
                "email: '" + email + "'" +
                ", id: " + id +
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
