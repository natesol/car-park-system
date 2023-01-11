package net.cps.common.utils;

/**
 * Organizations types for the client-server communication.
 */
public enum OrganizationType {
    //DUMMY_VALUE, // Fix MySQL8 enum start with 1.
    MANAGEMENT,
    PARKING_LOT,
    OFFICE;
    
    
    /* ----- Utility Methods ------------------------------------------ */
    
    public static OrganizationType fromString (String type) {
        String typeFormatted = type.trim().toUpperCase().replace(" ", "_").replace("-", "_");
        
        return switch (type) {
            case "MANAGEMENT" -> MANAGEMENT;
            case "PARKING_LOT" -> PARKING_LOT;
            case "OFFICE" -> OFFICE;
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
    }
    
    public static String toString (OrganizationType type) {
        return switch (type) {
            case MANAGEMENT -> "Management";
            case PARKING_LOT -> "Parking Lot";
            case OFFICE -> "Office";
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
    }
}
