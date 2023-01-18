package net.cps.server;

import net.cps.common.entities.*;
import net.cps.common.utils.EmployeeRole;
import net.cps.common.utils.OrganizationType;
import net.cps.common.utils.SubscriptionType;
import net.cps.server.utils.Logger;
import net.cps.server.utils.MySQLQueries;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
            
            session.createNativeQuery(MySQLQueries.CREATE_ORGANIZATIONS_TABLE).executeUpdate();
            session.createNativeQuery(MySQLQueries.CREATE_OFFICES_TABLE).executeUpdate();
            session.createNativeQuery(MySQLQueries.CREATE_PARKING_LOTS_TABLE).executeUpdate();
            session.createNativeQuery(MySQLQueries.CREATE_RATES_TABLE).executeUpdate();
            session.createNativeQuery(MySQLQueries.CREATE_EMPLOYEES_TABLE).executeUpdate();
            session.createNativeQuery(MySQLQueries.CREATE_CUSTOMERS_TABLE).executeUpdate();
            session.createNativeQuery(MySQLQueries.CREATE_SUBSCRIPTIONS_TABLE).executeUpdate();
            session.createNativeQuery(MySQLQueries.CREATE_VEHICLES_TABLE).executeUpdate();
            session.createNativeQuery(MySQLQueries.CREATE_PARKING_SPACES_TABLE).executeUpdate();
            session.createNativeQuery(MySQLQueries.CREATE_RESERVATIONS_TABLE).executeUpdate();
            session.createNativeQuery(MySQLQueries.CREATE_COMPLAINTS_TABLE).executeUpdate();
            session.createNativeQuery(MySQLQueries.CREATE_DAILY_STATISTICS_TABLE).executeUpdate();
            
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
            // Create the Management.
            Office management = new Office(OrganizationType.MANAGEMENT, "CityPark HQ - Haifa Port Parking", "Ha-Namal", 36, "Haifa", "IL");
            Database.createEntity(sessionFactory, management);
            
            // Create a list of Parking Lots (with rates table for each).
            ArrayList<ParkingLot> parkingLots = new ArrayList<>();
            parkingLots.add(new ParkingLot("Haifa Port Parking", "Ha-Namal", 36, "Haifa", "IL", 5));
            parkingLots.add(new ParkingLot("Haifa Mt. Carmel Parking", "Haim HaZaz", 7, "Haifa", "IL", 1));
            parkingLots.add(new ParkingLot("Kiryat-Haim Beach Parking", "Sderot HaNassi Truman", 10, "Haifa", "IL", 2));
            parkingLots.add(new ParkingLot("Eilat Coral Beach Parking", "Izmargad Ev. Coral Beach", 13, "Eilat", "IL", 11));
            parkingLots.get(0).setRates(6.0, 5.0, 60.0, null, 75.0);
            parkingLots.get(1).setRates(5.5, 3.5, 60.0, 54.0, 72.0);
            parkingLots.get(2).setRates(12.0, 10.0, null, 54.0, 72.0);
            parkingLots.get(3).setRates(null, 7.0, 60.0, 54.0, 82.0);
            Database.createMultipleEntities(sessionFactory, parkingLots);
            
            // Create a list of Employees.
            ArrayList<Employee> employees = new ArrayList<>();
            employees.add(new Employee("amirdhdlive@gmail.com", "Amir", "David", "amir123", EmployeeRole.NETWORK_MANAGER, management));
            employees.add(new Employee("harel.avraham@gmail.com", "Harel", "Avraham", "harel123", EmployeeRole.CUSTOMER_SERVICE_EMPLOYEE, management));
            employees.add(new Employee("shelly.brezner@gmail.com", "Shelly", "Brezner", "shelly123", EmployeeRole.PARKING_LOT_MANAGER, parkingLots.get(0)));
            employees.add(new Employee("einat.lasry@gmail.com", "Einat", "Lasry", "einat123", EmployeeRole.PARKING_LOT_EMPLOYEE, parkingLots.get(0)));
            employees.add(new Employee("yoav.furer@gmail.com", "Yoav", "Furer", "yoav123", EmployeeRole.PARKING_LOT_MANAGER, parkingLots.get(1)));
            employees.add(new Employee("sarah.smith@gmail.com", "Sarah", "Smith", "sarah123", EmployeeRole.PARKING_LOT_EMPLOYEE, parkingLots.get(1)));
            employees.add(new Employee("mike.johnson@gmail.com", "Mike", "Johnson", "mike123", EmployeeRole.PARKING_LOT_MANAGER, parkingLots.get(2)));
            employees.add(new Employee("emily.brown@gmail.com", "Emily", "Brown", "emily123", EmployeeRole.PARKING_LOT_EMPLOYEE, parkingLots.get(2)));
            employees.add(new Employee("matthew.davis@gmail.com", "Matthew", "Davis", "matthew123", EmployeeRole.PARKING_LOT_MANAGER, parkingLots.get(3)));
            employees.add(new Employee("ashley.miller@gmail.com", "Ashley", "Miller", "ashley123", EmployeeRole.PARKING_LOT_EMPLOYEE, parkingLots.get(3)));
            Database.createMultipleEntities(sessionFactory, employees);
            
            // Create a list of Customers.
            ArrayList<Customer> customers = new ArrayList<>();
            customers.add(new Customer("netanelshlomo@gmail.com", "Netanel", "Shlomo", "123456"));
            customers.add(new Customer("john.doe@gmail.com", "John", "Doe", "123456"));
            customers.add(new Customer("jane.doe@gmail.com", "Jane", "Doe", "123456"));
            customers.add(new Customer("bob.smith@gmail.com", "Bob", "Smith", "123456"));
            customers.add(new Customer("alice.smith@gmail.com", "Alice", "Smith", "123456"));
            customers.add(new Customer("foo.bar@gmail.com", "Foo", "Bar", "123456"));
            Database.createMultipleEntities(sessionFactory, customers);
            
            // Create a list of Vehicles.
            ArrayList<Vehicle> vehicles = new ArrayList<>();
            vehicles.add(new Vehicle("12345678", customers.get(0)));
            vehicles.add(new Vehicle("87654321", customers.get(0)));
            vehicles.add(new Vehicle("14725836", customers.get(1)));
            vehicles.add(new Vehicle("96385274", customers.get(2)));
            vehicles.add(new Vehicle("25836974", customers.get(3)));
            vehicles.add(new Vehicle("74185296", customers.get(4)));
            vehicles.add(new Vehicle("85274196", customers.get(5)));
            Database.createMultipleEntities(sessionFactory, vehicles);
            
            // Create a list of Subscriptions for Customers.
            ArrayList<Subscription> subscriptions = new ArrayList<>();
            subscriptions.add(new Subscription(customers.get(0), null, SubscriptionType.PREMIUM, List.of(new Vehicle[] {vehicles.get(0)}), LocalTime.of(0, 0)));
            subscriptions.add(new Subscription(customers.get(0), parkingLots.get(0), SubscriptionType.BASIC, List.of(new Vehicle[] {vehicles.get(0), vehicles.get(1)}), LocalTime.of(23, 59)));
            subscriptions.add(new Subscription(customers.get(1), parkingLots.get(0), SubscriptionType.BASIC, List.of(new Vehicle[] {vehicles.get(2)}), LocalTime.of(12, 30)));
            Database.createMultipleEntities(sessionFactory, subscriptions);
            
            // Reservations
            ArrayList<Reservation> reservations = new ArrayList<>();
            Calendar departure = Calendar.getInstance();
            Calendar arrival = Calendar.getInstance();
            departure.set(2023, Calendar.JANUARY, 24, 12, 30, 0);
            arrival.set(2023, Calendar.JANUARY, 24, 14, 30, 0);
            reservations.add(new Reservation(parkingLots.get(0), customers.get(0), vehicles.get(1), (Calendar) departure.clone(), (Calendar) arrival.clone()));
            departure.set(2023, Calendar.OCTOBER, 18, 10, 30, 0);
            arrival.set(2023, Calendar.OCTOBER, 18, 16, 30, 0);
            reservations.add(new Reservation(parkingLots.get(1), customers.get(1), vehicles.get(2), (Calendar) departure.clone(), (Calendar) arrival.clone()));
            
            // ended reservations
            arrival.set(2023, Calendar.JUNE, 12, 8, 30, 0);
            departure.set(2023, Calendar.JUNE, 12, 10, 30, 0);
            reservations.add(new Reservation(parkingLots.get(0), customers.get(0), vehicles.get(0), (Calendar) departure.clone(), (Calendar) arrival.clone()));
            reservations.get(2).setEntryTime((Calendar) arrival.clone());
            
            arrival.set(2023, Calendar.JUNE, 12, 11, 35, 0);
            departure.set(2023, Calendar.JUNE, 12, 13, 0, 0);
            reservations.add(new Reservation(parkingLots.get(0), customers.get(3), vehicles.get(4), (Calendar) departure.clone(), (Calendar) arrival.clone()));
            arrival.add(Calendar.MINUTE, 15);
            reservations.get(3).setEntryTime((Calendar) arrival.clone());
            
            arrival.set(2023, Calendar.JUNE, 12, 13, 0, 0);
            departure.set(2023, Calendar.JUNE, 12, 14, 0, 0);
            reservations.add(new Reservation(parkingLots.get(0), customers.get(4), vehicles.get(5), (Calendar) departure.clone(), (Calendar) arrival.clone()));
            arrival.add(Calendar.MINUTE, 35);
            reservations.get(4).setEntryTime((Calendar) arrival.clone());
            Database.createMultipleEntities(sessionFactory, reservations);
            
            // Complaints
            ArrayList<Complaint> complaints = new ArrayList<>();
            complaints.add(new Complaint(customers.get(0), "I was charged for a reservation that I didn't make."));
            complaints.add(new Complaint(customers.get(1), "My reservation was cancelled without my consent."));
            Database.createMultipleEntities(sessionFactory, complaints);
            
            // Daily Statistics
            ArrayList<DailyStatistics> dailyStatistics = new ArrayList<>();
            ArrayList<Reservation> reservationsForStatistics = new ArrayList<>();
            reservationsForStatistics.add(reservations.get(2));
            reservationsForStatistics.add(reservations.get(3));
            reservationsForStatistics.add(reservations.get(4));
            dailyStatistics.add(new DailyStatistics(parkingLots.get(0), reservationsForStatistics));
            Database.createMultipleEntities(sessionFactory, dailyStatistics);
        }
        catch (HibernateException e) {
            Logger.print("Error: database dummy data creation failed.", "ended with error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
