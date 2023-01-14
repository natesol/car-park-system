package net.cps.common.entities;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
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
    @Column(name = "license_plate", columnDefinition = "CHAR(8) NOT NULL UNIQUE")
    private String licensePlate;
    @ManyToMany
    private List<Subscription> subscriptions;
    @OneToMany(mappedBy = "vehicle")
    private List<Reservation> reservations;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parking_space_id", referencedColumnName = "id")
    private ParkingSpace parkingSpace;
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public Vehicle () {}
    
    public Vehicle (@NotNull String licensePlate, @NotNull Customer customer) {
        this.licensePlate = licensePlate;
        this.customer = customer;
        this.subscriptions = null;
        this.reservations = null;
        this.parkingSpace = null;
    }
    
    public Vehicle (@NotNull String licensePlate, @NotNull Customer customer, List<Subscription> subscriptions, List<Reservation> reservations, ParkingSpace parkingSpace) {
        this.licensePlate = licensePlate;
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
    
    public @NotNull String getLicensePlate () {
        return licensePlate;
    }
    
    public void setLicensePlate (@NotNull String licensePlate) {
        this.licensePlate = licensePlate;
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
   
}