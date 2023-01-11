package net.cps.client;

import net.cps.client.events.GetAllParkingLotEvent;
import net.cps.client.events.GetParkingLotEvent;
import net.cps.client.events.ServerAuthEvent;
import net.cps.client.ocsf.AbstractClient;
import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.RequestMessageCallback;
import net.cps.common.utils.Entities;
import net.cps.common.utils.RequestType;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Client side main class (entry point).
 */
public class CPSClient extends AbstractClient {
    private static CPSClient client = null;
    private static int requestID = 0;
    private static final Map<Integer, BiConsumer<RequestMessage, ResponseMessage>> callbacks = new HashMap<>();
    
    
    /* ----- Constructors ------------------------------------------- */

    private CPSClient(String host, int port) {
        super(host, port);
    }
    
    
    
    /* ----- Getters and Setters ------------------------------------ */
    
    /*
     * Returns the singleton instance of the client.
     */
    public static CPSClient getClient(String host, int port) {
        if (client == null) {
            client = new CPSClient(host, port);
        }
        return client;
    }
    public static CPSClient getClient() {
        return client;
    }
    
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    @Override
    protected void handleMessageFromServer(Object responseObj) {
        ResponseMessage response = (ResponseMessage) responseObj;
        RequestMessage request = response.getOriginalRequest();
        String header = request.getHeader();
        RequestType requestType = request.getType();
        
        Integer requestId = request.getId();
        BiConsumer<RequestMessage, ResponseMessage> callback = callbacks.get(requestId);
        if (callback != null) {
            callback.accept(request, response);
            callbacks.remove(requestId);
        }
        
        //if (requestType == RequestType.GET) {
        //
        //    if (header.startsWith(Entities.PARKING_LOT.getTableName())) {
        //
        //        System.out.println("Parking lots received");
        //
        //        // Get all parking lots
        //        if (!header.contains("/")) {
        //            EventBus.getDefault().post(new GetAllParkingLotEvent(response));
        //        }
        //        // Get a specific parking lot
        //        else {
        //            EventBus.getDefault().post(new GetParkingLotEvent(response));
        //        }
        //
        //        EventBus.getDefault().post(new ServerAuthEvent(response));
        //    }
        //
        //
        //    EventBus.getDefault().post(new ServerAuthEvent(response));
        //}
        //else if (requestType == RequestType.POST) {
        //    //if (response.getQuery().equals("customer/sign-up")) {
        //    //    EventBus.getDefault().post(new CustomerCreationEvent(response));
        //    //}
        //}
        //else if (requestType == RequestType.AUTH) {
        //    //if (response.getQuery().startsWith("auth/")) {
        //    //    EventBus.getDefault().post(new ServerAuthEvent(response));
        //    //}
        //}
    }
    
    
    public static void sendRequestToServer(RequestType type, String header, BiConsumer<RequestMessage, ResponseMessage> callback) {
        try {
            callbacks.put(requestID, callback);
            client.sendToServer(new RequestMessage(requestID++, type, header, callback));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void sendRequestToServer(RequestType type, String header, String body, BiConsumer<RequestMessage, ResponseMessage> callback) {
        try {
            callbacks.put(requestID, callback);
            client.sendToServer(new RequestMessage(requestID++, type, header, body, callback));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void sendRequestToServer(RequestType type, String header, String body, String message, BiConsumer<RequestMessage, ResponseMessage> callback) {
        try {
            callbacks.put(requestID, callback);
            client.sendToServer(new RequestMessage(requestID++, type, header, body, message, callback));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void sendRequestToServer(RequestType type, String header, String body, Object data, BiConsumer<RequestMessage, ResponseMessage> callback) {
        try {
            callbacks.put(requestID, callback);
            client.sendToServer(new RequestMessage(requestID++, type, header, body, data, callback));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void sendRequestToServer(RequestType type, String header, String body, String message, Object data, BiConsumer<RequestMessage, ResponseMessage> callback) {
        try {
            callbacks.put(requestID, callback);
            client.sendToServer(new RequestMessage(requestID++, type, header, body, message, data, callback));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
