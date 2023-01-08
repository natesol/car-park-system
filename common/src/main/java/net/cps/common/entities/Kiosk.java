package net.cps.common.entities;
import javax.persistence.*;
/*
@Entity
@Table(name = "Kiosk")*/
public class Kiosk {
 /*   @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
   */ private Long id;
    private Long parkingLotId;

    public Long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
