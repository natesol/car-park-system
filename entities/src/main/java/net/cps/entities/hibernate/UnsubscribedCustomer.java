package net.cps.entities.hibernate;

import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "UnsubscribedCustomer")
public class UnsubscribedCustomer extends AbstractCostumer{
    @OneToOne
    private Vehicle vehicle;

    public UnsubscribedCustomer(String email, ParkingLot parkingLot, Vehicle vehicle) {
        super(email, parkingLot);
        this.vehicle = vehicle;
    }

    public UnsubscribedCustomer(String email, ParkingLot parkingLot) {
        super(email, parkingLot);

    }

    public UnsubscribedCustomer() {

    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
