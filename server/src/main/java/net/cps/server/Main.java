package net.cps.server;

import net.cps.common.entities.Customer;
import net.cps.common.entities.Employee;
import net.cps.common.entities.Management;
import net.cps.common.entities.ParkingLot;
import net.cps.common.utils.EmployeeRole;
import net.cps.server.utils.Logger;
import net.cps.server.utils.MySQLQueries;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Timer;


/**
 * Server side main class (entry point).
 **/
public class Main {
    private static final Integer DEFAULT_PORT = 3000;
    private static CPSServer server;
    private static SessionFactory dbSessionFactory;
    
    /**
     * Server entry point.
     **/
    public static void main (String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        
        try {
            Logger.init("server.log");
            
            Logger.print("logger initialized successfully.", "'.log' file created at: " + Logger.getLogFile());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            dbSessionFactory = Database.getSessionFactory();
            Logger.print("database connection created successfully.", "database type: '" + Database.getDatabaseType() + "'", "database name: '" + Database.getDatabaseName() + "'", "database port: '" + Database.getDatabasePort() + "'");
            
            createTables(dbSessionFactory);
            createDummyData(dbSessionFactory);
            Logger.print("database initialized successfully.");
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        
        try {
            server = CPSServer.getServer(port);
            server.listen();
            
            Logger.print("server created successfully.", "running via host: '" + server.getHost() + "'", "listening on port: '" + server.getPort() + "'");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        // !!!!!!!!!!!!!!!!!!!!!!!!
        // !!! v Test Methods v !!!
        sendEmail("test@tset", "test", "test message");
        printMessageOnGivenTime("test message", LocalDateTime.now().plusSeconds(10));
        // !!! ^ Test Methods ^ !!!
        // !!!!!!!!!!!!!!!!!!!!
    }
    
    /**
     * Creates the database tables - clear all tables from 'cps_db' and creating a new clean tables.
     **/
    private static void createTables (SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        
        try {
            String databaseName = Database.getDatabaseName();
            
            session.createSQLQuery(MySQLQueries.DROP_DATABASE + databaseName).executeUpdate();
            session.createSQLQuery(MySQLQueries.CREATE_DATABASE + databaseName).executeUpdate();
            session.createSQLQuery(MySQLQueries.USE_DATABASE + databaseName).executeUpdate();
            
            session.createNativeQuery(MySQLQueries.CREATE_TABLE + MySQLQueries.ORGANIZATIONS_TABLE).executeUpdate();
            session.createNativeQuery(MySQLQueries.CREATE_TABLE + MySQLQueries.MANAGEMENTS_TABLE).executeUpdate();
            session.createNativeQuery(MySQLQueries.CREATE_TABLE + MySQLQueries.PARKING_LOTS_TABLE).executeUpdate();
            session.createNativeQuery(MySQLQueries.CREATE_TABLE + MySQLQueries.RATES_TABLE).executeUpdate();
            session.createNativeQuery(MySQLQueries.CREATE_TABLE + MySQLQueries.EMPLOYEES_TABLE).executeUpdate();
            session.createNativeQuery(MySQLQueries.CREATE_TABLE + MySQLQueries.CUSTOMERS_TABLE).executeUpdate();
            
            session.flush();
            session.getTransaction().commit();
            session.clear();
            
            Logger.print("database tables created successfully.");
            
        }
        catch (HibernateException e) {
            Logger.print("Error: database tables creation failed.", "ended with error: " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            assert session != null;
            session.close();
        }
        
    }
    
    /**
     * Create a dummy initial data using the 'Database' methods.
     **/
    private static void createDummyData (SessionFactory sessionFactory) {
        try {
            ArrayList<Customer> customers = new ArrayList<>();
            customers.add(new Customer("netanelshlomo@gmail.com", "357357357", "Netanel", "Shlomo", "123456"));
            customers.add(new Customer("john.doe@gmail.com", "321321321", "John", "Doe", "123456"));
            customers.add(new Customer("jane.doe@gmail.com", "456456465", "Jane", "Doe", "123456"));
            customers.add(new Customer("bob.smith@gmail.com", "654654654", "Bob", "Smith", "123456"));
            customers.add(new Customer("alice.smith@gmail.com", "258258258", "Alice", "Smith", "123456"));
            customers.add(new Customer("foo.bar@gmail.com", "987987987", "Foo", "Bar", "123456"));
            Database.createMultipleEntities(sessionFactory, customers);
            
            ArrayList<ParkingLot> parkingLots = new ArrayList<>();
            parkingLots.add(new ParkingLot("Haifa Port Parking", "Ha-Namal", 36, "Haifa", "IL", 5));
            parkingLots.add(new ParkingLot("Haifa Mt. Carmel Parking", "Haim HaZaz",7, "Haifa", "IL", 1));
            parkingLots.add(new ParkingLot("Kiryat-Haim Beach Parking", "Sderot HaNassi Truman", 10, "Haifa", "IL", 2));
            parkingLots.add(new ParkingLot("Eilat Coral Beach Parking", "Izmargad Ev. Coral Beach" ,13, "Eilat", "IL", 11));
            parkingLots.get(0).setRates(6.0, 5.0, 60.0, null, 75.0);
            parkingLots.get(1).setRates(5.5, 3.5, 60.0, 54.0, 72.0);
            parkingLots.get(2).setRates(12.0, 10.0, null, 54.0, 72.0);
            parkingLots.get(3).setRates(null, 7.0, 60.0, 54.0, 82.0);
            Database.createMultipleEntities(sessionFactory, parkingLots);
            
            Database.createEntity(sessionFactory, new Management("aaa", "aaa", 7, "Haifa", "IL"));
            
            ArrayList<Employee> employees = new ArrayList<>();
            employees.add(new Employee("amirdhdlive@gmail.com", "Amir", "David", "amir123", EmployeeRole.NETWORK_MANAGER, parkingLots.get(0)));
            employees.add(new Employee("harel.avraham@gmail.com", "Harel", "Avraham", "harel123", EmployeeRole.CUSTOMER_SERVICE_EMPLOYEE, parkingLots.get(0)));
            employees.add(new Employee("shelly.brezner@gmail.com", "Shelly", "Brezner", "shelly123", EmployeeRole.PARKING_LOT_MANAGER, parkingLots.get(1)));
            employees.add(new Employee("einat.lasry@gmail.com", "Einat", "Lasry", "einat123", EmployeeRole.PARKING_LOT_EMPLOYEE, parkingLots.get(1)));
            employees.add(new Employee("yoav.furer@gmail.com", "Yoav", "Furer", "yoav123", EmployeeRole.PARKING_LOT_MANAGER, parkingLots.get(2)));
            Database.createMultipleEntities(sessionFactory, employees);
        }
        catch (HibernateException e) {
            Logger.print("Error: database dummy data creation failed.", "ended with error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    /* -------------------------------------------------------------------------------------------- */
    /* ------- v Test Methods v ------------------------------------------------------------------- */
    
    private static void sendEmail (String to, String subject, String body) {
        // TODO: implement...
    }
    
    private static void printMessageOnGivenTime (String message, LocalDateTime time) {
        // TODO: implement...
        Timer timer = new Timer();
    }
    
    /* ------- ^ Test Methods ^ ------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------------- */
    
    
}
