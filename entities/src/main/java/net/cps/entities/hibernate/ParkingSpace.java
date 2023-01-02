package net.cps.entities.hibernate;

// maybe not nessecarry, can be included as a field at ParkingLot
public class ParkingSpace  {
    private Integer availibility;

    public ParkingSpace(Integer availibility) {
        this.availibility = availibility;
    }

    public Integer getAvailibility() {
        return availibility;
    }

    public void setAvailibility(Integer availibility) {
        this.availibility = availibility;
    }

}
