package net.cps.entities.hibernate;
import javax.persistence.*;

//@Entity
//@Table(name = "Robot")
public class Robot {
  //  @Id
   // @GeneratedValue(strategy = GenerationType.SEQUENCE)
    //@Column(name = "id", nullable = false)
    private Long id;
    private Long parkingLotId;
    public Robot(){}
    public Robot(Long id, Long parkingLotId) {
        this.id = id;
        this.parkingLotId = parkingLotId;
    }

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
