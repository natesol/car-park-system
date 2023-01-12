package net.cps.common.entities;


import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "daily_statistics")
public class DailyStatistics {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "created_at", nullable = false)
    private Calendar createdAt;
    @ManyToOne
    @JoinColumn(name = "parking_lot_id", referencedColumnName = "id")
    private ParkingLot parkingLot;
    @Column(name = "total_reservations", nullable = false)
    private Integer totalReservations;
    @Column(name = "total_fulfilled", nullable = false)
    private Integer totalFulfilled;
    @Column(name = "total_cancellations", nullable = false)
    private Integer totalCancellations;
    @Column(name = "total_late_customers", nullable = false)
    private Integer totalLateCustomers;
    @Column(name = "daily_average", nullable = false)
    private Double dailyAverage;
    @Column(name = "daily_median", nullable = false)
    private Double dailyMedian;
    @Column(name = "daily_standard_deviation", nullable = false)
    private Double dailyStandardDeviation;
    
    public DailyStatistics () {}
    
    public DailyStatistics (Integer id, Calendar createdAt, ParkingLot parkingLot, Integer totalReservations, Integer totalFulfilled, Integer totalCancellations, Integer totalLateCustomers) {
        this.id = id;
        this.createdAt = createdAt;
        this.parkingLot = parkingLot;
        this.totalReservations = totalReservations;
        this.totalFulfilled = totalFulfilled;
        this.totalCancellations = totalCancellations;
        this.totalLateCustomers = totalLateCustomers;
    }
    
    
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
    
    public Integer getTotalLateCustomers () {
        return totalLateCustomers;
    }
    
    public void setTotalLateCustomers (Integer totalLateCustomers) {
        this.totalLateCustomers = totalLateCustomers;
    }
    
    public Double getDailyAverage () {
        return dailyAverage;
    }
    
    public void setDailyAverage (Double dailyAverage) {
        this.dailyAverage = dailyAverage;
    }
    
    public Double getDailyMedian () {
        return dailyMedian;
    }
    
    public void setDailyMedian (Double dailyMedian) {
        this.dailyMedian = dailyMedian;
    }
    
    public Double getDailyStandardDeviation () {
        return dailyStandardDeviation;
    }
    
    public void setDailyStandardDeviation (Double dailyStandardDeviation) {
        this.dailyStandardDeviation = dailyStandardDeviation;
    }
}
