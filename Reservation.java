package org.example;
import java.util.Calendar;

public class Reservation {

    private Long id;
    private ParkingLot parkingLot;
    private Calendar arrivalTime;
    private Calendar departureTime;
    private Vehicle vehicle;
    private boolean statusReservation = true; //true for active reservation, false for cancel reservation
    private boolean EnterParkingLot = false;
    private boolean reportSend = false; //make true if you send overdue report, to avoid duplicate mails

    public Reservation(ParkingLot parkingLot, Calendar arrivalTime, Calendar departureTime, Vehicle vehicle) {
        this.parkingLot = parkingLot;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.vehicle = vehicle;
    }
    public Reservation() {}

    public Long getId() {return id;}

    public ParkingLot getParkingLot() {return parkingLot;}
    public void setParkingLot(ParkingLot parkingLot) {this.parkingLot = parkingLot;}

    public Calendar getArrivalTime() {return arrivalTime;}
    public void setArrivalTime(Calendar arrivalTime) {this.arrivalTime = arrivalTime;}

    public Calendar getDepartureTime() {return departureTime;}
    public void setDepartureTime(Calendar departureTime) {this.departureTime = departureTime;}

    public Vehicle getVehicle() {return vehicle;}
    public void setVehicle(Vehicle vehicle) {this.vehicle = vehicle;}

    public boolean getStatusReservation() {return statusReservation;}
    public void setStatusReservation(boolean statusReservation) {this.statusReservation = statusReservation;}

    public void cancelReservation (){
        this.statusReservation = false;
    }

    public boolean getEnterParkingLot() {return EnterParkingLot;}
    public void setEnterParkingLot(boolean enterParkingLot) {EnterParkingLot = enterParkingLot;}

    public boolean getReportSend() {return reportSend;}
    public void setReportSend(boolean reportSend) {this.reportSend = reportSend;}

    //public Reservation makeReservation(); //USE CONSTRUCTOR

    //public Reservation updateReservation(); //USE SETTERS

    //reservation overdue? do it in rates...
}