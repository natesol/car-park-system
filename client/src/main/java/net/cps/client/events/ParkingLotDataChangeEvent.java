package net.cps.client.events;

import jdk.jfr.Event;

public class ParkingLotDataChangeEvent extends Event {
    private Object data;

    public Object getData() {
        return data;
    }

    public ParkingLotDataChangeEvent(Object data) {
        this.data = data;
    }
}
