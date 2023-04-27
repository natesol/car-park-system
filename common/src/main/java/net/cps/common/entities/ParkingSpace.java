package net.cps.common.entities;

import net.cps.common.utils.ParkingSpaceState;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;

@Entity
@Table(name = "parking_spaces")
public class ParkingSpace implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;
    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "parking_lot_id", referencedColumnName = "id")
    private ParkingLot parkingLot;
    @NotNull
    @Column(name = "floor")
    private Integer floor;
    @NotNull
    @Column(name = "row_num")
    private Integer row;
    @NotNull
    @Column(name = "col_num")
    private Integer col;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private ParkingSpaceState state;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicle_number", referencedColumnName = "number")
    private Vehicle vehicle;
    @OneToOne(mappedBy = "parkingSpace", cascade = CascadeType.ALL)
    private Reservation reservation;
    
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public ParkingSpace () {}
    
    public ParkingSpace (@NotNull ParkingLot parkingLot, @NotNull Integer floor, @NotNull Integer row, @NotNull Integer col) {
        this.parkingLot = parkingLot;
        this.floor = floor;
        this.row = row;
        this.col = col;
        this.state = ParkingSpaceState.AVAILABLE;
        this.vehicle = null;
        this.reservation = null;
    }
    
    public ParkingSpace (@NotNull ParkingLot parkingLot, @NotNull Integer floor, @NotNull Integer row, @NotNull Integer col, @NotNull ParkingSpaceState state) {
        this.parkingLot = parkingLot;
        this.floor = floor;
        this.row = row;
        this.col = col;
        this.state = state;
        this.vehicle = null;
        this.reservation = null;
    }
    
    public ParkingSpace (@NotNull ParkingLot parkingLot, @NotNull Integer floor, @NotNull Integer row, @NotNull Integer col, @NotNull Vehicle vehicle) {
        this.parkingLot = parkingLot;
        this.floor = floor;
        this.row = row;
        this.col = col;
        this.state = ParkingSpaceState.OCCUPIED;
        this.vehicle = vehicle;
        this.reservation = null;
    }
    
    public ParkingSpace (@NotNull ParkingLot parkingLot, @NotNull Integer floor, @NotNull Integer row, @NotNull Integer col, @NotNull Reservation reservation) {
        this.parkingLot = parkingLot;
        this.floor = floor;
        this.row = row;
        this.col = col;
        this.state = ParkingSpaceState.RESERVED;
        this.vehicle = null;
        this.reservation = reservation;
    }
    
    
    /* ----- Getters & Setters -------------------------------------- */
    
    public Integer getId () {
        return id;
    }
    
    public void setId (Integer id) {
        this.id = id;
    }
    
    public @NotNull ParkingLot getParkingLot () {
        return parkingLot;
    }
    
    public void setParkingLot (@NotNull ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }
    
    public @NotNull Integer getFloor () {
        return floor;
    }
    
    public void setFloor (@NotNull Integer floor) {
        this.floor = floor;
    }
    
    public @NotNull Integer getRow () {
        return row;
    }
    
    public void setRow (@NotNull Integer row) {
        this.row = row;
    }
    
    public @NotNull Integer getCol () {
        return col;
    }
    
    public void setCol (@NotNull Integer col) {
        this.col = col;
    }
    
    public @NotNull ParkingSpaceState getState () {
        return state;
    }
    
    public void setState (@NotNull ParkingSpaceState state) {
        this.state = state;
    }
    
    public Vehicle getVehicle () {
        return vehicle;
    }
    
    public void setVehicle (Vehicle vehicle) {
        this.vehicle = vehicle;
    }
    
    public Reservation getReservation () {return reservation;}
    
    public void setReservation (Reservation reservation) {this.reservation = reservation;}
    
    public Calendar getArrivalTime () {
        return reservation.getArrivalTime();
    }
    
    public void setArrivalTime (Calendar arrivalTime) {
        reservation.setArrivalTime(arrivalTime);
    }
    
    public Calendar getDepartureTime () {
        return reservation.getDepartureTime();
    }
    
    public void setDepartureTime (Calendar departureTime) {
        reservation.setDepartureTime(departureTime);
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    @Override
    public String toString () {
        return "ParkingSpace {" +
                "id: " + id +
                ", parkingLot: " + parkingLot +
                ", floor: " + floor +
                ", row: " + row +
                ", col: " + col +
                ", state: " + state +
                ", vehicle: " + vehicle +
                ", reservation: " + reservation +
                '}';
    }
}
