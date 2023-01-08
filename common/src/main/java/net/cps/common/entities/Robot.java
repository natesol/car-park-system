package net.cps.common.entities;

import net.cps.common.utils.ParkingSpaceCondition;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "robots")
public class Robot implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    
    public Integer getId () {return id;}
    
    
    ParkingSpaceSmartArray parkingSpaceSmartArray;
    
    public Robot () {
        parkingSpaceSmartArray = new ParkingSpaceSmartArray(3,3,5);
    }
    
    public void printAvailableParkingSpaces () {
        for (int i = 0; i < parkingSpaceSmartArray.getRows(); i++) {
            for (int j = 0; j < parkingSpaceSmartArray.getColumns(); j++) {
                for (int k = 0; k < parkingSpaceSmartArray.getFloors(); k++) {
                    if (parkingSpaceSmartArray.getParkingSpace(i, j, k).getCondition() == ParkingSpaceCondition.AVAILABLE) {
                        System.out.println("Parking space available at: " + i + ", " + j + ", " + k);
                    }
                }
            }
        }
    }
    public void printOccupiedParkingSpaces () {
        for (int i = 0; i < parkingSpaceSmartArray.getRows(); i++) {
            for (int j = 0; j < parkingSpaceSmartArray.getColumns(); j++) {
                for (int k = 0; k < parkingSpaceSmartArray.getFloors(); k++) {
                    if (parkingSpaceSmartArray.getParkingSpace(i, j, k).getCondition() == ParkingSpaceCondition.OCCUPIED) {
                        System.out.println("Parking space occupied at: " + i + ", " + j + ", " + k);
                    }
                }
            }
        }
    }
    
    public void printParkingSpace (int row, int column, int floor) {
        System.out.println("Parking space at: " + row + ", " + column + ", " + floor + " is " + parkingSpaceSmartArray.getParkingSpace(row, column, floor).getCondition());
    }
    
    public void setParkingSpace (int row, int column, int floor, ParkingSpaceCondition condition) {
        parkingSpaceSmartArray.getParkingSpace(row, column, floor).setCondition(condition);
    }
    public ParkingSpace getParkingSpace (int row, int column, int floor) {
        return parkingSpaceSmartArray.getParkingSpace(row, column, floor);
    }
    
    public Integer[][][] getParkingSpaceSmartArray () {
        Integer[][][] parkingSpaceSmartArray = new Integer[this.parkingSpaceSmartArray.getRows()][this.parkingSpaceSmartArray.getColumns()][this.parkingSpaceSmartArray.getFloors()];
        
        for (int i = 0; i < this.parkingSpaceSmartArray.getRows(); i++) {
            for (int j = 0; j < this.parkingSpaceSmartArray.getColumns(); j++) {
                for (int k = 0; k < this.parkingSpaceSmartArray.getFloors(); k++) {
                    parkingSpaceSmartArray[i][j][k] = this.parkingSpaceSmartArray.getParkingSpace(i, j, k).getCondition().ordinal();
                }
            }
        }
        
        return parkingSpaceSmartArray;
    }
    
    public void insertVehicle (Vehicle vehicle) {
        parkingSpaceSmartArray.insert(vehicle);
    }
    public void removeVehicle (Vehicle vehicle) {
        parkingSpaceSmartArray.remove(vehicle);
    }
}


// TODO: add methods - and any other needed fields or methods to this class.
class ParkingSpaceSmartArray {
    private ParkingSpace[][][] array;
    private Integer rows;
    private Integer columns;
    private Integer floors;
    private Integer totalCapacity;
    
    
    public ParkingSpaceSmartArray(Integer x, Integer y, Integer z) {
        this.array = new ParkingSpace[x][y][z];
        this.rows = x;
        this.columns = y;
        this.floors = z;
        this.totalCapacity = x * y * z;
    }
    
    public void init() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                for (int k = 0; k < floors; k++) {
                    array[i][j][k] = new ParkingSpace(ParkingSpaceCondition.AVAILABLE);
                }
            }
        }
    }
    
    public void sort() {
        // implement sorting algorithm here
    }
    public void insert (Vehicle vehicle) {
        // implement insert algorithm here
    }
    public void remove (Vehicle vehicle)  {
        // implement remove algorithm here
    }
    
    ParkingSpace getParkingSpace (Integer x, Integer y, Integer z) {
        return array[x][y][z];
    }
    
    public ParkingSpace[][][] getArray() {
        return array;
    }
    
    public Integer getRows () {
        return rows;
    }
    
    public void setRows (Integer rows) {
        this.rows = rows;
    }
    
    public Integer getColumns () {
        return columns;
    }
    
    public void setColumns (Integer columns) {
        this.columns = columns;
    }
    
    public Integer getFloors () {
        return floors;
    }
    
    public void setFloors (Integer floors) {
        this.floors = floors;
    }
    
    public void setTotalCapacity (Integer totalCapacity) {
        this.totalCapacity = totalCapacity;
    }
    
    public Integer getTotalCapacity() {
        return totalCapacity;
    }
    
    public Integer getAvailableCapacity() {
        Integer availableCapacity = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                for (int k = 0; k < floors; k++) {
                    if (array[i][j][k].getCondition() == ParkingSpaceCondition.AVAILABLE) {
                        availableCapacity++;
                    }
                }
            }
        }
        return availableCapacity;
    }
    
}
