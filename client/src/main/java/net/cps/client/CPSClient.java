package net.cps.client;

import net.cps.client.events.CustomerCreationEvent;
import net.cps.client.events.ServerAuthEvent;
import net.cps.client.ocsf.AbstractClient;
import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.RequestType;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

/**
 * Client side main class (entry point).
 */
public class CPSClient extends AbstractClient {
    private static CPSClient client = null;
    private static int requestID = 0;
    
    
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
    
    public static void sendRequestToServer(RequestType type, String query, Object data) {
        RequestMessage request = new RequestMessage(requestID++, type, query, data, null);
        try {
            client.sendToServer(request);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void sendRequestToServer(RequestType type, String query, String body) {
        RequestMessage request = new RequestMessage(requestID++, type, query, null, body);
        try {
            client.sendToServer(request);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void sendRequestToServer(RequestType type, String query, Object data, String body) {
        RequestMessage request = new RequestMessage(requestID++, type, query, data, body);
        try {
            client.sendToServer(request);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void handleMessageFromServer(Object responseObj) {
        ResponseMessage response = (ResponseMessage) responseObj;
        
        System.out.println("Client received response from server: " + response.getBody());
        System.out.println(response);
        
        if (response.getType() == RequestType.POST) {
            if (response.getQuery().equals("customer/sign-up")) {
                EventBus.getDefault().post(new CustomerCreationEvent(response));
            }
        }
        else if (response.getType() == RequestType.AUTH) {
            if (response.getQuery().startsWith("auth/")) {
                EventBus.getDefault().post(new ServerAuthEvent(response));
            }
        }
    }
    
}
