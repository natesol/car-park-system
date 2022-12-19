package net.cps.server;

import net.cps.entities.Message;
import net.cps.entities.hibernate.ParkingLot;
import net.cps.server.ocsf.AbstractServer;
import net.cps.server.ocsf.ConnectionToClient;
import net.cps.server.ocsf.SubscribedClient;
import net.cps.server.utils.ServerLogger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CPSServer extends AbstractServer {
    private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
    
    public static final ServerLogger logger = new ServerLogger();
    
    public CPSServer(int port) {
        super(port);
        System.out.println("[SERVER] server created.");
    }
    
    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        Message message = (Message) msg;
        String request = message.getMessage();
        try {
            if (request.isBlank()) {
                message.setMessage("Error! we got an empty message");
                client.sendToClient(message);
            }
            else if (request.startsWith("get parking-lots")) {
                message.setMessage("get parking-lots");
                System.out.println("get parking-lots");
                
                Session session = Main.getDbSession();
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<ParkingLot> query = builder.createQuery(ParkingLot.class);
                query.from(ParkingLot.class);
                List<ParkingLot> data = session.createQuery(query).getResultList();

                client.sendToClient(data);
            }
            else {
                message.setMessage(request);
                sendToAllClients(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void sendToAllClients(Message message) {
        try {
            for (SubscribedClient SubscribedClient : SubscribersList) {
                SubscribedClient.getClient().sendToClient(message);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
