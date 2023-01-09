package net.cps.common.entities;

import net.cps.common.utils.ParkingSpaceState;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "parking_spaces")
public class ParkingSpace implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="parking_lot", updatable = false, nullable = false)
    private ParkingLot parkingLot;

    @Column(name = "floor_row", nullable = false)
    private Integer floorRow;
    
    @Column(name = "floor_num", nullable = false)
    private Integer numOfFloors;
    
    @Column(name = "floor_col", nullable = false)
    private Integer floorCols;
    
    @Column(name = "state", nullable = false)
    private ParkingSpaceState state;
    
    @Column(name = "vehicle_id", nullable = false)
    private String vehicleId;

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
