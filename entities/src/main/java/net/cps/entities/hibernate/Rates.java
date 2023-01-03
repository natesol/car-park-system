package net.cps.entities.hibernate;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
/*
@Entity
@Table(name = "rates")*/
public class Rates implements Serializable {
    public static final double DEFAULT_HOURLY_OCCASIONAL_PARKING = 8;
    public static final double DEFAULT_HOURLY_ONETIME_PARKING = 7;
    public static final double DEFAULT_REGULAR_SUBSCRIPTION_SINGLE_VEHICLE = 60;
    public static final double DEFAULT_REGULAR_SUBSCRIPTION_MULTIPLE_VEHICLE = 54;
    public static final double DEFAULT_FULL_SUBSCRIPTION_SINGLE_VEHICLE = 72;
    
   /* @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
   */ private Long id;
    
    // ILS per Hour.
  //  @Column(name = "hourly_occasional_parking", nullable = false)
    private double hourlyOccasionalParking;
    
    // ILS per Hour.
  //  @Column(name = "hourly_onetime_parking", nullable = false)
    private double hourlyOnetimeParking;
    
    // Number of Hours of - `hourlyOnetimeParking`.
  //  @Column(name = "regular_subscription_single_vehicle", nullable = false)
    private double regularSubscriptionSingleVehicle;
    
    // Number of Hours of - `hourlyOnetimeParking`, times the number of vehicles.
  //  @Column(name = "regular_subscription_multiple_vehicles", nullable = false)
    private double regularSubscriptionMultipleVehicles;
    
    // Number of Hours of - `hourlyOnetimeParking`.
  //  @Column(name = "full_subscription_single_vehicle", nullable = false)
    private double fullSubscriptionSingleVehicle;
    
  //  @OneToOne
    //@NotNull
    //@JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private ParkingLot parkingLot;
    
    public Rates() {}
    
    public Rates(Long id) {
        this.id = id;
        this.hourlyOccasionalParking = DEFAULT_HOURLY_OCCASIONAL_PARKING;
        this.hourlyOnetimeParking = DEFAULT_HOURLY_ONETIME_PARKING;
        this.regularSubscriptionSingleVehicle = DEFAULT_REGULAR_SUBSCRIPTION_SINGLE_VEHICLE;
        this.regularSubscriptionMultipleVehicles = DEFAULT_REGULAR_SUBSCRIPTION_MULTIPLE_VEHICLE;
        this.fullSubscriptionSingleVehicle = DEFAULT_FULL_SUBSCRIPTION_SINGLE_VEHICLE;
    }
    
    public Rates(ParkingLot parkingLot) {
        this.id = parkingLot.getId();
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
    
    public Long getId() {
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
    
    public void setId(Long id) {
        this.id = id;
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



