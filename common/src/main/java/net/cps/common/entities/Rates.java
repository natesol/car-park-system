package net.cps.common.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rates")
public class Rates implements Serializable {
    @Id
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private ParkingLot parkingLot;
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    private Integer parkingLotId;
    @Column(name = "hourly_occasional_parking", nullable = false)
    private Double hourlyOccasionalParking; // ILS per Hour.
    @Column(name = "hourly_onetime_parking", nullable = false)
    private Double hourlyOnetimeParking; // ILS per Hour.
    @Column(name = "regular_subscription_single_vehicle", nullable = false)
    private Double regularSubscriptionSingleVehicle; // Number of Hours of - `hourlyOnetimeParking`.
    @Column(name = "regular_subscription_multiple_vehicles", nullable = false)
    private Double regularSubscriptionMultipleVehicles; // Number of Hours of - `hourlyOnetimeParking`, times the number of vehicles.
    @Column(name = "full_subscription_single_vehicle", nullable = false)
    private Double fullSubscriptionSingleVehicle; // Number of Hours of - `hourlyOnetimeParking`.
    
    
    public static final double DEFAULT_HOURLY_OCCASIONAL_PARKING = 8;
    public static final double DEFAULT_HOURLY_ONETIME_PARKING = 7;
    public static final double DEFAULT_REGULAR_SUBSCRIPTION_SINGLE_VEHICLE = 60;
    public static final double DEFAULT_REGULAR_SUBSCRIPTION_MULTIPLE_VEHICLE = 54;
    public static final double DEFAULT_FULL_SUBSCRIPTION_SINGLE_VEHICLE = 72;
    public static final double CANCELLATION_FEE_MORE_THAN_3_HOURS = 0.9;
    public static final double CANCELLATION_FEE_LESS_THAN_3_HOURS = 0.5;
    public static final double CANCELLATION_FEE_LESS_THAN_1_HOURS = 0.1;
    

    /* ----- Constructors ------------------------------------------- */
    
    public Rates () {}
    
    public Rates (ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
        this.hourlyOccasionalParking = DEFAULT_HOURLY_OCCASIONAL_PARKING;
        this.hourlyOnetimeParking = DEFAULT_HOURLY_ONETIME_PARKING;
        this.regularSubscriptionSingleVehicle = DEFAULT_REGULAR_SUBSCRIPTION_SINGLE_VEHICLE;
        this.regularSubscriptionMultipleVehicles = DEFAULT_REGULAR_SUBSCRIPTION_MULTIPLE_VEHICLE;
        this.fullSubscriptionSingleVehicle = DEFAULT_FULL_SUBSCRIPTION_SINGLE_VEHICLE;
    }
    
    
    /* ----- Getters & Setters -------------------------------------- */
    
    public ParkingLot getParkingLot () {
        return this.parkingLot;
    }
    
    public Integer getId () {
        return this.parkingLotId;
    }
    
    public Integer getParkingLotId () {
        return parkingLotId;
    }
    
    public double getHourlyOccasionalParking () {
        return this.hourlyOccasionalParking;
    }
    
    public double getHourlyOnetimeParking () {
        return this.hourlyOnetimeParking;
    }
    
    public double getRegularSubscriptionSingleVehicle () {
        return this.regularSubscriptionSingleVehicle;
    }
    
    public double getRegularSubscriptionMultipleVehicles () {
        return this.regularSubscriptionMultipleVehicles;
    }
    
    public double getFullSubscriptionSingleVehicle () {
        return this.fullSubscriptionSingleVehicle;
    }
    
    public void setHourlyOccasionalParking (double hourlyOccasionalParking) {
        this.hourlyOccasionalParking = hourlyOccasionalParking;
    }
    
    public void setHourlyOnetimeParking (double hourlyOnetimeParking) {
        this.hourlyOnetimeParking = hourlyOnetimeParking;
    }
    
    public void setRegularSubscriptionSingleVehicle (double regularSubscriptionSingleVehicle) {
        this.regularSubscriptionSingleVehicle = regularSubscriptionSingleVehicle;
    }
    
    public void setRegularSubscriptionMultipleVehicles (double regularSubscriptionMultipleVehicles) {
        this.regularSubscriptionMultipleVehicles = regularSubscriptionMultipleVehicles;
    }
    
    public void setFullSubscriptionSingleVehicle (double fullSubscriptionSingleVehicle) {
        this.fullSubscriptionSingleVehicle = fullSubscriptionSingleVehicle;
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    @Override
    public String toString () {
        return "Rates {" +
                "parkingLotId: " + this.parkingLotId +
                ", hourlyOccasionalParking: " + hourlyOccasionalParking +
                ", hourlyOnetimeParking: " + hourlyOnetimeParking +
                ", regularSubscriptionSingleVehicle: " + regularSubscriptionSingleVehicle +
                ", regularSubscriptionMultipleVehicles: " + regularSubscriptionMultipleVehicles +
                ", fullSubscriptionSingleVehicle: " + fullSubscriptionSingleVehicle +
                "}";
    }
}



