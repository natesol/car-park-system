package net.cps.entities.hibernate;

public class ParkingLotManager extends AbstractEmployee{
    private Long id;

    public ParkingLotManager(Long id) {
        this.id = id;
    }

    public ParkingLotManager(Long id, String emailAddress, String firstName, String lastName, String role, String organization, Long id1) {
        super(id, emailAddress, firstName, lastName, role, organization);
        this.id = id1;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setRates(Long price){}

    public Report createReport(String type){return new Report();}
}
