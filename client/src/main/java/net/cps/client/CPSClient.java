package net.cps.client;

import net.cps.client.events.ParkingLotDataTmpEvent;
import net.cps.client.ocsf.AbstractClient;
import net.cps.entities.Message;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class CPSClient extends AbstractClient {
    private static CPSClient client = null;
    
    private static int msgId = 0;

    //
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
        // Message message = (Message) msg;
    
        EventBus.getDefault().post(new ParkingLotDataTmpEvent(msg));
        
//        if (message.getMessage().equals("update submitters IDs")) {
//            EventBus.getDefault().post(new UpdateMessageEvent(message));
//        } else if (message.getMessage().equals("client added successfully")) {
//            EventBus.getDefault().post(new NewSubscriberEvent(message));
//        } else if (message.getMessage().equals("Error! we got an empty message")) {
//            EventBus.getDefault().post(new ErrorEvent(message));
//        } else {
//            EventBus.getDefault().post(new MessageEvent(message));
//        }
    }
    
}
