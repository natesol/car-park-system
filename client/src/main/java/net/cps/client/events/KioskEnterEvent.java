package net.cps.client.events;

import jdk.jfr.Event;
import net.cps.common.entities.ParkingLot;
import org.jetbrains.annotations.NotNull;

public class KioskEnterEvent extends Event {
    private final ParkingLot parkingLot;
    
    public KioskEnterEvent (@NotNull ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }
    
    public ParkingLot getParkingLot () {
        return parkingLot;
    }
}
