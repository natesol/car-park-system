package net.cps.entities.hibernate;
import javax.persistence.*;
//optional
@Entity
@Table(name = "robot")
public class Robot {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;
    @OneToOne(mappedBy = "robot")
    private ParkingLot parkingLot;

    public Robot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public Robot() {

    }

    public Long getId() {
        return id;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }
}
