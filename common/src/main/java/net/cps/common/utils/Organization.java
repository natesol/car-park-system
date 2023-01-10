package net.cps.common.utils;

import org.jetbrains.annotations.NotNull;


public interface Organization {
    Integer DEFAULT_STREET_NUMBER = 1;
    String DEFAULT_STREET = "Street";
    String DEFAULT_CITY = "City";
    String DEFAULT_STATE = "IL";
    
    
    /* ----- Constructors ------------------------------------------- */
    
    // interface...
    
    
    /* ----- Getters and Setters ------------------------------------ */
    
    Integer getId ();
    
    void setId (Integer id);
    
    @NotNull OrganizationType getType ();
    
    void setType (@NotNull OrganizationType type);
    
    @NotNull Integer getStreetNumber ();
    
    void setStreetNumber (@NotNull Integer streetNumber);
    
    @NotNull String getStreet ();
    
    void setStreet (@NotNull String street);
    
    @NotNull String getCity ();
    
    void setCity (@NotNull String city);
    
    @NotNull String getState ();
    
    void setState (@NotNull String state);
    
    default String getAddress () {
        return getStreet() + "ST. " + getStreetNumber() + ", " + getCity() + ", " + getState();
    }
    
    default void setAddress (String address) {
        String[] addressParts = address.split(", ");
        setStreet(addressParts[0].split("ST. ")[0].trim());
        setStreetNumber(Integer.parseInt(addressParts[0].split("ST. ")[1].trim()));
        setCity(addressParts[1].trim());
        setState(addressParts[2].trim());
    }
    
    default void setAddress (@NotNull String street, @NotNull Integer streetNumber, @NotNull String city, @NotNull String state) {
        setStreet(street);
        setStreetNumber(streetNumber);
        setCity(city);
        setState(state);
    }
}