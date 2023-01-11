package net.cps.common.utils;


/**
 * Messages types for the client-server communication.
 */
public enum MessageType {
    DEFAULT,
    REQUEST,
    RESPONSE;
    
    
    /* ----- Utility Methods ------------------------------------------ */
    
    public static MessageType fromString (String type) {
        return switch (type) {
            case "DEFAULT" -> DEFAULT;
            case "REQUEST" -> REQUEST;
            case "RESPONSE" -> RESPONSE;
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
    }
}
