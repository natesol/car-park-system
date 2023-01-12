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
    private ParkingLot parkingLot;
    @NotNull
    @Column(name = "floor_num")
    private Integer floorNum;
    @NotNull
    @Column(name = "floor_row")
    private Integer floorRow;
    @NotNull
    @Column(name = "floor_col")
    private Integer floorCol;
    @NotNull
    @Column(name = "state")
    private ParkingSpaceState state;
    @OneToOne(mappedBy = "parkingSpace", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Vehicle vehicle;
    @OneToOne(mappedBy = "parkingSpace", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Reservation reservation;
    
    public ParkingSpace (int i, int j, int k, @NotNull ParkingLot parkingLot) {
        this.floorNum = i;
        this.floorRow = j;
        this.floorCol = k;
        this.parkingLot = parkingLot;
        this.state = ParkingSpaceState.AVAILABLE;
    }
    
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
    
    public Integer getFloorNum () {
        return floorNum;
    }
    
    public void setFloorNum (Integer floorNum) {
        this.floorNum = floorNum;
    }
    
    public Integer getFloorRow () {
        return floorRow;
    }
    
    public void setFloorRow (Integer floorRow) {
        this.floorRow = floorRow;
    }
    
    public Integer getFloorCol () {
        return floorCol;
    }
    
    public void setFloorCol (Integer floorCol) {
        this.floorCol = floorCol;
    }
    

    
    public Reservation getReservation () {return reservation;}
    
    public void setReservation (Reservation reservation) {this.reservation = reservation;}
    
    public ParkingSpace(ParkingSpaceState state) {
        this.state = state;
        this.vehicle = null;
        parkingLot = null;
    }
    public ParkingSpace(Vehicle vehicle) {
        this.state = ParkingSpaceState.OCCUPIED;
        this.vehicle = vehicle;
        parkingLot = null;
    }
    
    public ParkingSpace () {parkingLot = null;}
    
    public Vehicle getVehicle () {
        return vehicle;
    }
    
    public void setVehicle (Vehicle vehicle) {
        this.vehicle = vehicle;
    }
    
    public ParkingSpaceState getState () {
        return state;
    }
    public void setState (ParkingSpaceState state) {
        this.state = state;
    }
    
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
}
