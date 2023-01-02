package net.cps.entities.hibernate;

public class ParkingLotManager extends AbstractEmployee{
    private String id;

    public ParkingLotManager(String id) {
        this.id = id;
    }

    public ParkingLotManager(String id, String emailAddress, String firstName, String lastName, String role, String organization, String id1) {
        super(id, emailAddress, firstName, lastName, role, organization);
        this.id = id1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setRates(Long price){}

    public Report createReport(String type){return new Report();}
}
