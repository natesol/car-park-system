package net.cps.entities.hibernate;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "parking_lots")
public class ParkingLot implements Serializable {
    public static final int floorLength = 3; // x axes
    public static final int numOfFloors = 3; // y axes
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false, nullable = false)
    private int id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "address", nullable = false)
    private String address;
    
    @Column(name = "floor_width", nullable = false)
    private int floorWidth; // z axes
    
    @NotNull
    @OneToOne(mappedBy = "parkingLot", cascade = CascadeType.ALL)
    private Rates rates;
    
    public ParkingLot() {}
    
    public ParkingLot(Rates rates) {
        this.name = "parking-lot name";
        this.address = "parking-lot address";
        this.floorWidth = 1;
        this.rates = rates;
    }
    
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

//@Entity
//public class EntityA {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @OneToOne(mappedBy = "entityA", cascade = CascadeType.ALL)
//    @NotNull
//    private EntityB entityB;
//
//    // other fields and methods
//
//    public EntityA() {}
//
//    public EntityA(EntityB entityB) {
//        this.entityB = entityB;
//    }
//}
//
//@Entity
//public class EntityB {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @OneToOne
//    @JoinColumn(name = "id", nullable = false)
//    @NotNull
//    private EntityA entityA;
//
//    // other fields and methods
//
//    public EntityB() {}
//
//    public EntityB(EntityA entityA) {
//        this.entityA = entityA;
//    }
//}
//
//EntityA entityA = new EntityA(new EntityB(entityA));
