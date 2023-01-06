package net.cps.entities.hibernate;

import org.jetbrains.annotations.NotNull;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

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
    @NotNull
    @JoinColumn(name = "parking_lot_id", referencedColumnName = "id", nullable = false)
    private ParkingLot parkingLot;

    public Rates() {}

    public Rates(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
        this.hourlyOccasionalParking = DEFAULT_HOURLY_OCCASIONAL_PARKING;
        this.hourlyOnetimeParking = DEFAULT_HOURLY_ONETIME_PARKING;
        this.regularSubscriptionSingleVehicle = DEFAULT_REGULAR_SUBSCRIPTION_SINGLE_VEHICLE;
        this.regularSubscriptionMultipleVehicles = DEFAULT_REGULAR_SUBSCRIPTION_MULTIPLE_VEHICLE;
        this.fullSubscriptionSingleVehicle = DEFAULT_FULL_SUBSCRIPTION_SINGLE_VEHICLE;
    }

    public Long getId() {
        return id;
    }

    public double getHourlyOccasionalParking() {
        return hourlyOccasionalParking;
    }

    public void setHourlyOccasionalParking(double hourlyOccasionalParking) {
        this.hourlyOccasionalParking = hourlyOccasionalParking;
    }

    public double getHourlyOnetimeParking() {
        return hourlyOnetimeParking;
    }

    public void setHourlyOnetimeParking(double hourlyOnetimeParking) {
        this.hourlyOnetimeParking = hourlyOnetimeParking;
    }

    public double getRegularSubscriptionSingleVehicle() {
        return regularSubscriptionSingleVehicle;
    }

    public void setRegularSubscriptionSingleVehicle(double regularSubscriptionSingleVehicle) {
        this.regularSubscriptionSingleVehicle = regularSubscriptionSingleVehicle;
    }

    public double getRegularSubscriptionMultipleVehicles() {
        return regularSubscriptionMultipleVehicles;
    }

    public void setRegularSubscriptionMultipleVehicles(double regularSubscriptionMultipleVehicles) {
        this.regularSubscriptionMultipleVehicles = regularSubscriptionMultipleVehicles;
    }

    public double getFullSubscriptionSingleVehicle() {
        return fullSubscriptionSingleVehicle;
    }

    public void setFullSubscriptionSingleVehicle(double fullSubscriptionSingleVehicle) {
        this.fullSubscriptionSingleVehicle = fullSubscriptionSingleVehicle;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }
}



