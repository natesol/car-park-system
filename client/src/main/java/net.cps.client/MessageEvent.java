package net.cps.client;

import net.cps.entities.Message;

public class MessageEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public MessageEvent(Message message) {
        this.message = message;
    }
}
