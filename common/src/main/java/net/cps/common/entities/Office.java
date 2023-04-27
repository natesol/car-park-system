package net.cps.common.entities;

import net.cps.common.utils.OrganizationType;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "offices")
@PrimaryKeyJoinColumn(name = "id")
public class Office extends Organization implements Serializable {
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;
    @NotNull
    @Column(name = "name", columnDefinition = "VARCHAR(55) NOT NULL")
    private String name;
    @NotNull
    @Column(name = "street_number", columnDefinition = "MEDIUMINT NOT NULL")
    private Integer streetNumber;
    @NotNull
    @Column(name = "street_name", columnDefinition = "VARCHAR(55) NOT NULL")
    private String streetName;
    @NotNull
    @Column(name = "city_name", columnDefinition = "VARCHAR(55) NOT NULL")
    private String cityName;
    @NotNull
    @Column(name = "country_symbol", columnDefinition = "CHAR(2) NOT NULL")
    private String countrySymbol;
    
    public static final OrganizationType DEFAULT_TYPE = OrganizationType.OFFICE;
    public static final String DEFAULT_NAME = "Office Name";
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public Office () {
        super(DEFAULT_TYPE);
        this.name = DEFAULT_NAME;
        this.streetNumber = Organization.DEFAULT_STREET_NUMBER;
        this.streetName = Organization.DEFAULT_STREET;
        this.cityName = Organization.DEFAULT_CITY;
        this.countrySymbol = Organization.DEFAULT_STATE;
    }
    
    public Office (OrganizationType type) {
        super(type);
        this.name = DEFAULT_NAME;
        this.streetNumber = Organization.DEFAULT_STREET_NUMBER;
        this.streetName = Organization.DEFAULT_STREET;
        this.cityName = Organization.DEFAULT_CITY;
        this.countrySymbol = Organization.DEFAULT_STATE;
    }
    
    public Office (@NotNull String name, @NotNull String streetName, @NotNull Integer streetNumber, @NotNull String cityName, @NotNull String countrySymbol) {
        super(DEFAULT_TYPE);
        this.name = name;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.cityName = cityName;
        this.countrySymbol = countrySymbol;
    }
    
    public Office (OrganizationType type, @NotNull String name, @NotNull String streetName, @NotNull Integer streetNumber, @NotNull String cityName, @NotNull String countrySymbol) {
        super(type);
        this.name = name;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.cityName = cityName;
        this.countrySymbol = countrySymbol;
    }
    
    
    /* ----- Getters and Setters ------------------------------------ */
    
    public Integer getId () {
        return super.getOrganizationId();
    }
    
    public void setId (Integer id) {
        super.setOrganizationId(id);
    }
    
    public @NotNull String getName () {
        return name;
    }
    
    public void setName (@NotNull String name) {
        this.name = name;
    }
    
    @Override
    public @NotNull String getStreetName () {
        return streetName;
    }
    
    @Override
    public void setStreetName (@NotNull String streetName) {
        this.streetName = streetName;
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
    public @NotNull String getCityName () {
        return cityName;
    }
    
    @Override
    public void setCityName (@NotNull String cityName) {
        this.cityName = cityName;
    }
    
    @Override
    public @NotNull String getCountrySymbol () {
        return countrySymbol;
    }
    
    @Override
    public void setCountrySymbol (@NotNull String countrySymbol) {
        this.countrySymbol = countrySymbol;
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    @Override
    public String toString () {
        return "Office {" +
                "id: " + getId() +
                ", type: '" + getType() + "'" +
                ", name: '" + name + "'" +
                ", address: '" + getAddress() + "'" +
                "}";
    }
    
}
