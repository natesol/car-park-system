package net.cps.common.utils;


/**
 * Requests types for the client-server communication.
 */
public enum RequestType {
    GET,
    POST,
    UPDATE,
    DELETE,
    AUTH,
    CUSTOM;
    
    
    /* ----- Utility Methods ------------------------------------------ */
    
    public static RequestType fromString (String type) {
        return switch (type) {
            case "GET" -> GET;
            case "POST" -> POST;
            case "UPDATE" -> UPDATE;
            case "DELETE" -> DELETE;
            case "AUTH" -> AUTH;
            case "CUSTOM" -> CUSTOM;
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
    }
}
