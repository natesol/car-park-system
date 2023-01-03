package net.cps.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    Long id;
    LocalDateTime timeStamp;
    String message;
    Object data;
    /*recieved, expected*/

    public Message(Long id, LocalDateTime timeStamp, String message) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.message = message;
    }

    public Message(Long id, String message) {
        this.id = id;
        this.timeStamp = LocalDateTime.now();
        this.message = message;
        this.data = null;
    }

    public Message(Long id, String message, Object data) {
        this.id = id;
        this.timeStamp = LocalDateTime.now();
        this.message = message;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
