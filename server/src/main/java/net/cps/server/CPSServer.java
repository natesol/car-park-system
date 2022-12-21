package net.cps.server;

import net.cps.entities.Message;
import net.cps.entities.hibernate.ParkingLot;
import net.cps.entities.hibernate.Rates;
import net.cps.server.ocsf.AbstractServer;
import net.cps.server.ocsf.ConnectionToClient;
import net.cps.server.ocsf.SubscribedClient;
import net.cps.server.utils.DataBase;
import net.cps.server.utils.ServerLogger;
import org.hibernate.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CPSServer extends AbstractServer {
    private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
    private static final ServerLogger logger = new ServerLogger();
    private static Session dbSession;
    
    public CPSServer(int port) {
        super(port);
        System.out.println("[SERVER] server created.");
    }
    
    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        Message message = (Message) msg;
        String request = message.getMessage();
        dbSession = DataBase.createSession();
        dbSession.beginTransaction();
        
        try {
            if (request.equals("get-parking-lots")) {
                System.out.println("get-parking-lots");
                
                List<ParkingLot> data = DataBase.getAllEntities(dbSession, ParkingLot.class);
                message.setMessage("get-parking-lots");
                message.setData(data);
                client.sendToClient(message);
            }
            else if (request.equals("get-rates")) {
                System.out.println("get-rates");
                
                List<Rates> data = DataBase.getAllEntities(dbSession, Rates.class);
                message.setMessage("get-rates");
                message.setData(data);
                client.sendToClient(message);
            }
            else if (request.equals("update-rates")) {
                System.out.println("update-rates");
                
                Rates rates = (Rates) message.getData();
                DataBase.updateEntity(dbSession, rates);
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
