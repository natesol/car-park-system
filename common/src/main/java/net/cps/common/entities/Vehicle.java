package net.cps.common.entities;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @NotNull
    @Column(name = "number", columnDefinition = "")
    private String number;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;
    @OneToMany(mappedBy = "vehicle")
    private List<Reservation> reservations;
    
    public Vehicle (@NotNull Long vehicleNumber, Customer customer, Reservation reservation) {
        this.number = String.valueOf(vehicleNumber);
        this.customer = customer;
        //this.reservations.add(reservation);
    }
    
    public Vehicle (@NotNull Long vehicleNumber, Customer customer) {
        this.number = String.valueOf(vehicleNumber);
        this.customer = customer;
    }
    
    public Vehicle () {
    
    }
    
    //// In Customer class:
    //@OneToMany(cascade=ALL, mappedBy="customer")
    //public Set<Order> getOrders() { return orders; }
    //// In Order class:
    //@ManyToOne
    //@JoinColumn(name="CUST_ID", nullable=false)
    //public Customer getCustomer() { return customer; }
    
    //public Long getNumber () {
    //    return number;
    //}
    //
    //public Customer getCustomer () {
    //    return customer;
    //}
    //
    ////public List<Reservation> getReservations() {
    ////    return reservations;
    ////}
    //
    //public void setNumber (Long number) {
    //    this.number = number;
    //}
    //
    //public void setCustomer (Customer customer) {
    //    this.customer = customer;
    //}
    
    //public void setReservations(List<Reservation> reservations) {
    //    this.reservations = reservations;
    //}
    //public void addReservation (Reservation reservation){
    //    reservations.add(reservation);
    //}
}