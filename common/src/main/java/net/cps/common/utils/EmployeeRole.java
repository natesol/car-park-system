package net.cps.common.utils;


import org.jetbrains.annotations.NotNull;

/**
 * Employees roles enum.
 * This enum is used to determine the role of an employee in the system.
 **/
public enum EmployeeRole {
    ADMIN,                      // System administrator
    NETWORK_MANAGER,            // Network manager ('CityPark' CEO) - belongs to the `Management` organization.
    CUSTOMER_SERVICE_EMPLOYEE,  // Customer service employee        - can belong to the `Management` organization or to a general `Office` organization.
    PARKING_LOT_MANAGER,        // Parking lot manager              - belongs to a `ParkingLot` organization.
    PARKING_LOT_EMPLOYEE,       // Parking lot employee             - belongs to a `ParkingLot` organization.
    EMPLOYEE;                   // General employee                 - can belong to the `Management` organization or to a general `Office` organization.
    
    
    
    /* ----- Utility Methods ------------------------------------------ */
    
    public static String toString (@NotNull EmployeeRole role) {
        return switch (role) {
            case ADMIN -> "System Administrator";
            case NETWORK_MANAGER -> "Network Manager";
            case CUSTOMER_SERVICE_EMPLOYEE -> "Customer Service Employee";
            case PARKING_LOT_MANAGER -> "Parking Lot Manager";
            case PARKING_LOT_EMPLOYEE -> "Parking Lot Employee";
            case EMPLOYEE -> "Employee";
            default -> throw new IllegalArgumentException("Unexpected value: " + role);
        };
    }
    
    public String toPath () {
        return EmployeeRole.toPath(this);
    }
    
    public static String toPath (@NotNull EmployeeRole role) {
        return switch (role) {
            case ADMIN -> "admin";
            case NETWORK_MANAGER -> "networkManager";
            case CUSTOMER_SERVICE_EMPLOYEE -> "customerService";
            case PARKING_LOT_MANAGER -> "parkingLotManager";
            case PARKING_LOT_EMPLOYEE -> "parkingLotEmployee";
            case EMPLOYEE -> "employee";
            default -> throw new IllegalArgumentException("Unexpected value: " + role);
        };
    }
    
    public String toInitials () {
        return EmployeeRole.toInitials(this);
    }
    
    public static String toInitials (@NotNull EmployeeRole role) {
        return switch (role) {
            case ADMIN -> "AD";
            case NETWORK_MANAGER -> "NM";
            case CUSTOMER_SERVICE_EMPLOYEE -> "CS";
            case PARKING_LOT_MANAGER -> "PM";
            case PARKING_LOT_EMPLOYEE -> "PE";
            case EMPLOYEE -> "EM";
            default -> throw new IllegalArgumentException("Unexpected value: " + role);
        };
    }
    
    public static EmployeeRole fromString (@NotNull String role) {
        String roleFormated = role.trim().toUpperCase().replace(" ", "_").replace("-", "_");
        
        return switch (roleFormated) {
            case "ADMIN" -> ADMIN;
            case "NETWORK_MANAGER" -> NETWORK_MANAGER;
            case "CUSTOMER_SERVICE_EMPLOYEE" -> CUSTOMER_SERVICE_EMPLOYEE;
            case "PARKING_LOT_MANAGER" -> PARKING_LOT_MANAGER;
            case "PARKING_LOT_EMPLOYEE" -> PARKING_LOT_EMPLOYEE;
            case "EMPLOYEE" -> EMPLOYEE;
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        };
    }
}

