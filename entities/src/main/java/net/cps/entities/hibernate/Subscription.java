package net.cps.entities.hibernate;

import java.util.Date;

public class Subscription {
    private String customerId;
    private String carId;
    private String parkingLot;
    private Vehicle vehicle;
    private String subscriptionType;
    private Date startDate;

    public Subscription(String customerId, String carId, String parkingLot, Vehicle vehicle, String subscriptionType) {
        this.customerId = customerId;
        this.carId = carId;
        this.parkingLot = parkingLot;
        this.vehicle = vehicle;
        this.subscriptionType = subscriptionType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
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
