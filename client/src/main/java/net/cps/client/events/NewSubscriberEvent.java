package net.cps.client.events;

import net.cps.entities.Message;

public class NewSubscriberEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public NewSubscriberEvent(Message message) {
        this.message = message;
    }
}
