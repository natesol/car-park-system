package net.cps.common.utils;


public enum ReservationStatus {
    PENDING,           // Waiting - not yet fulfilled.
    CANCELLED,          // Cancelled - cancelled by customer before fulfilled.
    CHECKED_IN,         // Checked in - currently parking in the parking lot.
    CHECKED_OUT;         // Checked out - reservation fulfilled successfully.
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    public String toString () {
        return switch (this) {
            case PENDING -> "Pending";
            case CANCELLED -> "Cancelled";
            case CHECKED_IN -> "Checked In";
            case CHECKED_OUT -> "Checked Out";
            default -> "Unknown";
        };
    }
}
