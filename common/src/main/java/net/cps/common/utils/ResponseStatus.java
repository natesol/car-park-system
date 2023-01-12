package net.cps.common.utils;

/**
 * Response statuses for the client-server communication.
 */
public enum ResponseStatus {
    SUCCESS,        // request was successful - equivalent to HTTP 200
    FINISHED,       // request was successful and all the resources changes finished successfully (for CREATE, UPDATE, DELETE) - equivalent to HTTP 201
    NOT_FOUND,      // request was successful but the requested content was not found - equivalent to HTTP 204 or a 404
    UNAUTHORIZED,   // request was not authorized - equivalent to HTTP 401
    BAD_REQUEST,    // request was not successful (a bad request format, or some gateway error ended the request process) - equivalent to HTTP 400 or a 502
    ERROR;          // request was not successful (a general fatal error ended the request process) - equivalent to HTTP 500
    
    
    /* ----- Utility Methods ------------------------------------------ */
    
    public static ResponseStatus fromString (String status) {
        String statusFormatted = status.trim().toUpperCase().replace(" ", "_").replace("-", "_");
        return switch (statusFormatted) {
            case "SUCCESS" -> SUCCESS;
            case "FINISHED" -> FINISHED;
            case "BAD_REQUEST" -> BAD_REQUEST;
            case "UNAUTHORIZED" -> UNAUTHORIZED;
            case "NOT_FOUND" -> NOT_FOUND;
            case "ERROR" -> ERROR;
            default -> throw new IllegalArgumentException("Invalid status: " + status);
        };
    }
    
    public static String toString (ResponseStatus status) {
        return switch (status) {
            case SUCCESS -> "SUCCESS";
            case FINISHED -> "FINISHED";
            case BAD_REQUEST -> "BAD_REQUEST";
            case UNAUTHORIZED -> "UNAUTHORIZED";
            case NOT_FOUND -> "NOT_FOUND";
            case ERROR -> "ERROR";
            default -> throw new IllegalArgumentException("Invalid status: " + status);
        };
    }
}
