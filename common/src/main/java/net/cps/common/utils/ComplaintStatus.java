package net.cps.common.utils;

public enum ComplaintStatus {
    PLACEHOLDER,                // Fix for `Hibernate` enum mapping starting from 1.
    ACTIVE,         // Active complaint - waiting for resolution
    RESOLVED,         // Complaint closed - no further action required
    CANCELLED;      // Complaint cancelled - no further action required
}
