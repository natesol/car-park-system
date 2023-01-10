package net.cps.common.messages;

import net.cps.common.utils.AbstractMessage;
import net.cps.common.utils.MessageType;
import net.cps.common.utils.RequestType;
import net.cps.common.utils.ResponseStatus;

import java.io.Serializable;


/*
 * This class is used to send messages between the server and the client (a server response).
 * An instance of this class is sent to the client when the server has finished processing a request.
 */
public class ResponseMessage extends AbstractMessage implements Serializable {
    public static final MessageType MESSAGE_TYPE = MessageType.REQUEST;
    private final RequestMessage originalRequest;
    private final ResponseStatus status;
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public ResponseMessage (int id, RequestMessage originalRequest, ResponseStatus status) {
        super(id, MESSAGE_TYPE, originalRequest.getHeader(), originalRequest.getBody());
        this.originalRequest = originalRequest;
        this.status = status;
    }
    
    public ResponseMessage (int id, RequestMessage originalRequest, ResponseStatus status, String message) {
        super(id, MESSAGE_TYPE, originalRequest.getHeader(), originalRequest.getBody(), message);
        this.originalRequest = originalRequest;
        this.status = status;
    }
    
    public ResponseMessage (int id, RequestMessage originalRequest, ResponseStatus status, Object data) {
        super(id, MESSAGE_TYPE, originalRequest.getHeader(), originalRequest.getBody(), data);
        this.originalRequest = originalRequest;
        this.status = status;
    }
    
    public ResponseMessage (int id, RequestMessage originalRequest, ResponseStatus status, String message, Object data) {
        super(id, MESSAGE_TYPE, originalRequest.getHeader(), originalRequest.getBody(), message, data);
        this.originalRequest = originalRequest;
        this.status = status;
    }
    
    
    /* ----- Getters and Setters ------------------------------------ */
    
    public RequestMessage getOriginalRequest () {
        return originalRequest;
    }
    
    public ResponseStatus getStatus () {
        return status;
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    @Override
    public String toString () {
        return "ResponseMessage {" +
                "id: " + getId() +
                ", timeStamp: " + getTimeStamp() +
                ", messageType: " + getMessageType() +
                ", header: " + getHeader() +
                ", body: " + getBody() +
                ", data: " + getData() +
                ", originalRequest: " + originalRequest +
                ", status: " + status +
                '}';
    }
}