package net.cps.common.utils;

public enum ComplaintStatus {
    ACTIVE,         // Active complaint - waiting for resolution
    RESOLVED,         // Complaint closed - no further action required
    CANCELLED;      // Complaint cancelled - no further action required
}
