package net.cps.entities.hibernate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "parking_lots")
public class ParkingLot implements Serializable {
    public static final int floorLength = 3; // x axes
    public static final int numOfFloors = 3; // y axes
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false, nullable = false)
    private int id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "address", nullable = false)
    private String address;
    
    @Column(name = "floor_width", nullable = false)
    private int floorWidth; // z axes
    
    @OneToOne(mappedBy = "parkingLot", cascade = CascadeType.ALL)
    private Rates rates;
    
    public ParkingLot() {}
    
    public ParkingLot(String name, String address, int floorWidth) {
        this.name = name;
        this.address = address;
        this.floorWidth = floorWidth;
        this.rates = new Rates(this.id);
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getAddress() {
        return this.address;
    }
    
    public int getFloorWidth() {
        return this.floorWidth;
    }
    
    public Rates getRates() {
        return this.rates;
    }
    
    public int getTotalSpace() {
        return this.floorWidth * floorLength * numOfFloors;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public void setFloorWidth(int floorWidth) {
        this.floorWidth = floorWidth;
    }
    
    public void setRates(Rates rates) {
        this.rates = rates;
    }
}




//
//
//@Entity
//@Table(name="Airline")
//public class Airline {
//    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
//    @Column(name="IDAIRLINE", nullable=false)
//    private Integer idAirline;
//
//    @Column(name="NAME", nullable=false)
//    private String name;
//
//    @Column(name="CODE", nullable=false, length=3)
//    private String code;
//
//    @Column(name="ALIAS", nullable=true)
//    private String aliasName;
//
//    @OneToMany(cascade = CascadeType.ALL, mappedBy="airline")
//    private Set<AirlineFlight> airlineFlights = new HashSet<AirlineFlight>(0);
//
//    public Airline(){}
//
//    public Airline(String name, String code, String aliasName, Set<AirlineFlight> flights) {
//        setName(name);
//        setCode(code);
//        setAliasName(aliasName);
//        setAirlineFlights(flights);
//    }
//
//    public Integer getIdAirline() {
//        return idAirline;
//    }
//
//    private void setIdAirline(Integer idAirline) {
//        this.idAirline = idAirline;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = DAOUtil.convertToDBString(name);
//    }
//
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = DAOUtil.convertToDBString(code);
//    }
//
//    public String getAliasName() {
//        return aliasName;
//    }
//    public void setAliasName(String aliasName) {
//        if(aliasName != null)
//            this.aliasName = DAOUtil.convertToDBString(aliasName);
//    }
//
//    public Set<AirlineFlight> getAirlineFlights() {
//        return airlineFlights;
//    }
//    public void setAirlineFlights(Set<AirlineFlight> flights) {
//        this.airlineFlights = flights;
//    }
//}
//
//@Entity
//@Table(name="AirlineFlight")
//public class AirlineFlight {
//    @Id
//    @GeneratedValue(generator="identity")
//    @GenericGenerator(name="identity", strategy="identity")
//    @Column(name="IDAIRLINEFLIGHT", nullable=false)
//    private Integer idAirlineFlight;
//
//    @ManyToOne(fetch=FetchType.LAZY)
//    @JoinColumn(name="IDAIRLINE", nullable=false)
//    private Airline airline;
//
//    @Column(name="FLIGHTNUMBER", nullable=false)
//    private String flightNumber;
//
//    public AirlineFlight(){}
//
//    public AirlineFlight(Airline airline, String flightNumber) {
//        setAirline(airline);
//        setFlightNumber(flightNumber);
//    }
//
//    public Integer getIdAirlineFlight() {
//        return idAirlineFlight;
//    }
//    private void setIdAirlineFlight(Integer idAirlineFlight) {
//        this.idAirlineFlight = idAirlineFlight;
//    }
//
//    public Airline getAirline() {
//        return airline;
//    }
//    public void setAirline(Airline airline) {
//        this.airline = airline;
//    }
//
//    public String getFlightNumber() {
//        return flightNumber;
//    }
//    public void setFlightNumber(String flightNumber) {
//        this.flightNumber = DAOUtil.convertToDBString(flightNumber);
//    }
//}