package net.cps.common.messages;

import net.cps.common.utils.MessageType;

import java.io.Serializable;
import java.time.LocalDateTime;


/*
 * The abstract Message - the base class for all messages sent between the client and the server (and vice versa).
 */
public abstract class Message implements Serializable {
    private Integer id;
    private LocalDateTime timeStamp;
    private MessageType messageType;
    private String body;
    private Object data;
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public Message (int id) {
        this.id = id;
        this.timeStamp = LocalDateTime.now();
        this.messageType = MessageType.DEFAULT;
        this.body = null;
        this.data = null;
    }
    
    public Message (int id, String body) {
        this.id = id;
        this.timeStamp = LocalDateTime.now();
        this.messageType = MessageType.DEFAULT;
        this.body = body;
        this.data = null;
    }
    
    public Message (int id, String body, Object data) {
        this.id = id;
        this.timeStamp = LocalDateTime.now();
        this.messageType = MessageType.DEFAULT;
        this.body = body;
        this.data = data;
    }
    
    public Message (int id, String body, Object data, MessageType type) {
        this.id = id;
        this.timeStamp = LocalDateTime.now();
        this.messageType = type;
        this.body = body;
        this.data = data;
    }
    
    
    /* ----- Getters and Setters ------------------------------------ */
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public LocalDateTime getTimeStamp () {
        return timeStamp;
    }
    
    private void setTimeStamp (LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    public MessageType getMessageType () {
        return messageType;
    }
    
    private void setMessageType (MessageType messageType) {
        this.messageType = messageType;
    }
    
    public String getBody () {
        return body;
    }
    
    public void setBody (String body) {
        this.body = body;
    }
    
    public Object getData () {
        return data;
    }
    
    public void setData (Object data) {
        this.data = data;
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    @Override
    public String toString () {
        return "Message {" +
                "id: " + id +
                ", timeStamp: " + timeStamp +
                ", type: " + messageType +
                ", message: '" + body + '\'' +
                ", data: " + data +
                '}';
    }
}
