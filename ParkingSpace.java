package org.example;

import org.example.ParkingSpaceCondition;

import java.util.Calendar;

// maybe not nessecarry, can be included as a field at ParkingLot
public class ParkingSpace {
    private ParkingSpaceCondition condition;
    private ParkingLot parkingLot;
    private int row; //place in the parking lot
    private int column;
    private int floor;
    private Vehicle vehicle = null;
    private Calendar departureTime = null;
    private Calendar arrivalTime = null;

    public ParkingSpace(){}
    public ParkingSpace(int row, int column, int floor, ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
        this.row = row;
        this.column = column;
        this.floor = floor;
        this.condition = ParkingSpaceCondition.AVAILABLE;
    }

    public ParkingSpaceCondition getCondition() {
        return condition;
    }
    public void setCondition(ParkingSpaceCondition condition) {
        this.condition = condition;
    }

    public Calendar getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Calendar departureTime) {
        this.departureTime = departureTime;
    }

    public Calendar getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Calendar arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }
}
