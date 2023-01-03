package net.cps.entities.hibernate;

import java.util.Date;

public class Subscription {
    private Long customerId;
    private Long carId;
    private String parkingLot;
    private Vehicle vehicle;
    private String subscriptionType;
    private Date startDate;

    public Subscription(Long customerId, Long carId, String parkingLot, Vehicle vehicle, String subscriptionType) {
        this.customerId = customerId;
        this.carId = carId;
        this.parkingLot = parkingLot;
        this.vehicle = vehicle;
        this.subscriptionType = subscriptionType;
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

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

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
}
