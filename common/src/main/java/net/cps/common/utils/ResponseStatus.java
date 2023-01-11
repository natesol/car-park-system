package net.cps.common.utils;

/**
 * Response statuses for the client-server communication.
 */
public enum ResponseStatus {
    SUCCESS,        // request was successful - equivalent to HTTP 200
    CREATED,        // request was successful and a new resource was created - equivalent to HTTP 201
    NO_CONTENT,     // request was successful but no content was returned - equivalent to HTTP 204
    BAD_REQUEST,    // request was malformed (a 'bad request') - equivalent to HTTP 400
    UNAUTHORIZED,   // request was not authorized - equivalent to HTTP 401
    NOT_FOUND,      // request was not found - equivalent to HTTP 404
    ERROR,          // request was not successful (a general error ended the request process) - equivalent to HTTP 500
    BAD_GATEWAY;    // request was not successful (a gateway error ended the request process) - equivalent to HTTP 502
    
    
    /* ----- Utility Methods ------------------------------------------ */
    
    public static ResponseStatus fromString (String status) {
        String statusFormatted = status.trim().toUpperCase().replace(" ", "_").replace("-", "_");
        return switch (statusFormatted) {
            case "SUCCESS" -> SUCCESS;
            case "CREATED" -> CREATED;
            case "NO_CONTENT" -> NO_CONTENT;
            case "BAD_REQUEST" -> BAD_REQUEST;
            case "UNAUTHORIZED" -> UNAUTHORIZED;
            case "NOT_FOUND" -> NOT_FOUND;
            case "ERROR" -> ERROR;
            case "BAD_GATEWAY" -> BAD_GATEWAY;
            default -> throw new IllegalArgumentException("Invalid status: " + status);
        };
    }
    
    public static String toString (ResponseStatus status) {
        return switch (status) {
            case SUCCESS -> "SUCCESS";
            case CREATED -> "CREATED";
            case NO_CONTENT -> "NO_CONTENT";
            case BAD_REQUEST -> "BAD_REQUEST";
            case UNAUTHORIZED -> "UNAUTHORIZED";
            case NOT_FOUND -> "NOT_FOUND";
            case ERROR -> "ERROR";
            case BAD_GATEWAY -> "BAD_GATEWAY";
            default -> throw new IllegalArgumentException("Invalid status: " + status);
        };
    }
}
