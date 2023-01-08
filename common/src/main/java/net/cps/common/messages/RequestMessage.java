package net.cps.common.messages;

import net.cps.common.utils.MessageType;
import net.cps.common.utils.RequestType;

import java.io.Serializable;

/*
 * This class is used to send messages between the client and the server (a client request).
 * An instance of this class is sent to the server when the client has a request.
 */
public class RequestMessage extends Message implements Serializable {
    private RequestType type;
    private String query;
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public RequestMessage (int id, RequestType requestType, String query) {
        super(id, null, null, MessageType.REQUEST);
        this.type = requestType;
        this.query = query;
    }
    
    public RequestMessage (int id, RequestType requestType, String query, Object data) {
        super(id, null, data, MessageType.REQUEST);
        this.type = requestType;
        this.query = query;
    }
    
    public RequestMessage (int id, RequestType requestType, String query, Object data, String body) {
        super(id, body, data, MessageType.REQUEST);
        this.type = requestType;
        this.query = query;
    }
    
    
    /* ----- Getters and Setters ------------------------------------ */
    
    public RequestType getType () {
        return type;
    }
    
    public void setType (RequestType type) {
        this.type = type;
    }
    
    public String getQuery () {
        return query;
    }
    
    public void setQuery (String query) {
        this.query = query;
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    @Override
    public String toString () {
        return "RequestMessage {" +
                "id: " + getId() +
                ", timeStamp: " + getTimeStamp() +
                ", messageType: " + getMessageType() +
                ", body: " + getBody() +
                ", data: " + getData() +
                ", type: " + type +
                ", query: " + query +
                '}';
    }
}