package net.cps.client.events;

import jdk.jfr.Event;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.ResponseStatus;

public class UserAuthEvent extends Event {
    private final ResponseMessage response;
    
    public UserAuthEvent (ResponseMessage response) {
        this.response = response;
    }
    
    public ResponseMessage getResponse () {
        return response;
    }
    
    public Object getUser () {
        return response.getData();
    }
    
    public String getHeader () {
        return response.getHeader();
    }
    
    public String getMessage () {
        return response.getMessage();
    }
    
    public Boolean isSuccessful () {
        return response.getStatus() == ResponseStatus.SUCCESS;
    }
    
    public Boolean isFailed () {
        return response.getStatus() == ResponseStatus.UNAUTHORIZED;
    }
}
