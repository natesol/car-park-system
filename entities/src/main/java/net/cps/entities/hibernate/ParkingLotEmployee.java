package net.cps.entities.hibernate;

import java.util.List;

public class ParkingLotEmployee extends AbstractEmployee{

    private Long id;

    public ParkingLotEmployee(Long id) {
        this.id = id;
    }

    public Integer setDisabledPark(ParkingSpace parkingSpace){return 0;}

    public String moveToAlternativeParkingLot(){return "0";}

    public void setParkingLotInformation(List<Long>somthing){}

    public void saveParkingSpace(Long id, Long parkingLot){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
