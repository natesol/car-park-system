package net.cps.client.events;

import jdk.jfr.Event;
import net.cps.common.messages.ResponseMessage;
import org.jetbrains.annotations.NotNull;

public class UserLogoutEvent extends Event {
    private final ResponseMessage response;
    
    public UserLogoutEvent (@NotNull ResponseMessage response) {
        this.response = response;
    }
    
    public ResponseMessage getResponse () {
        return response;
    }
}
