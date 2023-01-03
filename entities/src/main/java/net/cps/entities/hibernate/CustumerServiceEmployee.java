package net.cps.entities.hibernate;

import java.util.List;

public class CustumerServiceEmployee extends AbstractEmployee{
    private List<Complaint>complaints;

    public void saveParkingSpace(Long id, String parkingLot){}

    public void handleComplaint(Complaint complaint){}

    public void compansiteCustumer(Customer customer, Long num){}

    public String respondeToCustumer(Customer customer, Complaint complaint){return "0";}

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints;
    }
}
