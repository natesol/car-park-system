package net.cps.common.utils;


public enum ReservationStatus {
    RESERVED,           // Reserved - not yet fulfilled.
    CANCELLED,          // Cancelled - cancelled by customer before fulfilled.
    CHECKED_IN,         // Checked in - currently parking in the parking lot.
    CHECKED_OUT         // Checked out - reservation fulfilled successfully.
}
