package net.cps.common.entities;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "parking_lot_id", referencedColumnName = "id")
    private ParkingLot parkingLot;
    @Column(name = "created_at", nullable = false)
    private Calendar createdAt;
    @Column(name = "expires_at", nullable = false)
    private Calendar expiresAt;
    @Column(name = "type", nullable = false)
    private String type;
    @ManyToMany
    //@JoinTable(name = "subscription_vehicles", joinColumns = @JoinColumn(name = "subscription_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "vehicle_id", referencedColumnName = "id"))
    private List<Vehicle> vehicle;
    @Column(name = "status", nullable = false)
    private String status;
    @Column(name = "price", nullable = false)
    private Double price;
    @Column(name = "departure_time", nullable = false)
    private Calendar departureTime;
    
    
    public Subscription () {}
    
    public Subscription (Customer customer, ParkingLot parkingLot, Calendar expiresAt, String type, List<Vehicle> vehicle, String status, Double price, Calendar departureTime) {
        this.customer = customer;
        this.parkingLot = parkingLot;
        this.createdAt = Calendar.getInstance();
        this.expiresAt = expiresAt;
        this.type = type;
        this.vehicle = vehicle;
        this.status = status;
        this.price = price;
        this.departureTime = departureTime;
    }
    
    public Integer getId () {
        return id;
    }
    
    public void setId (Integer id) {
        this.id = id;
    }
    
    public Customer getCustomer () {
        return customer;
    }
    
    public void setCustomer (Customer customer) {
        this.customer = customer;
    }
    
    public ParkingLot getParkingLot () {
        return parkingLot;
    }
    
    public void setParkingLot (ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }
    
    public Calendar getCreatedAt () {
        return createdAt;
    }
    
    public void setCreatedAt (Calendar createdAt) {
        this.createdAt = createdAt;
    }
    
    public Calendar getExpiresAt () {
        return expiresAt;
    }
    
    public void setExpiresAt (Calendar expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public String getType () {
        return type;
    }
    
    public void setType (String type) {
        this.type = type;
    }
    
    public List<Vehicle> getVehicle () {
        return vehicle;
    }
    
    public void setVehicle (List<Vehicle> vehicle) {
        this.vehicle = vehicle;
    }
    
    public String getStatus () {
        return status;
    }
    
    public void setStatus (String status) {
        this.status = status;
    }
    
    public Double getPrice () {
        return price;
    }
    
    public void setPrice (Double price) {
        this.price = price;
    }
    
    public Calendar getDepartureTime () {
        return departureTime;
    }
    
    public void setDepartureTime (Calendar departureTime) {
        this.departureTime = departureTime;
    }
}
