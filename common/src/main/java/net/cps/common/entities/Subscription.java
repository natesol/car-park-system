package net.cps.common.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    
    private Long customerId;
    private Long carId;
    private String parkingLot;
    //private Vehicle vehicle;
    private String subscriptionType;
    private Date startDate;
    //@Id
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Subscription(Long customerId, Long carId, String parkingLot, Vehicle vehicle, String subscriptionType, Customer customer) {
        this.customerId = customerId;
        this.carId = carId;
        this.parkingLot = parkingLot;
        //this.vehicle = vehicle;
        this.subscriptionType = subscriptionType;
        this.customer = customer;
    }
    
    public Subscription () {
    
    }
    
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(String parkingLot) {
        this.parkingLot = parkingLot;
    }

    //public Vehicle getVehicle() {
    //    return vehicle;
    //}
    //
    //public void setVehicle(Vehicle vehicle) {
    //    this.vehicle = vehicle;
    //}

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getAndDate() {
        return andDate;
    }

    public void setAndDate(Date andDate) {
        this.andDate = andDate;
    }

    private Date andDate;
    
    public Integer getId () {return id;}
    public void setId (Integer id) {this.id = id;}
}
