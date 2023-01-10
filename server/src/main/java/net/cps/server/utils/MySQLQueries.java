package net.cps.server.utils;

import net.cps.common.utils.Entities;

public class MySQLQueries {
    public static final String CREATE_DATABASE = "CREATE DATABASE IF NOT EXISTS ";
    public static final String DROP_DATABASE = "DROP DATABASE IF EXISTS ";
    public static final String USE_DATABASE = "USE ";
    
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
    
    public static final String SELECT_ALL = "SELECT * FROM ";
    public static final String SELECT = "SELECT ";
    public static final String FROM = " FROM ";
    public static final String WHERE = " WHERE ";
    public static final String INSERT_INTO = "INSERT INTO ";
    
    
    public static final String ORGANIZATIONS_TABLE = Entities.ABSTRACT_ORGANIZATION.getTableName() +
            """
                    (
                        id INT NOT NULL AUTO_INCREMENT,
                        type ENUM('Management', 'Parking Lot', 'Office') NOT NULL,
                        street_number INT NOT NULL,
                        street VARCHAR(255) NOT NULL,
                        city VARCHAR(255) NOT NULL,
                        state VARCHAR(255) NOT NULL,
                        PRIMARY KEY (id)
                    )""";
    
    public static final String MANAGEMENTS_TABLE = Entities.MANAGEMENT.getTableName() +
            """
                    (
                        id INT NOT NULL AUTO_INCREMENT,
                        organization_id INT NOT NULL,
                        name VARCHAR(255) NOT NULL,
                        PRIMARY KEY (id),
                        FOREIGN KEY (organization_id) REFERENCES organizations(id)
                    )""";
    
    public static final String PARKING_LOTS_TABLE = Entities.PARKING_LOT.getTableName() +
            """
                    (
                        id INT NOT NULL AUTO_INCREMENT,
                        organization_id INT NOT NULL,
                        name VARCHAR(255) NOT NULL,
                        num_of_floors INT NOT NULL,
                        floor_rows INT NOT NULL,
                        floor_cols INT NOT NULL,
                        PRIMARY KEY (id),
                        FOREIGN KEY (organization_id) REFERENCES organizations(id)
                    )""";
    
    public static final String RATES_TABLE = Entities.RATES.getTableName() +
            """
                    (
                              id INT NOT NULL PRIMARY KEY,
                              hourly_occasional_parking DOUBLE NOT NULL,
                              hourly_onetime_parking DOUBLE NOT NULL,
                              regular_subscription_single_vehicle DOUBLE NOT NULL,
                              regular_subscription_multiple_vehicles DOUBLE NOT NULL,
                              full_subscription_single_vehicle DOUBLE NOT NULL,
                              FOREIGN KEY (id) REFERENCES parking_lots(id)
                    )""";
    
    public static final String EMPLOYEES_TABLE = Entities.EMPLOYEE.getTableName() +
            """
                    (
                              id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                              first_name VARCHAR(255) NOT NULL,
                              last_name VARCHAR(255) NOT NULL,
                              role VARCHAR(255) NOT NULL,
                              email VARCHAR(255) NOT NULL,
                              password_hash VARCHAR(255) NOT NULL,
                              password_salt VARCHAR(255) NOT NULL,
                              is_active BOOLEAN NOT NULL,
                              organization VARCHAR(255)
                    )""";
    
    public static final String CUSTOMERS_TABLE = Entities.CUSTOMER.getTableName() +
            """
                    (
                          email VARCHAR(255) NOT NULL PRIMARY KEY,
                          id VARCHAR(255) NOT NULL,
                          first_name VARCHAR(255) NOT NULL,
                          last_name VARCHAR(255) NOT NULL,
                          password_salt VARCHAR(255) NOT NULL,
                          password_hash VARCHAR(255) NOT NULL,
                          is_active BOOLEAN NOT NULL,
                          balance DOUBLE NOT NULL
                    )""";
}
