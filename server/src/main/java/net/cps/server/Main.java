package net.cps.server;

import net.cps.common.entities.*;
import net.cps.common.utils.*;
import net.cps.server.utils.Logger;
import net.cps.server.utils.MySQLQueries;
import net.cps.server.utils.ScheduledTaskService;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


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
        
        
        ScheduledTaskService scheduledTaskService = new ScheduledTaskService();
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
    
            for (Entities entity : Entities.values()) {
                session.createNativeQuery(MySQLQueries.CREATE_TABLE + entity.getTableName() + entity.getTableQuery()).executeUpdate();
            }
            
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
            Vehicle vehicle = new Vehicle("12345678", customers.get(0));
            vehicles.add(vehicle);
            customers.get(0).addVehicle(vehicle);
            vehicle = new Vehicle("87654321", customers.get(0));
            vehicles.add(vehicle);
            customers.get(0).addVehicle(vehicle);
            vehicle = new Vehicle("14725836", customers.get(1));
            vehicles.add(vehicle);
            customers.get(1).addVehicle(vehicle);
            vehicle = new Vehicle("96385274", customers.get(2));
            vehicles.add(vehicle);
            customers.get(2).addVehicle(vehicle);
            vehicle = new Vehicle("25836974", customers.get(3));
            vehicles.add(vehicle);
            customers.get(3).addVehicle(vehicle);
            vehicle = new Vehicle("74185296", customers.get(4));
            vehicles.add(vehicle);
            customers.get(4).addVehicle(vehicle);
            vehicle = new Vehicle("85274196", customers.get(5));
            vehicles.add(vehicle);
            customers.get(5).addVehicle(vehicle);
            Database.createMultipleEntities(sessionFactory, vehicles);
            
            // Create a list of Subscriptions for Customers.
            ArrayList<Subscription> subscriptions = new ArrayList<>();
            Subscription subscription;
            subscription = new Subscription(customers.get(0), null, SubscriptionType.PREMIUM, List.of(new Vehicle[] {vehicles.get(0)}), LocalTime.of(0, 0));
            subscriptions.add(subscription);
            customers.get(0).addSubscription(subscription);
            subscription = new Subscription(customers.get(0), parkingLots.get(0), SubscriptionType.BASIC, List.of(new Vehicle[] {vehicles.get(0), vehicles.get(1)}), LocalTime.of(23, 59));
            subscriptions.add(subscription);
            customers.get(0).addSubscription(subscription);
            subscription = new Subscription(customers.get(1), parkingLots.get(0), SubscriptionType.BASIC, List.of(new Vehicle[] {vehicles.get(2)}), LocalTime.of(12, 30));
            subscriptions.add(subscription);
            customers.get(1).addSubscription(subscription);
            Database.createMultipleEntities(sessionFactory, subscriptions);
            
            // Reservations
            ArrayList<Reservation> reservations = new ArrayList<>();
            Calendar departure = Calendar.getInstance();
            Calendar arrival = Calendar.getInstance();
            departure.set(2023, Calendar.JANUARY, 24, 12, 30, 0);
            arrival.set(2023, Calendar.JANUARY, 24, 14, 30, 0);
            Reservation reservation;
            reservation = new Reservation(parkingLots.get(0), customers.get(0), vehicles.get(1), (Calendar) departure.clone(), (Calendar) arrival.clone());
            reservations.add(reservation);
            parkingLots.get(0).addReservation(reservation);
            vehicles.get(1).getReservations().add(reservation);
            departure.set(2023, Calendar.OCTOBER, 18, 10, 30, 0);
            arrival.set(2023, Calendar.OCTOBER, 18, 16, 30, 0);
            reservation = new Reservation(parkingLots.get(1), customers.get(1), vehicles.get(2), (Calendar) departure.clone(), (Calendar) arrival.clone());
            reservations.add(reservation);
            parkingLots.get(1).addReservation(reservation);
            vehicles.get(2).getReservations().add(reservation);
            arrival.set(2023, Calendar.JUNE, 12, 8, 30, 0);
            departure.set(2023, Calendar.JUNE, 12, 10, 30, 0);
            reservation = new Reservation(parkingLots.get(0), customers.get(0), vehicles.get(0), (Calendar) departure.clone(), (Calendar) arrival.clone());
            reservations.add(reservation);
            parkingLots.get(0).addReservation(reservation);
            vehicles.get(0).getReservations().add(reservation);
            reservations.get(2).setEntryTime((Calendar) arrival.clone());
            arrival.set(2023, Calendar.JUNE, 12, 11, 35, 0);
            departure.set(2023, Calendar.JUNE, 12, 13, 0, 0);
            reservation = new Reservation(parkingLots.get(0), customers.get(3), vehicles.get(4), (Calendar) departure.clone(), (Calendar) arrival.clone());
            reservations.add(reservation);
            parkingLots.get(0).addReservation(reservation);
            vehicles.get(4).getReservations().add(reservation);
            arrival.add(Calendar.MINUTE, 15);
            reservations.get(3).setEntryTime((Calendar) arrival.clone());
            arrival.set(2023, Calendar.JUNE, 12, 13, 0, 0);
            departure.set(2023, Calendar.JUNE, 12, 14, 0, 0);
            reservation = new Reservation(parkingLots.get(0), customers.get(4), vehicles.get(5), (Calendar) departure.clone(), (Calendar) arrival.clone());
            reservations.add(reservation);
            parkingLots.get(0).addReservation(reservation);
            vehicles.get(5).getReservations().add(reservation);
            arrival.add(Calendar.MINUTE, 35);
            reservations.get(4).setEntryTime((Calendar) arrival.clone());
            Database.createMultipleEntities(sessionFactory, reservations);
            
            // Complaints
            ArrayList<Complaint> complaints = new ArrayList<>();
            Complaint complaint;
            complaint = new Complaint(customers.get(0), "I was charged for a reservation that I didn't make.");
            complaints.add(complaint);
            complaint = new Complaint(customers.get(1), "My reservation was cancelled without my consent.");
            complaints.add(complaint);
            Database.createMultipleEntities(sessionFactory, complaints);
            
            // Test Database
            //testDatabase(sessionFactory, parkingLots, customers, vehicles, subscriptions, reservations, complaints);
        }
        catch (HibernateException e) {
            Logger.print("Error: database dummy data creation failed.", "ended with error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    /**
     * Test database.
     */
    private static void testDatabase (SessionFactory sessionFactory, ArrayList<ParkingLot> parkingLots, ArrayList<Customer> customers, ArrayList<Vehicle> vehicles, ArrayList<Subscription> subscriptions, ArrayList<Reservation> reservations, ArrayList<Complaint> complaints) {
        //einat&shelli
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        for (ParkingLot parkingLot : parkingLots) {
            session.update(parkingLot);
            session.flush();
        }
        
        transaction.commit();
        session.clear();
        session.close();
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        
        for (Customer customer : customers) {
            session.update(customer);
            session.flush();
        }
        transaction.commit();
        session.clear();
        session.close();
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        
        for (Vehicle vehicle1 : vehicles) {
            session.update(vehicle1);
            session.flush();
        }
        transaction.commit();
        session.clear();
        session.close();
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        
        for (Subscription subscription1 : subscriptions) {
            session.update(subscription1);
            session.flush();
        }
        transaction.commit();
        session.clear();
        session.close();
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        
        for (Reservation reservation1 : reservations) {
            session.update(reservation1);
            session.flush();
        }
        transaction.commit();
        session.clear();
        session.close();
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        
        for (Complaint complaint1 : complaints) {
            session.update(complaint1);
            session.flush();
        }
        transaction.commit();
        session.clear();
        session.close();
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        
        // Daily Statistics
        ArrayList<DailyStatistics> dailyStatistics = new ArrayList<>();
        DailyStatistics dailyStatistics1;
        ArrayList<Reservation> reservations1;
        for (ParkingLot parkingLot : parkingLots) {
            reservations1 = new ArrayList<Reservation>(parkingLot.getReservations());
            dailyStatistics1 = new DailyStatistics(parkingLot, reservations1);
            dailyStatistics.add(dailyStatistics1);
            if (dailyStatistics1 != null) System.out.println("daily statistics:" + dailyStatistics1);
        }
        Database.createMultipleEntities(sessionFactory, dailyStatistics);
        // insert the databases to sql server
        
        //EINAT TEST STATISTICS
        
        ArrayList<Reservation> reservations12;
        // Reservation reservation = new Reservation();
        // un correct data
        parkingLots.get(0).setReservations(reservations);
        reservations12 = new ArrayList<Reservation>(parkingLots.get(0).getReservations());
        ArrayList<Reservation> reservations2 = new ArrayList<Reservation>();
        int i = 0;
        for (Reservation reservation : reservations12) {
            Calendar calendar1 = Calendar.getInstance();
            reservation.setArrivalTime(calendar1);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.add(Calendar.HOUR_OF_DAY, (i * 2) % 7);
            reservation.setEntryTime(calendar2);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.add(Calendar.HOUR_OF_DAY, i + 3);
            reservation.setDepartureTime(calendar3);
            reservation.setStatus(ReservationStatus.CHECKED_OUT);
            reservations2.add(reservation);
            i++;
        }
        i = 0;
        for (Reservation reservation : reservations2) {
            System.out.println("reservation2 " + i + " entry: " + reservation.getEntryTime().getTime() + "arrival: " + reservation.getArrivalTime().getTime());
            i++;
        }
        DailyStatistics dailyStatistics12 = new DailyStatistics(parkingLots.get(0), reservations2);
        System.out.println("lates: " + dailyStatistics12.getTotalLatency());
        System.out.println("EINAT statistics med: " + dailyStatistics12.getDailyMedianLatency());
        System.out.println("EINAT statistics avg: " + dailyStatistics12.getDailyAverageLatency());
        
        //statistics
        ArrayList<ParkingLot> parkingLots1 = Database.getAllEntities(Database.getSessionFactory(), ParkingLot.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String cal = LocalDate.now().format(formatter);
        for (ParkingLot parkingLot : parkingLots1) {
            ArrayList<Reservation> reservations123 = Database.getCustomQuery(Database.getSessionFactory(), Reservation.class, "SELECT * FROM " + Entities.RESERVATION.getTableName() + " WHERE DATE(arrival_time) = '" + cal + "' AND parking_lot_id='" + parkingLot.getId() + "'");
            System.out.println("HI EINAT1");
            if (reservations123 == null || reservations123.size() == 0) return;
            DailyStatistics dailyStatistics123 = new DailyStatistics(parkingLot, reservations123);
            System.out.println("EINAT2" + dailyStatistics123.getTotalReservations());
        }
        
        
        System.out.println("********TEST EINAT AND SHELLI\n*******");
        
        parkingLots.clear();
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ParkingLot> query = builder.createQuery(ParkingLot.class);
        query.from(ParkingLot.class);
        List<ParkingLot> data = session.createQuery(query).getResultList();
        reservations.clear();
        reservations = Database.getAllEntities(sessionFactory, Reservation.class);
        
        for (ParkingLot parkingLot : data) {
            // System.out.println("--------"+parkingLot.getId()+" "+parkingLot.getName()+" rates: "+parkingLot.getRates());
            Robot robot = new Robot(parkingLot); //must!
            for (Reservation reservation2 : reservations) {
                String parkingLotName = parkingLot.getName();
                String reservationparkingLotName = reservation2.getParkingLot().getName();
                System.out.println(parkingLotName.equals(reservationparkingLotName));
                if (parkingLotName.equals(reservationparkingLotName)) {
                    //insert! return boolean for success or fail
                    robot.insert(reservation2.getVehicle(), reservation2);
                    ArrayList<ParkingSpace> parkingSpacesRobot = robot.FromArrayToList();
                    for (ParkingSpace parkingSpace : parkingSpacesRobot) { //db
                        session.update(parkingSpace);
                        session.flush();
                    }
                    transaction.commit();
                    session.clear();
                    session.close();
                    session = sessionFactory.openSession();
                    transaction = session.beginTransaction();
                }
            }
            //test: remove
            for (Reservation reservation2 : reservations) {
                String parkingLotName = parkingLot.getName();
                String reservationparkingLotName = reservation2.getParkingLot().getName();
                if (parkingLotName.equals(reservationparkingLotName)) {
                    //remove! return boolean for success or fail
                    robot.remove(reservation2.getVehicle());
                    ArrayList<ParkingSpace> parkingSpacesRobot = robot.FromArrayToList();
                    for (ParkingSpace parkingSpace : parkingSpacesRobot) { //db
                        session.update(parkingSpace);
                        session.flush();
                    }
                    transaction.commit();
                    session.clear();
                    session.close();
                    session = sessionFactory.openSession();
                    transaction = session.beginTransaction();
                }
            }
            //test: change status parking space
            robot.setConditionToPlace(0, 0, 0, ParkingSpaceState.DISABLED);
            ArrayList<ParkingSpace> parkingSpacesRobot = robot.FromArrayToList();
            for (ParkingSpace parkingSpace : parkingSpacesRobot) { //db
                session.update(parkingSpace);
                session.flush();
            }
            transaction.commit();
            session.clear();
            session.close();
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            robot.setConditionToPlace(1, 0, 0, ParkingSpaceState.RESERVED);
            parkingSpacesRobot = robot.FromArrayToList();
            for (ParkingSpace parkingSpace : parkingSpacesRobot) { //db
                session.update(parkingSpace);
                session.flush();
            }
            transaction.commit();
            session.clear();
            session.close();
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            
            
        }
        System.out.println("end test");
    }
}
