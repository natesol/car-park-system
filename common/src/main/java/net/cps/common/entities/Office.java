package net.cps.common.entities;

import net.cps.common.utils.Organization;
import net.cps.common.utils.OrganizationType;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "offices")
public class Office implements Organization, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;
    @NotNull
    @Column(name = "name")
    private String name;
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
    
    
    public static final OrganizationType TYPE = OrganizationType.MANAGEMENT;
    public static final String DEFAULT_NAME = "Management Name";
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public Office () {
        this.type = TYPE;
        this.name = DEFAULT_NAME;
        this.streetNumber = Organization.DEFAULT_STREET_NUMBER;
        this.street = Organization.DEFAULT_STREET;
        this.city = Organization.DEFAULT_CITY;
        this.state = Organization.DEFAULT_STATE;
    }
    
    public Office (@NotNull String name, @NotNull String street, @NotNull Integer streetNumber, @NotNull String city, @NotNull String state) {
        this.type = TYPE;
        this.name = name;
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
    
    @Override
    public @NotNull OrganizationType getType () {
        return type;
    }
    
    @Override
    public void setType (@NotNull OrganizationType type) {
        this.type = type;
    }
    
    public @NotNull String getName () {
        return name;
    }
    
    public void setName (@NotNull String name) {
        this.name = name;
    }
    
    @Override
    public @NotNull Integer getStreetNumber () {
        return streetNumber;
    }
    
    @Override
    public void setStreetNumber (@NotNull Integer streetNumber) {
        this.streetNumber = streetNumber;
    }
    
    @Override
    public @NotNull String getStreet () {
        return street;
    }
    
    @Override
    public void setStreet (@NotNull String street) {
        this.street = street;
    }
    
    @Override
    public @NotNull String getCity () {
        return city;
    }
    
    @Override
    public void setCity (@NotNull String city) {
        this.city = city;
    }
    
    @Override
    public @NotNull String getState () {
        return state;
    }
    
    @Override
    public void setState (@NotNull String state) {
        this.state = state;
    }
}
