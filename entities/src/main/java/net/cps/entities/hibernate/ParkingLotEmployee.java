package net.cps.entities.hibernate;

import java.util.List;

public class ParkingLotEmployee extends AbstractEmployee{

    public Integer setDisabledPark(ParkingSpace parkingSpace){return 0;}

    public String moveToAlternativeParkingLot(){return "0";}

    public void setParkingLotInformation(List<Long>somthing){}

    public void saveParkingSpace(Integer id, String parkingLot){}
}
