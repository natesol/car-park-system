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
    @Column(name = "type", nullable = false, columnDefinition = "ENUM('MANAGEMENT', 'PARKING_LOT', 'OFFICE') NOT NULL")
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
    
    abstract public String getStreet ();
    
    abstract public void setStreet (String street);
    
    abstract public Integer getStreetNumber ();
    
    abstract public void setStreetNumber (Integer streetNumber);
    
    abstract public String getCity ();
    
    abstract public void setCity (String city);
    
    abstract public String getState ();
    
    abstract public void setState (String state);
    
    public String getAddress () {
        return getStreet() + "ST. " + getStreetNumber() + ", " + getCity() + ", " + getState();
    }
    
    public void setAddress (String address) {
        String[] addressParts = address.split(", ");
        setStreet(addressParts[0].split("ST. ")[0].trim());
        setStreetNumber(Integer.parseInt(addressParts[0].split("ST. ")[1].trim()));
        setCity(addressParts[1].trim());
        setState(addressParts[2].trim());
    }
    
    public void setAddress (@NotNull String street, @NotNull Integer streetNumber, @NotNull String city, @NotNull String state) {
        setStreet(street);
        setStreetNumber(streetNumber);
        setCity(city);
        setState(state);
    }
}