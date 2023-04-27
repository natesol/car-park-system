package net.cps.common.messages;

import net.cps.common.utils.AbstractMessage;
import net.cps.common.utils.MessageType;
import net.cps.common.utils.RequestCallback;
import net.cps.common.utils.RequestType;

import java.io.Serializable;


/**
 * <p>
 * Request `header` regular requests (GET, CREATE, UPDATE, DELETE) format:
 *      - general request: <entity table name>
 *        e.g. "parking_lot"
 *      - specific request: <entity table name>/<entity id>?<entity id>...
 *        * the id is the primary key of the entity.
 *        * it can be a single id or multiple ids separated by a question mark (?).
 *        e.g. "parking_lot/1", "parking_lot/1?2?3"
 *      - request with filter: <entity table name>/<filter name>=<filter value>?<filter name>=<filter value>...
 *        * the filter name is the name of the column in the table.
 *        e.g. "parking_lot/name=Haifa Port Parking", "parking_lot/floor_cols=3?floor_rows=5"
 *      - request with complex filter: <entity table name>/<1th filter name>=<1th filter value>&<2nd filter name>=<2nd filter value>?<1th filter name>=<1th filter value>...
 *        * the filter name is the name of the column in the table.
 *        * each filter is separated by an ampersand (&) and each group of filters is separated by a question mark (?).
 *        e.g. to get a `parking_lot` that is in 'Haifa' and have `7` cols on each floor: "parking_lot/city=Haifa&floor_cols=3"
 * </p>
 * <p>
 * Request `header` special requests (AUTH) format:
 *      - auth request: <auth type>/<auth data>
 *        * auth type can be only: "login", "logout" or "register".
 *        * auth data is the data needed for the auth type.
 *        e.g. "login/email=admin@gmail.com&password=1234" or "logout/1" or "register"
 * </p>
 * <p>
 * Request `header` special requests (CUSTOM) format:
 *      - there is no specific format for custom requests.
 *      - by creating a custom request you need to create your own request format, and handle it in the server.
 * </p>
 * <p>
 *      the `header` and `body` are not used in the `CUSTOM` request type.
 * <b>* NOTE:</b> the server will return a `bad request` response if the request format is not valid (unless the request type is CUSTOM).
 * </p>
 * <br />
 * <p>
 * In addition to the header, the request can also contain a `body`. The `body` is used to send additional data to the server.
 * Also, there is a `message` field which is used to send the message description if needed to the server.
 * And there is a `data` field, which is used to send any object to the server - it can be a single object or a list of objects.
 * The last field is the `callback` field, which is used to send a callback function with the request, in a way that the callback
 * function will be executed on the client side when the server will send a response to the request.
 * The callback function is used to handle the response from the server.
 * </p>
 **/

/**
 * A message sent from the client to the server.
 * This class is used to send messages between the client and the server (a client request).
 * An instance of this class is sent to the server when the client has a request.
 **/
public class RequestMessage extends AbstractMessage implements Serializable {
    public static final MessageType MESSAGE_TYPE = MessageType.REQUEST;
    private final RequestType type;
    private final transient RequestCallback callback;
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public RequestMessage (int id, RequestType requestType, String header, RequestCallback callback) {
        super(id, MESSAGE_TYPE, header);
        this.type = requestType;
        this.callback = callback;
    }
    
    public RequestMessage (int id, RequestType requestType, String header, String body, RequestCallback callback) {
        super(id, MESSAGE_TYPE, header, body);
        this.type = requestType;
        this.callback = callback;
    }
    
    public RequestMessage (int id, RequestType requestType, String header, String body, String message, RequestCallback callback) {
        super(id, MESSAGE_TYPE, header, body, message);
        this.type = requestType;
        this.callback = callback;
    }
    
    public RequestMessage (int id, RequestType requestType, String header, String body, Object data, RequestCallback callback) {
        super(id, MESSAGE_TYPE, header, body, data);
        this.type = requestType;
        this.callback = callback;
    }
    
    public RequestMessage (int id, RequestType requestType, String header, String body, String message, Object data, RequestCallback callback) {
        super(id, MESSAGE_TYPE, header, body, message, data);
        this.type = requestType;
        this.callback = callback;
    }
    
    
    /* ----- Getters and Setters ------------------------------------ */
    
    public MessageType getMessageType () {
        return MESSAGE_TYPE;
    }
    
    public RequestType getType () {
        return type;
    }
    
    public RequestCallback getCallback () {
        return callback;
    }
    
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    public void onResponse (ResponseMessage response) {
        if (callback != null) {
            callback.accept(response.getOriginalRequest(), response);
        }
    }
    
    
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
                ", callback: " + callback +
                '}';
    }
}