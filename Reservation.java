package org.example;
import java.util.Calendar;

public class Reservation {

    private Long id; //reservationId
    private ParkingLot parkingLot;
    private Calendar arrivalTime;
    private Calendar departureTime;
    private Vehicle vehicle;
    private boolean statusReservation; //true for active reservation, false for cancel reservation

    public Reservation(Long id, ParkingLot parkingLot, Calendar arrivalTime, Calendar departureTime, Vehicle vehicle) {
        this.id = id;
        this.parkingLot = parkingLot;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.vehicle = vehicle;
    }
    public Reservation() {}

    public ParkingLot getParkingLot() {return parkingLot;}
    public void setParkingLot(ParkingLot parkingLot) {this.parkingLot = parkingLot;}

    public Calendar getArrivalTime() {return arrivalTime;}
    public void setArrivalTime(Calendar arrivalTime) {this.arrivalTime = arrivalTime;}

    public Calendar getDepartureTime() {return departureTime;}
    public void setDepartureTime(Calendar departureTime) {this.departureTime = departureTime;}

    public Vehicle getVehicle() {return vehicle;}
    public void setVehicle(Vehicle vehicle) {this.vehicle = vehicle;}

    public boolean isStatusReservation() {
        return statusReservation;
    }

    public void cancelReservation (){
        this.statusReservation = false;
    }

    //public Reservation makeReservation(); //USE CONSTRUCTOR

    //public Reservation updateReservation(); //USE SETTERS

    //reservation overdue? do it in rates...
}