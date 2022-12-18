package net.cps.entities.hibernate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "parking_lots")
public class ParkingLot implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "rates")
    private String rates;
    
    public void setName(String s) {
        this.name = s;
    }
    
    public void setAddress(String s) {
        this.address = s;
    }
    
    // Getters and setters for the fields go here...
}
