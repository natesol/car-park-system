package net.cps.client;

import net.cps.entities.Message;
import org.greenrobot.eventbus.EventBus;

import net.cps.client.ocsf.AbstractClient;

public class CPSClient extends AbstractClient {
    
    private static CPSClient client = null;
    
    private CPSClient(String host, int port) {
        super(host, port);
    }
    
    public static CPSClient getClient() {
        if (client == null) {
            client = new CPSClient("localhost", 3000);
        }
        return client;
    }
    
    @Override
    protected void handleMessageFromServer(Object msg) {
        Message message = (Message) msg;
        if (message.getMessage().equals("update submitters IDs")) {
            EventBus.getDefault().post(new UpdateMessageEvent(message));
        } else if (message.getMessage().equals("client added successfully")) {
            EventBus.getDefault().post(new NewSubscriberEvent(message));
        } else if (message.getMessage().equals("Error! we got an empty message")) {
            EventBus.getDefault().post(new ErrorEvent(message));
        } else {
            EventBus.getDefault().post(new MessageEvent(message));
        }
    }
    
}
