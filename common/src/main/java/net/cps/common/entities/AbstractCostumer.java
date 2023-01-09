package net.cps.common.entities;

import java.util.Date;


public abstract class AbstractCostumer   {
    private Long vehicleNumber;

    public Integer orderParking(String a, Date b){
        return null;
    }

    public Integer cancelReservation (Reservation reservation){
        return 0;
    }

    public Integer makeReservation(String a, Date b, Date c){
        return 0;
    }

    public void followRequest(){
    }

    public String registerVehicle(Integer somthing){return "0";}

    public String makePayment(Long a, Long b){return  "0";}

}
