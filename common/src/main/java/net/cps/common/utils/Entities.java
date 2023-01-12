package net.cps.common.utils;

import net.cps.common.entities.*;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Entities utility enum.
 * This enum contains each and every entity class in the system.
 * It is used to generalize and simplify all the entity-related operations (against the database or in the system).
 */
public enum Entities {
    ORGANIZATION(
            Organization.class,
            "organizations",
            "id",
            Integer.class,
            (Integer::parseInt)
    ),
    MANAGEMENT(Management.class, "managements", "id", Integer.class, (Integer::parseInt)),
    PARKING_LOT(ParkingLot.class, "parking_lots", "id", Integer.class, (Integer::parseInt)),
    RATES(Rates.class, "rates", "id", Integer.class, (Integer::parseInt)),
    EMPLOYEE(Employee.class, "employees", "id", Integer.class, (Integer::parseInt)),
    CUSTOMER(Customer.class, "customers", "email", String.class, (s -> s)),
    SUBSCRIPTION(Subscription.class, "subscriptions", "id", Integer.class, (Integer::parseInt)),
    VEHICLE(Vehicle.class, "vehicles", "id", Integer.class, (Integer::parseInt));
    
    
    private final Class<?> entityClass;
    private final String tableName;
    private final String primaryKey;
    private final Class<?> primaryKeyClass;
    private final Function<String, ?> primaryKeyConverter;
    
    
    /* ----- Constructors ---------------------------------------------- */
    
    Entities (Class<?> entityClass, String tableName, String primaryKey, Class<?> primaryKeyClass, Function<String, ?> primaryKeyConverter) {
        this.entityClass = entityClass;
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.primaryKeyClass = primaryKeyClass;
        this.primaryKeyConverter = primaryKeyConverter;
    }
    
    
    /* ----- Getters and Setters ------------------------------------ */
    
    public String getClassName () {
        return entityClass.getSimpleName();
    }
    
    public Class<?> getEntityClass () {
        return entityClass;
    }
    
    public String getTableName () {
        return tableName;
    }
    
    public String getPrimaryKey () {
        return primaryKey;
    }
    
    public Class<?> getPrimaryKeyClass () {
        return primaryKeyClass;
    }
    
    public Function<String, ?> getPrimaryKeyConverter () {
        return primaryKeyConverter;
    }
    
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    public static String toString (Entities entity) {
        return switch (entity) {
            case ORGANIZATION -> "Organization";
            case MANAGEMENT -> "Management";
            case PARKING_LOT -> "Parking Lot";
            case RATES -> "Rates";
            case EMPLOYEE -> "Employee";
            case CUSTOMER -> "Customer";
            case SUBSCRIPTION -> "Subscription";
            case VEHICLE -> "Vehicle";
            default -> throw new IllegalArgumentException("Invalid entity: " + entity);
        };
    }
    
    public static Entities fromString (String entity) {
        Entities entityEnum = getEntityByTableName(entity);
        if (entityEnum != null) {
            return entityEnum;
        }
        
        String entityFormatted = camelCaseToSnakeCase(entity).trim().toUpperCase().replace(" ", "_").replace("-", "_");
        return switch (entityFormatted) {
            case "ABSTRACT_ORGANIZATION" -> ORGANIZATION;
            case "MANAGEMENT" -> MANAGEMENT;
            case "PARKING_LOT" -> PARKING_LOT;
            case "RATES" -> RATES;
            case "EMPLOYEE" -> EMPLOYEE;
            case "CUSTOMER" -> CUSTOMER;
            case "SUBSCRIPTION" -> SUBSCRIPTION;
            case "VEHICLE" -> VEHICLE;
            default -> throw new IllegalArgumentException("Invalid entity: " + entity);
        };
    }
    
    private static String camelCaseToSnakeCase (String camelCase) {
        camelCase = camelCase.substring(0, 1).toLowerCase() + camelCase.substring(1);
        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(camelCase);
        StringBuilder snakeCase = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(snakeCase, "_" + matcher.group().toLowerCase());
        }
        matcher.appendTail(snakeCase);
        return snakeCase.toString();
    }
    
    private static Entities getEntityByTableName (String tableName) {
        for (Entities entity : Entities.values()) {
            if (entity.getTableName().equals(tableName)) {
                return entity;
            }
        }
        return null;
    }
    
    private static Entities getEntityByName (String entityName) {
        String entityNameFormatted = entityName.trim().toLowerCase();
        entityNameFormatted = entityNameFormatted.substring(0, 1).toUpperCase() + entityNameFormatted.substring(1);
        
        for (Entities entity : Entities.values()) {
            if (entity.getClassName().equals(entityNameFormatted)) {
                return entity;
            }
        }
        return null;
    }
}
