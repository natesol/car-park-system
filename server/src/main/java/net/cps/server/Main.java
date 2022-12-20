package net.cps.server;

import net.cps.entities.hibernate.Customer;
import net.cps.entities.hibernate.Employee;
import net.cps.entities.hibernate.ParkingLot;
import net.cps.entities.hibernate.Rates;
import net.cps.server.utils.HibernateUtils;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.List;

public class Main {
    private static final int DEFAULT_PORT = 3000;
    private static CPSServer server;
    private static SessionFactory dbSessionFactory;
    
    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        
        try {
            server = new CPSServer(port);
            server.listen();
            System.out.println("[SERVER] server is listening on port: " + port + ".");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            dbSessionFactory = HibernateUtils.getSessionFactory();
            Session dbSession = dbSessionFactory.openSession();
            dbSession.beginTransaction();
//            createDummyDB(dbSession);
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
              parkingId INT NOT NULL PRIMARY KEY,
              hourly_occasional_parking DOUBLE NOT NULL,
              hourly_onetime_parking DOUBLE NOT NULL,
              regular_subscription_single_vehicle DOUBLE NOT NULL,
              regular_subscription_multiple_vehicles DOUBLE NOT NULL,
              full_subscription_single_vehicle DOUBLE NOT NULL,
              FOREIGN KEY (parkingId) REFERENCES parking_lots(id)
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
    
            session.save(new ParkingLot("parking #1", "haifa 123", 5));
            session.save(new ParkingLot("parking #2", "haifa 61/4", 1));
            session.save(new ParkingLot("parking #3", "tel-aviv 99", 2));
            session.save(new ParkingLot("parking #4", "eilat 7", 11));
    
            Rates rates2 = session.get(Rates.class, 2);
            rates2.setHourlyOccasionalParking(6);
            rates2.setHourlyOnetimeParking(5);
            session.save(rates2);
            
            Rates rates3 = session.get(Rates.class, 3);
            rates2.setHourlyOccasionalParking(12);
            rates2.setHourlyOnetimeParking(10);
            session.save(rates3);
            
            session.save(new Employee("test", "test", "test"));
            session.save(new Employee("netanel", "shlomo", "admin"));
            
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
}
