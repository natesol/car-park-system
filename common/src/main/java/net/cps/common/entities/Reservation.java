package net.cps.common.entities;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "reservations")
public class Reservation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "vehicle_number", referencedColumnName = "number")
    private Vehicle vehicle;
    @NotNull
    @OneToOne
    @JoinColumn(name = "parking_lot_id", referencedColumnName = "id")
    private ParkingLot parkingLot;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_email", referencedColumnName = "email")
    private Customer customer;
    @NotNull
    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;
    @NotNull
    @Column(name = "departure_time")
    private LocalDateTime departureTime;
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ParkingSpace parkingSpace;
    
    
    //use if the car already entered the parking lot
    public Reservation (@NotNull ParkingLot parkingLot, @NotNull LocalDateTime arrivalDate,
                        @NotNull LocalDateTime departureTime, @NotNull Vehicle vehicle, ParkingSpace parkingSpace) {
        this.parkingLot = parkingLot;
        this.arrivalTime = arrivalDate;
        this.departureTime = departureTime;
        this.vehicle = vehicle;
        this.parkingSpace = parkingSpace;
    }
    
    public Reservation (@NotNull ParkingLot parkingLot, @NotNull LocalDateTime arrivalDate, @NotNull LocalDateTime departureTime, @NotNull Vehicle vehicle) {
        this.parkingLot = parkingLot;
        this.arrivalTime = arrivalDate;
        this.departureTime = departureTime;
        this.vehicle = vehicle;
    }
    
    public Reservation () {
    
    }
}
