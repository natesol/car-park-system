package net.cps.entities.hibernate;

public class Report {
    private String id;
    private String parkingLotId;
    private String title;
    private String period;
    private String type;
    private String status;

    public Report(){}
    public Report(String id, String parkingLotId, String title, String period, String type, String status) {
        this.id = id;
        this.parkingLotId = parkingLotId;
        this.title = title;
        this.period = period;
        this.type = type;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(String parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
