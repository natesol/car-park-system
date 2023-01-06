package net.cps.server;

import net.cps.entities.hibernate.*;
import net.cps.server.utils.Database;
import net.cps.server.utils.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static final int DEFAULT_PORT = 3000;
    private static CPSServer server;
    private static SessionFactory dbSessionFactory;
    
    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        
        try {
            Logger.init("./server.log");
            
            Logger.print("logger initialized successfully.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            server = CPSServer.getServer(port);
            server.listen();
            
            Logger.print("server created successfully.", "listening on port: " + port + ".");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            dbSessionFactory = Database.getSessionFactory();
            Session dbSession = dbSessionFactory.openSession();
            dbSession.beginTransaction();
            //createDummyDB(dbSession);
            // new line :
            createDemoDb(dbSession);
            dbSession.close();
            System.out.println("[SERVER] server created database session successfully.");
        }
        catch (HibernateException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * clear all tables from 'cps_db' and creating a new dummy data.
     **/
    private static void createDummyDB(Session session) {
        try {
            session.createNativeQuery("DROP DATABASE IF EXISTS cps_db").executeUpdate();
            session.createNativeQuery("CREATE DATABASE cps_db").executeUpdate();
            session.createNativeQuery("USE cps_db").executeUpdate();

            session.createNativeQuery("""
                CREATE TABLE parking_lots (
                  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                  name VARCHAR(255) NOT NULL,
                  address VARCHAR(255) NOT NULL,
                  floor_width INT NOT NULL
                )
            """).executeUpdate();
            session.createNativeQuery("""
                CREATE TABLE rates (
                  id INT NOT NULL PRIMARY KEY,
                  hourly_occasional_parking DOUBLE NOT NULL,
                  hourly_onetime_parking DOUBLE NOT NULL,
                  regular_subscription_single_vehicle DOUBLE NOT NULL,
                  regular_subscription_multiple_vehicles DOUBLE NOT NULL,
                  full_subscription_single_vehicle DOUBLE NOT NULL,
                  FOREIGN KEY (id) REFERENCES parking_lots(id)
                )
            """).executeUpdate();
            session.createNativeQuery("""
                CREATE TABLE employees (
                  id INT PRIMARY KEY AUTO_INCREMENT,
                  first_name VARCHAR(255),
                  last_name VARCHAR(255),
                  role VARCHAR(255)
                )
            """).executeUpdate();
            session.createNativeQuery("""
                CREATE TABLE customers (
                  id INT PRIMARY KEY AUTO_INCREMENT,
                  first_name VARCHAR(255),
                  last_name VARCHAR(255),
                  email VARCHAR(255)
                )
            """).executeUpdate();
            
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
            
            Rates rates2 = session.get(Rates.class, 2L);
            rates2.setHourlyOccasionalParking(5.5);
            rates2.setHourlyOnetimeParking(3.5);
            session.update(rates2);
            Rates rates3 = session.get(Rates.class, 3);
            rates3.setHourlyOccasionalParking(12);
            rates3.setHourlyOnetimeParking(10);
            session.update(rates3);
            
            //session.save(new Employee("test", "test", "test"));
            //session.save(new Employee("netanel", "shlomo", "admin"));
            
            session.save(new Customer("amir", "david", "amirdenekamp@gmail.com"));

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
    // from here : new code - create demo data base /////////////////////
    //create parking lot with its fields (robot etc.)
    //create employees from all kinds and connects them to parking lot
    //create clients from 2 kinds (include their vehicles and if needed reservations)
    public static void createDemoDb(Session session){
        try {
            generateParkingLot(session);
            generateEmployees(session);
            generateCustomers(session);
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
    private static String generateRandomName (int leftLimit, int rightLimit, int targetStringLength){
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
    private static void generateParkingLot(Session session) throws Exception {
        Random random = new Random();
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        for (int i = 0; i < 2; i++){
            ParkingLot parkingLot = new ParkingLot(generateRandomName(leftLimit, rightLimit,random.nextInt(3,7)),
                    generateRandomName(leftLimit, rightLimit,random.nextInt(3,7)));
            session.save(parkingLot);
            session.flush();
            Robot robot = new Robot(parkingLot);
            session.save(robot);
            session.flush();
            Rates rates = new Rates(parkingLot);
            session.save(rates);
            session.flush();
            List<ParkingSpace> parkingSpaces = new ArrayList<>();
            for (int j = 0; j < parkingLot.getFloorSize()*parkingLot.getColSize()*parkingLot.getRowSize(); j++){
                ParkingSpace parkingSpace = new ParkingSpace(j,parkingLot);
                session.save(parkingSpace);
                session.flush();
                parkingSpaces.add(parkingSpace);
            }
            parkingLot.setParkingSpace(parkingSpaces);
            parkingLot.setRates(rates);
            parkingLot.setRobot(robot);
            session.save(parkingLot);
            session.flush();
        }
        for (int i = 0; i < 2; i++){
            ParkingLot parkingLot = new ParkingLot(generateRandomName(leftLimit, rightLimit,random.nextInt(3,7)),
                    generateRandomName(leftLimit, rightLimit,random.nextInt(3,7)), random.nextInt(4,8),
                    random.nextInt(4,8), random.nextInt(4,8));
            session.save(parkingLot);
            session.flush();
            Robot robot = new Robot(parkingLot);
            session.save(robot);
            session.flush();
            Rates rates = new Rates(parkingLot);
            session.save(rates);
            session.flush();
            List<ParkingSpace> parkingSpaces = new ArrayList<>();
            for (int j = 0; j < parkingLot.getFloorSize()*parkingLot.getColSize()*parkingLot.getRowSize(); j++){
                ParkingSpace parkingSpace = new ParkingSpace(j,parkingLot);
                session.save(parkingSpace);
                session.flush();
                parkingSpaces.add(parkingSpace);
            }
            parkingLot.setParkingSpace(parkingSpaces);
            parkingLot.setRates(rates);
            parkingLot.setRobot(robot);
            session.save(parkingLot);
            session.flush();
        }

    }
    private static void generateEmployees(Session session) throws Exception{
        Random random = new Random();
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Employee networkManager = new Employee("networkManager", "networkManager@gmail.com",
                generateRandomName(leftLimit, rightLimit,random.nextInt(3,7)),  generateRandomName(leftLimit, rightLimit,random.nextInt(3,7)),
                0L);
        session.save(networkManager);
        session.flush();
        Employee CustomerServiceEmployee = new Employee("CustomerServiceEmployee", "CustomerServiceEmployee@gmail.com",
                generateRandomName(leftLimit, rightLimit,random.nextInt(3,7)),  generateRandomName(leftLimit, rightLimit,random.nextInt(3,7)),
                0L);
        session.save(CustomerServiceEmployee);
        session.flush();
        CustomerServiceEmployee = new Employee("CustomerServiceEmployee", "CustomerServiceEmployee@gmail.com",
                generateRandomName(leftLimit, rightLimit,random.nextInt(3,7)),  generateRandomName(leftLimit, rightLimit,random.nextInt(3,7)),
                0L);
        session.save(CustomerServiceEmployee);
        session.flush();
        List<ParkingLot> parkingLots = getAllParkingLots(session);
        for (int i =0; i < parkingLots.size(); i++){
            Employee parkingLotManager = new Employee("parkingLotManager", "parkingLotManager@gmail.com",
                    generateRandomName(leftLimit, rightLimit,random.nextInt(3,7)),  generateRandomName(leftLimit, rightLimit,random.nextInt(3,7)),
                    parkingLots.get(i).getId());
            session.save(parkingLotManager);
            session.flush();
            Employee parkingLotEmployee = new Employee("parkingLotEmployee", "parkingLotEmployee@gmail.com",
                    generateRandomName(leftLimit, rightLimit,random.nextInt(3,7)),  generateRandomName(leftLimit, rightLimit,random.nextInt(3,7)),
                    parkingLots.get(i).getId());
            session.save(parkingLotEmployee);
            session.flush();
            parkingLots.get(i).addEmployee(parkingLotManager);
            parkingLots.get(i).addEmployee(parkingLotEmployee);
            session.save(parkingLots.get(i));
            session.flush();
        }
        int amount = random.nextInt(2,parkingLots.size()*4);
        for (int i = 0; i < amount; i++){
            int randPark = random.nextInt(0, parkingLots.size());
            Employee parkingLotEmployee = new Employee("parkingLotEmployee", "parkingLotEmployee@gmail.com",
                    generateRandomName(leftLimit, rightLimit,random.nextInt(3,7)),  generateRandomName(leftLimit, rightLimit,random.nextInt(3,7)),
                    parkingLots.get(randPark).getId());
            session.save(parkingLotEmployee);
            session.flush();
            parkingLots.get(randPark).addEmployee(parkingLotEmployee);
            session.save(parkingLots.get(randPark));
            session.flush();
        }

    }
    private static void generateCustomers(Session session) throws Exception {
        Random random = new Random();
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        List<ParkingLot> parkingLots = getAllParkingLots(session);

        for (int i = 0; i < 15; i++){
            int place = random.nextInt(0, parkingLots.size());
            SubscribedCustomer subscribedCustomer = new SubscribedCustomer(generateRandomName(leftLimit, rightLimit,random.nextInt(3,7))+ "@gmail.com",
                    parkingLots.get(place), LocalDate.now(), LocalDate.now(), "standard");
            session.save(subscribedCustomer);
            session.flush();
            Vehicle vehicle = new Vehicle(random.nextLong(1000000,9999999), subscribedCustomer);
            session.save(vehicle);
            session.flush();
            subscribedCustomer.addVehicle(vehicle);
            session.save(subscribedCustomer);
            session.flush();
            parkingLots.get(place).addCustomer(subscribedCustomer);
            session.save(parkingLots.get(place));
            session.flush();
        }
        List<SubscribedCustomer> subscribedCustomers = getAllSubscribedCustomers(session);
        int limit = random.nextInt(3, subscribedCustomers.size()*2);
        for (int i = 0; i < limit; i++){
            int place = random.nextInt(0, subscribedCustomers.size());
            Vehicle vehicle = new Vehicle(random.nextLong(1000000,9999999), subscribedCustomers.get(place));
            session.save(vehicle);
            session.flush();
            subscribedCustomers.get(place).addVehicle(vehicle);
            session.save(subscribedCustomers.get(place));
            session.flush();
        }
        for (int i = 0; i < 10; i++){
            int place = random.nextInt(0, parkingLots.size());
            UnsubscribedCustomer customer = new UnsubscribedCustomer(generateRandomName(leftLimit, rightLimit,random.nextInt(3,7))+ "@gmail.com",
                    parkingLots.get(place));
            session.save(customer);
            session.flush();
            Vehicle vehicle = new Vehicle(random.nextLong(1000000,9999999), customer);
            session.save(vehicle);
            session.flush();
            Reservation reservation = new Reservation(parkingLots.get(place), LocalDateTime.now(), LocalDateTime.now(), vehicle);
            session.save(reservation);
            session.flush();
            vehicle.addReservation(reservation);
            session.save(vehicle);
            session.flush();
            parkingLots.get(place).addReservation(reservation);
            parkingLots.get(place).addCustomer(customer);
            session.save(parkingLots.get(place));
            session.flush();
        }

    }
    private static List<ParkingLot> getAllParkingLots(Session session) throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ParkingLot> query = builder.createQuery(ParkingLot.class);
        query.from(ParkingLot.class);
        List<ParkingLot> data = session.createQuery(query).getResultList();
        return data;
    }
    private static List<SubscribedCustomer> getAllSubscribedCustomers(Session session) throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<SubscribedCustomer> query = builder.createQuery(SubscribedCustomer.class);
        query.from(SubscribedCustomer.class);
        List<SubscribedCustomer> data = session.createQuery(query).getResultList();
        return data;
    }
}
