package net.cps.common.entities;

import java.util.Date;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Table(name = "reservation")
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reservationId", updatable = false, nullable = false)
    private Long reservationId;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "parking_lot_id",referencedColumnName = "id")
    private ParkingLot parkingLot;
    @NotNull
    private LocalDateTime arrivalDate;
    @NotNull
    private LocalDateTime departureTime;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "vehicle_number", referencedColumnName = "id")
    private Vehicle vehicle;
    @OneToOne
    private ParkingSpace parkingSpace;
    
    //use if the car already entered the parking lot
    public Reservation(@NotNull ParkingLot parkingLot, @NotNull LocalDateTime arrivalDate,
                       @NotNull LocalDateTime departureTime, @NotNull Vehicle vehicle, ParkingSpace parkingSpace) {
        this.parkingLot = parkingLot;
        this.arrivalDate = arrivalDate;
        this.departureTime = departureTime;
        this.vehicle = vehicle;
        this.parkingSpace = parkingSpace;
    }
    public Reservation(@NotNull ParkingLot parkingLot, @NotNull LocalDateTime arrivalDate, @NotNull LocalDateTime departureTime, @NotNull Vehicle vehicle) {
        this.parkingLot = parkingLot;
        this.arrivalDate = arrivalDate;
        this.departureTime = departureTime;
        this.vehicle = vehicle;
    }
    
    public Reservation() {
    
    }
}
