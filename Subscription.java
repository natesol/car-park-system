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

    public Subscription () {}
    //ORDINARY SUBSCRIPTION
    public Subscription(Long id, Long customerId, Long carId, ParkingLot parkingLot, Vehicle vehicle, String subscriptionType, Calendar startDate, Customer customer, Integer departureTimeHour, Integer departureTimeMinuets) {
        this.id = id;
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
    public Subscription(Long id, Long customerId, Long carId, ParkingLot parkingLot, Vehicle vehicle, String subscriptionType, Calendar startDate, Customer customer) {
        if (subscriptionType != "full"){
            System.out.println("ERROR, you need to enter departure hour and minuets\n");
            return;
        }
        this.id = id;
        this.customerId = customerId;
        this.carId = carId;
        this.parkingLot = parkingLot;
        this.vehicle = vehicle;
        this.subscriptionType = subscriptionType;
        this.startDate = startDate;
        this.endDate = startDate;
        endDate.add(Calendar.DAY_OF_MONTH, 28); // add 28 days
        this.customer = customer;
        this.departureTimeMinuets = 0;
        this.departureTimeHour = 0;
    }
    //GETTERS AND SETTERS

    public void setId(Long id) {this.id = id;}
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

    public Reservation makeReservation(Calendar arrivalDate, Long idReservation){
        Calendar departureTime=Calendar.getInstance();
        departureTime.set(Calendar.MINUTE, departureTimeMinuets);
        departureTime.set(Calendar.HOUR_OF_DAY, departureTimeHour);
        Reservation reservation = new Reservation(idReservation, parkingLot, arrivalDate, departureTime, vehicle);
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