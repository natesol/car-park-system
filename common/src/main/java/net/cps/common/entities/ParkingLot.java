package net.cps.common.entities;

import net.cps.common.utils.AbstractOrganization;
import net.cps.common.utils.OrganizationType;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "parking_lots")
@PrimaryKeyJoinColumn(name = "organization_id")
public class ParkingLot extends AbstractOrganization implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;
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
    
    
    public static final OrganizationType TYPE = OrganizationType.PARKING_LOT;
    public static final String DEFAULT_NAME = "Parking Lot Name";
    public static final Integer DEFAULT_FLOOR_NUM = 3;
    public static final Integer DEFAULT_FLOOR_ROWS = 3;
    public static final Integer DEFAULT_FLOOR_COLS = 1;
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public ParkingLot () {
        super(TYPE);
        this.name = DEFAULT_NAME;
        this.numOfFloors = DEFAULT_FLOOR_NUM;
        this.floorRows = DEFAULT_FLOOR_ROWS;
        this.floorCols = DEFAULT_FLOOR_COLS;
        this.rates = new Rates(this);
    }
    
    public ParkingLot (@NotNull String name, String street, Integer streetNumber, String city, String state, @NotNull Integer floorCols) {
        super(TYPE, street, streetNumber, city, state);
        this.name = name;
        this.numOfFloors = DEFAULT_FLOOR_NUM;
        this.floorRows = DEFAULT_FLOOR_ROWS;
        this.floorCols = floorCols;
        this.rates = new Rates(this);
    }
    
    public ParkingLot (@NotNull String name, String street, Integer streetNumber, String city, String state, @NotNull Integer numOfFloors, @NotNull Integer floorRows, @NotNull Integer floorCols) {
        super(TYPE, street, streetNumber, city, state);
        this.name = name;
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
