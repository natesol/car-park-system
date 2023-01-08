package net.cps.common.messages;

import net.cps.common.utils.MessageType;
import net.cps.common.utils.RequestType;

import java.io.Serializable;


/*
 * This class is used to send messages between the server and the client (a server response).
 * An instance of this class is sent to the client when the server has finished processing a request.
 */
public class ResponseMessage extends Message implements Serializable {
    private RequestType type;
    private String query;
    private Boolean isSuccess;
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public ResponseMessage (int id, String body, Object data, RequestType requestType, Boolean isSuccess) {
        super(id, body, data, MessageType.RESPONSE);
        this.type = requestType;
        this.query = null;
        this.isSuccess = isSuccess;
    }
    
    public ResponseMessage (int id, String body, Object data, RequestType requestType, String query, Boolean isSuccess) {
        super(id, body, data, MessageType.RESPONSE);
        this.type = requestType;
        this.query = query;
        this.isSuccess = isSuccess;
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
    
    public Boolean getIsSuccess () {
        return isSuccess;
    }
    
    public void setIsSuccess (Boolean success) {
        isSuccess = success;
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    @Override
    public String toString () {
        return "ResponseMessage {" +
                "id: " + getId() +
                ", timeStamp: " + getTimeStamp() +
                ", messageType: " + getMessageType() +
                ", body: " + getBody() +
                ", data: " + getData() +
                ", type: " + type +
                ", query: " + query +
                ", isSuccess: " + isSuccess +
                '}';
    }
}