package net.cps.server;

import net.cps.common.entities.Customer;
import net.cps.common.entities.Employee;
import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.Entities;
import net.cps.common.utils.RequestType;
import net.cps.common.utils.ResponseStatus;
import net.cps.server.ocsf.AbstractServer;
import net.cps.server.ocsf.ConnectionToClient;
import net.cps.server.ocsf.SubscribedClient;
import net.cps.server.utils.Logger;
import net.cps.server.utils.MailSender;
import net.cps.server.utils.RandomCode;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.SocketException;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;


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
            switch (requestType) {
                case GET -> client.sendToClient(handleGetRequest(dbSessionFactory, request));
                case CREATE -> client.sendToClient(handleCreateRequest(dbSessionFactory, request));
                case UPDATE -> client.sendToClient(handleUpdateRequest(dbSessionFactory, request));
                case DELETE -> client.sendToClient(handleDeleteRequest(dbSessionFactory, request));
                case AUTH -> client.sendToClient(handleAuthRequest(dbSessionFactory, request));
                case CUSTOM -> {
                    // Custom Requests Handlers...
                    
                    Logger.print("Warning: The request 'CUSTOM: " + request.getHeader() + "' from the client passed unaddressed.");
                }
                default -> {
                    Logger.print("Warning: The request '" + requestType + ": " + request.getHeader() + "' from the client passed unaddressed.");
                }
            }
        }
        catch (SocketException e) {
            if (!(requestType == RequestType.AUTH && request.getHeader().startsWith("logout"))) {
                Logger.print("Warning: The socket connection for the request: '" + requestType + ": " + request.getHeader() + "' from the client has been closed.");
            }
            // else: Client has disconnected
        }
        catch (IOException e) {
            e.printStackTrace();
            Logger.print("Error: an error occurred while handling: '" + requestType + ": " + request.getHeader() + "' request from client.");
        }
        finally {
            Database.closeSession();
        }
    }
    
    private <T> @NotNull ResponseMessage handleGetRequest (SessionFactory sessionFactory, RequestMessage request) {
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
                
                String id = requestHeader;
                data = Database.getEntity(sessionFactory, T, entity.getPrimaryKeyConverter().apply(id));
                return new ResponseMessage(requestId, request, data != null ? ResponseStatus.SUCCESS : ResponseStatus.NOT_FOUND, data);
            }
            
            // get a list of entities of type `T` by one parameter.
            if (!requestHeader.contains("?") && requestHeader.contains("=")) {
                //List<T> data;
                //
                //String[] params = requestHeader.split("&");
                //SimpleEntry<String, String>[] fields = new SimpleEntry[params.length];
                //for (int i = 0 ; i < params.length ; i++) {
                //    String[] param = params[i].split("=");
                //    fields[i] = new SimpleEntry<>(param[0], param[1]);
                //}
                //data = Database.getMultipleEntities(sessionFactory, T, fields);
                //System.out.println(data);
                //System.out.println(data.size());
                //System.out.println(fields[0].getKey() + "=" + fields[0].getValue());
                //return new ResponseMessage(requestId, request, data != null ? ResponseStatus.SUCCESS : ResponseStatus.NOT_FOUND, data);
                requestHeader = requestHeader + "?";
            }
            
            // get a list of entities of type `T`.
            if (requestHeader.contains("?")) {
                List<T> data;
                
                // get the list of entities by ids.
                if (!requestHeader.contains("=")) {
                    String[] ids = requestHeader.split("\\?");
                    ArrayList<Object> primaryKeyList = (ArrayList<Object>) Arrays.stream(ids).map(entity.getPrimaryKeyConverter()).toList();
                    data = Database.getMultipleEntities(sessionFactory, T, primaryKeyList);
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
    
    private <T, U> @NotNull ResponseMessage handleCreateRequest (SessionFactory sessionFactory, RequestMessage request) {
        try {
            String query = request.getHeader().split("/")[0];
            Integer requestId = request.getId();
            Entities entity = Entities.fromString(query);
            Class<T> T = (Class<T>) entity.getEntityClass();
            Class<U> U = (Class<U>) entity.getPrimaryKeyClass();
            Object obj = request.getData();
            
            // no entity to create from the client.
            if (obj == null) {
                return new ResponseMessage(requestId, request, ResponseStatus.BAD_REQUEST, "Bad Request: response to 'CREATE: " + query + "'", null);
            }
            
            // create multiple entities by the data list given on the request.
            if (obj instanceof List) {
                ArrayList<T> data = (ArrayList<T>) request.getData();
                ArrayList<U> ids = (ArrayList<U>) Database.createMultipleEntities(sessionFactory, data);
                return new ResponseMessage(requestId, request, ids.size() == data.size() ? ResponseStatus.FINISHED : ResponseStatus.SUCCESS, ids);
            }
            
            // create one entity of type `T`.
            if (obj.getClass() == T) {
                T data = (T) request.getData();
                U id = (U) Database.createEntity(sessionFactory, data);
                return new ResponseMessage(requestId, request, id != null ? ResponseStatus.FINISHED : ResponseStatus.SUCCESS, id);
            }
            
            return new ResponseMessage(requestId, request, ResponseStatus.BAD_REQUEST, "Bad Request: response to 'CREATE: " + query + "'", null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseMessage(request.getId(), request, ResponseStatus.ERROR, "Error: response to 'CREATE: " + request.getHeader() + "'", e);
        }
    }
    
    private <T, U> @NotNull ResponseMessage handleUpdateRequest (SessionFactory sessionFactory, RequestMessage request) {
        try {
            String query = request.getHeader().split("/")[0];
            Integer requestId = request.getId();
            Entities entity = Entities.fromString(query);
            Class<T> T = (Class<T>) entity.getEntityClass();
            Class<U> U = (Class<U>) entity.getPrimaryKeyClass();
            Object obj = request.getData();
            
            // no entity to update from the client.
            if (obj == null) {
                return new ResponseMessage(requestId, request, ResponseStatus.BAD_REQUEST, "Bad Request: response to 'UPDATE: " + query + "'", null);
            }
            
            // update multiple entities by the data list given on the request.
            if (obj instanceof List) {
                ArrayList<T> data = (ArrayList<T>) request.getData();
                ArrayList<Object> ids = Database.updateMultipleEntities(sessionFactory, data);
                return new ResponseMessage(requestId, request, ids.size() == data.size() ? ResponseStatus.FINISHED : ResponseStatus.SUCCESS, ids);
            }
            
            // update one entity of type `T`.
            if (obj.getClass() == T) {
                T data = (T) request.getData();
                U id = (U) Database.updateEntity(sessionFactory, data);
                return new ResponseMessage(requestId, request, id != null ? ResponseStatus.FINISHED : ResponseStatus.SUCCESS, id);
            }
            
            return new ResponseMessage(requestId, request, ResponseStatus.BAD_REQUEST, "Bad Request: response to 'UPDATE: " + query + "'", null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseMessage(request.getId(), request, ResponseStatus.ERROR, "Error: response to 'UPDATE: " + request.getHeader() + "'", e);
        }
    }
    
    private <T, U> @NotNull ResponseMessage handleDeleteRequest (SessionFactory sessionFactory, RequestMessage request) {
        try {
            String query = request.getHeader().split("/")[0];
            Integer requestId = request.getId();
            Entities entity = Entities.fromString(query);
            Class<T> T = (Class<T>) entity.getEntityClass();
            Class<U> U = (Class<U>) entity.getPrimaryKeyClass();
            Object obj = request.getData();
            
            // no entity to delete from the client.
            if (obj == null) {
                return new ResponseMessage(requestId, request, ResponseStatus.BAD_REQUEST, "Bad Request: response to 'DELETE: " + query + "'", null);
            }
            
            // delete multiple entities by the data list given on the request.
            if (obj instanceof List) {
                ArrayList<T> data = (ArrayList<T>) request.getData();
                ArrayList<Object> ids = Database.deleteMultipleEntities(sessionFactory, data);
                return new ResponseMessage(requestId, request, ids.size() == data.size() ? ResponseStatus.FINISHED : ResponseStatus.SUCCESS, ids);
            }
            
            // delete one entity of type `T`.
            if (obj.getClass() == T) {
                T data = (T) request.getData();
                U id = (U) Database.deleteEntity(sessionFactory, data);
                return new ResponseMessage(requestId, request, id != null ? ResponseStatus.FINISHED : ResponseStatus.SUCCESS, id);
            }
            
            return new ResponseMessage(requestId, request, ResponseStatus.BAD_REQUEST, "Bad Request: response to 'UPDATE: " + query + "'", null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseMessage(request.getId(), request, ResponseStatus.ERROR, "Error: response to 'UPDATE: " + request.getHeader() + "'", e);
        }
    }
    
    private <T> @NotNull ResponseMessage handleAuthRequest (SessionFactory sessionFactory, RequestMessage request) {
        try {
            String query = request.getHeader();
            Integer requestId = request.getId();
            Object _obj = request.getData();
            
            // create a new customer account.
            if (query.startsWith("register")) {
                if (_obj.getClass() != Customer.class) {
                    return new ResponseMessage(requestId, request, ResponseStatus.BAD_REQUEST, "Bad Request: response to 'AUTH: " + query + "'. None user type entity sent on the request.", null);
                }
                
                Customer customer = (Customer) _obj;
                if (Database.getEntity(sessionFactory, Customer.class, "email", customer.getEmail()) != null || Database.getEntity(sessionFactory, Employee.class, "email", customer.getEmail()) != null) {
                    return new ResponseMessage(requestId, request, ResponseStatus.UNAUTHORIZED, "Sorry, this email address is already in use. Please enter a different email address or try logging in with that address.", null);
                }
                
                Integer id = (Integer) Database.createEntity(sessionFactory, customer);
                Customer createdCustomer = Database.getEntity(sessionFactory, Customer.class, id);
                if (createdCustomer == null) {
                    Logger.print("Error: On response to - AUTH: '" + request.getHeader() + "'.", "While creating an account with email: '" + customer.getEmail() + "', the account was not created.");
                    Logger.error("On response to - AUTH: 'register'. While creating an account with id: '" + customer.getEmail() + "', the account was not created.");
                    return new ResponseMessage(requestId, request, ResponseStatus.ERROR, "Sorry, its seems like something went wrong while creating the new account. Please try again later.", null);
                }
                if (!createdCustomer.getEmail().equals(customer.getEmail())) {
                    Logger.print("Error: On response to - AUTH: '" + request.getHeader() + "'.", "While creating an account with email: '" + customer.getEmail() + "', the email: '" + createdCustomer.getEmail() + "' was created instead.");
                    Logger.error("On response to - AUTH: 'register'. While creating an account with email: '" + customer.getEmail() + "', the email: '" + createdCustomer.getEmail() + "' was created instead.");
                    return new ResponseMessage(requestId, request, ResponseStatus.ERROR, "Sorry, its seems like something went wrong while creating the new account. Please try again later.", null);
                }
                
                return new ResponseMessage(requestId, request, ResponseStatus.FINISHED, "Account created successfully.", createdCustomer);
            }
            
            // authenticate a user login (and return the user instance on approved credentials).
            if (query.startsWith("login")) {
                String[] fields = query.split("/")[1].split("&");
                String email = fields[0].split("=")[1];
                String password = fields[1].split("=")[1];
                
                // the given email belongs to a customer.
                Customer customer = Database.getEntity(sessionFactory, Customer.class, "email", email);
                if (customer != null) {
                    if (customer.getIsActive()) {
                        return new ResponseMessage(requestId, request, ResponseStatus.UNAUTHORIZED, "Sorry, but you are currently logged in on another device. Users not allowed to connect to the system via different devices at the same time. Please log out and try again.", null);
                    }
                    if (!customer.isPasswordEquals(password)) {
                        return new ResponseMessage(requestId, request, ResponseStatus.UNAUTHORIZED, "Sorry, but the email you entered does not match the password you entered. Please check the password and try again.", null);
                    }
                    
                    customer.setIsActive(true);
                    Database.updateEntity(sessionFactory, customer);
                    return new ResponseMessage(requestId, request, ResponseStatus.SUCCESS, Entities.CUSTOMER.getClassName(), customer);
                }
                
                // the given email belongs to an employee.
                Employee employee = Database.getEntity(sessionFactory, Employee.class, "email", email);
                if (employee != null) {
                    if (employee.getIsActive()) {
                        return new ResponseMessage(requestId, request, ResponseStatus.UNAUTHORIZED, "Sorry, but you are currently logged in on another device. Users not allowed to connect to the system via different devices at the same time. Please log out and try again.", null);
                    }
                    if (!employee.isPasswordEquals(password)) {
                        return new ResponseMessage(requestId, request, ResponseStatus.UNAUTHORIZED, "Sorry, but the email you entered does not match the password you entered. Please check the password and try again.", null);
                    }
                    
                    employee.setIsActive(true);
                    Database.updateEntity(sessionFactory, employee);
                    return new ResponseMessage(requestId, request, ResponseStatus.SUCCESS, Entities.EMPLOYEE.getClassName(), employee);
                }
                
                // the given email do not exist on the database.
                return new ResponseMessage(requestId, request, ResponseStatus.UNAUTHORIZED, "Sorry, but the email you entered does not exist in our system. Please check the email and try again, or sign-up for a new account.", null);
            }
            
            // logout a user from the system.
            if (query.startsWith("logout")) {
                if (_obj.getClass() == Customer.class) {
                    ((Customer) _obj).setIsActive(false);
                }
                else if (_obj.getClass() == Employee.class) {
                    ((Employee) _obj).setIsActive(false);
                }
                
                Database.updateEntity(sessionFactory, _obj);
                return new ResponseMessage(requestId, request, ResponseStatus.SUCCESS);
            }
            
            // reset a user password.
            if (query.startsWith("forgot-password")) {
                String email = query.split("=")[1];
                String resetCode = RandomCode.generate(6);
                
                // the given email belongs to a customer.
                Customer customer = Database.getEntity(sessionFactory, Customer.class, "email", email);
                if (customer != null) {
                    MailSender.sendMail(resetCode, email, customer.getFullName(), "passwordReset", null, null, null, null);
                    
                    return new ResponseMessage(requestId, request, ResponseStatus.SUCCESS, Entities.CUSTOMER.getClassName() + "/" + resetCode, customer);
                }
    
                // the given email belongs to an employee.
                Employee employee = Database.getEntity(sessionFactory, Employee.class, "email", email);
                if (employee != null) {
                    MailSender.sendMail(resetCode, email, employee.getFullName(), "passwordReset", null, null, null, null);
                    
                    return new ResponseMessage(requestId, request, ResponseStatus.SUCCESS, Entities.EMPLOYEE.getClassName() + "/" + resetCode, employee);
                }
                
                return new ResponseMessage(requestId, request, ResponseStatus.UNAUTHORIZED, "Sorry, but the email you entered does not exist in our system. Please check the email and try again, or sign-up for a new account.", null);
            }
            
            return new ResponseMessage(requestId, request, ResponseStatus.BAD_REQUEST, "Bad Request: response to 'AUTH: " + query + "'.", null);
        }
        catch (Throwable e) {
            e.printStackTrace();
            return new ResponseMessage(request.getId(), request, ResponseStatus.ERROR, e.getMessage(), e);
        }
    }
}
