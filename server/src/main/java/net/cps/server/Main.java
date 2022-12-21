package net.cps.server;

import net.cps.entities.hibernate.Customer;
import net.cps.entities.hibernate.Employee;
import net.cps.entities.hibernate.Rates;
import net.cps.server.utils.DataBase;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;

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
            dbSessionFactory = DataBase.getSessionFactory();
            Session dbSession = dbSessionFactory.openSession();
            dbSession.beginTransaction();
            createDummyDB(dbSession);
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
            
            Rates rates2 = session.get(Rates.class, 2);
            rates2.setHourlyOccasionalParking(5.5);
            rates2.setHourlyOnetimeParking(3.5);
            session.update(rates2);
            Rates rates3 = session.get(Rates.class, 3);
            rates3.setHourlyOccasionalParking(12);
            rates3.setHourlyOnetimeParking(10);
            session.update(rates3);
            
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
