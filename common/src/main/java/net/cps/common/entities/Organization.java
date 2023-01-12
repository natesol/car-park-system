package net.cps.common.entities;

import net.cps.common.utils.OrganizationType;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "organizations")
public abstract class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "INT NOT NULL AUTO_INCREMENT")
    private Integer id;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "ENUM('MANAGEMENT', 'OFFICE', 'PARKING_LOT') NOT NULL")
    private OrganizationType type;
    
    public static final Integer DEFAULT_STREET_NUMBER = 1;
    public static final String DEFAULT_STREET = "Street";
    public static final String DEFAULT_CITY = "City";
    public static final String DEFAULT_STATE = "IL";
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public Organization () {}
    
    public Organization (@NotNull OrganizationType type) {
        this.type = type;
    }
    
    
    /* ----- Getters and Setters ------------------------------------ */
    
    public Integer getOrganizationId () {
        return id;
    }
    
    public void setOrganizationId (Integer id) {
        this.id = id;
    }
    
    public @NotNull OrganizationType getType () {
        return type;
    }
    
    public void setType (@NotNull OrganizationType type) {
        this.type = type;
    }
    
    abstract public String getStreetName ();
    
    abstract public void setStreetName (String streetName);
    
    abstract public Integer getStreetNumber ();
    
    abstract public void setStreetNumber (Integer streetNumber);
    
    abstract public String getCityName ();
    
    abstract public void setCityName (String cityName);
    
    abstract public String getCountrySymbol ();
    
    abstract public void setCountrySymbol (String countrySymbol);
    
    public String getAddress () {
        return getStreetName() + "ST. " + getStreetNumber() + ", " + getCityName() + ", " + getCountrySymbol();
    }
    
    public void setAddress (String address) {
        String[] addressParts = address.split(", ");
        setStreetName(addressParts[0].split("ST. ")[0].trim());
        setStreetNumber(Integer.parseInt(addressParts[0].split("ST. ")[1].trim()));
        setCityName(addressParts[1].trim());
        setCountrySymbol(addressParts[2].trim());
    }
    
    public void setAddress (@NotNull String street, @NotNull Integer streetNumber, @NotNull String city, @NotNull String state) {
        setStreetName(street);
        setStreetNumber(streetNumber);
        setCityName(city);
        setCountrySymbol(state);
    }
}