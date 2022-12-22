package net.cps.server;

import net.cps.entities.Message;
import net.cps.entities.hibernate.ParkingLot;
import net.cps.entities.hibernate.Rates;
import net.cps.server.ocsf.AbstractServer;
import net.cps.server.ocsf.ConnectionToClient;
import net.cps.server.ocsf.SubscribedClient;
import net.cps.server.utils.Database;
import net.cps.server.utils.Logger;
import org.hibernate.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * singleton
 */
public class CPSServer extends AbstractServer {
    private static CPSServer server = null;
    
    private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
    private static Session dbSession;
    
    public CPSServer(int port) {
        super(port);
    }
    
    public static CPSServer getServer(int port) {
        if (server == null) {
            server = new CPSServer(port);
        }
        return server;
    }
    
    public static CPSServer getServer() {
        return server;
    }
    
    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        Message message = (Message) msg;
        String request = message.getMessage();
        dbSession = Database.createSession();
        dbSession.beginTransaction();
        
        try {
            if (request.equals("get-parking-lots")) {
                System.out.println("get-parking-lots");
                
                List<ParkingLot> data = Database.getAllEntities(dbSession, ParkingLot.class);
                message.setMessage("get-parking-lots");
                message.setData(data);
                client.sendToClient(message);
            }
            else if (request.equals("get-rates")) {
                System.out.println("get-rates");
                
                List<Rates> data = Database.getAllEntities(dbSession, Rates.class);
                message.setMessage("get-rates");
                message.setData(data);
                client.sendToClient(message);
            }
            else if (request.equals("update-rates")) {
                System.out.println("update-rates");
                
                Rates rates = (Rates) message.getData();
                Database.updateEntity(dbSession, rates);
                message.setMessage("update-rates");
            }
            else {
                message.setMessage(request);
                sendToAllClients(message);
            }
        }
        catch (IOException e) {
            System.out.println("[SERVER] an error occurred while handling: '" + request + "' message from client.");
            e.printStackTrace();
        }
        finally {
            assert dbSession != null;
            dbSession.close();
        }
    }
    
    public void sendToAllClients(Message message) {
        try {
            for (SubscribedClient SubscribedClient : SubscribersList) {
                SubscribedClient.getClient().sendToClient(message);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
