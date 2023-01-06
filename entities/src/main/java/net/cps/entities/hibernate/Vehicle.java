package net.cps.entities.hibernate;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;
    @NotNull
    Long vehicleNumber;
    @NotNull
    @ManyToOne
    AbstractCostumer customer;
    @OneToMany(mappedBy = "vehicle")
    List <Reservation> reservations = new ArrayList<>();

    public Vehicle(@NotNull Long vehicleNumber, @NotNull AbstractCostumer customer, Reservation reservation) {
        this.vehicleNumber = vehicleNumber;
        this.customer = customer;
        this.reservations.add(reservation);
    }

    public Vehicle(@NotNull Long vehicleNumber, @NotNull AbstractCostumer customer) {
        this.vehicleNumber = vehicleNumber;
        this.customer = customer;
    }

    public Vehicle() {

    }

    public Long getVehicleNumber() {
        return vehicleNumber;
    }

    public AbstractCostumer getCustomer() {
        return customer;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setVehicleNumber(Long vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public void setCustomer(AbstractCostumer customer) {
        this.customer = customer;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
    public void addReservation (Reservation reservation){
        reservations.add(reservation);
    }
}
