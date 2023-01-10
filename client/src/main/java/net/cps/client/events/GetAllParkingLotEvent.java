package net.cps.client.events;

import jdk.jfr.Event;
import net.cps.common.messages.ResponseMessage;

public class GetAllParkingLotEvent extends Event {
    private ResponseMessage response;

    public GetAllParkingLotEvent (ResponseMessage response) {
        this.response = response;
    }
    
    public ResponseMessage getResponse() {
        return response;
    }
    
    public void setResponse(ResponseMessage response) {
        this.response = response;
    }
}
