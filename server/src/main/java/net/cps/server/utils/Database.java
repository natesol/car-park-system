package net.cps.server.utils;

import net.cps.entities.hibernate.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * static class that handle all database related functions.
 * - mainly a 'Hibernate' utilities wrapper.
**/
public class Database {
    private static final SessionFactory SESSION_FACTORY = createSessionFactory();
    
    private static SessionFactory createSessionFactory() {
        try {
            Configuration configuration = new Configuration()
                    .configure();
            configuration.addAnnotatedClass(AbstractCostumer.class);
            configuration.addAnnotatedClass(Complaint.class);
            configuration.addAnnotatedClass(Employee.class);
            configuration.addAnnotatedClass(ParkingLot.class);
            configuration.addAnnotatedClass(ParkingSpace.class);
            configuration.addAnnotatedClass(Rates.class);
            configuration.addAnnotatedClass(Reservation.class);
            configuration.addAnnotatedClass(Robot.class);
            configuration.addAnnotatedClass(SubscribedCustomer.class);
            configuration.addAnnotatedClass(UnsubscribedCustomer.class);
            configuration.addAnnotatedClass(Vehicle.class);
            
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            return configuration.buildSessionFactory(serviceRegistry);
        }
        catch (Throwable e) {
            System.out.println("[SERVER] initial 'SessionFactory' creation failed.\n[SERVER] ended with the exception trace:");
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }
    
    public static Session createSession() {
        return SESSION_FACTORY.openSession();
    }
    
    public static void endSession() {
        SESSION_FACTORY.close();
    }
    
    /**
     * add an entity as a new row to the entity-related-table in the database.
     * - Create method
     *
     * @return the new entity given id.
     **/
    public static <T> Integer addEntity(Session session, T entityObject) {
        Transaction transaction = session.getTransaction();
        Integer id = null;
        
        try {
            id = (Integer) session.save(entityObject);
            session.flush();
            transaction.commit();
            session.clear();
        }
        catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
    
            System.out.println("[SERVER] 'CREATE' database transaction failed.");
            e.printStackTrace();
        }
        finally {
            session.close();
        }
         return id;
    }
    
    /**
     * get one instances of an entity type from the entity-related-table in the database.
     * - Read method
     **/
    public static <T> T getEntity(Session session, Class T, int id) {
        Transaction transaction = session.getTransaction();
        T data = null;
    
        try {
            data = (T) session.get(T, id);
            session.flush();
            transaction.commit();
            session.clear();
        }
        catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
    
            System.out.println("[SERVER] 'READ' database transaction failed.");
            e.printStackTrace();
        }
        finally {
             session.close();
        }
        return data;
    }
    
    /**
     * get all the instances of an entity type from the entity-related-table in the database.
     * - Read method
     **/
    public static <T> List<T> getAllEntities(Session session, Class T) {
        Transaction transaction = session.getTransaction();
        List<T> data = null;
    
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> query = (CriteriaQuery<T>) builder.createQuery(T);
            query.from(T);
            data = session.createQuery(query).getResultList();
            transaction.commit();
        }
        catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
    
            System.out.println("[SERVER] 'READ' database transaction failed.");
            e.printStackTrace();
        }
        finally {
             session.close();
        }
        return data;
    }
    
    /**
     * update an entity already existing row on the entity-related-table in the database.
     * - Update method
     **/
    public static <T> void updateEntity(Session session, T entityObject) {
        Transaction transaction = session.getTransaction();
    
        try {
            session.update(entityObject);
            session.flush();
            transaction.commit();
            session.clear();
        }
        catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
        
            System.out.println("[SERVER] 'UPDATE' database transaction failed.");
            e.printStackTrace();
        }
        finally {
             session.close();
        }
    }
    
    /**
     * delete an entity row from the entity-related-table in the database.
     * - Delete method
     **/
    public static <T> void deleteEntity(Session session, T entityObject) {
        Transaction transaction = session.getTransaction();
        
        try {
            session.delete(entityObject);
            session.flush();
            transaction.commit();
            session.clear();
        }
        catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
        
            System.out.println("[SERVER] 'DELETE' database transaction failed.");
            e.printStackTrace();
        }
        finally {
             session.close();
        }
    }
}
