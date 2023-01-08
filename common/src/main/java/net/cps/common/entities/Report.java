package net.cps.common.entities;

public class Report {
    private Long id;
    private Long parkingLotId;
    private String title;
    private String period;
    private String type;
    private String status;

    public Report(){}
    public Report(Long id, Long parkingLotId, String title, String period, String type, String status) {
        this.id = id;
        this.parkingLotId = parkingLotId;
        this.title = title;
        this.period = period;
        this.type = type;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Long parkingLotId) {
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
