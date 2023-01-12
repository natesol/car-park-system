package org.example;


import java.io.Serializable;
import java.util.List;

public class ParkingLot  implements Serializable {
    public static final String DEFAULT_NAME = "Parking Lot";
    public static final String DEFAULT_ADDRESS = "Address";
    public static final int DEFAULT_FLOOR_WIDTH = 1;
    
    public static final int floorLength = 3; // x axes
    public static final int numOfFloors = 3; // y axes
    private List<Reservation> reservationsList;
    private List<Subscription> subscriptionList;
    private Robot robot;
    private Integer id;
    private String name;
    private String address;
    private int floorWidth; // z axes
    
    //private Rates rates;
    
    public ParkingLot() {
        this.name = DEFAULT_NAME;
        this.address = DEFAULT_ADDRESS;
        this.floorWidth = DEFAULT_FLOOR_WIDTH;
     //   this.rates = new Rates(this);
    }
    
    public ParkingLot(String name, String address, int floorWidth) {
        this.name = name;
        this.address = address;
        this.floorWidth = floorWidth;
       // this.rates = new Rates(this);
    }
    
    public int getId () {
        return this.id;
    }
    
    public String getName () {
        return this.name;
    }
    
    public String getAddress () {
        return this.address;
    }
    
    public int getFloorWidth () {
        return this.floorWidth;
    }
    
  /*  public @NotNull Rates getRates () {
        return this.rates;
    }
    */
    public int getTotalSpace () {
        return this.floorWidth * floorLength * numOfFloors;
    }
    
    public void setName (String name) {
        this.name = name;
    }

    public List<Reservation> getReservationsList() {
        return reservationsList;
    }

    public void setReservationsList(List<Reservation> reservationsList) {
        this.reservationsList = reservationsList;
    }

    public void setAddress (String address) {
        this.address = address;
    }
    
    public void setFloorWidth (int floorWidth) {
        this.floorWidth = floorWidth;
    }

    public List<Subscription> getSubscriptionList() {
        return subscriptionList;
    }

    public void setSubscriptionList(List<Subscription> subscriptionList) {
        this.subscriptionList = subscriptionList;
    }

    public void setRates (Double hourlyOccasionalParking, Double hourlyOnetimeParking, Double regularSubscriptionSingleVehicle, Double regularSubscriptionMultipleVehicles, Double fullSubscriptionSingleVehicle) {
        if (hourlyOccasionalParking != null) {
          //  this.rates.setHourlyOccasionalParking(hourlyOccasionalParking);
        }
        if (hourlyOnetimeParking != null) {
         //   this.rates.setHourlyOnetimeParking(hourlyOnetimeParking);
        }
        if (regularSubscriptionSingleVehicle != null) {
            //this.rates.setRegularSubscriptionSingleVehicle(regularSubscriptionSingleVehicle);
        }
        if (regularSubscriptionMultipleVehicles != null) {
            //this.rates.setRegularSubscriptionMultipleVehicles(regularSubscriptionMultipleVehicles);
        }
        if (fullSubscriptionSingleVehicle != null) {
          //  this.rates.setFullSubscriptionSingleVehicle(fullSubscriptionSingleVehicle);
        }
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    @Override
    public String toString () {
        return "ParkingLot {" +
                "id: " + id +
                ", name: '" + name + "'" +
                ", address: '" + address + "'" +
                ", floorWidth: " + floorWidth +
              //  ", rates: " + rates +
                "}";
    }


}
