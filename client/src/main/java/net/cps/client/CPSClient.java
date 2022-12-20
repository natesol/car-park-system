package net.cps.client;

import net.cps.client.events.ParkingLotDataTmpEvent;
import net.cps.client.events.RatesDataTmpEvent;
import net.cps.client.ocsf.AbstractClient;
import net.cps.entities.Message;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class CPSClient extends AbstractClient {
    private static CPSClient client = null;
    
    private static int msgId = 0;

    private CPSClient(String host, int port) {
        super(host, port);
    }
    
    public static CPSClient getClient(String host, int port) {
        if (client == null) {
            client = new CPSClient(host, port);
        }
        return client;
    }
    
    public static CPSClient getClient() {
        return client;
    }
    
    public static void sendMessageToServer(String data, Object obj) {
        try {
            Message message = new Message(msgId++, data);
            message.setData(obj);
            CPSClient.getClient().sendToServer(message);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void sendMessageToServer(String data) {
        try {
            Message message = new Message(msgId++, data);
            CPSClient.getClient().sendToServer(message);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void handleMessageFromServer(Object msg) {
        Message message = (Message) msg;
        
        if (message.getMessage().equals("get-parking-lots")) {
            System.out.println("get-parking-lots");
            
            EventBus.getDefault().post(new ParkingLotDataTmpEvent(message.getData()));
        }
        else if (message.getMessage().equals("get-rates")) {
            System.out.println("get-rates");
            
            EventBus.getDefault().post(new RatesDataTmpEvent(message.getData()));
        }
    }
    
}
