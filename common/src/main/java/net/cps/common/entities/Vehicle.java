package net.cps.common.entities;

import javax.persistence.*;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;
    @NotNull
    Long vehicleNumber;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    //@OneToMany(mappedBy = "vehicle")
    //List <Reservation> reservations = new ArrayList<>();
    
    public Vehicle(@NotNull Long vehicleNumber,Customer customer, Reservation reservation) {
        this.vehicleNumber = vehicleNumber;
        this.customer = customer;
        //this.reservations.add(reservation);
    }
    
    public Vehicle(@NotNull Long vehicleNumber, Customer customer) {
        this.vehicleNumber = vehicleNumber;
        this.customer = customer;
    }
    
    public Vehicle() {
    
    }
    
    public Long getVehicleNumber() {
        return vehicleNumber;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    //public List<Reservation> getReservations() {
    //    return reservations;
    //}
    
    public void setVehicleNumber(Long vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    //public void setReservations(List<Reservation> reservations) {
    //    this.reservations = reservations;
    //}
    //public void addReservation (Reservation reservation){
    //    reservations.add(reservation);
    //}
}