package net.cps.server.utils;

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
    
    
    public static final String CREATE_TABLE_PARKING_LOTS = """
            CREATE TABLE parking_lots (
                      id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(255) NOT NULL,
                      address VARCHAR(255) NOT NULL,
                      floor_width INT NOT NULL
            )""";
    
    public static final String CREATE_TABLE_RATES = """
            CREATE TABLE rates (
                      id INT NOT NULL PRIMARY KEY,
                      hourly_occasional_parking DOUBLE NOT NULL,
                      hourly_onetime_parking DOUBLE NOT NULL,
                      regular_subscription_single_vehicle DOUBLE NOT NULL,
                      regular_subscription_multiple_vehicles DOUBLE NOT NULL,
                      full_subscription_single_vehicle DOUBLE NOT NULL,
                      FOREIGN KEY (id) REFERENCES parking_lots(id)
            )""";
    
    public static final String CREATE_TABLE_EMPLOYEES = """
            CREATE TABLE employees (
                      id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                      first_name VARCHAR(255) NOT NULL,
                      last_name VARCHAR(255) NOT NULL,
                      role VARCHAR(255) NOT NULL,
                      email VARCHAR(255) NOT NULL,
                      password_hash VARCHAR(255) NOT NULL,
                      password_salt VARCHAR(255) NOT NULL,
                      organization VARCHAR(255)
            )""";
    
    //public static final String CREATE_TABLE_CUSTOMERS = """
    //        CREATE TABLE customers (
    //                  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    //                  first_name VARCHAR(255) NOT NULL,
    //                  last_name VARCHAR(255) NOT NULL,
    //                  email VARCHAR(255) NOT NULL,
    //                  password_hash VARCHAR(255) NOT NULL,
    //                  password_salt VARCHAR(255) NOT NULL
    //        )""";
    public static final String CREATE_TABLE_CUSTOMERS = """
            CREATE TABLE customers (
                          email VARCHAR(255) NOT NULL PRIMARY KEY,
                          first_name VARCHAR(255) NOT NULL,
                          last_name VARCHAR(255) NOT NULL,
                          password_hash VARCHAR(255) NOT NULL,
                          password_salt VARCHAR(255) NOT NULL
            );""";
}
