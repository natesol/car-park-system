package net.cps.entities.hibernate;

import java.util.Date;

public class Reservation {
    private String customerId;
    private String reservationId;
    private ParkingLot parkingLot;
    private Date arrivalDate;
    private Date departureTime;

    public Reservation(String customerId, String reservationId, ParkingLot parkingLot) {
        this.customerId = customerId;
        this.reservationId = reservationId;
        this.parkingLot = parkingLot;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }


}
