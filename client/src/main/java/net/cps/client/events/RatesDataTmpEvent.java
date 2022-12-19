package net.cps.client.events;

import jdk.jfr.Event;

public class RatesDataTmpEvent extends Event {
    private Object data;

    public Object getData() {
        return data;
    }

    public RatesDataTmpEvent(Object data) {
        this.data = data;
    }
}
