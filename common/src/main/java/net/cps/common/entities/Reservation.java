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
    @JoinColumn(name = "parking_lot_id", referencedColumnName = "id")
    private ParkingLot parkingLot;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_email", referencedColumnName = "email")
    private Customer customer;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "vehicle_number", referencedColumnName = "number")
    private Vehicle vehicle;
    @NotNull
    @Column(name = "arrival_time")
    private Calendar arrivalTime;
    @NotNull
    @Column(name = "departure_time")
    private Calendar departureTime;
    @Column(name = "entry_time")
    private Calendar entryTime;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus status;
    @NotNull
    @Column(name = "payed")
    private Double payed;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parking_space_id", unique = true)
    private ParkingSpace parkingSpace;
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public Reservation () {}
    
    public Reservation (@NotNull ParkingLot parkingLot, @NotNull Customer customer, @NotNull Vehicle vehicle, @NotNull Calendar arrivalTime, @NotNull Calendar departureTime) {
        this.parkingLot = parkingLot;
        this.customer = customer;
        this.vehicle = vehicle;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.status = ReservationStatus.PENDING;
        this.payed = 0.0;
    }
    
    public Reservation (@NotNull ParkingLot parkingLot, @NotNull Customer customer, @NotNull Vehicle vehicle, @NotNull Calendar arrivalTime, @NotNull Calendar departureTime, @NotNull ReservationStatus status) {
        this.parkingLot = parkingLot;
        this.customer = customer;
        this.vehicle = vehicle;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.status = status;
        this.payed = 0.0;
    }
    
    public Reservation (@NotNull ParkingLot parkingLot, @NotNull Customer customer, @NotNull Vehicle vehicle, @NotNull Calendar arrivalTime, @NotNull Calendar departureTime, @NotNull ReservationStatus status, @NotNull Double payed) {
        this.parkingLot = parkingLot;
        this.customer = customer;
        this.vehicle = vehicle;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.status = status;
        this.payed = payed;
    }
    
    
    /* ----- Getters & Setters -------------------------------------- */
    
    public Integer getId () {
        return id;
    }
    
    public void setId (Integer id) {
        this.id = id;
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
    
    public Vehicle getVehicle () {
        return vehicle;
    }
    
    public void setVehicle (Vehicle vehicle) {
        this.vehicle = vehicle;
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
    
    public Calendar getEntryTime () {
        return entryTime;
    }
    
    public void setEntryTime (Calendar entryTime) {
        this.entryTime = entryTime;
    }
    
    public @NotNull ReservationStatus getStatus () {
        return status;
    }
    
    public void setStatus (@NotNull ReservationStatus status) {
        this.status = status;
    }
    
    public @NotNull Double getPayed () {
        return payed;
    }
    
    public void setPayed (@NotNull Double payed) {
        this.payed = payed;
    }
    
    public ParkingSpace getParkingSpace () {
        return parkingSpace;
    }
    
    public void setParkingSpace (ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    @Override
    public String toString () {
        return "Reservation {" +
                "id: " + id +
                ", parkingLot: " + parkingLot +
                ", customer: " + customer +
                ", vehicle: " + vehicle +
                ", arrivalTime: " + arrivalTime.getTime() +
                ", departureTime: " + departureTime.getTime() +
                ", entryTime: " + entryTime.getTime() +
                ", status: " + status +
                ", payed: " + payed +
                ", parkingSpace: " + parkingSpace +
                '}';
    }
    
}
