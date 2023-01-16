package net.cps.client;

import net.cps.client.events.UserAuthEvent;
import net.cps.client.events.UserLogoutEvent;
import net.cps.client.ocsf.AbstractClient;
import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.RequestCallback;
import net.cps.common.utils.RequestType;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Client side main class (entry point).
 */
public class CPSClient extends AbstractClient {
    private static CPSClient client = null; // singleton - self instance
    private static final Map<Integer, RequestCallback> callbacks = new HashMap<>();
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
    
    public static void sendRequestToServer (RequestType type, String header, RequestCallback callback) {
        try {
            callbacks.put(requestId, callback);
            client.sendToServer(new RequestMessage(requestId, type, header, callback));
            requestId++;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void sendRequestToServer (RequestType type, String header, String body, RequestCallback callback) {
        try {
            callbacks.put(requestId, callback);
            client.sendToServer(new RequestMessage(requestId, type, header, body, callback));
            requestId++;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void sendRequestToServer (RequestType type, String header, String body, String message, RequestCallback callback) {
        try {
            callbacks.put(requestId, callback);
            client.sendToServer(new RequestMessage(requestId, type, header, body, message, callback));
            requestId++;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void sendRequestToServer (RequestType type, String header, String body, Object data, RequestCallback callback) {
        try {
            callbacks.put(requestId, callback);
            client.sendToServer(new RequestMessage(requestId, type, header, body, data, callback));
            requestId++;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void sendRequestToServer (RequestType type, String header, String body, String message, Object data, RequestCallback callback) {
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
        RequestCallback callback = callbacks.get(requestId);
        
        if (callback != null) {
            callback.accept(request, response);
            callbacks.remove(requestId);
            return;
        }
        
        RequestType type = request.getType();
        switch (type) {
            case GET -> {
                System.out.println("[CLIENT] unhandled response for GET request: " + response);
            }
            case CREATE -> {
                System.out.println("[CLIENT] unhandled response for CREATE request: " + response);
            }
            case UPDATE -> {
                System.out.println("[CLIENT] unhandled response for UPDATE request: " + response);
            }
            case DELETE -> {
                System.out.println("[CLIENT] unhandled response for DELETE request: " + response);
            }
            case AUTH -> {
                if (request.getHeader().startsWith("login")) EventBus.getDefault().post(new UserAuthEvent(response));
                if (request.getHeader().startsWith("logout")) EventBus.getDefault().post(new UserLogoutEvent(response));
            }
            case CUSTOM -> {
                System.out.println("[CLIENT] unhandled response for CUSTOM request: " + response);
            }
            default -> System.out.println("Unknown request type: " + type);
        }
    }
    
}

//public static String generateRandomString() {
//    StringBuilder sb = new StringBuilder();
//    for (int i = 0; i < 6; i++) {
//        int randomNum = (int) (Math.random() * 36);
//        if (randomNum < 10) {
//            sb.append(randomNum);
//        } else {
//            sb.append((char) (randomNum + 87));
//        }
//    }
//    return sb.toString();
//}

