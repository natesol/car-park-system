package net.cps.entities.hibernate;

public class NetworkManager extends AbstractEmployee{
    private String id;

    public NetworkManager(String id) {
        this.id = id;
    }

    public Report requestCurrentCapacity(){return new Report();}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
