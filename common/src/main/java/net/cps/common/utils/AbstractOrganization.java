package net.cps.common.utils;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "organizations")
public abstract class AbstractOrganization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;
    @NotNull
    @Column(name = "type")
    private OrganizationType type;
    @NotNull
    @Column(name = "street_number")
    private Integer streetNumber;
    @NotNull
    @Column(name = "street")
    private String street;
    @NotNull
    @Column(name = "city")
    private String city;
    @NotNull
    @Column(name = "state")
    private String state;
    
    
    final static Integer DEFAULT_STREET_NUMBER = 1;
    final static String DEFAULT_STREET = "Street";
    final static String DEFAULT_CITY = "City";
    final static String DEFAULT_STATE = "IL";
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public AbstractOrganization () {}
    
    public AbstractOrganization (@NotNull OrganizationType type) {
        this.type = type;
        this.streetNumber = DEFAULT_STREET_NUMBER;
        this.street = DEFAULT_STREET;
        this.city = DEFAULT_CITY;
        this.state = DEFAULT_STATE;
    }
    
    public AbstractOrganization (@NotNull OrganizationType type, @NotNull String street, @NotNull Integer streetNumber, @NotNull String city, @NotNull String state) {
        this.type = type;
        this.streetNumber = streetNumber;
        this.street = street;
        this.city = city;
        this.state = state;
    }
    
    
    /* ----- Getters and Setters ------------------------------------ */
    
    public Integer getId () {
        return id;
    }
    
    public void setId (Integer id) {
        this.id = id;
    }
    
    public @NotNull OrganizationType getType () {
        return type;
    }
    
    public void setType (@NotNull OrganizationType type) {
        this.type = type;
    }
    
    public @NotNull Integer getStreetNumber () {
        return streetNumber;
    }
    
    public void setStreetNumber (@NotNull Integer streetNumber) {
        this.streetNumber = streetNumber;
    }
    
    public @NotNull String getStreet () {
        return street;
    }
    
    public void setStreet (@NotNull String street) {
        this.street = street;
    }
    
    public @NotNull String getCity () {
        return city;
    }
    
    public void setCity (@NotNull String city) {
        this.city = city;
    }
    
    public @NotNull String getState () {
        return state;
    }
    
    public void setState (@NotNull String state) {
        this.state = state;
    }
    
    public String getAddress () {
        return street + "ST. " + streetNumber + ", " + city + ", " + state;
    }
    
    public void setAddress (String address) {
        String[] addressParts = address.split(", ");
        street = addressParts[0].split("ST. ")[0].trim();
        streetNumber = Integer.parseInt(addressParts[0].split("ST. ")[1].trim());
        city = addressParts[1].trim();
        state = addressParts[2].trim();
    }
    
    public void setAddress (@NotNull String street, @NotNull Integer streetNumber, @NotNull String city, @NotNull String state) {
        this.street = street;
        this.streetNumber = streetNumber;
        this.city = city;
        this.state = state;
    }
}