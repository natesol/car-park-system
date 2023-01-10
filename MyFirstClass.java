package org.example;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
public class MyFirstClass {
    public static void main(String[] args) {

        System.out.println("Hello World!");
        Vehicle[]vehicles=new Vehicle[44];
        Reservation[]reservations=new Reservation[44];
        ArrayList<ParkingLot> parkingLots = new ArrayList<>();
      //  ArrayList<Robot> robots = new ArrayList<>();
        Robot[]robots=new Robot[5];

        parkingLots.add(new ParkingLot("parking #1", "haifa 123", 5));
        parkingLots.add(new ParkingLot("parking #2", "haifa 61/4", 1));
        parkingLots.add(new ParkingLot("parking #3", "tel-aviv 99", 2));
        parkingLots.add(new ParkingLot("parking #4", "eilat 7", 8));
        parkingLots.add(new ParkingLot("Savta Ugia", "Harish", 4));

        parkingLots.get(0).setRates(6.0, 5.0, 60.0, null, 75.0);
        parkingLots.get(1).setRates(5.5, 3.5, 60.0, 54.0, 72.0);
        parkingLots.get(2).setRates(12.0, 10.0, null, 54.0, 72.0);
        parkingLots.get(3).setRates(null, 7.0, 60.0, 54.0, 82.0);
        parkingLots.get(4).setRates(12.0, 10.0, null, 54.0, 72.0);


        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        robots[0] = new Robot(parkingLots.get(0));
        robots[1] = new Robot(parkingLots.get(1));
        robots[2] = new Robot(parkingLots.get(2));
        robots[3] = new Robot(parkingLots.get(3));
        robots[4] = new Robot(parkingLots.get(4));

        for (int i=0;i<44;i++){
            long j = i;
            vehicles[i] = new Vehicle();
            reservations[i] = new Reservation();
            vehicles[i].setId(j);
            reservations[i].setArrivalDate(Calendar.getInstance());
            reservations[i].setReservationId(j);
            reservations[i].setCustomerId(vehicles[i].getId());
            reservations[i].setParkingLot(parkingLots.get(0));

            // print the result
            Calendar calendar = Calendar.getInstance();
            if(i==0) calendar.set(2021,10,18,15,55);
            if(i==1) calendar.set(2020,10,18,15,55);
            if(i==2) calendar.set(2022,10,18,15,55);
            if(i==3) calendar.set(2021,10,18,15,5);
            if(i==4) calendar.set(2022,10,10,18,55);
            if(i==5) calendar.set(2023,10,18,16,55);
            if(i==6) calendar.set(2023,10,18,12,55);
            if(i==7) calendar.set(2023,10,18,13,55);
            if(i==8) calendar.set(2023,10,18,14,55);
            if(i==9) calendar.set(1997,5,18,15,55);
            if(i==10) calendar.set(1991,1,28,15,55);
            if(i==11) calendar.set(2020,11,18,15,55);
            if(i>11) calendar.set(1000,1,1);
            reservations[i].setDepartureTime(calendar);
         //   System.out.println("INSERT "+i+ ", date: "+reservations[i].getDepartureTime().getTime());
            robots[0].insertVehicle(vehicles[i],reservations[i]);
           // System.out.println("SMART ARRAY AFTER INSERT car "+ vehicles[i].getId() + "\n in parkingLot "+reservations[i].getParkingLot()+"\n departure time: "+reservations[i].getDepartureTime().getTime());
            // System.out.println(robots[i%5].getParkingSpaceSmartArray());
          //  System.out.println("AVAILABLE:\n");
            //robot.printAvailableParkingSpaces();
            //  System.out.println("OCCUPIED:");
        }
        robots[0].printParkingLotMap();
        System.out.println("======END OF INSERT\n======");
        //CASE UNABLE PLACE
        ParkingSpaceSmartArray parkingSpaceSmartArray = robots[0].getParkingSpaceSmartArray();
        ParkingSpace parkingSpace = parkingSpaceSmartArray.getParkingSpace(0,0,0);

        //Reservation reservation = vehicle.getReservationsList().stream().min(<reservation.getArrivalDate()>);
        parkingSpaceSmartArray.setConditionToPlace(0,0,0,ParkingSpaceCondition.RESERVED,reservations[12]);
        robots[0].printParkingLotMap();

        for (int i=43;i>=0;i--) {
            robots[0].removeVehicle(vehicles[i]);
            robots[0].printParkingLotMap();
            System.out.println("=END OF REMOVE "+i+" date: "+reservations[i].getDepartureTime().getTime()+"\n=");

        }
    }

}
