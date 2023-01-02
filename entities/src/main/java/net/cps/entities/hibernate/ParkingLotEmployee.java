package net.cps.entities.hibernate;

import java.util.List;

public class ParkingLotEmployee extends AbstractEmployee{

    private String id;

    public ParkingLotEmployee(String id) {
        this.id = id;
    }

    public Integer setDisabledPark(ParkingSpace parkingSpace){return 0;}

    public String moveToAlternativeParkingLot(){return "0";}

    public void setParkingLotInformation(List<Long>somthing){}

    public void saveParkingSpace(Integer id, String parkingLot){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
