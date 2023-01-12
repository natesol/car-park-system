package net.cps.common.entities;

import net.cps.common.utils.OrganizationType;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "managements")
@PrimaryKeyJoinColumn(name = "organization_id")
public class Management extends Organization implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "INT NOT NULL AUTO_INCREMENT")
    private Integer id;
    @NotNull
    @Column(name = "name", columnDefinition = "VARCHAR(55) NOT NULL")
    private String name;
    @NotNull
    @Column(name = "street_number", columnDefinition = "MEDIUMINT NOT NULL")
    private Integer streetNumber;
    @NotNull
    @Column(name = "street", columnDefinition = "VARCHAR(55) NOT NULL")
    private String street;
    @NotNull
    @Column(name = "city", columnDefinition = "VARCHAR(55) NOT NULL")
    private String city;
    @NotNull
    @Column(name = "state", columnDefinition = "VARCHAR(55) NOT NULL")
    private String state;
    
    public static final OrganizationType TYPE = OrganizationType.MANAGEMENT;
    public static final String DEFAULT_NAME = "Management Name";
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public Management () {
        super(TYPE);
        this.name = DEFAULT_NAME;
        this.streetNumber = Organization.DEFAULT_STREET_NUMBER;
        this.street = Organization.DEFAULT_STREET;
        this.city = Organization.DEFAULT_CITY;
        this.state = Organization.DEFAULT_STATE;
    }
    
    public Management (@NotNull String name, @NotNull String street, @NotNull Integer streetNumber, @NotNull String city, @NotNull String state) {
        super(TYPE);
        this.name = name;
        this.street = street;
        this.streetNumber = streetNumber;
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
    
    public @NotNull String getName () {
        return name;
    }
    
    public void setName (@NotNull String name) {
        this.name = name;
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
    public @NotNull Integer getStreetNumber () {
        return streetNumber;
    }
    
    @Override
    public void setStreetNumber (@NotNull Integer streetNumber) {
        this.streetNumber = streetNumber;
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
