package net.cps.client.events;

import jdk.jfr.Event;

public class ParkingLotDataTmpEvent extends Event {
    private Object data;

    public Object getData() {
        return data;
    }

    public ParkingLotDataTmpEvent(Object data) {
        this.data = data;
    }
}
