package net.cps.common.utils;


/**
 * Employees roles of the CPS system.
 */
public enum EmployeeRole {
    ADMIN,
    EMPLOYEE,
    NETWORK_MANAGER,
    PARKING_LOT_MANAGER,
    PARKING_LOT_EMPLOYEE,
    CUSTOMER_SERVICE_EMPLOYEE;
    
    
    /* ----- Utility Methods ------------------------------------------ */
    
    public static String toString (EmployeeRole role) {
        return switch (role) {
            case ADMIN -> "Admin";
            case EMPLOYEE -> "Employee";
            case NETWORK_MANAGER -> "Network Manager";
            case PARKING_LOT_MANAGER -> "Parking Lot Manager";
            case PARKING_LOT_EMPLOYEE -> "Parking Lot Employee";
            case CUSTOMER_SERVICE_EMPLOYEE -> "Customer Service Employee";
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        };
    }
    
    public static EmployeeRole fromString (String role) {
        String roleFormated = role.trim().toUpperCase().replace(" ", "_").replace("-", "_");
        
        return switch (roleFormated) {
            case "ADMIN" -> ADMIN;
            case "EMPLOYEE" -> EMPLOYEE;
            case "NETWORK_MANAGER" -> NETWORK_MANAGER;
            case "PARKING_LOT_MANAGER" -> PARKING_LOT_MANAGER;
            case "PARKING_LOT_EMPLOYEE" -> PARKING_LOT_EMPLOYEE;
            case "CUSTOMER_SERVICE_EMPLOYEE" -> CUSTOMER_SERVICE_EMPLOYEE;
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        };
    }
}

