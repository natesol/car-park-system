package net.cps.entities.hibernate;


import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parking_lot")
public class ParkingLot {
    public static final int floorLength = 3; // x axis
    public static final int numOfFloors = 3; // z axis
    public static final int floorDepth = 4; // z axis
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @NotNull
    private String parkingLotName;

    @NotNull
    private String location;

    @NotNull
    private Integer rowSize;

    @NotNull
    private Integer colSize;

    @NotNull
    private Integer floorSize;

    //@OneToOne(cascade = CascadeType.ALL)
    // private Kiosk kiosk;

    @NotNull
    @OneToOne
    @JoinColumn(name = "robot_id", referencedColumnName = "id")
    private Robot robot;

    @OneToOne(mappedBy = "parkingLot")
    private Rates rates;
    @OneToMany(mappedBy = "parkingLot")
    List<ParkingSpace> parkingSpace;
    @OneToMany
    List<Employee> employees = new ArrayList<>();
    @OneToMany(mappedBy="parkingLot")
    List<AbstractCostumer> customers = new ArrayList<>();
    @OneToMany(mappedBy="parkingLot")
    List <Reservation> reservations = new ArrayList<>();

    public ParkingLot(@NotNull String parkingLotName, @NotNull String location) {
        this.parkingLotName = parkingLotName;
        this.location = location;
        rowSize = floorLength;
        colSize = floorDepth;
        floorSize = numOfFloors;
    }
    public ParkingLot(@NotNull String parkingLotName, @NotNull String location, Integer rowSize,
                      Integer colSize, Integer floorSize) {
        this.parkingLotName = parkingLotName;
        this.location = location;
        this.rowSize = rowSize;
        this.colSize = colSize;
        this.floorSize = floorSize;
    }

    public ParkingLot() {

    }

    public Long getId() {
        return id;
    }

    public String getParkingLotName() {
        return parkingLotName;
    }

    public String getLocation() {
        return location;
    }

    public Integer getRowSize() {
        return rowSize;
    }

    public Integer getColSize() {
        return colSize;
    }

    public Integer getFloorSize() {
        return floorSize;
    }

    public Robot getRobot() {
        return robot;
    }

    public Rates getRates() {
        return rates;
    }

    public List<ParkingSpace> getParkingSpace() {
        return parkingSpace;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<AbstractCostumer> getCustomers() {
        return customers;
    }

    public void setParkingLotName(String parkingLotName) {
        this.parkingLotName = parkingLotName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setRowSize(Integer rowSize) {
        this.rowSize = rowSize;
    }

    public void setColSize(Integer colSize) {
        this.colSize = colSize;
    }

    public void setFloorSize(Integer floorSize) {
        this.floorSize = floorSize;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public void setRates(Rates rates) {
        this.rates = rates;
    }

    public void setParkingSpace(List<ParkingSpace> parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public void setCustomers(List<AbstractCostumer> customers) {
        this.customers = customers;
    }

    public void addEmployee (Employee employee){
        employees.add(employee);
    }
    public void addCustomer (AbstractCostumer customer){
        customers.add(customer);
    }
    public void addReservation (Reservation reservation){
        reservations.add(reservation);
    }
}
