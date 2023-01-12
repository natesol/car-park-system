package org.example;


import java.io.Serializable;
import java.util.List;

public class Vehicle implements Serializable {

    private Long id;
    private List<Reservation> reservationsList;
    private Customer customer;

    public List<Reservation> getReservationsList() {
        return reservationsList;
    }

    public Vehicle(){}
    public Vehicle(Long id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId () {return id;}

    public void setReservationsList(List<Reservation> reservationsList) {
        this.reservationsList = reservationsList;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
