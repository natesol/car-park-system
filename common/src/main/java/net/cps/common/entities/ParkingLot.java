package net.cps.common.entities;

import net.cps.common.utils.OrganizationType;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "parking_lots")
@PrimaryKeyJoinColumn(name = "organization_id")
public class ParkingLot extends Organization implements Serializable {
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
    @Column(name = "street_name", columnDefinition = "VARCHAR(55) NOT NULL")
    private String streetName;
    @NotNull
    @Column(name = "city_name", columnDefinition = "VARCHAR(55) NOT NULL")
    private String cityName;
    @NotNull
    @Column(name = "country_symbol", columnDefinition = "CHAR(2) NOT NULL")
    private String countrySymbol;
    @NotNull
    @Column(name = "num_of_floors", columnDefinition = "TINYINT NOT NULL")
    private Integer numOfFloors;
    @NotNull
    @Column(name = "floor_rows", columnDefinition = "TINYINT NOT NULL")
    private Integer floorRows;
    @NotNull
    @Column(name = "floor_cols", columnDefinition = "TINYINT NOT NULL")
    private Integer floorCols;
    @NotNull
    @OneToOne(mappedBy = "parkingLot", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Rates rates;
    
    public static final OrganizationType TYPE = OrganizationType.PARKING_LOT;
    public static final String DEFAULT_NAME = "Parking Lot Name";
    public static final Integer DEFAULT_FLOOR_NUM = 3;
    public static final Integer DEFAULT_FLOOR_ROWS = 3;
    public static final Integer DEFAULT_FLOOR_COLS = 1;
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public ParkingLot () {
        super(TYPE);
        this.name = DEFAULT_NAME;
        this.streetNumber = Organization.DEFAULT_STREET_NUMBER;
        this.streetName = Organization.DEFAULT_STREET;
        this.cityName = Organization.DEFAULT_CITY;
        this.countrySymbol = Organization.DEFAULT_STATE;
        this.numOfFloors = DEFAULT_FLOOR_NUM;
        this.floorRows = DEFAULT_FLOOR_ROWS;
        this.floorCols = DEFAULT_FLOOR_COLS;
        this.rates = new Rates(this);
    }
    
    public ParkingLot (@NotNull String name, @NotNull String streetName, @NotNull Integer streetNumber, @NotNull String cityName, @NotNull String countrySymbol, @NotNull Integer floorCols) {
        super(TYPE);
        this.name = name;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.cityName = cityName;
        this.countrySymbol = countrySymbol;
        this.numOfFloors = DEFAULT_FLOOR_NUM;
        this.floorRows = DEFAULT_FLOOR_ROWS;
        this.floorCols = floorCols;
        this.rates = new Rates(this);
    }
    
    public ParkingLot (@NotNull String name, @NotNull String streetName, @NotNull Integer streetNumber, @NotNull String cityName, @NotNull String countrySymbol, @NotNull Integer numOfFloors, @NotNull Integer floorRows, @NotNull Integer floorCols) {
        super(TYPE);
        this.name = name;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.cityName = cityName;
        this.countrySymbol = countrySymbol;
        this.numOfFloors = numOfFloors;
        this.floorRows = floorRows;
        this.floorCols = floorCols;
        this.rates = new Rates(this);
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
    
    public @NotNull Integer getStreetNumber () {
        return streetNumber;
    }
    
    public void setStreetNumber (@NotNull Integer streetNumber) {
        this.streetNumber = streetNumber;
    }
    
    public @NotNull String getStreetName () {
        return streetName;
    }
    
    public void setStreetName (@NotNull String streetName) {
        this.streetName = streetName;
    }
    
    public @NotNull String getCityName () {
        return cityName;
    }
    
    public void setCityName (@NotNull String cityName) {
        this.cityName = cityName;
    }
    
    public @NotNull String getCountrySymbol () {
        return countrySymbol;
    }
    
    public void setCountrySymbol (@NotNull String countrySymbol) {
        this.countrySymbol = countrySymbol;
    }
    
    public @NotNull Integer getNumOfFloors () {
        return numOfFloors;
    }
    
    public void setNumOfFloors (@NotNull Integer numOfFloors) {
        this.numOfFloors = numOfFloors;
    }
    
    public @NotNull Integer getFloorRows () {
        return floorRows;
    }
    
    public void setFloorRows (@NotNull Integer floorRows) {
        this.floorRows = floorRows;
    }
    
    public @NotNull Integer getFloorCols () {
        return this.floorCols;
    }
    
    public void setFloorCols (@NotNull Integer floorCols) {
        this.floorCols = floorCols;
    }
    
    public Integer getCapacity () {
        return this.floorCols * floorRows * numOfFloors;
    }
    
    public @NotNull Rates getRates () {
        return this.rates;
    }
    
    public void setRates (@NotNull Rates rates) {
        this.rates = rates;
    }
    
    public void setRates (Double hourlyOccasionalParking, Double hourlyOnetimeParking, Double regularSubscriptionSingleVehicle, Double regularSubscriptionMultipleVehicles, Double fullSubscriptionSingleVehicle) {
        if (hourlyOccasionalParking != null) {
            this.rates.setHourlyOccasionalParking(hourlyOccasionalParking);
        }
        if (hourlyOnetimeParking != null) {
            this.rates.setHourlyOnetimeParking(hourlyOnetimeParking);
        }
        if (regularSubscriptionSingleVehicle != null) {
            this.rates.setRegularSubscriptionSingleVehicle(regularSubscriptionSingleVehicle);
        }
        if (regularSubscriptionMultipleVehicles != null) {
            this.rates.setRegularSubscriptionMultipleVehicles(regularSubscriptionMultipleVehicles);
        }
        if (fullSubscriptionSingleVehicle != null) {
            this.rates.setFullSubscriptionSingleVehicle(fullSubscriptionSingleVehicle);
        }
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    @Override
    public String toString () {
        return "ParkingLot {" +
                "id: " + id +
                ", name: '" + name + "'" +
                ", address: '" + getAddress() + "'" +
                ", numOfFloors: " + numOfFloors +
                ", floorRows: " + floorRows +
                ", floorCols: " + floorCols +
                ", rates: " + rates +
                "}";
    }
}
