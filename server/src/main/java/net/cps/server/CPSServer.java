package net.cps.server;

import net.cps.common.entities.Customer;
import net.cps.common.entities.Employee;
import net.cps.common.entities.ParkingLot;
import net.cps.common.entities.Rates;
import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.RequestType;
import net.cps.server.ocsf.AbstractServer;
import net.cps.server.ocsf.ConnectionToClient;
import net.cps.server.ocsf.SubscribedClient;
import net.cps.server.utils.Logger;
import net.cps.server.utils.MySQLQueries;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Server class (simple 'OCSF' `AbstractServer` implementation).
 * - a singleton class.
 */
public class CPSServer extends AbstractServer {
    private static CPSServer server = null; // singleton - self instance
    private static final ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
    
    
    /**
     * Basic private constructor (to pass the `port` to parent class).
     **/
    private CPSServer (Integer port) {
        super(port);
    }
    
    /**
     * Singleton - get server instance.
     **/
    public static CPSServer getServer () {
        return server;
    }
    
    /**
     * Overloaded singleton - get server instance (first time initiating class with a new `port` to listen).
     **/
    public static CPSServer getServer (Integer port) {
        if (server == null) {
            server = new CPSServer(port);
        }
        return server;
    }
    
    /**
     * Get the server connection host.
     **/
    public String getHost () {
        return getServerSocket().getInetAddress().getHostName();
    }
    
    /**
     * 'AbstractServer' method override - called when a new client is connected.
     **/
    @Override
    protected void clientConnected (ConnectionToClient client) {
        String hostAddress = Objects.requireNonNull(client.getInetAddress()).getHostAddress();
        
        Logger.print("client connected via host: " + hostAddress);
        Logger.info("client connected via host: " + hostAddress);
    }
    
    /**
     * 'AbstractServer' method override - called when a client is disconnected.
     **/
    @Override
    synchronized protected void clientDisconnected (ConnectionToClient client) {
        String hostAddress = Objects.requireNonNull(client.getInetAddress()).getHostAddress();
        
        Logger.print("client " + hostAddress + " has disconnected");
        Logger.info("client " + hostAddress + " has disconnected");
    }
    
    /**
     * 'AbstractServer' method override - called when a client has sent a message.
     *
     * @param requestObj the message sent by the client.
     *                   must be an instance of `Message` entity.
     * @param client     the connection from which the message originated.
     **/
    @Override
    protected void handleMessageFromClient (Object requestObj, ConnectionToClient client) {
        RequestMessage request = (RequestMessage) requestObj;
        String query = request.getQuery();
        RequestType requestType = request.getType();
        
        SessionFactory dbSessionFactory = Database.getSessionFactory();
        
        try {
            if (requestType == RequestType.GET) {
                if (query.equals("customer")) {
                    client.sendToClient(handleGetRequest(dbSessionFactory, request.getId(), query, "customer", Customer.class));
                }
                else if (query.startsWith("parking-lot")) {
                    client.sendToClient(handleGetRequest(dbSessionFactory, request.getId(), query, "parking-lot", ParkingLot.class));
                }
                else if (query.startsWith("rates")) {
                    client.sendToClient(handleGetRequest(dbSessionFactory, request.getId(), query, "rates", Rates.class));
                }
            }
            else if (requestType == RequestType.POST) {
                if (query.startsWith("customer")) {
                    client.sendToClient(handlePostRequest(dbSessionFactory, request.getId(), query, request.getData(), "customer", Customer.class));
                }
                else if (query.startsWith("parking-lot")) {
                    client.sendToClient(handlePostRequest(dbSessionFactory, request.getId(), query, request.getData(), "parking-lot", ParkingLot.class));
                }
                else if (query.startsWith("rates")) {
                    client.sendToClient(handlePostRequest(dbSessionFactory, request.getId(), query, request.getData(), "rates", Rates.class));
                }
            }
            else if (requestType == RequestType.UPDATE) {
                if (query.startsWith("customer")) {
                    client.sendToClient(handleUpdateRequest(dbSessionFactory, request.getId(), query, request.getData(), "customer", Customer.class));
                }
                else if (query.startsWith("parking-lot")) {
                    client.sendToClient(handleUpdateRequest(dbSessionFactory, request.getId(), query, request.getData(), "parking-lot", ParkingLot.class));
                }
                else if (query.startsWith("rates")) {
                    client.sendToClient(handleUpdateRequest(dbSessionFactory, request.getId(), query, request.getData(), "rates", Rates.class));
                }
            }
            else if (requestType == RequestType.DELETE) {
                if (query.startsWith("customer")) {
                    client.sendToClient(handleDeleteRequest(dbSessionFactory, request.getId(), query, "customer", Customer.class));
                }
                else if (query.startsWith("parking-lot")) {
                    client.sendToClient(handleDeleteRequest(dbSessionFactory, request.getId(), query, "parking-lot", ParkingLot.class));
                }
                else if (query.startsWith("rates")) {
                    client.sendToClient(handleDeleteRequest(dbSessionFactory, request.getId(), query, "rates", Rates.class));
                }
            }
            else if (requestType == RequestType.AUTH) {
                if (query.startsWith("auth")) {
                    client.sendToClient(handleAuthRequest(dbSessionFactory, request.getId(), query));
                }
            }
            else if (requestType == RequestType.CUSTOM) {
                // custom request
                System.out.println("custom request !!!!!!!!!!!!!");
            }
        }
        catch (IOException e) {
            Logger.print("Error: an error occurred while handling: '" + query + "' message from client.");
            e.printStackTrace();
        }
        finally {
            Database.closeSession();
        }
    }
    
    private <T> ResponseMessage handleGetRequest (SessionFactory sessionFactory, Integer requestId, String query, String entityQuery, Class<T> T) {
        // get all entities of type `T`
        if (query.equals(entityQuery)) {
            List<ParkingLot> data = Database.getAllEntities(sessionFactory, ParkingLot.class);
            
            return new ResponseMessage(requestId, "response to 'GET' '" + query + "'", data, RequestType.GET, query, true);
        }
        // get a list of entities of type `T`
        else if (query.startsWith(entityQuery + "/")) {
            String[] splitQuery = query.split("/");
            String[] ids = splitQuery.length > 1 ? splitQuery[1].split("\\?") : new String[] {"0"};
            List<Integer> idList = new ArrayList<>();
            for (String id : ids) {
                idList.add(Integer.parseInt(id));
            }
            
            if (ids.length > 1) {
                List<T> data = Database.getMultipleEntities(sessionFactory, T, idList);
                return new ResponseMessage(requestId, "response to 'GET' '" + query + "'", data, RequestType.GET, query, true);
            }
            else {
                T data = Database.getEntity(sessionFactory, T, idList.get(0));
                return new ResponseMessage(requestId, "response to 'GET' '" + query + "'", data, RequestType.GET, query, true);
            }
        }
        return new ResponseMessage(requestId, "bad request: response to 'GET' '" + query + "'", null, RequestType.GET, "bad request", false);
    }
    
    private <T> ResponseMessage handlePostRequest (SessionFactory sessionFactory, Integer requestId, String query, Object requestData, String entityQuery, Class<T> T) {
        // handle user registration
        if (query.equals("customer/sign-up")) {
            Customer customer = (Customer) requestData;
            if (Database.getEntity(sessionFactory, Customer.class, customer.getEmail()) != null) {
                return new ResponseMessage(requestId, "Sorry, that email address is already in use. Please enter a different email address or try logging in with that address.", customer, RequestType.POST, query, false);
            }
        }
        
        // create a new entity of type `T`
        if (query.equals(entityQuery)) {
            T data = (T) requestData;
            Database.addEntity(sessionFactory, data);
            
            return new ResponseMessage(requestId, "response to 'POST' '" + query + "'", data, RequestType.POST, query, true);
        }
        // create a list of entities of type `T`
        else if (query.startsWith(entityQuery + "/")) {
            String[] splitQuery = query.split("/");
            String[] ids = splitQuery.length > 1 ? splitQuery[1].split("\\?") : new String[] {"0"};
            
            if (ids.length > 1) {
                List<T> data = (List<T>) requestData;
                ArrayList<Object> idsList = Database.addMultipleEntities(sessionFactory, data);
                
                return new ResponseMessage(requestId, "response to 'POST' '" + query + "'", idsList, RequestType.POST, query, true);
            }
            else {
                T data = (T) requestData;
                Object id = Database.addEntity(sessionFactory, data);
                
                return new ResponseMessage(requestId, "response to 'POST' '" + query + "'", id, RequestType.POST, query, true);
            }
        }
        
        // bad request
        return new ResponseMessage(requestId, "bad request: response to 'POST' '" + query + "'", null, RequestType.POST, "bad request", false);
    }
    
    private <T> ResponseMessage handleUpdateRequest (SessionFactory sessionFactory, Integer requestId, String query, Object requestData, String entityQuery, Class<T> T) {
        // update a single entity of type `T`
        if (query.equals(entityQuery)) {
            T data = (T) requestData;
            Database.updateEntity(sessionFactory, data);
            
            return new ResponseMessage(requestId, "response to 'UPDATE' '" + query + "'", null, RequestType.POST, query, true);
        }
        // update a list of entities of type `T`
        else if (query.startsWith(entityQuery + "/")) {
            String[] splitQuery = query.split("/");
            String[] ids = splitQuery.length > 1 ? splitQuery[1].split("\\?") : new String[] {"0"};
            
            if (ids.length > 1) {
                List<T> data = (List<T>) requestData;
                Database.updateMultipleEntities(sessionFactory, data);
                
                return new ResponseMessage(requestId, "response to 'UPDATE' '" + query + "'", null, RequestType.POST, query, true);
            }
            else {
                T data = (T) requestData;
                Database.updateEntity(sessionFactory, data);
                
                return new ResponseMessage(requestId, "response to 'UPDATE' '" + query + "'", null, RequestType.POST, query, true);
            }
        }
        return new ResponseMessage(requestId, "bad request: response to 'UPDATE' '" + query + "'", null, RequestType.UPDATE, "bad request", false);
    }
    
    private <T> ResponseMessage handleDeleteRequest (SessionFactory sessionFactory, Integer requestId, String query, String entityQuery, Class<T> T) {
        // delete all entities of type `T`
        if (query.equals(entityQuery)) {
            Database.deleteAllEntities(sessionFactory, T);
            
            return new ResponseMessage(requestId, "response to 'DELETE' '" + query + "'", null, RequestType.DELETE, query, true);
        }
        // delete a list of entities of type `T`
        else if (query.startsWith(entityQuery + "/")) {
            String[] splitQuery = query.split("/");
            String[] ids = splitQuery.length > 1 ? splitQuery[1].split("\\?") : new String[] {"0"};
            List<Integer> idList = new ArrayList<>();
            for (String id : ids) {
                idList.add(Integer.parseInt(id));
            }
            
            if (ids.length > 1) {
                List<T> entitiesList = Database.getMultipleEntities(sessionFactory, T, idList);
                Database.deleteMultipleEntities(sessionFactory, entitiesList);
                return new ResponseMessage(requestId, "response to 'DELETE' '" + query + "'", null, RequestType.DELETE, query, true);
            }
            else {
                T entity = Database.getEntity(sessionFactory, T, idList.get(0));
                Database.deleteEntity(sessionFactory, entity);
                return new ResponseMessage(requestId, "response to 'DELETE' '" + query + "'", null, RequestType.DELETE, query, true);
            }
        }
        return new ResponseMessage(requestId, "bad request: response to 'DELETE' '" + query + "'", null, RequestType.DELETE, "bad request", false);
    }
    
    private <T> ResponseMessage handleAuthRequest (SessionFactory sessionFactory, Integer requestId, String query) {
        try {
            String[] splitQuery = query.split("/");
            String email = splitQuery[1];
            String password = splitQuery[2];
            
            Customer customer = Database.getEntity(sessionFactory, Customer.class, email);
            if (customer != null) {
                if (!customer.verifyPassword(password)) {
                    return new ResponseMessage(requestId, "Sorry, but the password you entered does not match the email you entered. Please check the password and try again.", null, RequestType.AUTH, query, false);
                }
                return new ResponseMessage(requestId, "customer", customer, RequestType.AUTH, query, true);
            }
            
            List<Employee> employeeList = Database.getCustomQuery(sessionFactory, MySQLQueries.SELECT_ALL + "employees" + MySQLQueries.WHERE + "email = '" + email + "'", Employee.class);
            Employee employee = employeeList.size() > 0 ? (Employee) (employeeList.get(0)) : null;
            if (employee != null) {
                if (!employee.verifyPassword(password)) {
                    return new ResponseMessage(requestId, "Sorry, but the password you entered does not match the email you entered. Please check the password and try again.", null, RequestType.AUTH, query, false);
                }
                return new ResponseMessage(requestId, "employee", employee, RequestType.AUTH, query, true);
            }
            
            return new ResponseMessage(requestId, "Sorry, but the email you entered does not exist in our system. Please check the email and try again, or sign-up for a new account.", null, RequestType.AUTH, query, false);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
