package net.cps.common.messages;

import net.cps.common.utils.AbstractMessage;
import net.cps.common.utils.MessageType;
import net.cps.common.utils.RequestType;

import java.io.Serializable;

/*
 * This class is used to send messages between the client and the server (a client request).
 * An instance of this class is sent to the server when the client has a request.
 *
 * Request `header` format:
 *  - general request: <entity table name>
 *    e.g. "parking_lot"
 *  - specific request: <entity table name>/<entity id>?<entity id>...
 *    * the id is the primary key of the entity.
 *    * it can be a single id or multiple ids separated by a question mark (?).
 *    e.g. "parking_lot/1", "parking_lot/1?2?3"
 *  - request with filter: <entity table name>/<filter name>=<filter value>?<filter name>=<filter value>...
 *    * the filter name is the name of the column in the table.
 *    e.g. "parking_lot/name=Haifa Port Parking", "parking_lot/floor_cols=3?floor_rows=5"
 *  - request with complex filter: <entity table name>/<1th filter name>=<1th filter value>&<2th filter name>=<2th filter value>?<1th filter name>=<1th filter value>...
 *    * the filter name is the name of the column in the table.
 *    * each filter is separated by an ampersand (&) and each group of filters is separated by a question mark (?).
 *    e.g. to get a `parking_lot` that is in 'Haifa' and have `7` cols on each floor: "parking_lot/city=Haifa&floor_cols=3"
 *
 * * Note: the server will ignore the request if the header is not in the correct format (unless the request type is CUSTOM).
 *
 * In addition to the header, the request can also contain a body. The body is used to send additional data to the server.
 * Also, there is a `message` field which is used to send the message description if needed to the server.
 * And finally, there is a `data` field which is used to send any data to the server - it can be a single object or a list of objects.
 */
public class RequestMessage extends AbstractMessage implements Serializable {
    public static final MessageType MESSAGE_TYPE = MessageType.REQUEST;
    private final RequestType type;
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public RequestMessage (int id, RequestType requestType, String header) {
        super(id, MESSAGE_TYPE, header);
        this.type = requestType;
    }
    
    public RequestMessage (int id, RequestType requestType, String header, String body) {
        super(id, MESSAGE_TYPE, header, body);
        this.type = requestType;
    }
    
    public RequestMessage (int id, RequestType requestType, String header, String body, String message) {
        super(id, MESSAGE_TYPE, header, body, message);
        this.type = requestType;
    }
    
    public RequestMessage (int id, RequestType requestType, String header, String body, Object data) {
        super(id, MESSAGE_TYPE, header, body, data);
        this.type = requestType;
    }
    
    public RequestMessage (int id, RequestType requestType, String header, String body, String message, Object data) {
        super(id, MESSAGE_TYPE, header, body, message, data);
        this.type = requestType;
    }
    
    
    /* ----- Getters and Setters ------------------------------------ */
    
    public RequestType getType () {
        return type;
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    @Override
    public String toString () {
        return "RequestMessage {" +
                "id: " + getId() +
                ", timeStamp: " + getTimeStamp() +
                ", type: " + type +
                ", header: " + getHeader() +
                ", body: " + getBody() +
                ", data: " + getData() +
                ", message: " + getMessage() +
                '}';
    }
}