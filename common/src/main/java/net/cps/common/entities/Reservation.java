package net.cps.common.entities;

import net.cps.common.utils.ReservationStatus;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;


@Entity
@Table(name = "reservations")
public class Reservation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parking_lot_id", referencedColumnName = "id")
    private ParkingLot parkingLot;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_email", referencedColumnName = "email")
    private Customer customer;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "vehicle_number", referencedColumnName = "number")
    private Vehicle vehicle;
    @NotNull
    @Column(name = "arrival_time")
    private Calendar arrivalTime;
    @NotNull
    @Column(name = "departure_time")
    private Calendar departureTime;
    @Column(name = "entry_time")
    private Calendar entryTime;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus status;
    @NotNull
    @Column(name = "payed")
    private Double payed;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parking_space_id", unique = true)
    private ParkingSpace parkingSpace;
    
    public static final double CANCELLATION_FEE_MORE_THAN_3_HOURS = 0.9;
    public static final double CANCELLATION_FEE_LESS_THAN_3_HOURS = 0.5;
    public static final double CANCELLATION_FEE_LESS_THAN_1_HOURS = 0.1;
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public Reservation () {}
    
    public Reservation (@NotNull ParkingLot parkingLot, @NotNull Customer customer, @NotNull Vehicle vehicle, @NotNull Calendar arrivalTime, @NotNull Calendar departureTime) {
        this.parkingLot = parkingLot;
        this.customer = customer;
        this.vehicle = vehicle;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.status = ReservationStatus.PENDING;
        this.payed = calculatePrice();
    }
    
    public Reservation (@NotNull ParkingLot parkingLot, @NotNull Customer customer, @NotNull Vehicle vehicle, @NotNull Calendar arrivalTime, @NotNull Calendar departureTime, @NotNull ReservationStatus status) {
        this.parkingLot = parkingLot;
        this.customer = customer;
        this.vehicle = vehicle;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.status = status;
        this.payed = calculatePrice();
    }
    
    public Reservation (@NotNull ParkingLot parkingLot, @NotNull Customer customer, @NotNull Vehicle vehicle, @NotNull Calendar arrivalTime, @NotNull Calendar departureTime, @NotNull ReservationStatus status, @NotNull Double payed) {
        this.parkingLot = parkingLot;
        this.customer = customer;
        this.vehicle = vehicle;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.status = status;
        this.payed = payed;
    }
    
    
    /* ----- Getters & Setters -------------------------------------- */
    
    public Integer getId () {
        return id;
    }
    
    public void setId (Integer id) {
        this.id = id;
    }
    
    public @NotNull ParkingLot getParkingLot () {
        return parkingLot;
    }
    
    public void setParkingLot (@NotNull ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }
    
    public @NotNull String getParkingLotName () {
        return parkingLot.getName();
    }
    
    public @NotNull Customer getCustomer () {
        return customer;
    }
    
    public void setCustomer (@NotNull Customer customer) {
        this.customer = customer;
    }
    
    public @NotNull Vehicle getVehicle () {
        return vehicle;
    }
    
    public void setVehicle (@NotNull Vehicle vehicle) {
        this.vehicle = vehicle;
    }
    
    public @NotNull String getVehicleNumber () {
        return vehicle.getNumber();
    }
    
    public @NotNull Calendar getArrivalTime () {
        return arrivalTime;
    }
    
    public void setArrivalTime (@NotNull Calendar arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    
    public @NotNull String getArrivalTimeFormatted () {
        return String.format("%02d/%02d/%02d %02d:%02d", arrivalTime.get(Calendar.DAY_OF_MONTH), arrivalTime.get(Calendar.MONTH), arrivalTime.get(Calendar.YEAR), arrivalTime.get(Calendar.HOUR_OF_DAY), arrivalTime.get(Calendar.MINUTE));
    }
    
    public @NotNull Calendar getDepartureTime () {
        return departureTime;
    }
    
    public void setDepartureTime (@NotNull Calendar departureTime) {
        this.departureTime = departureTime;
    }
    
    public @NotNull String getDepartureTimeFormatted () {
        return String.format("%02d/%02d/%02d %02d:%02d", departureTime.get(Calendar.DAY_OF_MONTH), departureTime.get(Calendar.MONTH), departureTime.get(Calendar.YEAR), departureTime.get(Calendar.HOUR_OF_DAY), departureTime.get(Calendar.MINUTE));
    }
    
    public Calendar getEntryTime () {
        return entryTime;
    }
    
    public void setEntryTime (Calendar entryTime) {
        this.entryTime = entryTime;
    }
    
    public @NotNull ReservationStatus getStatus () {
        return status;
    }
    
    public void setStatus (@NotNull ReservationStatus status) {
        this.status = status;
    }
    
    public @NotNull Double getPayed () {
        return payed;
    }
    
    public void setPayed (@NotNull Double payed) {
        this.payed = payed;
    }
    
    public ParkingSpace getParkingSpace () {
        return parkingSpace;
    }
    
    public void setParkingSpace (ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    public boolean isPayed () {
        return payed > 0.0;
    }
    
    public boolean isCancelled () {
        return status == ReservationStatus.CANCELLED;
    }
    
    public boolean isPending () {
        return status == ReservationStatus.PENDING;
    }
    
    public boolean isEnded () {
        return status == ReservationStatus.CHECKED_OUT;
    }
    
    public boolean isOngoing () {
        return status == ReservationStatus.CHECKED_IN;
    }
    
    public Double calculatePrice () {
        return parkingLot.calculateReservationPrice(arrivalTime, departureTime);
    }
    
    public Double calculateCancellationFee () {
        Calendar now = Calendar.getInstance();
        long diff = departureTime.getTimeInMillis() - now.getTimeInMillis();
        long diffHours = diff / (60 * 60 * 1000);
        if (diffHours > 3) {
            return payed * CANCELLATION_FEE_MORE_THAN_3_HOURS;
        } else if (diffHours > 1) {
            return payed * CANCELLATION_FEE_LESS_THAN_3_HOURS;
        } else {
            return payed * CANCELLATION_FEE_LESS_THAN_1_HOURS;
        }
    }
    
    @Override
    public String toString () {
        return "Reservation {" +
                "id: " + id +
                ", parkingLot: " + parkingLot +
                ", customer: " + customer +
                ", vehicle: " + vehicle +
                ", arrivalTime: " + arrivalTime.getTime() +
                ", departureTime: " + departureTime.getTime() +
                ", entryTime: " + entryTime.getTime() +
                ", status: " + status +
                ", payed: " + payed +
                ", parkingSpace: " + parkingSpace +
                '}';
    }
    
}
