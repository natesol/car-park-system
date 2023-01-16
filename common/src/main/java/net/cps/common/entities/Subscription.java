package net.cps.common.entities;

import net.cps.common.utils.SubscriptionState;
import net.cps.common.utils.SubscriptionType;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "subscriptions")
public class Subscription implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_email", referencedColumnName = "email", nullable = false)
    private Customer customer;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parking_lot_id", referencedColumnName = "id", nullable = true)
    private ParkingLot parkingLot;
    @NotNull
    @Column(name = "created_at", nullable = false)
    private Calendar createdAt;
    @NotNull
    @Column(name = "expires_at", nullable = false)
    private Calendar expiresAt;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SubscriptionType type;
    @NotNull
    @ManyToMany(mappedBy = "subscriptions")
    private List<Vehicle> vehicles;
    @NotNull
    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private SubscriptionState state;
    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public Subscription () {}
    
    public Subscription (@NotNull Customer customer, ParkingLot parkingLot, @NotNull SubscriptionType type, @NotNull List<Vehicle> vehicles, @NotNull LocalTime departureTime) {
        this.customer = customer;
        this.parkingLot = parkingLot;
        this.createdAt = Calendar.getInstance();
        createdAt.clear(Calendar.HOUR_OF_DAY);
        this.expiresAt = Calendar.getInstance();
        this.expiresAt.add(Calendar.DATE, 28);
        this.type = type;
        this.vehicles = vehicles;
        this.departureTime = departureTime;
        this.state = SubscriptionState.ACTIVE;
        this.price = 0.0;
    }
    
    public Subscription (@NotNull Customer customer, ParkingLot parkingLot, @NotNull Calendar startAt, @NotNull SubscriptionType type, @NotNull List<Vehicle> vehicles, @NotNull LocalTime departureTime) {
        this.customer = customer;
        this.parkingLot = parkingLot;
        this.createdAt = startAt;
        this.expiresAt = (Calendar) startAt.clone();
        this.expiresAt.add(Calendar.DATE, 28);
        this.type = type;
        this.vehicles = vehicles;
        this.departureTime = departureTime;
        this.state = SubscriptionState.ACTIVE;
        this.price = 0.0;
    }
    
    
    /* ----- Getters and Setters ------------------------------------ */
    
    public Integer getId () {
        return id;
    }
    
    public void setId (Integer id) {
        this.id = id;
    }
    
    public @NotNull Customer getCustomer () {
        return customer;
    }
    
    public void setCustomer (@NotNull Customer customer) {
        this.customer = customer;
    }
    
    public ParkingLot getParkingLot () {
        return parkingLot;
    }
    
    public void setParkingLot (ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }
    
    public Integer getParkingLotId () {
        return parkingLot.getId();
    }
    
    public @NotNull Calendar getCreatedAt () {
        return createdAt;
    }
    
    public void setCreatedAt (@NotNull Calendar createdAt) {
        this.createdAt = createdAt;
    }
    
    public @NotNull Calendar getExpiresAt () {
        return expiresAt;
    }
    
    public void setExpiresAt (@NotNull Calendar expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public @NotNull SubscriptionType getType () {
        return type;
    }
    
    public void setType (@NotNull SubscriptionType type) {
        this.type = type;
    }
    
    public @NotNull List<Vehicle> getVehicles () {
        return vehicles;
    }
    
    public void setVehicles (@NotNull List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
    
    public @NotNull SubscriptionState getState () {
        return state;
    }
    
    public void setState (@NotNull SubscriptionState state) {
        this.state = state;
    }
    
    public @NotNull Double getPrice () {
        return price;
    }
    
    public void setPrice (@NotNull Double price) {
        this.price = price;
    }
    
    public @NotNull LocalTime getDepartureTime () {
        return departureTime;
    }
    
    public void setDepartureTime (@NotNull LocalTime departureTime) {
        this.departureTime = departureTime;
    }
}
