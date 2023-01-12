package net.cps.common.entities;

import net.cps.common.utils.ReservationStatus;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;


@Entity
@Table(name = "reservations")
public class Reservation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "vehicle_number", referencedColumnName = "number")
    private Vehicle vehicle;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "parking_lot_id", referencedColumnName = "id")
    private ParkingLot parkingLot;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_email", referencedColumnName = "email")
    private Customer customer;
    @NotNull
    @Column(name = "arrival_time")
    private Calendar arrivalTime;
    @NotNull
    @Column(name = "departure_time")
    private Calendar departureTime;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parking_space_id", unique = true)
    private ParkingSpace parkingSpace;
    
    @NotNull
    @Column(name = "status")
    private ReservationStatus status;
    
    
    //use if the car already entered the parking lot
    public Reservation (@NotNull ParkingLot parkingLot, @NotNull Calendar arrivalDate, @NotNull Calendar departureTime, @NotNull Vehicle vehicle, ParkingSpace parkingSpace) {
        this.parkingLot = parkingLot;
        this.arrivalTime = arrivalDate;
        this.departureTime = departureTime;
        this.vehicle = vehicle;
        this.parkingSpace = parkingSpace;
    }
    
    public Reservation () {
    
    }
    
    public Integer getId () {
        return id;
    }
    
    public void setId (Integer id) {
        this.id = id;
    }
    
    public Vehicle getVehicle () {
        return vehicle;
    }
    
    public void setVehicle (Vehicle vehicle) {
        this.vehicle = vehicle;
    }
    
    public ParkingLot getParkingLot () {
        return parkingLot;
    }
    
    public void setParkingLot (ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }
    
    public Customer getCustomer () {
        return customer;
    }
    
    public void setCustomer (Customer customer) {
        this.customer = customer;
    }
    
    public Calendar getArrivalTime () {
        return arrivalTime;
    }
    
    public void setArrivalTime (Calendar arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    
    public Calendar getDepartureTime () {
        return departureTime;
    }
    
    public void setDepartureTime (Calendar departureTime) {
        this.departureTime = departureTime;
    }
    
    public ParkingSpace getParkingSpace () {
        return parkingSpace;
    }
    
    public ReservationStatus getStatus () {
        return status;
    }
    
    public void setStatus (ReservationStatus status) {
        this.status = status;
    }
    
    public void setParkingSpace (ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
    }
    
    public Reservation (@NotNull ParkingLot parkingLot, @NotNull Calendar arrivalDate, @NotNull Calendar departureTime, @NotNull Vehicle vehicle) {
        this.parkingLot = parkingLot;
        this.arrivalTime = arrivalDate;
        this.departureTime = departureTime;
        this.vehicle = vehicle;
    }
    
}
