package net.cps.server;

import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.Entities;
import net.cps.common.utils.RequestType;
import net.cps.common.utils.ResponseStatus;
import net.cps.server.ocsf.AbstractServer;
import net.cps.server.ocsf.ConnectionToClient;
import net.cps.server.ocsf.SubscribedClient;
import net.cps.server.utils.Logger;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * Server class - a simple `OCSF` `AbstractServer` implementation.
 * The CPS server is a singleton class and can be accessed via the `getServer` method.
 * The server is responsible for handling all incoming requests from clients.
 */
public class CPSServer extends AbstractServer {
    private static CPSServer server = null; // singleton - self instance
    private static final ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
    
    
    /* ----- Constructors ------------------------------------------- */
    
    /**
     * Basic private constructor (to pass the `port` to parent class).
     **/
    private CPSServer (Integer port) {
        super(port);
    }
    
    
    /* ----- Getters and Setters ------------------------------------ */
    
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
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    /**
     * `AbstractServer` method override - called when a new client is connected.
     **/
    @Override
    protected void clientConnected (ConnectionToClient client) {
        String hostAddress = Objects.requireNonNull(client.getInetAddress()).getHostAddress();
        
        Logger.print("client connected via host: " + hostAddress);
    }
    
    /**
     * `AbstractServer` method override - called when a client is disconnected.
     **/
    @Override
    synchronized protected void clientDisconnected (ConnectionToClient client) {
        String hostAddress = Objects.requireNonNull(client.getInetAddress()).getHostAddress();
        
        Logger.print("client " + hostAddress + " has disconnected");
    }
    
    /**
     * `AbstractServer` method override - called when a client has sent a message.
     *
     * @param requestObj the message sent by the client.
     *                   must be an instance of `Message` entity.
     * @param client     the connection from which the message originated.
     **/
    @Override
    protected void handleMessageFromClient (Object requestObj, ConnectionToClient client) {
        RequestMessage request = (RequestMessage) requestObj;
        RequestType requestType = request.getType();
        
        SessionFactory dbSessionFactory = Database.getSessionFactory();
        
        try {
            if (requestType == RequestType.GET) {
                client.sendToClient(handleGetRequest(dbSessionFactory, request));
            }
            else if (requestType == RequestType.CREATE) {
                client.sendToClient(handleCreateRequest(dbSessionFactory, request));
            }
            else if (requestType == RequestType.UPDATE) {
                // client.sendToClient(handleUpdateRequest(dbSessionFactory, request));
                System.out.println("UPDATE request");
            }
            else if (requestType == RequestType.DELETE) {
                // client.sendToClient(handleDeleteRequest(dbSessionFactory, request));
                System.out.println("DELETE request");
            }
            else if (requestType == RequestType.AUTH) {
                System.out.println("AUTH request");
                //if (query.startsWith("auth")) {
                //    client.sendToClient(handleAuthRequest(dbSessionFactory, request.getId()));
                //}
            }
            else if (requestType == RequestType.CUSTOM) {
                // custom request
                System.out.println("CUSTOM request");
            }
        }
        catch (IOException e) {
            Logger.print("Error: an error occurred while handling: '" + requestType + ": " + request.getHeader() + "' request from client.");
            e.printStackTrace();
        }
        finally {
            Database.closeSession();
        }
    }
    
    private <T> ResponseMessage handleGetRequest (SessionFactory sessionFactory, RequestMessage request) {
        try {
            String query = request.getHeader().split("/")[0];
            Integer requestId = request.getId();
            String requestHeader = request.getHeader();
            Entities entity = Entities.fromString(query);
            Class<T> T = (Class<T>) entity.getEntityClass();
            
            
            // get all entities of type `T`.
            if (requestHeader.equals(query)) {
                List<T> data = (List<T>) Database.getAllEntities(sessionFactory, entity.getEntityClass());
                return new ResponseMessage(requestId, request, data.size() > 0 ? ResponseStatus.SUCCESS : ResponseStatus.NOT_FOUND, data);
            }
            
            requestHeader = requestHeader.split("/")[1];
            
            // get one entity of type `T` by id.
            if (!requestHeader.contains("?") && !requestHeader.contains("=")) {
                T data;
                
                String id = requestHeader.split("/")[1];
                data = Database.getEntity(sessionFactory, T, entity.getPrimaryKeyConverter().apply(id));
                return new ResponseMessage(requestId, request, data != null ? ResponseStatus.SUCCESS : ResponseStatus.NOT_FOUND, data);
            }
            
            // get a list of entities of type `T`.
            if (requestHeader.contains("?")) {
                List<T> data;
                
                // get the list of entities by ids.
                if (!requestHeader.contains("=")) {
                    String[] ids = requestHeader.split("\\?");
                    List<?> primaryKeyList = Arrays.stream(ids).map(entity.getPrimaryKeyConverter()).toList();
                    data = Database.getMultipleEntities(sessionFactory, T, (List<Object>) primaryKeyList);
                    return new ResponseMessage(requestId, request, data != null && data.size() > 0 ? ResponseStatus.SUCCESS : ResponseStatus.NOT_FOUND, data);
                }
                
                String[] requestList = requestHeader.split("\\?");
                StringBuilder sqlQuery = new StringBuilder("SELECT * FROM " + entity.getTableName() + " WHERE ");
                
                // get the list of entities by a list of parameters.
                if (!requestHeader.contains("&")) {
                    for (String field : requestList) {
                        String[] fieldSplit = field.split("=");
                        String fieldName = fieldSplit[0];
                        String fieldValue = fieldSplit[1];
                        
                        sqlQuery.append(fieldName).append(" = '").append(fieldValue).append("' ");
                        sqlQuery.append("OR ");
                    }
                    sqlQuery.delete(sqlQuery.length() - 4, sqlQuery.length());
                    
                    data = Database.getCustomQuery(sessionFactory, T, sqlQuery.toString());
                    return new ResponseMessage(requestId, request, data != null && data.size() > 0 ? ResponseStatus.SUCCESS : ResponseStatus.NOT_FOUND, data);
                }
                
                // get the list of entities by a list of complex parameters.
                for (String fieldsList : requestList) {
                    String[] fields = fieldsList.split("&");
                    
                    for (String field : fields) {
                        String[] fieldSplit = field.split("=");
                        String fieldName = fieldSplit[0];
                        String fieldValue = fieldSplit[1];
                        
                        sqlQuery.append(fieldName).append(" = '").append(fieldValue).append("' ");
                        sqlQuery.append("AND ");
                    }
                    sqlQuery.delete(sqlQuery.length() - 5, sqlQuery.length());
                    
                    sqlQuery.append("OR ");
                }
                sqlQuery.delete(sqlQuery.length() - 4, sqlQuery.length());
                
                data = Database.getCustomQuery(sessionFactory, T, sqlQuery.toString());
                return new ResponseMessage(requestId, request, data != null && data.size() > 0 ? ResponseStatus.SUCCESS : ResponseStatus.NOT_FOUND, data);
            }
            
            return new ResponseMessage(requestId, request, ResponseStatus.BAD_REQUEST, "Bad Request: response to 'GET: " + query + "'", null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseMessage(request.getId(), request, ResponseStatus.ERROR, "Error: response to 'GET: " + request.getHeader() + "'", e);
        }
    }
    
    private <T, U> ResponseMessage handleCreateRequest (SessionFactory sessionFactory, RequestMessage request) {
        try {
            String query = request.getHeader().split("/")[0];
            Integer requestId = request.getId();
            String requestHeader = request.getHeader();
            Entities entity = Entities.fromString(query);
            Class<T> T = (Class<T>) entity.getEntityClass();
            Class<U> U = (Class<U>) entity.getPrimaryKeyClass();
            Object obj = request.getData();
            
            // no entity to create from the client.
            if (obj == null) {
                return new ResponseMessage(requestId, request, ResponseStatus.BAD_REQUEST, "Bad Request: response to 'CREATE: " + query + "'", null);
            }
            
            // create multiple entities by the data list given on the request.
            if ((obj instanceof List)) {
                List<T> data = (List<T>) request.getData();
                ArrayList<U> ids = (ArrayList<U>) Database.createMultipleEntities(sessionFactory, data);
                return new ResponseMessage(requestId, request, ids.size() == data.size() ? ResponseStatus.CREATED : ResponseStatus.SUCCESS, ids);
            }

            // create one entity of type `T`.
            //if (obj instanceof T) {
                T data = (T) request.getData();
                U id = (U) Database.createEntity(sessionFactory, data);
                return new ResponseMessage(requestId, request, id != null ? ResponseStatus.CREATED : ResponseStatus.SUCCESS, id);
            //}
            
            //return new ResponseMessage(requestId, request, ResponseStatus.BAD_REQUEST, "Bad Request: response to 'CREATE: " + query + "'", null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseMessage(request.getId(), request, ResponseStatus.ERROR, "Error: response to 'CREATE: " + request.getHeader() + "'", e);
        }
    }
    
    private <T> ResponseMessage handleUpdateRequest (SessionFactory sessionFactory, Integer requestId, String query, Object requestData, String entityQuery, Class<T> T) {
        //String query = request.getHeader().split("/")[0];
        //// update a single entity of type `T`
        //if (query.equals(entityQuery)) {
        //    T data = (T) requestData;
        //    Database.updateEntity(sessionFactory, data);
        //
        //    return new ResponseMessage(requestId, "response to 'UPDATE' '" + query + "'", null, RequestType.POST, query, true);
        //}
        //// update a list of entities of type `T`
        //else if (query.startsWith(entityQuery + "/")) {
        //    String[] splitQuery = query.split("/");
        //    String[] ids = splitQuery.length > 1 ? splitQuery[1].split("\\?") : new String[] {"0"};
        //
        //    if (ids.length > 1) {
        //        List<T> data = (List<T>) requestData;
        //        Database.updateMultipleEntities(sessionFactory, data);
        //
        //        return new ResponseMessage(requestId, "response to 'UPDATE' '" + query + "'", null, RequestType.POST, query, true);
        //    }
        //    else {
        //        T data = (T) requestData;
        //        Database.updateEntity(sessionFactory, data);
        //
        //        return new ResponseMessage(requestId, "response to 'UPDATE' '" + query + "'", null, RequestType.POST, query, true);
        //    }
        //}
        //return new ResponseMessage(requestId, "bad request: response to 'UPDATE' '" + query + "'", null, RequestType.UPDATE, "bad request", false);
        //
        return null;
    }
    
    private <T> ResponseMessage handleDeleteRequest (SessionFactory sessionFactory, Integer requestId, String query, String entityQuery, Class<T> T) {
        //// delete all entities of type `T`
        //if (query.equals(entityQuery)) {
        //    Database.deleteAllEntities(sessionFactory, T);
        //
        //    return new ResponseMessage(requestId, "response to 'DELETE' '" + query + "'", null, RequestType.DELETE, query, true);
        //}
        //// delete a list of entities of type `T`
        //else if (query.startsWith(entityQuery + "/")) {
        //    String[] splitQuery = query.split("/");
        //    String[] ids = splitQuery.length > 1 ? splitQuery[1].split("\\?") : new String[] {"0"};
        //    List<Integer> idList = new ArrayList<>();
        //    for (String id : ids) {
        //        idList.add(Integer.parseInt(id));
        //    }
        //
        //    if (ids.length > 1) {
        //        List<T> entitiesList = Database.getMultipleEntities(sessionFactory, T, idList);
        //        Database.deleteMultipleEntities(sessionFactory, entitiesList);
        //        return new ResponseMessage(requestId, "response to 'DELETE' '" + query + "'", null, RequestType.DELETE, query, true);
        //    }
        //    else {
        //        T entity = Database.getEntity(sessionFactory, T, idList.get(0));
        //        Database.deleteEntity(sessionFactory, entity);
        //        return new ResponseMessage(requestId, "response to 'DELETE' '" + query + "'", null, RequestType.DELETE, query, true);
        //    }
        //}
        //return new ResponseMessage(requestId, "bad request: response to 'DELETE' '" + query + "'", null, RequestType.DELETE, "bad request", false);
        
        return null;
    }
    
    private <T> ResponseMessage handleAuthRequest (SessionFactory sessionFactory, Integer requestId, String query) {
        //try {
        //    String[] splitQuery = query.split("/");
        //    String email = splitQuery[1];
        //    String password = splitQuery[2];
        //
        //    Customer customer = Database.getEntity(sessionFactory, Customer.class, email);
        //    if (customer != null) {
        //        if (!customer.isPasswordEquals(password)) {
        //            return new ResponseMessage(requestId, "Sorry, but the password you entered does not match the email you entered. Please check the password and try again.", null, RequestType.AUTH, query, false);
        //        }
        //        return new ResponseMessage(requestId, "customer", customer, RequestType.AUTH, query, true);
        //    }
        //
        //    //List<Employee> employeeList = Database.getCustomQuery(sessionFactory, MySQLQueries.SELECT_ALL + "employees" + MySQLQueries.WHERE + "email = '" + email + "'", Employee.class);
        //    Employee employee = employeeList.size() > 0 ? (Employee) (employeeList.get(0)) : null;
        //    if (employee != null) {
        //        if (!employee.isPasswordEquals(password)) {
        //            return new ResponseMessage(requestId, "Sorry, but the password you entered does not match the email you entered. Please check the password and try again.", null, RequestType.AUTH, query, false);
        //        }
        //        return new ResponseMessage(requestId, "employee", employee, RequestType.AUTH, query, true);
        //    }
        //
        //    return new ResponseMessage(requestId, "Sorry, but the email you entered does not exist in our system. Please check the email and try again, or sign-up for a new account.", null, RequestType.AUTH, query, false);
        //}
        //catch (Throwable e) {
        //    e.printStackTrace();
        //}
        return null;
    }
}
