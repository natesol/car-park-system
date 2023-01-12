package net.cps.common.utils;


/**
 * Request type enum.
 * This enum is used to define the type of request to be sent to the server.
 */
public enum RequestType {
    GET,        // GET request - get a data from the server (e.g. get a list of employees).
    CREATE,     // CREATE request - create a new data on the server (e.g. create a new employee).
    UPDATE,     // UPDATE request - update an existing data on the server (e.g. update an existing employee).
    DELETE,     // DELETE request - delete an existing data on the server (e.g. delete an existing employee).
    AUTH,       // AUTH request - authenticate the user on the server (e.g. approve the employee's credentials).
    CUSTOM;     // CUSTOM request - a generic request type for custom requests (any other request that will need a special handling on the server side).
    
    
    /* ----- Utility Methods ------------------------------------------ */
    
    /**
     * Returns the RequestType enum value that corresponds to the given string.
     *
     * @param type the string to convert to RequestType enum value.
     */
    public static RequestType fromString (String type) {
        String typeFormatted = type.trim().toUpperCase().replaceAll(" ", "_").replaceAll("-", "_");
    
        try {
            return RequestType.valueOf(typeFormatted);
        }
        catch (IllegalArgumentException e) {
            return RequestType.CUSTOM;
        }
    }
    
    /**
     * Returns the string representation of the RequestType enum value.
     *
     * @param type the RequestType enum value to convert to string.
     */
    public static String toString (RequestType type) {
        return type.toString().replaceAll("_", " ");
    }
}
