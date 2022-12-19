package net.cps.entities.hibernate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "parking_lots")
public class ParkingLot implements Serializable {
    public static final int floorLength = 3; // x axes
    public static final int numOfFloors = 3; // y axes
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private int id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "address", nullable = false)
    private String address;
    
    @Column(name = "floor_width", nullable = false)
    private int floorWidth; // z axes
    
    @OneToOne(mappedBy = "parkingLot", cascade = CascadeType.ALL)
    private Rates rates;
    
    public ParkingLot() {}
    
    public ParkingLot(String name, String address, int floorWidth) {
        this.name = name;
        this.address = address;
        this.floorWidth = floorWidth;
        this.rates = new Rates(this);
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getAddress() {
        return this.address;
    }
    
    public int getFloorWidth() {
        return this.floorWidth;
    }
    
    public Rates getRates() {
        return this.rates;
    }
    
    public int getTotalSpace() {
        return this.floorWidth * floorLength * numOfFloors;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public void setFloorWidth(int floorWidth) {
        this.floorWidth = floorWidth;
    }
    
    public void setRates(Rates rates) {
        this.rates = rates;
    }
}

