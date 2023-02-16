package net.cps.common.entities;

import net.cps.common.utils.OrganizationType;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "parking_lots")
@PrimaryKeyJoinColumn(name = "id")
public class ParkingLot extends Organization implements Serializable {
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;
    @NotNull
    @Column(name = "name", columnDefinition = "VARCHAR(55) NOT NULL")
    private String name;
    @NotNull
    @Column(name = "street_number", columnDefinition = "MEDIUMINT NOT NULL")
    private Integer streetNumber;
    @NotNull
    @Column(name = "street_name", columnDefinition = "VARCHAR(55) NOT NULL")
    private String streetName;
    @NotNull
    @Column(name = "city_name", columnDefinition = "VARCHAR(55) NOT NULL")
    private String cityName;
    @NotNull
    @Column(name = "country_symbol", columnDefinition = "CHAR(2) NOT NULL")
    private String countrySymbol;
    @NotNull
    @Column(name = "num_of_floors", columnDefinition = "TINYINT NOT NULL")
    private Integer numOfFloors;
    @NotNull
    @Column(name = "num_of_rows", columnDefinition = "TINYINT NOT NULL")
    private Integer numOfRows;
    @NotNull
    @Column(name = "num_of_cols", columnDefinition = "TINYINT NOT NULL")
    private Integer numOfCols;
    @NotNull
    @OneToOne(mappedBy = "parkingLot", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Rates rates;
    @NotNull
    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ParkingSpace> parkingSpaces;
    @NotNull
    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL)
    private List<Reservation> reservations;
    @NotNull
    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL)
    private List<Subscription> subscriptions;
    
    public static final Integer MINUTE = 60 * 1000;
    public static final Integer HOUR = 60 * MINUTE;
    @Transient
    public static final OrganizationType DEFAULT_TYPE = OrganizationType.PARKING_LOT;
    @Transient
    public static final String DEFAULT_NAME = "Parking Lot Name";
    @Transient
    public static final Integer DEFAULT_FLOOR_NUM = 3;
    @Transient
    public static final Integer DEFAULT_FLOOR_ROWS = 3;
    @Transient
    public static final Integer DEFAULT_FLOOR_COLS = 3;
    @Transient
    public static final Integer MAX_COLS = 12;
    @Transient
    private transient Robot robot;
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public ParkingLot () {
        super(DEFAULT_TYPE);
        this.name = DEFAULT_NAME;
        this.streetNumber = Organization.DEFAULT_STREET_NUMBER;
        this.streetName = Organization.DEFAULT_STREET;
        this.cityName = Organization.DEFAULT_CITY;
        this.countrySymbol = Organization.DEFAULT_STATE;
        this.numOfFloors = DEFAULT_FLOOR_NUM;
        this.numOfRows = DEFAULT_FLOOR_ROWS;
        this.numOfCols = DEFAULT_FLOOR_COLS;
        this.rates = new Rates(this);
        this.parkingSpaces = createParkingSpaces();
        this.reservations = new ArrayList<>();
        this.subscriptions = new ArrayList<>();
        this.robot = new Robot(this);
    }
    
    public ParkingLot (@NotNull String name, @NotNull String streetName, @NotNull Integer streetNumber, @NotNull String cityName, @NotNull String countrySymbol, @NotNull Integer numOfCols) {
        super(DEFAULT_TYPE);
        this.name = name;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.cityName = cityName;
        this.countrySymbol = countrySymbol;
        this.numOfFloors = DEFAULT_FLOOR_NUM;
        this.numOfRows = DEFAULT_FLOOR_ROWS;
        this.numOfCols = numOfCols;
        this.rates = new Rates(this);
        this.parkingSpaces = createParkingSpaces();
        this.reservations = new ArrayList<>();
        this.subscriptions = new ArrayList<>();
        this.robot = new Robot(this);
    }
    
    public ParkingLot (@NotNull String name, @NotNull String streetName, @NotNull Integer streetNumber, @NotNull String cityName, @NotNull String countrySymbol, @NotNull Integer numOfFloors, @NotNull Integer numOfRows, @NotNull Integer numOfCols) {
        super(DEFAULT_TYPE);
        this.name = name;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.cityName = cityName;
        this.countrySymbol = countrySymbol;
        this.numOfFloors = numOfFloors;
        this.numOfRows = numOfRows;
        this.numOfCols = numOfCols;
        this.rates = new Rates(this);
        this.parkingSpaces = createParkingSpaces();
        this.reservations = new ArrayList<>();
        this.subscriptions = new ArrayList<>();
        this.robot = new Robot(this);
    }
    
    
    /* ----- Getters and Setters ------------------------------------ */
    
    public Integer getId () {
        return super.getOrganizationId();
    }
    
    public void setId (Integer id) {
        super.setOrganizationId(id);
    }
    
    public @NotNull String getName () {
        return name;
    }
    
    public void setName (@NotNull String name) {
        this.name = name;
    }
    
    public @NotNull Integer getStreetNumber () {
        return streetNumber;
    }
    
    public void setStreetNumber (@NotNull Integer streetNumber) {
        this.streetNumber = streetNumber;
    }
    
    public @NotNull String getStreetName () {
        return streetName;
    }
    
    public void setStreetName (@NotNull String streetName) {
        this.streetName = streetName;
    }
    
    public @NotNull String getCityName () {
        return cityName;
    }
    
    public void setCityName (@NotNull String cityName) {
        this.cityName = cityName;
    }
    
    public @NotNull String getCountrySymbol () {
        return countrySymbol;
    }
    
    public void setCountrySymbol (@NotNull String countrySymbol) {
        this.countrySymbol = countrySymbol;
    }
    
    public @NotNull Integer getNumOfFloors () {
        return numOfFloors;
    }
    
    public void setNumOfFloors (@NotNull Integer numOfFloors) {
        this.numOfFloors = numOfFloors;
    }
    
    public @NotNull Integer getNumOfRows () {
        return numOfRows;
    }
    
    public void setNumOfRows (@NotNull Integer numOfRows) {
        this.numOfRows = numOfRows;
    }
    
    public @NotNull Integer getNumOfCols () {
        return this.numOfCols;
    }
    
    public void setNumOfCols (@NotNull Integer numOfCols) {
        this.numOfCols = numOfCols;
    }
    
    public Integer getCapacity () {
        return this.numOfCols * numOfRows * numOfFloors;
    }
    
    public @NotNull Rates getRates () {
        return this.rates;
    }
    
    public void setRates (@NotNull Rates rates) {
        this.rates = rates;
    }
    
    public @NotNull List<ParkingSpace> getParkingSpaces () {
        return parkingSpaces;
    }
    
    public void setParkingSpaces (@NotNull List<ParkingSpace> parkingSpaces) {
        this.parkingSpaces = parkingSpaces;
    }
    
    public @NotNull List<Reservation> getReservations () {
        return reservations;
    }
    
    public @NotNull List<Subscription> getSubscriptions () {
        return subscriptions;
    }
    
    public void setSubscriptions (@NotNull List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }
    
    public void setReservations (@NotNull List<Reservation> reservations) {
        this.reservations = reservations;
    }
    
    public void setRates (Double hourlyOccasionalParking, Double hourlyOnetimeParking, Double regularSubscriptionSingleVehicle, Double regularSubscriptionMultipleVehicles, Double fullSubscriptionSingleVehicle) {
        if (hourlyOccasionalParking != null) {
            this.rates.setHourlyOccasionalParking(hourlyOccasionalParking);
        }
        if (hourlyOnetimeParking != null) {
            this.rates.setHourlyOnetimeParking(hourlyOnetimeParking);
        }
        if (regularSubscriptionSingleVehicle != null) {
            this.rates.setRegularSubscriptionSingleVehicle(regularSubscriptionSingleVehicle);
        }
        if (regularSubscriptionMultipleVehicles != null) {
            this.rates.setRegularSubscriptionMultipleVehicles(regularSubscriptionMultipleVehicles);
        }
        if (fullSubscriptionSingleVehicle != null) {
            this.rates.setFullSubscriptionSingleVehicle(fullSubscriptionSingleVehicle);
        }
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    public ArrayList<ParkingSpace> createParkingSpaces () {
        ArrayList<ParkingSpace> parkingSpaces = new ArrayList<>();
        for (int i = 0 ; i < this.numOfFloors ; i++) {
            for (int j = 0 ; j < this.numOfRows ; j++) {
                for (int k = 0 ; k < this.numOfCols ; k++) {
                    parkingSpaces.add(new ParkingSpace(this, i, j, k));
                }
            }
        }
        return parkingSpaces;
    }
    
    public Boolean checkAvailability (@NotNull Reservation reservation) {
        this.robot = new Robot(this);
        return this.robot.getAvailableCapacityByTime(reservation.getArrivalTime(), reservation.getDepartureTime()) > 0;
    }
    
    public Boolean addReservation (@NotNull Reservation reservation) {
        if (this.robot.getAvailableCapacityByTime(reservation.getArrivalTime(), reservation.getDepartureTime()) > 0) {
            return this.reservations.add(reservation);
        }
        return false;
    }
    
    public Boolean insertVehicle (@NotNull Reservation reservation) {
        this.robot = new Robot(this);
        Boolean result = this.robot.insert(reservation.getVehicle(), reservation);
        if (result) {
            this.setParkingSpaces(this.robot.fromArrayToList());
        }
        return result;
    }
    
    public Boolean removeVehicle (@NotNull Vehicle vehicle) {
        this.robot = new Robot(this);
        return this.robot.remove(vehicle);
    }
    
    public Boolean removeReservation (Reservation reservation) {
        return this.reservations.remove(reservation);
    }
    
    public Double calculateReservationPrice (@NotNull Calendar arrivalTime, @NotNull Calendar departureTime) {
        return ((departureTime.getTimeInMillis() - arrivalTime.getTimeInMillis()) / HOUR) * this.rates.getHourlyOnetimeParking();
    }
    
    public Double calculateOccasionalParkingPrice (@NotNull Calendar arrivalTime, @NotNull Calendar departureTime) {
        return ((departureTime.getTimeInMillis() - arrivalTime.getTimeInMillis()) / HOUR) * this.rates.getHourlyOccasionalParking();
    }
    
    public Double calculateCancellationPrice (@NotNull Reservation reservation) {
        if (reservation.getArrivalTime().getTimeInMillis() - Calendar.getInstance().getTimeInMillis() <= HOUR) {
            return reservation.getPayed() * Reservation.CANCELLATION_FEE_LESS_THAN_1_HOURS;
        }
        else if (reservation.getArrivalTime().getTimeInMillis() - Calendar.getInstance().getTimeInMillis() <= HOUR * 3L) {
            return reservation.getPayed() * Reservation.CANCELLATION_FEE_LESS_THAN_3_HOURS;
        }
        else {
            return reservation.getPayed() * Reservation.CANCELLATION_FEE_MORE_THAN_3_HOURS;
        }
    }
    
    @Override
    public String toString () {
        return "ParkingLot {" +
                "id: " + getId() +
                ", name: '" + name + "'" +
                ", address: '" + getAddress() + "'" +
                ", numOfFloors: " + numOfFloors +
                ", floorRows: " + numOfRows +
                ", floorCols: " + numOfCols +
                ", rates: " + rates +
                "}";
    }
}
