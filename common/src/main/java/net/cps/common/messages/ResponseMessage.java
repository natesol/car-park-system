package net.cps.common.messages;

import net.cps.common.utils.AbstractMessage;
import net.cps.common.utils.MessageType;
import net.cps.common.utils.RequestType;
import net.cps.common.utils.ResponseStatus;

import java.io.Serializable;


/**
 * <p>
 * A response message `status` is used to indicate the status of the response.
 * </p>
 * <p>
 * Response messages `status` format:
 *  - `SUCCESS`         indicates that the request was successful.
 *  - `CREATED`         indicates that the request was successful and a new resource was created.
 *  - `NOT_FOUND`       indicates that the request was successful but the requested content not found.
 *  - `UNAUTHORIZED`    indicates that the request was not authorized.
 *  - `BAD_REQUEST`     indicates that the request was not successful (a bad request format, or some gateway error ended the request process).
 *  - `ERROR`           indicates that the request was not successful (a general fatal error ended the request process).
 * </p>
 **/

/**
 * A message sent from the server to the client.
 * This class is used to send messages between the server and the client (a server response).
 * An instance of this class is sent to the client when the server has finished processing a request.
 **/
public class ResponseMessage extends AbstractMessage implements Serializable {
    public static final MessageType MESSAGE_TYPE = MessageType.REQUEST;
    private final RequestMessage originalRequest;
    private final ResponseStatus status;
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public ResponseMessage (int id, RequestMessage originalRequest, ResponseStatus status) {
        super(id, MESSAGE_TYPE, null, null);
        this.originalRequest = originalRequest;
        this.status = status;
    }
    
    public ResponseMessage (int id, RequestMessage originalRequest, ResponseStatus status, String message) {
        super(id, MESSAGE_TYPE, null, null, message);
        this.originalRequest = originalRequest;
        this.status = status;
    }
    
    public ResponseMessage (int id, RequestMessage originalRequest, ResponseStatus status, Object data) {
        super(id, MESSAGE_TYPE, null, null, data);
        this.originalRequest = originalRequest;
        this.status = status;
    }
    
    public ResponseMessage (int id, RequestMessage originalRequest, ResponseStatus status, String message, Object data) {
        super(id, MESSAGE_TYPE, null, null, message, data);
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