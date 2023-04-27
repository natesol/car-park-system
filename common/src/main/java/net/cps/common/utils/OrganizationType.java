package net.cps.common.utils;


/**
 * Organizations types for the 'CityPark' system.
 **/
public enum OrganizationType {
    MANAGEMENT,         // The Company HQ Offices (Management).
    OFFICE,             // General Offices.
    PARKING_LOT;        // Parking Lots.
    
    
    /* ----- Utility Methods ------------------------------------------ */
    
    public static OrganizationType fromString (String type) {
        String typeFormatted = type.trim().toUpperCase().replace(" ", "_").replace("-", "_");
        
        return switch (type) {
            case "MANAGEMENT" -> MANAGEMENT;
            case "OFFICE" -> OFFICE;
            case "PARKING_LOT" -> PARKING_LOT;
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
    }
    
    public static String toString (OrganizationType type) {
        return switch (type) {
            case MANAGEMENT -> "Management";
            case OFFICE -> "Office";
            case PARKING_LOT -> "Parking Lot";
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
    }
}
