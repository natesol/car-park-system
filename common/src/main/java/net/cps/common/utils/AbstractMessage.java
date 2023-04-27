package net.cps.common.utils;

import java.io.Serializable;
import java.time.LocalDateTime;


/*
 * The base class for all messages sent between the client and the server (and vice versa).
 */
public abstract class AbstractMessage implements Serializable {
    private final Integer id;
    private final LocalDateTime timeStamp;
    private final MessageType type;
    private String header;
    private String body;
    private String message;
    private Object data;
    
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public AbstractMessage (Integer id, MessageType type, String header) {
        this.id = id;
        this.timeStamp = LocalDateTime.now();
        this.type = type;
        this.header = header;
        this.body = null;
        this.message = null;
        this.data = null;
    }
    
    public AbstractMessage (Integer id, MessageType type, String header, String body) {
        this.id = id;
        this.timeStamp = LocalDateTime.now();
        this.type = type;
        this.header = header;
        this.body = body;
        this.message = null;
        this.data = null;
    }
    
    public AbstractMessage (Integer id, MessageType type, String header, String body, String message) {
        this.id = id;
        this.timeStamp = LocalDateTime.now();
        this.type = type;
        this.header = header;
        this.body = body;
        this.message = message;
        this.data = null;
    }
    
    public AbstractMessage (Integer id, MessageType type, String header, String body, Object data) {
        this.id = id;
        this.timeStamp = LocalDateTime.now();
        this.type = type;
        this.header = header;
        this.body = body;
        this.message = null;
        this.data = data;
    }
    
    public AbstractMessage (Integer id, MessageType type, String header, String body, String message, Object data) {
        this.id = id;
        this.timeStamp = LocalDateTime.now();
        this.type = type;
        this.header = header;
        this.body = body;
        this.message = message;
        this.data = data;
    }
    
    
    /* ----- Getters and Setters ------------------------------------ */
    
    public Integer getId () {
        return id;
    }
    
    public LocalDateTime getTimeStamp () {
        return timeStamp;
    }
    
    public MessageType getMessageType () {
        return type;
    }
    
    public String getHeader () {
        return header;
    }
    
    public void setHeader (String header) {
        this.header = header;
    }
    
    public String getBody () {
        return body;
    }
    
    public void setBody (String body) {
        this.body = body;
    }
    
    public String getMessage () {
        return message;
    }
    
    public void setMessage (String message) {
        this.message = message;
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
                ", type: " + type +
                ", header: '" + header + "'" +
                ", body: '" + body + "'" +
                ", message: '" + message + "'" +
                ", data: " + data +
                '}';
    }
}
