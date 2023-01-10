package net.cps.common.utils;


/**
 * Employees roles of the CPS system.
 */
public enum EmployeeRole {
    ADMIN,
    NETWORK_MANAGER,
    CUSTOMER_SERVICE_EMPLOYEE,
    PARKING_LOT_MANAGER,
    PARKING_LOT_EMPLOYEE,
    EMPLOYEE;
    
    
    /* ----- Utility Methods ------------------------------------------ */
    
    public static String toString (EmployeeRole role) {
        return switch (role) {
            case ADMIN -> "Admin";
            case NETWORK_MANAGER -> "Network Manager";
            case CUSTOMER_SERVICE_EMPLOYEE -> "Customer Service Employee";
            case PARKING_LOT_MANAGER -> "Parking Lot Manager";
            case PARKING_LOT_EMPLOYEE -> "Parking Lot Employee";
            case EMPLOYEE -> "Employee";
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        };
    }
    
    public static EmployeeRole fromString (String role) {
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

