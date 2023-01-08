package net.cps.common.utils;

public enum ParkingSpaceCondition {
    AVAILABLE,
    OCCUPIED,
    RESERVED,
    DISABLED,
    OUT_OF_ORDER;
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    public static String toString(ParkingSpaceCondition condition) {
        return switch (condition) {
            case AVAILABLE -> "Available";
            case OCCUPIED -> "Occupied";
            case RESERVED -> "Reserved";
            case DISABLED -> "Disabled";
            case OUT_OF_ORDER -> "Out of Order";
            default -> "Unknown";
        };
    }
    
    public static ParkingSpaceCondition fromString(String condition) {
        String conditionFormatted = condition.toLowerCase().trim().replaceAll(" ", "_").replaceAll("-", "_");
        return switch (conditionFormatted) {
            case "AVAILABLE" -> AVAILABLE;
            case "OCCUPIED" -> OCCUPIED;
            case "RESERVED" -> RESERVED;
            case "DISABLED" -> DISABLED;
            case "OUT_OF_ORDER" -> OUT_OF_ORDER;
            default -> null;
        };
    }
}
