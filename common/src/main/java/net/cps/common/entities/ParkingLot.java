package net.cps.common.entities;

import net.cps.common.utils.AbstractOrganization;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "parking_lots")
public class ParkingLot extends AbstractOrganization implements Serializable {
    public static final String DEFAULT_NAME = "Parking Lot";
    public static final String DEFAULT_ADDRESS = "Address";
    public static final int DEFAULT_FLOOR_COLS = 1;
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "address", nullable = false)
    private String address;
    
    @Column(name = "floor_rows", nullable = false)
    private static final Integer floorRows = 3;
    
    @Column(name = "num_of_floors", nullable = false)
    private static final Integer numOfFloors = 3;
    
    @Column(name = "floor_cols", nullable = false)
    private Integer floorCols;
    
    //@NotNull
    //@OneToOne(cascade = CascadeType.ALL)
    //private Kiosk kiosk;
    
    @NotNull
    @OneToOne(mappedBy = "parkingLot", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Rates rates;
    
    //@NotNull
    //@OneToOne
    //@JoinColumn(name = "robot_id", referencedColumnName = "id")
    //private Robot robot;
    //
    //@OneToMany(fetch = FetchType.LAZY, mappedBy = "parking_lot", cascade = {CascadeType.ALL})
    //private List<Employee> employees;
    
    //@OneToMany(mappedBy="parkingLot")
    //List <Reservation> reservations;
    
    public ParkingLot () {
        this.name = DEFAULT_NAME;
        this.address = DEFAULT_ADDRESS;
        this.floorCols = DEFAULT_FLOOR_COLS;
        this.rates = new Rates(this);
    }
    
    public ParkingLot (String name, String address, int floorCols) {
        this.name = name;
        this.address = address;
        this.floorCols = floorCols;
        this.rates = new Rates(this);
    }
    
    public int getId () {
        return this.id;
    }
    
    public String getName () {
        return this.name;
    }
    
    public String getAddress () {
        return this.address;
    }
    
    public int getFloorCols () {
        return this.floorCols;
    }
    
    public @NotNull Rates getRates () {
        return this.rates;
    }
    
    public int getTotalSpace () {
        return this.floorCols * floorRows * numOfFloors;
    }
    
    public void setName (String name) {
        this.name = name;
    }
    
    public void setAddress (String address) {
        this.address = address;
    }
    
    public void setFloorWidth (int floorWidth) {
        this.floorCols = floorWidth;
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
    
    @Override
    public String toString () {
        return "ParkingLot {" +
                "id: " + id +
                ", name: '" + name + "'" +
                ", address: '" + address + "'" +
                ", floorCols: " + floorCols +
                ", rates: " + rates +
                "}";
    }
}
