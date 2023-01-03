package net.cps.entities.hibernate;

import java.util.Date;

public class Reservation {
    private Long customerId;
    private Long reservationId;
    private ParkingLot parkingLot;
    private Date arrivalDate;
    private Date departureTime;

    public Reservation(Long customerId, Long reservationId, ParkingLot parkingLot) {
        this.customerId = customerId;
        this.reservationId = reservationId;
        this.parkingLot = parkingLot;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
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
