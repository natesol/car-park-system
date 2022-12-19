package net.cps.server.utils;

import net.cps.entities.hibernate.Customer;
import net.cps.entities.hibernate.Employee;
import net.cps.entities.hibernate.ParkingLot;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtils {
    private static final SessionFactory SESSION_FACTORY = createSessionFactory();
    
    private static SessionFactory createSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(ParkingLot.class);
            configuration.addAnnotatedClass(Employee.class);
            configuration.addAnnotatedClass(Customer.class);
//            configuration.addPackage("net.cps.entities.hibernate");
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            
            return configuration.buildSessionFactory(serviceRegistry);
        }
        catch (Throwable e) {
            System.err.println("[SERVER] initial 'SessionFactory' creation failed.\n[SERVER] ended with the exception trace:" + e);
            throw new ExceptionInInitializerError(e);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }
    
    public static void shutdownSession() {
        // Close caches and connection pools
        SESSION_FACTORY.close();
    }
    
    public static <T> void updateCellInDB(Session session, T objectType) {
        try {
            session.update(objectType);
            session.flush();
            session.getTransaction().commit();
            session.clear();
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            e.printStackTrace();
        }
    }
    
    public static <T> void saveRowInDB(Session session, T objectType) {
        try {
            session.save(objectType);
            session.flush();
            session.getTransaction().commit();
            session.clear();
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            e.printStackTrace();
        }
    }
    
    public static <T> void deleteRowInDB(Session session, T objectType) {
        try {
            session.delete(objectType);
            session.flush();
            session.getTransaction().commit();
            session.clear();
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            e.printStackTrace();
        }
    }
}
