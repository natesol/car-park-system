package net.cps.entities.hibernate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rates")
public class Rates implements Serializable {
    public static final double DEFAULT_HOURLY_OCCASIONAL_PARKING = 8;
    public static final double DEFAULT_HOURLY_ONETIME_PARKING = 7;
    public static final double DEFAULT_REGULAR_SUBSCRIPTION_SINGLE_VEHICLE = 60;
    public static final double DEFAULT_REGULAR_SUBSCRIPTION_MULTIPLE_VEHICLE = 54;
    public static final double DEFAULT_FULL_SUBSCRIPTION_SINGLE_VEHICLE = 72;
    
    @Id
    @Column(name = "parking_lot_id")
    private int id;
    
    // ILS per Hour.
    @Column(name = "hourly_occasional_parking", nullable = false)
    private double hourlyOccasionalParking;
    
    // ILS per Hour.
    @Column(name = "hourly_onetime_parking", nullable = false)
    private double hourlyOnetimeParking;
    
    // Number of Hours of - `hourlyOnetimeParking`.
    @Column(name = "regular_subscription_single_vehicle", nullable = false)
    private double regularSubscriptionSingleVehicle;
    
    // Number of Hours of - `hourlyOnetimeParking`, times the number of vehicles.
    @Column(name = "regular_subscription_multiple_vehicles", nullable = false)
    private double regularSubscriptionMultipleVehicles;
    
    // Number of Hours of - `hourlyOnetimeParking`.
    @Column(name = "full_subscription_single_vehicle", nullable = false)
    private double fullSubscriptionSingleVehicle;
    
    @OneToOne
    @JoinColumn(name = "parking_lot_id", referencedColumnName = "id")
    private ParkingLot parkingLot;
    
    public Rates() {}
    
    public Rates(ParkingLot parkingLot) {
        this.id = parkingLot.getId();
        this.parkingLot = parkingLot;
        this.hourlyOccasionalParking = DEFAULT_HOURLY_OCCASIONAL_PARKING;
        this.hourlyOnetimeParking = DEFAULT_HOURLY_ONETIME_PARKING;
        this.regularSubscriptionSingleVehicle = DEFAULT_REGULAR_SUBSCRIPTION_SINGLE_VEHICLE;
        this.regularSubscriptionMultipleVehicles = DEFAULT_REGULAR_SUBSCRIPTION_MULTIPLE_VEHICLE;
        this.fullSubscriptionSingleVehicle = DEFAULT_FULL_SUBSCRIPTION_SINGLE_VEHICLE;
    }
    
    public Rates(int id) {
        this.id = id;
        this.parkingLot = parkingLot;
        this.hourlyOccasionalParking = DEFAULT_HOURLY_OCCASIONAL_PARKING;
        this.hourlyOnetimeParking = DEFAULT_HOURLY_ONETIME_PARKING;
        this.regularSubscriptionSingleVehicle = DEFAULT_REGULAR_SUBSCRIPTION_SINGLE_VEHICLE;
        this.regularSubscriptionMultipleVehicles = DEFAULT_REGULAR_SUBSCRIPTION_MULTIPLE_VEHICLE;
        this.fullSubscriptionSingleVehicle = DEFAULT_FULL_SUBSCRIPTION_SINGLE_VEHICLE;
    }
    
    public ParkingLot getParkingLot() {
        return this.parkingLot;
    }
    
    public int getId() {
        return this.id;
    }
    
    public double getHourlyOccasionalParking() {
        return this.hourlyOccasionalParking;
    }
    
    public double getHourlyOnetimeParking() {
        return this.hourlyOnetimeParking;
    }
    
    public double getRegularSubscriptionSingleVehicle() {
        return this.regularSubscriptionSingleVehicle;
    }
    
    public double getRegularSubscriptionMultipleVehicles() {
        return this.regularSubscriptionMultipleVehicles;
    }
    
    public double getFullSubscriptionSingleVehicle() {
        return this.fullSubscriptionSingleVehicle;
    }
    
    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }
    
    public void setHourlyOccasionalParking(double hourlyOccasionalParking) {
        this.hourlyOccasionalParking = hourlyOccasionalParking;
    }
    
    public void setHourlyOnetimeParking(double hourlyOnetimeParking) {
        this.hourlyOnetimeParking = hourlyOnetimeParking;
    }
    
    public void setRegularSubscriptionSingleVehicle(double regularSubscriptionSingleVehicle) {
        this.regularSubscriptionSingleVehicle = regularSubscriptionSingleVehicle;
    }
    
    public void setRegularSubscriptionMultipleVehicles(double regularSubscriptionMultipleVehicles) {
        this.regularSubscriptionMultipleVehicles = regularSubscriptionMultipleVehicles;
    }
    
    public void setFullSubscriptionSingleVehicle(double fullSubscriptionSingleVehicle) {
        this.fullSubscriptionSingleVehicle = fullSubscriptionSingleVehicle;
    }
}