package net.cps.common.entities;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "vehicles")
public class Vehicle implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_email", referencedColumnName = "email")
    private Customer customer;
    @NotNull
    @Column(name = "number", columnDefinition = "CHAR(8) NOT NULL UNIQUE")
    private String number;
    @ManyToMany
    private List<Subscription> subscriptions;
    @OneToMany(mappedBy = "vehicle")
    private List<Reservation> reservations;
    @OneToOne(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private ParkingSpace parkingSpace;
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public Vehicle () {}
    
    public Vehicle (@NotNull String number, @NotNull Customer customer) {
        this.number = number;
        this.customer = customer;
        this.subscriptions = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.parkingSpace = null;
    }
    
    public Vehicle (@NotNull String number, @NotNull Customer customer, List<Subscription> subscriptions) {
        this.number = number;
        this.customer = customer;
        this.subscriptions = subscriptions;
        this.reservations = new ArrayList<>();
        this.parkingSpace = null;
    }
    
    public Vehicle (@NotNull String number, @NotNull Customer customer, List<Subscription> subscriptions, List<Reservation> reservations, ParkingSpace parkingSpace) {
        this.number = number;
        this.customer = customer;
        this.subscriptions = subscriptions;
        this.reservations = reservations;
        this.parkingSpace = parkingSpace;
    }
    
    
    /* ----- Getters and Setters ------------------------------------ */
    
    public Integer getId () {
        return id;
    }
    
    public void setId (Integer id) {
        this.id = id;
    }
    
    public @NotNull String getNumber () {
        return number;
    }
    
    public void setNumber (@NotNull String number) {
        this.number = number;
    }
    
    public @NotNull Customer getCustomer () {
        return customer;
    }
    
    public void setCustomer (@NotNull Customer customer) {
        this.customer = customer;
    }
    
    public List<Subscription> getSubscriptions () {
        return subscriptions;
    }
    
    public void setSubscriptions (List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }
    
    public List<Reservation> getReservations () {
        return reservations;
    }
    
    public void setReservations (List<Reservation> reservations) {
        this.reservations = reservations;
    }
    
    public ParkingSpace getParkingSpace () {
        return parkingSpace;
    }
    
    public void setParkingSpace (ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    @Override
    public String toString () {
        return "Vehicle {" +
                "id: " + id +
                ", licensePlate: " + number +
                ", customer: " + customer +
                ", subscriptions: " + (subscriptions != null ? subscriptions : "null") +
                ", reservations: " + (reservations != null ? reservations : "null") +
                ", parkingSpace: " + (parkingSpace != null ? parkingSpace : "null") +
                '}';
    }
    
}