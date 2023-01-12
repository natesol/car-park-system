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
    
    
    public static final String CREATE_ORGANIZATIONS_TABLE = CREATE_TABLE + Entities.ORGANIZATION.getTableName() + Entities.ORGANIZATION.getTableQuery();
    
    public static final String CREATE_OFFICES_TABLE = CREATE_TABLE + Entities.OFFICE.getTableName() + Entities.OFFICE.getTableQuery();
    
    public static final String CREATE_PARKING_LOTS_TABLE = CREATE_TABLE + Entities.PARKING_LOT.getTableName() + Entities.PARKING_LOT.getTableQuery();
    
    public static final String CREATE_RATES_TABLE = CREATE_TABLE + Entities.RATES.getTableName() + Entities.RATES.getTableQuery();
    
    public static final String CREATE_EMPLOYEES_TABLE = CREATE_TABLE + Entities.EMPLOYEE.getTableName() + Entities.EMPLOYEE.getTableQuery();
    
    public static final String CREATE_CUSTOMERS_TABLE = CREATE_TABLE + Entities.CUSTOMER.getTableName() + Entities.CUSTOMER.getTableQuery();
    
    public static final String CREATE_SUBSCRIPTIONS_TABLE = CREATE_TABLE + Entities.SUBSCRIPTION.getTableName() + Entities.SUBSCRIPTION.getTableQuery();
    
    public static final String CREATE_VEHICLES_TABLE = CREATE_TABLE + Entities.VEHICLE.getTableName() + Entities.VEHICLE.getTableQuery();
    
    public static final String CREATE_RESERVATIONS_TABLE = CREATE_TABLE + Entities.RESERVATION.getTableName() + Entities.RESERVATION.getTableQuery();
    
    public static final String CREATE_PARKING_SPACES_TABLE = CREATE_TABLE + Entities.PARKING_SPACE.getTableName() + Entities.PARKING_SPACE.getTableQuery();
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    public static String insertQueryToSelectQuery (String insert) {
        String table = insert.split(" ")[2];
        StringBuilder select = new StringBuilder("SELECT * FROM " + table + " WHERE ");
        insert = insert.replace("INSERT INTO " + table + " ", "");
        String[] parts = insert.split(" VALUES ");
        String[] fields = parts[0].replace("(", "").replace(")", "").split(", ");
        String[] valuesList = parts[1].replaceAll("\\), \\(", " OR ").replace("(", "").replace(")", "").split(" OR ");
        
        for (String s : valuesList) {
            String[] values = s.split(", ");
            select.append("(");
            for (int j = 0 ; j < fields.length ; j++) {
                select.append(fields[j]).append(" = ").append(values[j]).append(", ");
            }
            select = new StringBuilder(select.substring(0, select.length() - 2));
            select.append(") OR ");
        }
        return select.substring(0, select.length() - 4);
    }
}
