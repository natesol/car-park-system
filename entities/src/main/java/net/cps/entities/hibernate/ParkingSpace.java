package net.cps.entities.hibernate;


import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "ParkingSpace")
public class ParkingSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;
    private Integer place;
    private Boolean enabled;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "parking_lot_id")
    private ParkingLot parkingLot;
    @OneToOne
    private Vehicle vehicle;
    public ParkingSpace(@NotNull Integer place, @NotNull ParkingLot parkingLot) {
        this.place = place;
        this.enabled = true;
        this.parkingLot = parkingLot;
    }



    public ParkingSpace() {

    }

    public Integer getPlace() {
        return place;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
