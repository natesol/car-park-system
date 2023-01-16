package net.cps.common.utils;

public enum SubscriptionState {
    ACTIVE,         // Subscription is currently active
    EXPIRED,        // Subscription has expired without being renewed
    RENEWED,        // Subscription has been renewed (at least once) and is active
    CANCELLED       // Subscription has been cancelled by the customer
}
