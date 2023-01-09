package net.cps.common.entities;
import javax.persistence.*;


@Entity
@Table(name = "Kiosk")
public class Kiosk {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @Column(name = "parking_lot", nullable = false)
    private ParkingLot parkingLot;
    
    
    
    public Kiosk() {
        // default constructor
    }
    
    public Kiosk(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }
}
