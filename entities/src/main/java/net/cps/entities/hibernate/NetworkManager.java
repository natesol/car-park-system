package net.cps.entities.hibernate;

public class NetworkManager extends AbstractEmployee{
    private Long id;

    public NetworkManager(Long id) {
        this.id = id;
    }

    public Report requestCurrentCapacity(){return new Report();}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
