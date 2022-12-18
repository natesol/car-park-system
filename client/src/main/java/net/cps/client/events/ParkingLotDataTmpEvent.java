package net.cps.client.events;

import jdk.jfr.Event;
import net.cps.entities.Message;

public class ParkingLotDataTmpEvent extends Event {
    private Object message;

    public Object getMessage() {
        return message;
    }

    public ParkingLotDataTmpEvent(Object message) {
        this.message = message;
    }
}
