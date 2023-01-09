package net.cps.common.utils;

/**
 * Organizations types for the client-server communication.
 */
public enum OrganizationType {
    MANAGEMENT,
    PARKING_LOT,
    OFFICE;
    
    
    /* ----- Utility Methods ------------------------------------------ */
    
    public static OrganizationType fromString (String type) {
        return switch (type) {
            case "MANAGEMENT" -> MANAGEMENT;
            case "PARKING_LOT" -> PARKING_LOT;
            case "OFFICE" -> OFFICE;
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
    }
}
