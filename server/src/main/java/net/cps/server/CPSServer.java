package net.cps.server;

import net.cps.entities.Message;
import net.cps.server.ocsf.AbstractServer;
import net.cps.server.ocsf.ConnectionToClient;
import net.cps.server.ocsf.SubscribedClient;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CPSServer extends AbstractServer {
    private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
    
    public CPSServer(int port) {
        super(port);
        System.out.println("[SERVER] server created.");
    }
    
    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        Message message = (Message) msg;
        String request = message.getMessage();
        try {
            //we got an empty message, so we will send back an error message with the error details.
            if (request.isBlank()) {
                message.setMessage("Error! we got an empty message");
                client.sendToClient(message);
            }
            //we got a request to change submitters IDs with the updated IDs at the end of the string, so we save
            // the IDs at data field in Message entity and send back to all subscribed clients a request to update
            //their IDs text fields. An example of use of observer design pattern.
            //message format: "change submitters IDs: 123456789, 987654321"
            else if (request.startsWith("change submitters IDs:")) {
                message.setData(request.substring(23));
                message.setMessage("update submitters IDs");
                sendToAllClients(message);
            }
            //we got a request to add a new client as a subscriber.
            else if (request.equals("add client")) {
                SubscribedClient connection = new SubscribedClient(client);
                SubscribersList.add(connection);
                message.setMessage("client added successfully");
                client.sendToClient(message);
            }
            //we got a message from client requesting to echo Hello, so we will send back to client Hello world!
            else if (request.startsWith("echo Hello")) {
                message.setMessage("Hello World!");
                client.sendToClient(message);
            }
            //add code here to send submitters IDs to client
            else if (request.startsWith("send Submitters IDs")) {
                message.setMessage("313537250, 313600272");
                client.sendToClient(message);
            }
            //add code here to send submitters names to client
            else if (request.startsWith("send Submitters")) {
                message.setMessage("Netanel, Amir");
                client.sendToClient(message);
            }
            //add code here to send the date to client
            else if (request.equals("what day it is?")) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uuuu");
                LocalDate today = LocalDate.now();
                
                message.setMessage(dtf.format(today));
                client.sendToClient(message);
            }
            //add code here to sum 2 numbers received in the message and send result back to client
            //(use substring method as shown above)
            //message format: "add n+m"
            else if (request.startsWith("add")) {
                String[] operands = request.replaceAll("\\s+", "")
                                           .replace("add", "")
                                           .split("\\+");
                int n = Integer.parseInt(operands[0]);
                int m = Integer.parseInt(operands[1]);
                
                message.setMessage(Integer.toString(n+m));
                client.sendToClient(message);
            }
            //add code here to send received message to all clients.
            //The string we received in the message is the message we will send back to all clients subscribed.
            //Example:
            // message received: "Good morning"
            // message sent: "Good morning"
            //see code for changing submitters IDs for help
            else {
                message.setMessage(request);
                sendToAllClients(message);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
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
