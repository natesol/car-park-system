package net.cps.common.entities;

import net.cps.common.utils.Organization;
import net.cps.common.utils.OrganizationType;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "parking_lots")
public class ParkingLot implements Organization, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;
    @NotNull
    @Column(name = "name", nullable = false)
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
    @NotNull
    @Column(name = "num_of_floors", nullable = false)
    private Integer numOfFloors;
    @NotNull
    @Column(name = "floor_rows", nullable = false)
    private Integer floorRows;
    @NotNull
    @Column(name = "floor_cols", nullable = false)
    private Integer floorCols;
    @NotNull
    @OneToOne(mappedBy = "parkingLot", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Rates rates;
    
    public static final String DEFAULT_NAME = "Parking Lot Name";
    public static final OrganizationType TYPE = OrganizationType.PARKING_LOT;
    
    public static final Integer DEFAULT_FLOOR_NUM = 3;
    public static final Integer DEFAULT_FLOOR_ROWS = 3;
    public static final Integer DEFAULT_FLOOR_COLS = 1;
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public ParkingLot () {
        this.type = TYPE;
        this.name = DEFAULT_NAME;
        this.streetNumber = Organization.DEFAULT_STREET_NUMBER;
        this.street = Organization.DEFAULT_STREET;
        this.city = Organization.DEFAULT_CITY;
        this.state = Organization.DEFAULT_STATE;
        this.numOfFloors = DEFAULT_FLOOR_NUM;
        this.floorRows = DEFAULT_FLOOR_ROWS;
        this.floorCols = DEFAULT_FLOOR_COLS;
        this.rates = new Rates(this);
    }
    
    public ParkingLot (@NotNull String name, @NotNull String street, @NotNull Integer streetNumber, @NotNull String city, @NotNull String state, @NotNull Integer floorCols) {
        this.type = TYPE;
        this.name = name;
        this.streetNumber = streetNumber;
        this.street = street;
        this.city = city;
        this.state = state;
        this.numOfFloors = DEFAULT_FLOOR_NUM;
        this.floorRows = DEFAULT_FLOOR_ROWS;
        this.floorCols = floorCols;
        this.rates = new Rates(this);
    }
    
    public ParkingLot (@NotNull String name, @NotNull String street, @NotNull Integer streetNumber, @NotNull String city, @NotNull String state, @NotNull Integer numOfFloors, @NotNull Integer floorRows, @NotNull Integer floorCols) {
        this.type = TYPE;
        this.name = name;
        this.streetNumber = streetNumber;
        this.street = street;
        this.city = city;
        this.state = state;
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
