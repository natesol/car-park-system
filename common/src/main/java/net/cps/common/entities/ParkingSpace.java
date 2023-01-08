package net.cps.common.entities;

import net.cps.common.utils.ParkingSpaceCondition;

// maybe not nessecarry, can be included as a field at ParkingLot
public class ParkingSpace  {
    private ParkingSpaceCondition condition;
    
    private String vehicleId;

    public ParkingSpace(ParkingSpaceCondition condition) {
        this.condition = condition;
        this.vehicleId = null;
    }
    public ParkingSpace(String vehicleId) {
        this.condition = ParkingSpaceCondition.OCCUPIED;
        this.vehicleId = vehicleId;
    }

    public ParkingSpaceCondition getCondition() {
        return condition;
    }
    public void setCondition(ParkingSpaceCondition condition) {
        this.condition = condition;
    }
    
    public String getVehicleId() {
        return vehicleId;
    }
    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }
}
