package net.cps.common.entities;

import net.cps.common.utils.ParkingSpaceState;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "parking_spaces")
public class ParkingSpace implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="parking_lot", updatable = false, nullable = false)
    private ParkingLot parkingLot;
    @NotNull
    @Column(name = "floor_num")
    private Integer floorNum;
    @NotNull
    @Column(name = "floor_row")
    private Integer floorRow;
    @NotNull
    @Column(name = "floor_col")
    private Integer floorCols;
    @NotNull
    @Column(name = "state")
    private ParkingSpaceState state;
    @Column(name = "vehicle_id")
    private String vehicleId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reservation", unique = true)
    private Reservation reservation;
    
    public Reservation getReservation () {return reservation;}
    
    public void setReservation (Reservation reservation) {this.reservation = reservation;}
    //@OneToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "reservation_id")
    //private Reservation reservation;

    
    //public Reservation getReservation_id () {return reservation_id;}
    //
    //public void setReservation_id (Reservation reservation_id) {this.reservation_id = reservation_id;}
    //
    
    public ParkingSpace(ParkingSpaceState condition) {
        this.state = condition;
        this.vehicleId = null;
    }
    public ParkingSpace(String vehicleId) {
        this.state = ParkingSpaceState.OCCUPIED;
        this.vehicleId = vehicleId;
    }
    
    public ParkingSpace () {
    
    }
    
    public ParkingSpaceState getState () {
        return state;
    }
    public void setState (ParkingSpaceState state) {
        this.state = state;
    }
    
    public String getVehicleId() {
        return vehicleId;
    }
    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }
}
