package net.cps.client.events;

import jdk.jfr.Event;

public class RatesDataChangeEvent extends Event {
    private Object data;

    public Object getData() {
        return data;
    }
    
    public RatesDataChangeEvent(Object data) {
        this.data = data;
    }
}
