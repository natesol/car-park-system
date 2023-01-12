package org.example;

import java.util.Calendar;

public class Subscription {
    private Long id;
    private Long customerId;
    private Long carId;
    private ParkingLot parkingLot; //if type==full, parkingLot=null;
    private Vehicle vehicle;
    private String subscriptionType; //full or ordinary
    private Calendar startDate;
    private Calendar endDate; //startDate+28 days
    private Customer customer;
    private Integer departureTimeHour; //please send default 0
    private Integer departureTimeMinuets; //please send default 0
    private boolean sendReminder = false;

    public Subscription () {}
    //ORDINARY SUBSCRIPTION
    public Subscription(Long customerId, Long carId, ParkingLot parkingLot, Vehicle vehicle, String subscriptionType, Calendar startDate, Customer customer, Integer departureTimeHour, Integer departureTimeMinuets) {
        this.customerId = customerId;
        this.carId = carId;
        this.parkingLot = parkingLot;
        this.vehicle = vehicle;
        this.subscriptionType = subscriptionType;
        this.startDate = startDate;
        this.endDate = startDate;
        endDate.add(Calendar.DAY_OF_MONTH, 28); // add 28 days
        this.customer = customer;
        this.departureTimeHour = departureTimeHour;
        this.departureTimeMinuets = departureTimeMinuets;
    }
    //FULL SUBSCRIPTION
    public Subscription(Long customerId, Long carId, Vehicle vehicle, String subscriptionType, Calendar startDate, Customer customer) {
        if (subscriptionType != "full"){
            System.out.println("ERROR, you need to enter specific parking lot\n");
        }
        this.customerId = customerId;
        this.carId = carId;
        this.vehicle = vehicle;
        this.subscriptionType = subscriptionType;
        this.startDate = startDate;
        this.endDate = startDate;
        endDate.add(Calendar.DAY_OF_MONTH, 28); // add 28 days
        this.customer = customer;
        this.departureTimeHour = 0;
        this.departureTimeMinuets = 0;
    }

    //GETTERS AND SETTERS

    public Long getId() {return id;}

    public Calendar getEndDate() {return endDate;}
    public void setEndDate(Calendar endDate) {this.endDate = endDate;}

    public Long getCustomerId() {return customerId;}
    public void setCustomerId(Long customerId) {this.customerId = customerId;}

    public Long getCarId() {return carId;}
    public void setCarId(Long carId) {}

    public ParkingLot getParkingLot() {return parkingLot;}
    public void setParkingLot(ParkingLot parkingLot) {this.parkingLot = parkingLot;}

    public Vehicle getVehicle() {return vehicle;}
    public void setVehicle(Vehicle vehicle) {this.vehicle = vehicle;}

    public String getSubscriptionType() {return subscriptionType;}
    public void setSubscriptionType(String subscriptionType) {this.subscriptionType = subscriptionType;}

    public Calendar getStartDate() {return startDate;}
    public void setStartDate(Calendar startDate) {this.startDate = startDate;}

    public Customer getCustomer() {return customer;}
    public void setCustomer(Customer customer) {this.customer = customer;}

    public int getDepartureTimeHour() {return departureTimeHour;}
    public void setDepartureTimeHour(int departureTimeHour) {this.departureTimeHour = departureTimeHour;}

    public int getDepartureTimeMinuets() {return departureTimeMinuets;}
    public void setDepartureTimeMinuets(int departureTimeMinuets) {this.departureTimeMinuets = departureTimeMinuets;}

    public boolean getSendReminder() {return sendReminder;}
    public void setSendReminder(boolean sendReminder) {this.sendReminder = sendReminder;}

    public Reservation makeReservation(Calendar arrivalDate, ParkingLot parkingLot){
        Calendar departureTime=Calendar.getInstance();
        departureTime.set(Calendar.MINUTE, departureTimeMinuets);
        departureTime.set(Calendar.HOUR_OF_DAY, departureTimeHour);
        if(departureTime.before(arrivalDate)){ //hour time after 0:00
            departureTime.add(Calendar.DAY_OF_MONTH,1);//add 1 day
        }
        Reservation reservation = new Reservation(parkingLot, arrivalDate, departureTime, vehicle, true);
        return reservation;
    }

    public Reservation updateReservation(Reservation reservation, Calendar arrivalDate, Calendar departureTime){
        reservation.setDepartureTime(departureTime);
        reservation.setArrivalTime(arrivalDate);
        return reservation;
    }

    public void cancelReservation(Reservation reservation){
        reservation.cancelReservation();
        return;
    }


}