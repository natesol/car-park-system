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
    
}


// TODO: add methods - and any other needed fields or methods to this class.
class ParkingSpaceSmartArray {
    private ParkingSpace[][][] array;
    private Integer x;
    private Integer y;
    private Integer z;
    private Integer totalCapacity;
    
    
    public ParkingSpaceSmartArray(Integer x, Integer y, Integer z) {
        this.array = new ParkingSpace[x][y][z];
        this.x = x;
        this.y = y;
        this.z = z;
        this.totalCapacity = x * y * z;
    }
    
    public void init() {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                for (int k = 0; k < z; k++) {
                    array[i][j][k] = new ParkingSpace(ParkingSpaceCondition.AVAILABLE);
                }
            }
        }
    }
    
    public void sort() {
        // implement sorting algorithm here
    }
    public void insert (Vehicle parkingSpace) {
        // implement insert algorithm here
    }
    public void remove(ParkingSpace parkingSpace) {
        // implement remove algorithm here
    }
    
    public ParkingSpace[][][] getArray() {
        return array;
    }
    
    public Integer getTotalCapacity() {
        return totalCapacity;
    }
    
    public Integer getAvailableCapacity() {
        Integer availableCapacity = 0;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                for (int k = 0; k < z; k++) {
                    if (array[i][j][k].getCondition() == ParkingSpaceCondition.AVAILABLE) {
                        availableCapacity++;
                    }
                }
            }
        }
        return availableCapacity;
    }
    
}
