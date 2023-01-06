package net.cps.entities.hibernate;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subscribedCustomer")
public class SubscribedCustomer extends AbstractCostumer{
    @NotNull
    LocalDate subscriptionStartDate;
    LocalDate standardDepartureTime;
    @OneToMany
    List<Vehicle> vehicles = new ArrayList<>();
    String type;

    public SubscribedCustomer(String email, ParkingLot parkingLot, @NotNull LocalDate subscriptionStartDate, LocalDate standardDepartureTime, String type) {
        super(email, parkingLot);
        this.subscriptionStartDate = subscriptionStartDate;
        this.standardDepartureTime = standardDepartureTime;
        this.type = type;
    }

    public SubscribedCustomer() {

    }

    public LocalDate getSubscriptionStartDate() {
        return subscriptionStartDate;
    }

    public void setSubscriptionStartDate(LocalDate subscriptionStartDate) {
        this.subscriptionStartDate = subscriptionStartDate;
    }

    public LocalDate getStandardDepartureTime() {
        return standardDepartureTime;
    }

    public void setStandardDepartureTime(LocalDate standardDepartureTime) {
        this.standardDepartureTime = standardDepartureTime;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public void addVehicle (Vehicle vehicle){
        vehicles.add(vehicle);
    }
}
