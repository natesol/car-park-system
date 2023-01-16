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
            (Integer::parseInt),
            """
                    (
                        id              INT NOT NULL AUTO_INCREMENT,
                        type            ENUM('MANAGEMENT', 'OFFICE', 'PARKING_LOT') NOT NULL,
                        PRIMARY KEY (id)
                    )"""
    ),
    OFFICE(
            Office.class,
            "offices",
            "id",
            Integer.class,
            (Integer::parseInt),
            """
                    (
                        id INT NOT NULL,
                        name            VARCHAR(55) NOT NULL,
                        street_number   MEDIUMINT NOT NULL,
                        street_name     VARCHAR(55) NOT NULL,
                        city_name       VARCHAR(55) NOT NULL,
                        country_symbol  CHAR(2) NOT NULL,
                        PRIMARY KEY (id),
                        FOREIGN KEY (id) REFERENCES organizations(id)
                    )"""
    ),
    PARKING_LOT(
            ParkingLot.class,
            "parking_lots",
            "id",
            Integer.class, (Integer::parseInt),
            """
                    (
                        id INT NOT NULL,
                        name            VARCHAR(55) NOT NULL,
                        street_number   MEDIUMINT NOT NULL,
                        street_name     VARCHAR(55) NOT NULL,
                        city_name       VARCHAR(55) NOT NULL,
                        country_symbol  CHAR(2) NOT NULL,
                        num_of_floors   TINYINT NOT NULL,
                        num_of_rows     TINYINT NOT NULL,
                        num_of_cols     TINYINT NOT NULL,
                        PRIMARY KEY (id),
                        FOREIGN KEY (id) REFERENCES organizations(id)
                    )"""
    ),
    RATES(Rates.class,
            "rates",
            "id",
            Integer.class,
            (Integer::parseInt),
            """
                    (
                        id                                      INT NOT NULL,
                        hourly_occasional_parking               FLOAT NOT NULL,
                        hourly_onetime_parking                  FLOAT NOT NULL,
                        regular_subscription_single_vehicle     FLOAT NOT NULL,
                        regular_subscription_multiple_vehicles  FLOAT NOT NULL,
                        full_subscription_single_vehicle        FLOAT NOT NULL,
                        PRIMARY KEY (id),
                        FOREIGN KEY (id) REFERENCES parking_lots(id)
                    )"""
    ),
    EMPLOYEE(Employee.class,
            "employees",
            "id",
            Integer.class,
            (Integer::parseInt),
            """
                    (
                        id              INT NOT NULL AUTO_INCREMENT,
                        organization_id INT NOT NULL,
                        role            ENUM('ADMIN', 'NETWORK_MANAGER', 'CUSTOMER_SERVICE_EMPLOYEE', 'PARKING_LOT_MANAGER', 'PARKING_LOT_EMPLOYEE', 'EMPLOYEE') NOT NULL,
                        email           VARCHAR(100) UNIQUE NOT NULL,
                        first_name      VARCHAR(55) NOT NULL,
                        last_name       VARCHAR(55) NOT NULL,
                        password_hash   CHAR(128) NOT NULL,
                        password_salt   CHAR(24) NOT NULL,
                        is_active       BOOLEAN NOT NULL,
                        PRIMARY KEY (id),
                        FOREIGN KEY (organization_id) REFERENCES organizations(id)
                    )"""
    ),
    CUSTOMER(Customer.class,
            "customers",
            "id",
            Integer.class,
            (Integer::parseInt),
            """
                    (
                        id              INT NOT NULL AUTO_INCREMENT,
                        email           VARCHAR(100) UNIQUE NOT NULL,
                        first_name      VARCHAR(55) NOT NULL,
                        last_name       VARCHAR(55) NOT NULL,
                        password_hash   CHAR(128) NOT NULL,
                        password_salt   CHAR(24) NOT NULL,
                        is_active       BOOLEAN NOT NULL,
                        balance         DOUBLE NOT NULL,
                        PRIMARY KEY (id)
                    )"""
    ),
    SUBSCRIPTION(Subscription.class,
            "subscriptions",
            "id",
            Integer.class,
            (Integer::parseInt),
            """
                    (
                        id              INT NOT NULL AUTO_INCREMENT,
                        customer_email  VARCHAR(100) NOT NULL,
                        parking_lot_id  INT,
                        created_at      TIMESTAMP NOT NULL,
                        expires_at      TIMESTAMP NOT NULL,
                        type            ENUM('BASIC', 'PREMIUM') NOT NULL,
                        departure_time  TIME NOT NULL,
                        state           ENUM('ACTIVE', 'EXPIRED', 'RENEWED', 'CANCELLED') NOT NULL,
                        price           DOUBLE NOT NULL,
                        PRIMARY KEY (id),
                        FOREIGN KEY (customer_email) REFERENCES customers(email),
                        FOREIGN KEY (parking_lot_id) REFERENCES parking_lots(id)
                    )"""
    ),
    RESERVATION(Reservation.class,
            "reservations",
            "id",
            Integer.class,
            (Integer::parseInt),
            """
                    (
                        id                      INT NOT NULL AUTO_INCREMENT,
                        parking_lot_id          INT NOT NULL,
                        customer_email          VARCHAR(100) NOT NULL,
                        vehicle_license_plate   CHAR(8) UNIQUE NOT NULL,
                        arrival_time            DATETIME NOT NULL,
                        departure_time          DATETIME NOT NULL,
                        entry_time              DATETIME,
                        status                  ENUM('PENDING', 'CANCELLED', 'CHECKED_IN', 'CHECKED_OUT') NOT NULL,
                        payed                   DOUBLE NOT NULL,
                        parking_space_id        INT UNIQUE,
                        PRIMARY KEY (id),
                        FOREIGN KEY (parking_lot_id) REFERENCES parking_lots(id),
                        FOREIGN KEY (customer_email) REFERENCES customers(email),
                        FOREIGN KEY (vehicle_license_plate) REFERENCES vehicles(license_plate),
                        FOREIGN KEY (parking_space_id) REFERENCES parking_spaces(id)
                    )"""
    ),
    PARKING_SPACE(ParkingSpace.class,
            "parking_spaces",
            "id",
            Integer.class,
            (Integer::parseInt),
            """
                    (
                        id              INT NOT NULL AUTO_INCREMENT,
                        parking_lot_id  INT NOT NULL,
                        floor           TINYINT NOT NULL,
                        row_num         TINYINT NOT NULL,
                        col_num         TINYINT NOT NULL,
                        state           ENUM('AVAILABLE', 'OCCUPIED', 'RESERVED', 'DISABLED', 'OUT_OF_ORDER') NOT NULL,
                        PRIMARY KEY (id),
                        FOREIGN KEY (parking_lot_id) REFERENCES parking_lots(id)
                    )"""
    ),
    VEHICLE(Vehicle.class,
            "vehicles",
            "id",
            Integer.class,
            (Integer::parseInt),
            """
                    (
                        id                  INT NOT NULL AUTO_INCREMENT,
                        customer_email      VARCHAR(100) NOT NULL,
                        license_plate       CHAR(8) UNIQUE NOT NULL,
                        parking_space_id    INT,
                        PRIMARY KEY (id),
                        FOREIGN KEY (customer_email) REFERENCES customers(email),
                        FOREIGN KEY (parking_space_id) REFERENCES parking_spaces(id)
                    )"""
    ),
    COMPLAINT(Complaint.class,
            "complaints",
            "id",
            Integer.class,
            (Integer::parseInt),
            """
                    (
                        id                  INT NOT NULL AUTO_INCREMENT,
                        customer_email      VARCHAR(100) NOT NULL,
                        status              ENUM('ACTIVE', 'RESOLVED', 'CANCELLED') NOT NULL,
                        submission_time     DATETIME NOT NULL,
                        resolution_time     DATETIME,
                        content             TEXT NOT NULL,
                        resolution          TEXT,
                        employee_id         INT,
                        PRIMARY KEY (id),
                        FOREIGN KEY (customer_email) REFERENCES customers(email),
                        FOREIGN KEY (employee_id) REFERENCES employees(id)
                    )"""
    ),
    DAILY_STATISTICS(DailyStatistics.class,
            "daily_statistics",
            "id",
            Integer.class,
            (Integer::parseInt),
            """
                    (
                        id                      INT NOT NULL AUTO_INCREMENT,
                        parking_lot_id          INT NOT NULL,
                        created_at              DATETIME NOT NULL,
                        total_reservations      INT NOT NULL,
                        total_fulfilled         INT NOT NULL,
                        total_cancellations     INT NOT NULL,
                        total_latency           INT NOT NULL,
                        daily_average_latency   DOUBLE NOT NULL,
                        daily_median_latency    DOUBLE NOT NULL,
                        PRIMARY KEY (id),
                        FOREIGN KEY (parking_lot_id) REFERENCES parking_lots(id)
                    )"""
    );
    
    
    private final Class<?> entityClass;
    private final String tableName;
    private final String primaryKey;
    private final Class<?> primaryKeyClass;
    private final Function<String, ?> primaryKeyConverter;
    private final String tableQuery;
    
    
    /* ----- Constructors ---------------------------------------------- */
    
    Entities (Class<?> entityClass, String tableName, String primaryKey, Class<?> primaryKeyClass, Function<String, ?> primaryKeyConverter, String tableQuery) {
        this.entityClass = entityClass;
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.primaryKeyClass = primaryKeyClass;
        this.primaryKeyConverter = primaryKeyConverter;
        this.tableQuery = tableQuery;
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
    
    public String getTableQuery () {
        return tableQuery;
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
    
    public String getPrimaryKeyGetterName () {
        return "get" + primaryKey.substring(0, 1).toUpperCase() + primaryKey.substring(1);
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    public static String toString (Entities entity) {
        return switch (entity) {
            case ORGANIZATION -> "Organization";
            case OFFICE -> "Management";
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
            case "MANAGEMENT" -> OFFICE;
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
