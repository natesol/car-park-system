package net.cps.entities.hibernate;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractCostumer {
    //we might need a reservation id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;
    String email;
    @ManyToOne
    @JoinColumn(name = "parking_lot_id", referencedColumnName="id")
    ParkingLot parkingLot;
    @OneToMany(mappedBy = "customer")
    List<Complaint> complaints;
    Double balance;

    public AbstractCostumer(String email, ParkingLot parkingLot) {
        this.email = email;
        this.parkingLot = parkingLot;
        balance = 0.0;
    }

    public AbstractCostumer() {

    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public Double getBalance() {
        return balance;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
    public void addComplaint (Complaint complaint){
        this.complaints.add(complaint);
    }
}
