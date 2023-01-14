package net.cps.common.entities;


import net.cps.common.utils.ReservationStatus;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

@Entity
@Table(name = "daily_statistics")
public class DailyStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @NotNull
    @Column(name = "created_at", nullable = false)
    private Calendar createdAt;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "parking_lot_id", referencedColumnName = "id")
    private ParkingLot parkingLot;
    @NotNull
    @Column(name = "total_reservations", nullable = false)
    private Integer totalReservations;
    @NotNull
    @Column(name = "total_fulfilled", nullable = false)
    private Integer totalFulfilled;
    @NotNull
    @Column(name = "total_cancellations", nullable = false)
    private Integer totalCancellations;
    @NotNull
    @Column(name = "total_latency", nullable = false)
    private Integer totalLatency;
    @NotNull
    @Column(name = "daily_average_latency", nullable = false)
    private Double dailyAverageLatency;
    @NotNull
    @Column(name = "daily_median_latency", nullable = false)
    private Double dailyMedianLatency;
    
    
    /* ----- Constructors ------------------------------------------- */
    
    // important for Hibernate !!!
    // !!! SELECT * FROM complaints WHERE DATE(submission_time) = '2020-01-01';
    // important for Hibernate !!!
    
    public DailyStatistics () {}
    
    public DailyStatistics (@NotNull ParkingLot parkingLot, ArrayList<Reservation> reservations) {
        this.parkingLot = parkingLot;
        this.createdAt = reservations.get(0).getArrivalTime();
        this.createdAt.set(Calendar.HOUR_OF_DAY, 0);
        this.createdAt.set(Calendar.MINUTE, 0);
        this.createdAt.set(Calendar.SECOND, 0);
        this.createdAt.set(Calendar.MILLISECOND, 0);
        
        this.totalReservations = reservations.size();
        this.totalFulfilled = calculateTotalFulfilled(reservations);
        this.totalCancellations = calculateTotalCancellations(reservations);
        this.totalLatency = calculateTotalLatency(reservations);
        this.dailyAverageLatency = calculateDailyAverageLatency(reservations, totalLatency);
        //this.dailyMedianLatency = calculateDailyMedianLatency(reservations, totalLatency);
        this.dailyMedianLatency = 0.0;
    }
    
    
    /* ----- Getters and Setters ------------------------------------ */
    
    public Integer getId () {
        return id;
    }
    
    public void setId (Integer id) {
        this.id = id;
    }
    
    public Calendar getCreatedAt () {
        return createdAt;
    }
    
    public void setCreatedAt (Calendar createdAt) {
        this.createdAt = createdAt;
    }
    
    public ParkingLot getParkingLot () {
        return parkingLot;
    }
    
    public void setParkingLot (ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }
    
    public Integer getTotalReservations () {
        return totalReservations;
    }
    
    public void setTotalReservations (Integer totalReservations) {
        this.totalReservations = totalReservations;
    }
    
    public Integer getTotalFulfilled () {
        return totalFulfilled;
    }
    
    public void setTotalFulfilled (Integer totalFulfilled) {
        this.totalFulfilled = totalFulfilled;
    }
    
    public Integer getTotalCancellations () {
        return totalCancellations;
    }
    
    public void setTotalCancellations (Integer totalCancellations) {
        this.totalCancellations = totalCancellations;
    }
    
    public Integer getTotalLatency () {
        return totalLatency;
    }
    
    public void setTotalLatency (Integer totalLatency) {
        this.totalLatency = totalLatency;
    }
    
    public Double getDailyAverageLatency () {
        return dailyAverageLatency;
    }
    
    public void setDailyAverageLatency (Double dailyAverageLatency) {
        this.dailyAverageLatency = dailyAverageLatency;
    }
    
    public Double getDailyMedianLatency () {
        return dailyMedianLatency;
    }
    
    public void setDailyMedianLatency (Double dailyMedianLatency) {
        this.dailyMedianLatency = dailyMedianLatency;
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    private Integer calculateTotalFulfilled (ArrayList<Reservation> reservations) {
        int totalFulfilled = 0;
        
        for (Reservation reservation : reservations) {
            if (reservation.getStatus() == ReservationStatus.CHECKED_OUT && reservation.getEntryTime().before(reservation.getArrivalTime())) {
                totalFulfilled++;
            }
        }
        
        return totalFulfilled;
    }
    
    private Integer calculateTotalCancellations (ArrayList<Reservation> reservations) {
        int totalCancellations = 0;
        
        for (Reservation reservation : reservations) {
            if (reservation.getStatus() == ReservationStatus.CANCELLED) {
                totalCancellations++;
            }
        }
    
        return totalCancellations;
    }
    
    private Integer calculateTotalLatency (ArrayList<Reservation> reservations) {
        int totalLateCustomers = 0;
        
        for (Reservation reservation : reservations) {
            if (reservation.getStatus() == ReservationStatus.CHECKED_OUT && reservation.getEntryTime().after(reservation.getArrivalTime())) {
                totalLateCustomers++;
            }
        }
    
        return totalLateCustomers;
    }
    
    private Double calculateDailyAverageLatency (ArrayList<Reservation> reservations, int totalLatency) {
        double dailyAverageLatency = 0;
        
        for (Reservation reservation : reservations) {
            if (reservation.getStatus() == ReservationStatus.CHECKED_OUT && reservation.getEntryTime().after(reservation.getArrivalTime())) {
                dailyAverageLatency += reservation.getEntryTime().getTimeInMillis() - reservation.getArrivalTime().getTimeInMillis();
            }
        }
        
        dailyAverageLatency /= totalLatency;
        dailyAverageLatency /= 1000;
        dailyAverageLatency /= 60;
    
        return dailyAverageLatency;
    }
    
    private Double calculateDailyMedianLatency (ArrayList<Reservation> reservations, int totalLatency) {
        ArrayList<Double> latencies = new ArrayList<>();
        
        for (Reservation reservation : reservations) {
            if (reservation.getStatus() == ReservationStatus.CHECKED_OUT && reservation.getEntryTime().after(reservation.getArrivalTime())) {
                latencies.add((double) (reservation.getEntryTime().getTimeInMillis() - reservation.getArrivalTime().getTimeInMillis()));
            }
        }
        
        Collections.sort(latencies);
        
        double dailyMedianLatency = 0;
        
        if (totalLatency % 2 == 0) {
            dailyMedianLatency = (latencies.get(totalLatency / 2) + latencies.get(totalLatency / 2 - 1)) / 2;
        }
        else {
            dailyMedianLatency = latencies.get(totalLatency / 2);
        }
        
        dailyMedianLatency /= 1000;
        dailyMedianLatency /= 60;
    
        return dailyMedianLatency;
    }
    
    
    @Override
    public String toString () {
        return "DailyStatistics {" +
                "id: " + id +
                ", createdAt: " + createdAt.getTime() +
                ", parkingLot: " + parkingLot +
                ", totalReservations: " + totalReservations +
                ", totalFulfilled: " + totalFulfilled +
                ", totalCancellations: " + totalCancellations +
                ", totalLatency: " + totalLatency +
                ", dailyAverageLatency: " + dailyAverageLatency +
                ", dailyMedianLatency: " + dailyMedianLatency +
                '}';
    }
}
