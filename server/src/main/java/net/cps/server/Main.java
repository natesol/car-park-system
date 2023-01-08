package net.cps.server;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Timer;

import net.cps.common.utils.EmployeeRole;
import net.cps.server.utils.MySQLQueries;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import net.cps.common.entities.Customer;
import net.cps.common.entities.Employee;
import net.cps.common.entities.ParkingLot;
import net.cps.common.entities.Rates;
import net.cps.server.utils.Logger;


/**
 * Server side main class (entry point).
 */
public class Main {
    private static final int DEFAULT_PORT = 3000;
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
            createDummyData1(dbSessionFactory);
            Logger.print("database initialized successfully.");
        }
        catch (HibernateException e) {
            e.printStackTrace();
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
            
            session.createNativeQuery(MySQLQueries.CREATE_TABLE_PARKING_LOTS).executeUpdate();
            session.createNativeQuery(MySQLQueries.CREATE_TABLE_RATES).executeUpdate();
            session.createNativeQuery(MySQLQueries.CREATE_TABLE_EMPLOYEES).executeUpdate();
            session.createNativeQuery(MySQLQueries.CREATE_TABLE_CUSTOMERS).executeUpdate();
            
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
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        
        try {
            session.createNativeQuery("""
                        INSERT INTO parking_lots (name, address, floor_width)
                        VALUES
                            ("parking #1", "haifa 123", 5),
                            ("parking #2", "haifa 61/4", 1),
                            ("parking #3", "tel-aviv 99", 2),
                            ("parking #4", "eilat 7", 11);
                    """).executeUpdate();
            session.createNativeQuery("""
                        INSERT INTO rates (
                            id,
                            hourly_occasional_parking,
                            hourly_onetime_parking,
                            regular_subscription_single_vehicle,
                            regular_subscription_multiple_vehicles,
                            full_subscription_single_vehicle
                        )
                        VALUES
                            (1, 8, 7, 60, 54, 72),
                            (2, 8, 7, 60, 54, 72),
                            (3, 8, 7, 60, 54, 72),
                            (4, 8, 7, 60, 54, 72);
                    """).executeUpdate();
            
            Rates rates2 = session.get(Rates.class, 2);
            rates2.setHourlyOccasionalParking(5.5);
            rates2.setHourlyOnetimeParking(3.5);
            session.update(rates2);
            Rates rates3 = session.get(Rates.class, 3);
            rates3.setHourlyOccasionalParking(12);
            rates3.setHourlyOnetimeParking(10);
            session.update(rates3);
            
            session.save(new Employee("test", "test", "test@test", "test", EmployeeRole.EMPLOYEE));
            session.save(new Employee("netanel", "shlomo", "netanel@gmail", "123456", EmployeeRole.EMPLOYEE));
            
            session.save(new Customer("amir", "david", "amirdenekamp@gmail.com", "1234"));
            
            session.flush();
            session.getTransaction().commit();
            session.clear();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        finally {
            assert session != null;
            session.close();
        }
    }
    
    
    private static void createDummyData1 (SessionFactory sessionFactory) {
        try {
            ArrayList<Customer> customers = new ArrayList<>();
            customers.add(new Customer("John", "Doe", "johndoe@gmail.com", "123456789"));
            customers.add(new Customer("Jane", "Doe", "janedoe@gmail.com", "987654321"));
            customers.add(new Customer("Bob", "Smith", "bobsmith@gmail.com", "123987456"));
            Database.addMultipleEntities(sessionFactory, customers);
            
            ArrayList<Employee> employees = new ArrayList<>();
            employees.add(new Employee("Amir", "David", "amirdenekamp@gmail.com", "amir123", EmployeeRole.NETWORK_MANAGER));
            employees.add(new Employee("Jane", "Doe", "janedoe@gmail.com", "jane123", EmployeeRole.EMPLOYEE));
            employees.add(new Employee("Bob", "Smith", "bobsmith@gmail.com", "bob123", EmployeeRole.EMPLOYEE));
            Database.addMultipleEntities(sessionFactory, employees);
            
            ArrayList<ParkingLot> parkingLots = new ArrayList<>();
            parkingLots.add(new ParkingLot("parking #1", "haifa 123", 5));
            parkingLots.add(new ParkingLot("parking #2", "haifa 61/4", 1));
            parkingLots.add(new ParkingLot("parking #3", "tel-aviv 99", 2));
            parkingLots.add(new ParkingLot("parking #4", "eilat 7", 11));
            parkingLots.get(0).setRates(6.0, 5.0, 60.0, null, 75.0);
            parkingLots.get(1).setRates(5.5, 3.5, 60.0, 54.0, 72.0);
            parkingLots.get(2).setRates(12.0, 10.0, null, 54.0, 72.0);
            parkingLots.get(3).setRates(null, 7.0, 60.0, 54.0, 82.0);
            Database.addMultipleEntities(sessionFactory, parkingLots);
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
