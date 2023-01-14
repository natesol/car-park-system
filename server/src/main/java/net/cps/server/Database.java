package net.cps.server;

import net.cps.common.utils.Entities;
import net.cps.server.utils.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;


public class Database {
    private static final Configuration configuration = new Configuration();
    private static final SessionFactory sessionFactory = createSessionFactory();
    
    
    /* ----- Constructors ------------------------------------------- */
    
    /**
     * Static class.
     **/
    private Database () {}
    
    
    /* ----- Getters and Setters ------------------------------------ */
    
    /**
     * Get the connected database type (dialect).
     **/
    public static String getDatabaseType () {
        return configuration.getProperty("hibernate.dialect").split("\\.")[3].replace("Dialect", "");
    }
    
    /**
     * Get the connected database name.
     **/
    public static String getDatabaseName () {
        return configuration.getProperty("hibernate.connection.url").split("/")[3].split("\\?")[0];
    }
    
    /**
     * Get the connected database port number.
     **/
    public static Integer getDatabasePort () {
        return Integer.parseInt(configuration.getProperty("hibernate.connection.url").split(":")[3].split("/")[0].split("\\?")[0]);
    }
    
    /**
     * Get the connected database 'SessionFactory' object.
     **/
    public static SessionFactory getSessionFactory () {
        return sessionFactory;
    }
    
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    /**
     * Create the base hibernate 'SessionFactory' object for the database.
     **/
    private static SessionFactory createSessionFactory () {
        try {
            for (Entities entity : Entities.values()) {
                configuration.addAnnotatedClass(entity.getEntityClass());
            }
            
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            return configuration.buildSessionFactory(serviceRegistry);
        }
        catch (Throwable e) {
            Logger.print("failed to create session factory.", "creation ended with the exception: " + e.getMessage());
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }
    
    /**
     * Get a new 'Session' object.
     **/
    public static Session createSession () {
        return sessionFactory.openSession();
    }
    
    /**
     * close opened 'Session' objects.
     **/
    public static void closeSession () {
        sessionFactory.getCurrentSession().close();
    }
    
    /**
     * Close all sessions and release the 'SessionFactory' object (destroy the 'SessionFactory' and all its sessions).
     **/
    public static void closeAllConnections () {
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().close();
        sessionFactory.close();
    }
    
    
    /**
     * Create method - add an entity as a new row to the entity-related-table in the database.
     *
     * @param sessionFactory the 'SessionFactory' object to use.
     * @param entityObject   the entity to add.
     * @return the new entity given id (the primary key).
     **/
    public static <T> Object createEntity (SessionFactory sessionFactory, T entityObject) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Object id = null;
        
        try (session) {
            id = session.save(entityObject);
            session.flush();
            transaction.commit();
            session.clear();
            
            Logger.print("a new entity added to the database.", "entity: " + entityObject.getClass().getSimpleName() + ", id: " + id);
            Logger.info("a new entity added to the database. entity: " + entityObject.getClass().getSimpleName() + ", id: " + id);
        }
        catch (Throwable e) {
            if (e instanceof HibernateException && session.isOpen() && transaction != null) {
                transaction.rollback();
            }
            
            e.printStackTrace();
            Logger.print("Error: failed to add entity: " + entityObject.getClass().getSimpleName() + " to the database.", "'CREATE' transaction ended with the exception: " + e.getMessage());
            Logger.error("failed to add entity: " + entityObject.getClass().getSimpleName() + " to the database.");
        }
        session.close();
        
        return id;
    }
    
    /**
     * Create method - add a list of entities as a new rows to the entity-related-table in the database.
     *
     * @param sessionFactory the 'SessionFactory' object to use.
     * @param entitiesList   the entities to add.
     * @return a list of the new entities given ids (primary keys).
     **/
    public static <T> ArrayList<Object> createMultipleEntities (SessionFactory sessionFactory, List<T> entitiesList) {
        String entityClassName = entitiesList.get(0).getClass().getSimpleName();
        ArrayList<Object> ids = new ArrayList<>();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        
        try (session) {
            for (T entity : entitiesList) {
                ids.add((Object) session.save(entity));
            }
            
            session.flush();
            transaction.commit();
            session.clear();
            
            Logger.print("a list of entities added to the database.", "entities: " + entityClassName + ", ids: " + ids);
            Logger.info("a list of entities added to the database. entities: " + entityClassName + ", ids: " + ids);
        }
        catch (Throwable e) {
            if (e instanceof HibernateException && session.isOpen() && transaction != null) {
                transaction.rollback();
            }
            
            e.printStackTrace();
            Logger.print("Error: failed to add entities: " + entityClassName + " to the database.", "'CREATE' transaction ended with the exception: " + e.getMessage());
            Logger.error("failed to add entities: " + entityClassName + " to the database.");
        }
        session.close();
        
        return ids;
    }
    
    /**
     * Create method - execute a custom `INSERT` query, and return the new entity/ies given id.
     *
     * @param sessionFactory the 'SessionFactory' object to use.
     * @param query          the custom `INSERT` query.
     *                       the query must be in the following format: `INSERT INTO &lt;table_name&gt; (&lt;column_name&gt;, &lt;column_name&gt;, ...) VALUES (&lt;value&gt;, &lt;value&gt;, ...);`.
     * @return the new entities given ids (primary keys).
     **/
    public static <T> ArrayList<Object> createCustomQuery (SessionFactory sessionFactory, String query) {
        ArrayList<Object> ids = null;
        Entities entity = Entities.fromString(query.split(" ")[2]);
        String entityClassName = entity.getClassName();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        
        try (session) {
            session.createNativeQuery(query).executeUpdate();
            
            Class<?> T = entity.getClass();
            ArrayList<T> entities = (ArrayList<T>) session.createNativeQuery(query, T).getResultList();
            ids = (ArrayList<Object>) entities.stream().map((e) -> {
                try {
                    return e.getClass().getDeclaredMethod(entity.getPrimaryKeyGetterName());
                }
                catch (NoSuchMethodException ex) {
                    throw new RuntimeException(ex);
                }
            });
            
            session.flush();
            transaction.commit();
            session.clear();
            
            Logger.print("a new entities added to the database.", "entities: " + entityClassName + ", ids: " + ids);
            Logger.info("a new entities added to the database. entities: " + entityClassName + ", ids: " + ids);
        }
        catch (Throwable e) {
            if (e instanceof HibernateException && session.isOpen() && transaction != null) {
                transaction.rollback();
            }
            
            e.printStackTrace();
            Logger.print("Error: failed to add entities: " + entityClassName + " to the database.", "'CREATE' transaction ended with the exception: " + e.getMessage());
            Logger.error("failed to add entities: " + entityClassName + " to the database.");
        }
        session.close();
        
        return ids;
    }
    
    
    /**
     * Read method - get one instances of an entity type from the entity-related-table in the database.
     *
     * @param sessionFactory the 'SessionFactory' object to use.
     * @param T              the entity class.
     *                       the entity must have a primary key.
     * @param id             the entity id.
     * @return the entity instance.
     **/
    public static <T> T getEntity (SessionFactory sessionFactory, Class<T> T, Object id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        T data = null;
        
        try (session) {
            data = (T) session.get(T, (Serializable) id);
            session.flush();
            transaction.commit();
            session.clear();
        }
        catch (Throwable e) {
            if (e instanceof HibernateException && session.isOpen() && transaction != null) {
                transaction.rollback();
            }
            
            Logger.print("Error: failed to get entity: " + T.getSimpleName() + " from the database.", "'GET' transaction ended with the exception: " + e.getMessage());
            e.printStackTrace();
            Logger.error("failed to get entity: " + T.getSimpleName() + " from the database.");
        }
        session.close();
        
        return data;
    }
    
    public static <T> T getEntity (SessionFactory sessionFactory, Class<T> T, String fieldName, String fieldValue) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        T data = null;
        
        try (session) {
            data = (T) session.createQuery("FROM " + Entities.fromString(T.getSimpleName()).getTableName() + " WHERE " + fieldName + " = '" + fieldValue + "'").getSingleResult();
            session.flush();
            transaction.commit();
            session.clear();
        }
        catch (Throwable e) {
            if (e instanceof HibernateException && session.isOpen() && transaction != null) {
                transaction.rollback();
            }
            
            e.printStackTrace();
            Logger.print("Error: failed to get entity: " + T.getSimpleName() + " from the database.", "'GET' transaction ended with the exception: " + e.getMessage());
            Logger.error("failed to get entity: " + T.getSimpleName() + " from the database.");
        }
        session.close();
        
        return data;
        
    }
    
    public static <T> T getEntity (SessionFactory sessionFactory, Class<T> T, List<SimpleEntry<String, String>>[] fields) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        T data = null;
        
        try (session) {
            StringBuilder query = new StringBuilder("FROM " + Entities.fromString(T.getSimpleName()).getTableName() + " WHERE ");
            for (int i = 0 ; i < fields.length ; i++) {
                query.append(fields[i].get(0)).append(" = '").append(fields[i].get(1)).append("'");
                if (i < fields.length - 1) {
                    query.append(" AND ");
                }
            }
            data = (T) session.createQuery(query.toString()).getSingleResult();
            
            session.flush();
            transaction.commit();
            session.clear();
        }
        catch (Throwable e) {
            if (e instanceof HibernateException && session.isOpen() && transaction != null) {
                transaction.rollback();
            }
            
            e.printStackTrace();
            Logger.print("Error: failed to get entity: " + T.getSimpleName() + " from the database.", "'GET' transaction ended with the exception: " + e.getMessage());
            Logger.error("failed to get entity: " + T.getSimpleName() + " from the database.");
        }
        session.close();
        
        return data;
    }
    
    /**
     * Read method - get a list of instances of an entity type from the entity-related-table in the database.
     *
     * @param sessionFactory the 'SessionFactory' object to use.
     * @param T              the entity class.
     *                       the entity must have a primary key.
     * @param ids            the entities ids.
     * @return the entities instance.
     **/
    public static <T> ArrayList<T> getMultipleEntities (SessionFactory sessionFactory, Class<T> T, List<Object> ids) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        ArrayList<T> data = new ArrayList<>();
        
        try (session) {
            for (Object id : ids) {
                T entity = (T) session.get(T, (Serializable) id);
                if (entity != null) {
                    data.add(entity);
                }
            }
            
            session.flush();
            transaction.commit();
            session.clear();
        }
        catch (Throwable e) {
            if (e instanceof HibernateException && session.isOpen() && transaction != null) {
                transaction.rollback();
            }
            
            Logger.print("Error: failed to get entities: " + T.getSimpleName() + " from the database.", "'GET' transaction ended with the exception:" + e.getMessage());
            e.printStackTrace();
            Logger.error("failed to get entities: " + T.getSimpleName() + " from the database.");
            
            throw new HibernateException(e);
        }
        session.close();
        
        return data.size() > 0 ? data : null;
    }
    
    public static <T> ArrayList<T> getMultipleEntities (SessionFactory sessionFactory, Class<T> T, List<SimpleEntry<String, String>>[] fields) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        ArrayList<T> data = new ArrayList<>();
        
        try (session) {
            StringBuilder query = new StringBuilder("FROM " + Entities.fromString(T.getSimpleName()).getTableName() + " WHERE ");
            for (int i = 0 ; i < fields.length ; i++) {
                query.append(fields[i].get(0)).append(" = '").append(fields[i].get(1)).append("'");
                if (i < fields.length - 1) {
                    query.append(" AND ");
                }
            }
            data = (ArrayList<T>) session.createQuery(query.toString()).list();
            
            session.flush();
            transaction.commit();
            session.clear();
        }
        catch (Throwable e) {
            if (e instanceof HibernateException && session.isOpen() && transaction != null) {
                transaction.rollback();
            }
            
            e.printStackTrace();
            Logger.print("Error: failed to get entity: " + T.getSimpleName() + " from the database.", "'GET' transaction ended with the exception: " + e.getMessage());
            Logger.error("failed to get entity: " + T.getSimpleName() + " from the database.");
        }
        session.close();
        
        return data;
    }
    
    /**
     * Read method - get all the instances of an entity type from the entity-related-table in the database.
     *
     * @param sessionFactory the 'SessionFactory' object to use.
     * @param entityClass    the entity class.
     *                       the entity must have a primary key.
     * @return the entities instance.
     **/
    public static <T> ArrayList<T> getAllEntities (SessionFactory sessionFactory, Class<T> entityClass) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        ArrayList<T> data;
        
        try (session) {
            data = (ArrayList<T>) session.createQuery("FROM " + entityClass.getSimpleName()).getResultList();
            
            session.flush();
            transaction.commit();
            session.clear();
        }
        catch (Throwable e) {
            if (e instanceof HibernateException && session.isOpen() && transaction != null) {
                transaction.rollback();
            }
            
            e.printStackTrace();
            Logger.print("Error: failed to get all entities: " + entityClass.getSimpleName() + " from the database.", "'GET' transaction ended with the exception:" + e.getMessage());
            Logger.error("failed to get all entities: " + entityClass.getSimpleName() + " from the database.");
            
            throw new HibernateException(e);
        }
        session.close();
        
        return data;
    }
    
    /**
     * Read method - execute a custom `SELECT` query, and return the entities instance.
     *
     * @param sessionFactory the 'SessionFactory' object to use.
     * @param T              the entity class.
     * @param query          the custom query.
     *                       the query must be in the following format: `SELECT * FROM &lt;table_name&gt; WHERE &lt;column_name&gt; = &lt;value&gt;;`,
     *                       or in the following format: `SELECT * FROM &lt;table_name&gt; WHERE &lt;column_name&gt; IN (&lt;value&gt;, &lt;value&gt;, ...);`,
     *                       or in the following format: `SELECT * FROM &lt;table_name&gt;;`.
     * @return the entities instance.
     **/
    public static <T> ArrayList<T> getCustomQuery (SessionFactory sessionFactory, Class<T> T, String query) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        ArrayList<T> data;
        
        try (session) {
            data = (ArrayList<T>) session.createNativeQuery(query, T).getResultList();
            session.flush();
            transaction.commit();
            session.clear();
        }
        catch (Throwable e) {
            if (e instanceof HibernateException && session.isOpen() && transaction != null) {
                transaction.rollback();
            }
            
            Logger.print("Error: failed to get entities: " + query.split(" ")[3] + " from the database.", "'GET' transaction ended with the exception:" + e.getMessage());
            e.printStackTrace();
            Logger.error("failed to get entities: " + query.split(" ")[3] + " from the database.");
            
            throw new HibernateException(e);
        }
        session.close();
        
        return data;
    }
    
    
    /**
     * Update method - update an entity already existing row on the entity-related-table in the database.
     *
     * @param sessionFactory the 'SessionFactory' object to use.
     * @param entityObject   the entity instance.
     *                       the entity must have a primary key.
     * @return the entity id.
     **/
    public static <T> Object updateEntity (SessionFactory sessionFactory, T entityObject) {
        Object id = null;
        String className = entityObject.getClass().getSimpleName();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        
        try (session) {
            session.update(entityObject);
            id = entityObject.getClass().getDeclaredMethod(Entities.fromString(className).getPrimaryKeyGetterName()).invoke(entityObject);
            
            session.flush();
            transaction.commit();
            session.clear();
            
            Logger.print("an entity updated on the database.", "entity: " + className + ", id: " + id);
            Logger.info("an entity updated on the database. entity: " + className + ", id: " + id);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            Logger.print("Error: access to the entity's id failed.", "'UPDATE' transaction ended with the exception: " + e.getMessage());
        }
        catch (Throwable e) {
            if (e instanceof HibernateException && session.isOpen() && transaction != null) {
                transaction.rollback();
            }
            
            e.printStackTrace();
            Logger.print("Error: failed to update entity: " + className + " on the database.", "'UPDATE' transaction ended with the exception: " + e.getMessage());
            Logger.error("failed to update entity: " + className + " on the database.");
        }
        
        session.close();
        return id;
    }
    
    /**
     * Update method - update multiple entities already existing rows on the entity-related-table in the database.
     *
     * @param sessionFactory the 'SessionFactory' object to use.
     * @param entitiesList   the entities instance.
     *                       the entities must have a primary key.
     * @return a list of the updated entities ids.
     **/
    public static <T> ArrayList<Object> updateMultipleEntities (SessionFactory sessionFactory, List<T> entitiesList) {
        ArrayList<Object> ids = new ArrayList<>();
        String className = entitiesList.get(0).getClass().getSimpleName();
        Entities _entity = Entities.fromString(className);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        
        try (session) {
            Method getter = _entity.getClass().getDeclaredMethod(_entity.getPrimaryKeyGetterName());
            
            for (T entity : entitiesList) {
                session.update(entity);
                ids.add(getter.invoke(entity));
            }
            session.flush();
            transaction.commit();
            session.clear();
            
            Logger.print("entities updated on the database.", "entities: " + className + ", ids: " + ids);
            Logger.info("entities updated on the database. entities: " + className + ", ids: " + ids);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            Logger.print("Error: access to the entity's id failed.", "'UPDATE' transaction ended with the exception: " + e.getMessage());
        }
        catch (Throwable e) {
            if (e instanceof HibernateException && session.isOpen() && transaction != null) {
                transaction.rollback();
            }
            
            e.printStackTrace();
            Logger.print("Error: failed to update entities: " + className + " on the database.", "'UPDATE' transaction ended with the exception: " + e.getMessage());
            Logger.error("failed to update entities: " + className + " on the database.");
        }
        
        session.close();
        return ids;
    }
    
    /**
     * Update method - execute a custom `UPDATE` query.
     *
     * @param sessionFactory the 'SessionFactory' object to use.
     * @param query          the custom query.
     *                       the query must be in the following format: `SET &lt;table_name&gt; SET &lt;column_name&gt; = &lt;value&gt; WHERE &lt;column_name&gt; = &lt;value&gt;;`,
     *                       or in the following format: `SET &lt;table_name&gt; SET &lt;column_name&gt; = &lt;value&gt; WHERE &lt;column_name&gt; IN (&lt;value&gt;, &lt;value&gt;, ...);`,
     *                       or in the following format: `SET &lt;table_name&gt; SET &lt;column_name&gt; = &lt;value&gt;;`.
     * @return a list of the updated entities ids.
     **/
    public static ArrayList<Object> updateCustomQuery (SessionFactory sessionFactory, String query) {
        ArrayList<Object> ids = new ArrayList<>();
        Entities _entity = Entities.fromString(query.split(" ")[1]);
        String className = _entity.getClassName();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        
        try (session) {
            session.createNativeQuery(query).executeUpdate();
            ids = (ArrayList<Object>) session.createNativeQuery(query.replace("SET", "SELECT *")).getResultList();
            
            session.flush();
            transaction.commit();
            session.clear();
            
            Logger.print("entities updated on the database.", "entities: " + className + ", via custom query: " + query);
            Logger.info("entities updated on the database. entities: " + className + ", via custom query: " + query);
        }
        catch (Throwable e) {
            if (e instanceof HibernateException && session.isOpen() && transaction != null) {
                transaction.rollback();
            }
            
            e.printStackTrace();
            Logger.print("Error: failed to update entities: " + className + " on the database.", "'UPDATE' transaction ended with the exception: " + e.getMessage());
            Logger.error("failed to update entities: " + className + " on the database.");
        }
        
        session.close();
        return ids;
    }
    
    
    /**
     * Delete method - delete an entity row from the entity-related-table in the database.
     *
     * @param sessionFactory the 'SessionFactory' object to use.
     * @param entityObject   the entity instance.
     *                       the entity must have a primary key.
     * @return the deleted entity id.
     **/
    public static <T> Object deleteEntity (SessionFactory sessionFactory, T entityObject) {
        Object id = null;
        String className = entityObject.getClass().getSimpleName();
        Entities _entity = Entities.fromString(className);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        
        try (session) {
            session.delete(entityObject);
            id = entityObject.getClass().getDeclaredMethod(_entity.getPrimaryKeyGetterName()).invoke(entityObject);
            
            session.flush();
            transaction.commit();
            session.clear();
            
            Logger.print("an entity deleted from the database.", "entity: " + className + ", id: " + id);
            Logger.info("an entity deleted from the database. entity: " + className + ", id: " + id);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            Logger.print("Error: access to the entity's id failed.", "'DELETE' transaction ended with the exception: " + e.getMessage());
        }
        catch (Throwable e) {
            if (e instanceof HibernateException && session.isOpen() && transaction != null) {
                transaction.rollback();
            }
            
            e.printStackTrace();
            Logger.print("Error: failed to delete entity: " + className + " from the database.", "'DELETE' transaction ended with the exception: " + e.getMessage());
            Logger.error("failed to delete entity: " + className + " from the database.");
        }
        session.close();
        
        return id;
    }
    
    /**
     * Delete method - delete multiple entities rows from the entity-related-table in the database.
     *
     * @param sessionFactory the 'SessionFactory' object to use.
     * @param entitiesList   the entities instance.
     *                       the entities must have a primary key.
     * @return a list of the deleted entities ids.
     **/
    public static <T> ArrayList<Object> deleteMultipleEntities (SessionFactory sessionFactory, List<T> entitiesList) {
        ArrayList<Object> ids = new ArrayList<>();
        String className = entitiesList.get(0).getClass().getSimpleName();
        Entities _entity = Entities.fromString(className);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        
        try (session) {
            for (T entity : entitiesList) {
                session.delete(entity);
                ids.add(entity.getClass().getDeclaredMethod(_entity.getPrimaryKeyGetterName()).invoke(entity));
            }
            session.flush();
            transaction.commit();
            session.clear();
            
            Logger.print("entities deleted from the database.", "entities: " + className + ", ids: " + ids);
            Logger.info("entities deleted from the database. entities: " + className + ", ids: " + ids);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            Logger.print("Error: access to the entity's id failed.", "'DELETE' transaction ended with the exception:");
        }
        catch (Throwable e) {
            if (e instanceof HibernateException && session.isOpen() && transaction != null) {
                transaction.rollback();
            }
            
            e.printStackTrace();
            Logger.print("Error: failed to delete entities: " + className + " from the database.", "'DELETE' transaction ended with the exception: " + e.getMessage());
            Logger.error("failed to delete entities: " + className + " from the database.");
        }
        session.close();
        
        return ids;
    }
    
    /**
     * Delete method - delete all entities rows from the entity-related-table in the database (the table will be truncated).
     *
     * @param sessionFactory the 'SessionFactory' object to use.
     * @param entityClass    the entity class.
     * @return a list of the deleted entities ids.
     **/
    public static <T> ArrayList<Object> deleteAllEntities (SessionFactory sessionFactory, Class<T> entityClass) {
        ArrayList<Object> ids = new ArrayList<>();
        String className = entityClass.getSimpleName();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        
        try (session) {
            ids = (ArrayList<Object>) session.createQuery("FROM " + className).getResultList().stream().map(e -> {
                try {
                    return entityClass.getDeclaredMethod(Entities.fromString(entityClass.getSimpleName()).getPrimaryKeyGetterName()).invoke(e);
                }
                catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    throw new RuntimeException(ex);
                }
            });
            session.createQuery("DELETE FROM " + className).executeUpdate();
            
            session.flush();
            transaction.commit();
            session.clear();
            
            Logger.print("all entities deleted from the database.", "entities: " + className + ".");
            Logger.info("all entities deleted from the database. entities: " + className + ".");
        }
        catch (Throwable e) {
            if (e instanceof HibernateException && session.isOpen() && transaction != null) {
                transaction.rollback();
            }
            
            e.printStackTrace();
            Logger.print("Error: failed to delete all entities: " + className + " from the database.", "'DELETE' transaction ended with the exception: " + e.getMessage());
            Logger.error("failed to delete all entities: " + className + " from the database.");
        }
        session.close();
        
        return ids;
    }
    
    /**
     * Delete method - execute a custom 'DELETE' query.
     *
     * @param sessionFactory the 'SessionFactory' object to use.
     * @param query          the custom query.
     *                       the query must be a 'DELETE' query.
     * @return a list of the deleted entities ids.
     *
     * !!!
     **/
    public static ArrayList<Object> deleteCustomQuery (SessionFactory sessionFactory, String query) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        ArrayList<Object> ids = new ArrayList<>();
        
        try (session) {
            session.createQuery(query).executeUpdate();
            
            session.flush();
            transaction.commit();
            session.clear();
            
            Logger.print("entities deleted from the database.", "entities: " + query.split(" ")[2] + ", via custom query: " + query);
            Logger.info("entities deleted from the database. entities: " + query.split(" ")[2] + ", via custom query: " + query);
        }
        catch (Throwable e) {
            if (e instanceof HibernateException && session.isOpen() && transaction != null) {
                transaction.rollback();
            }
            
            e.printStackTrace();
            Logger.print("Error: failed to delete entities: " + query.split(" ")[2] + " from the database.", "'DELETE' transaction ended with the exception: " + e.getMessage());
            Logger.error("failed to delete entities: " + query.split(" ")[2] + " from the database.");
        }
        session.close();
        
        return ids;
    }
}
