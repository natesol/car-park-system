package net.cps.client;

import net.cps.client.ocsf.AbstractClient;
import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.RequestMessageCallback;
import net.cps.common.utils.RequestType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Client side main class (entry point).
 */
public class CPSClient extends AbstractClient {
    private static CPSClient client = null; // singleton - self instance
    private static final Map<Integer, RequestMessageCallback> callbacks = new HashMap<>();
    private static int requestId = 1;
    
    
    
    /* ----- Constructors ------------------------------------------- */
    
    private CPSClient (String host, int port) {
        super(host, port);
    }
    
    
    
    /* ----- Getters and Setters ------------------------------------ */
    
    /*
     * Returns the singleton instance of the client.
     */
    public static CPSClient getClient (String host, int port) {
        if (client == null) {
            client = new CPSClient(host, port);
        }
        return client;
    }
    
    public static CPSClient getClient () {
        return client;
    }
    
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    public static void sendRequestToServer (RequestType type, String header, RequestMessageCallback callback) {
        try {
            callbacks.put(requestId, callback);
            client.sendToServer(new RequestMessage(requestId, type, header, callback));
            requestId++;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void sendRequestToServer (RequestType type, String header, String body, RequestMessageCallback callback) {
        try {
            callbacks.put(requestId, callback);
            client.sendToServer(new RequestMessage(requestId, type, header, body, callback));
            requestId++;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void sendRequestToServer (RequestType type, String header, String body, String message, RequestMessageCallback callback) {
        try {
            callbacks.put(requestId, callback);
            client.sendToServer(new RequestMessage(requestId, type, header, body, message, callback));
            requestId++;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void sendRequestToServer (RequestType type, String header, String body, Object data, RequestMessageCallback callback) {
        try {
            callbacks.put(requestId, callback);
            client.sendToServer(new RequestMessage(requestId, type, header, body, data, callback));
            requestId++;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void sendRequestToServer (RequestType type, String header, String body, String message, Object data, RequestMessageCallback callback) {
        try {
            callbacks.put(requestId, callback);
            client.sendToServer(new RequestMessage(requestId, type, header, body, message, data, callback));
            requestId++;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /*
     * Handles requests responses from the server.
     */
    @Override
    protected void handleMessageFromServer (Object responseObj) {
        ResponseMessage response = (ResponseMessage) responseObj;
        RequestMessage request = response.getOriginalRequest();
        Integer requestId = request.getId();
        RequestMessageCallback callback = callbacks.get(requestId);
        
        if (callback != null) {
            callback.accept(request, response);
            callbacks.remove(requestId);
            return;
        }
        
        RequestType type = request.getType();
        
        if (type == RequestType.GET) {
            System.out.println("[CLIENT] received response for GET request: " + response);
        }
        else if (type == RequestType.CREATE) {
            System.out.println("[CLIENT] received response for CREATE request: " + response);
        }
        else if (type == RequestType.UPDATE) {
            System.out.println("[CLIENT] received response for UPDATE request: " + response);
        }
        else if (type == RequestType.DELETE) {
            System.out.println("[CLIENT] received response for DELETE request: " + response);
        }
        else if (type == RequestType.AUTH) {
            System.out.println("[CLIENT] received response for AUTH request: " + response);
        }
        else {
            System.out.println("[CLIENT] received response for unknown request: " + response);
        }
    }
    
}
