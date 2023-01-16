package net.cps.common.entities;

import net.cps.common.utils.ParkingSpaceState;
import net.cps.common.utils.ReservationStatus;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Note: this class is not an entity, it is used to manage the data of a parking lot.
 *       It is used as if the Robot was an outside entity that the system can work with.
 */
public class Robot {
    private final ParkingLot parkingLot;
    private final ParkingSpace[][][] array;
    private final Integer floors;
    private final Integer rows;
    private final Integer columns;
    private final Integer totalCapacity;
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public Robot (ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
        this.rows = parkingLot.getNumOfRows();
        if (this.rows != 3) System.out.println("ERROR: rows need to be 3\n");
        this.columns = parkingLot.getNumOfCols();
        this.floors = parkingLot.getNumOfFloors();
        if (this.floors != 3) System.out.println("ERROR: floors need to be 3\n");
        this.totalCapacity = floors * rows * columns;
        this.array = new ParkingSpace[rows][columns][floors];
        initParkingSpaces();
    }
    
    
    
    /* ----- Getters & Setters -------------------------------------- */
    
    ParkingSpace getParkingSpace (Integer row, Integer col, Integer floor) {
        return array[row][col][floor];
    }
    
    public ParkingSpace[][][] getMatrix3D () {
        return array;
    }
    
    public Integer getFloors () {
        return floors;
    }
    
    public Integer getRows () {
        return rows;
    }
    
    public Integer getCols () {
        return columns;
    }
    
    public Integer getTotalCapacity () {
        return totalCapacity;
    }
    
    public Integer getAvailableCapacity () {
        Integer availableCapacity = 0;
        for (int i = 0 ; i < rows ; i++) {
            for (int j = 0 ; j < columns ; j++) {
                for (int k = 0 ; k < floors ; k++) {
                    if (array[i][j][k].getState() == ParkingSpaceState.AVAILABLE) {
                        availableCapacity++;
                    }
                }
            }
        }
        return availableCapacity;
    }
    
    //reservation created after checking with this function. also : must delete reservation after vehicle leaves the parking lot
    public Integer getAvailableCapacityByTime (Calendar entranceTime, Calendar departureTime) {
        Integer numOfParkingSpaces = rows * columns * floors;
        Integer numOfUnavailablePS = 0;
        for (int i = 0 ; i < rows ; i++) {
            for (int j = 0 ; j < columns ; j++) {
                for (int k = 0 ; k < floors ; k++) {
                    if (array[i][j][k].getState() == ParkingSpaceState.DISABLED || array[i][j][k].getState() == ParkingSpaceState.OUT_OF_ORDER) {
                        numOfUnavailablePS++;
                    }
                }
            }
        }
        ArrayList<Reservation> reservations = parkingLot.getReservations();
        
        for (Reservation reservation : reservations) {
            // if someone is late to take his vehicle, then the departure time is wrong.
            Calendar vehicleDepartureTime = reservation.getDepartureTime();
            if (vehicleDepartureTime.before(Calendar.getInstance()))
                vehicleDepartureTime = Calendar.getInstance();
            if (!(vehicleDepartureTime.before(entranceTime) || reservation.getArrivalTime().after(departureTime)))
                numOfUnavailablePS++;
        }
        
        return numOfParkingSpaces - numOfUnavailablePS;
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    public void initParkingSpaces () {
        ArrayList<ParkingSpace> parkingSpaces = parkingLot.getParkingSpaces();
        
        for (ParkingSpace parkingSpace : parkingSpaces) {
            array[parkingSpace.getRow()][parkingSpace.getCol()][parkingSpace.getFloor()] = parkingSpace;
        }
    }
    public void initParkingSpaces1 () {
        for (int i = 0 ; i < rows ; i++) {
            for (int j = 0 ; j < columns ; j++) {
                for (int k = 0 ; k < floors ; k++) {
                    array[i][j][k] = new ParkingSpace(parkingLot, k, i, j); //AVAILABLE automatically
                }
            }
        }
    }
    
    public void printAvailableParkingSpaces () {
        for (int i = 0 ; i < rows ; i++) {
            for (int j = 0 ; j < columns ; j++) {
                for (int k = 0 ; k < floors ; k++) {
                    if (array[i][j][k].getState() == ParkingSpaceState.AVAILABLE) {
                        System.out.println("Parking space available at: " + i + ", " + j + ", " + k);
                    }
                }
            }
        }
    }
    
    public void printOccupiedParkingSpaces () {
        for (int i = 0 ; i < rows ; i++) {
            for (int j = 0 ; j < columns ; j++) {
                for (int k = 0 ; k < floors ; k++) {
                    if (array[i][j][k].getState() == ParkingSpaceState.OCCUPIED) {
                        System.out.println("Parking space occupied at: " + i + ", " + j + ", " + k);
                    }
                }
            }
        }
    }
    
    public void printParkingSpace (int row, int col, int floor) {
        System.out.println("Parking space at: " + row + ", " + col + ", " + floor + " is " + array[row][col][floor].getState());
    }
    
    public void printParkingLotMap () {
        System.out.println("PRINT MAP BY DEPARTURE TIME:");
        for (int k = 0 ; k < floors ; k++) {
            System.out.println("floor " + k + ":");
            for (int j = 0 ; j < columns ; j++) {
                for (int i = 0 ; i < rows ; i++) {
                    if (array[i][j][k].getState() == ParkingSpaceState.OCCUPIED) {
                        System.out.print(array[i][j][k].getVehicle().getLicensePlate() + ": " + array[i][j][k].getReservation().getDepartureTime().getTime() + "| ");
                    }
                    else {
                        System.out.print(array[i][j][k].getState() + "| ");
                    }
                }
                System.out.println();
            }
        }
    }
    
    
    /* ----- Algorithm Methods -------------------------------------- */
    
    public boolean swap (ParkingSpace p1, ParkingSpace p2) {
        int row1 = p1.getRow();
        int column1 = p1.getCol();
        int floor1 = p1.getFloor();
        int row2 = p2.getRow();
        int column2 = p2.getCol();
        int floor2 = p2.getFloor();
        //send OCCUPIED/AVAILABLE indexes
        if ((array[row1][column1][floor1].getState() != ParkingSpaceState.AVAILABLE) && (array[row1][column1][floor1].getState() != ParkingSpaceState.OCCUPIED)) {
            System.out.println("error swap\n");
            return false;
        }
        if ((array[row2][column2][floor2].getState() != ParkingSpaceState.AVAILABLE) && (array[row2][column2][floor2].getState() != ParkingSpaceState.OCCUPIED)) {
            System.out.println("error swap\n");
            return false;
        }
        ParkingSpaceState state = array[row1][column1][floor1].getState();
        Vehicle vehicle = array[row1][column1][floor1].getVehicle();
        Reservation reservation = array[row1][column1][floor1].getReservation();
        
        array[row1][column1][floor1].setVehicle(array[row2][column2][floor2].getVehicle());
        array[row1][column1][floor1].setState(array[row2][column2][floor2].getState());
        array[row1][column1][floor1].setReservation(array[row2][column2][floor2].getReservation());
        
        array[row2][column2][floor2].setVehicle(vehicle);
        array[row2][column2][floor2].setState(state);
        array[row2][column2][floor2].setReservation(reservation);
        return true;
    }
    
    public boolean bubbleSort (ArrayList<ParkingSpace> myList) {
        //PLEASE send OCCUPIED/AVAILABLE indexes
        // A function to implement bubble sort, and swap the places in array
        int size = myList.size();
        int i, j;
        for (i = 0; i < size - 1 ; i++) {
            // Last i elements are already in place
            for (j = 0; j < size - i - 1 ; j++) {
                //case empty parking (we don't want to compare null)
                if (myList.get(j).getState() == ParkingSpaceState.AVAILABLE) {
                    //j before j+1: dont swap
                }
                else if (myList.get(j + 1).getState() == ParkingSpaceState.AVAILABLE) {
                    //j+1 is empty - need to swap
                    if (swap(myList.get(j), myList.get(j + 1)) == false) return false; //error - return 1
                }
                //case full parking
                //j>j+1: need to swap
                else if (myList.get(j).getDepartureTime().after(myList.get(j + 1).getDepartureTime())) {
                    if (swap(myList.get(j), myList.get(j + 1)) == false) return false; //error - return 1
                }
                else {
                    //j before j+1: dont swap
                }
            }
        }
        return true;
        
    }
    
    public boolean changeArrayInL (int row, int column, int floor) {
        //change sort "L"
        //1) create a list of places you want to sort.
        // the places are in "L" shape, because the car need to go to the zero row and go to the zero floor.
        ArrayList<ParkingSpace> myList = new ArrayList<ParkingSpace>();
        //the places are sorted from the min place - close to the enter of parking
        
        //moves in same row&column (change floors) - go to the zero floor(move up: ^).
        for (int i = 0 ; i < floor ; i++) {
            if (array[0][column][i].getState() == ParkingSpaceState.AVAILABLE || array[row][column][i].getState() == ParkingSpaceState.OCCUPIED)
                myList.add(array[row][column][i]);
        }
        //moves in same floor (change rows) - go to the zero row (move: <-).
        for (int i = 0 ; i <= row ; i++) {
            if (array[i][column][floor].getState() == ParkingSpaceState.AVAILABLE || array[i][column][floor].getState() == ParkingSpaceState.OCCUPIED)
                myList.add(array[i][column][floor]);
        }
        //System.out.println("moves: "+myList.size());
        //2) sort the list - create a new map after exit/enter.
        return bubbleSort(myList);
    }
    
    public boolean changeArrayInsertCase2 (int row, int column, int floor, Vehicle vehicle, Reservation reservation) {
        //case2 - between cars
        //search next available index2
        int i = 0;
        int j = 0;
        int k = floor;
        int flag = 0;
        ArrayList<ParkingSpace> myList = new ArrayList<ParkingSpace>();
        //priority to columns because we don't need many moves in ths parkingLot
        for ( ; k < floors ; k++) {
            if (k == floor) { //first time
                i = row;
            }
            else {
                i = 0;
            }
            for ( ; i < rows ; i++) {
                if (k == floor && i == row) { //first time
                    j = column;
                }
                else {
                    j = 0;
                }
                for ( ; j < columns ; j++) {
                    if (array[i][j][k].getState() == ParkingSpaceState.AVAILABLE) { //found index2
                        //index 2 = index 1
                        
                        array[i][j][k].setState(ParkingSpaceState.OCCUPIED);
                        array[i][j][k].setVehicle(array[row][column][floor].getVehicle());
                        array[i][j][k].setReservation(array[row][column][floor].getReservation());
                        
                        //insert car to index 1
                        array[row][column][floor].setVehicle(vehicle);
                        array[row][column][floor].setReservation(reservation);
                        array[row][column][floor].setState(ParkingSpaceState.OCCUPIED);
                        
                        flag = 1;
                        break;
                    }
                    if (flag == 1) break;
                }
                if (flag == 1) break;
            }
            if (flag == 1) break;
        }
        //if "L"1 in "L"2 we want to compute 1 time
        if ((column == j && floor == k)) {
            return (changeArrayInL(Math.max(row, i), column, floor));
        }
        else if (row == 0 && row == i && column == j) {
            return (changeArrayInL(0, column, Math.max(floor, k)));
        }
        else {
            // "L" to index1==row,column,floor
            // "L" to index2==i,j,k
            return (changeArrayInL(row, column, floor) && changeArrayInL(i, j, k));
        }
    }
    
    public boolean insert (Vehicle vehicle, Reservation reservation) {
        //search the true place in the array
        if (getAvailableCapacity() == 0) {
            System.out.println("sorry, the parking lot is full\n");
            return false;
        }
        //CHECK IN
        reservation.setStatus(ReservationStatus.CHECKED_IN);
        //loop: run on the array. condition: (available||out over range||find place between cars (exit time))
        //priority to minimum rows because we don't need many moves in ths parkingLot (enter in zero row in zero floor)
        for (int i = 0 ; (i < rows) ; i++) {
            for (int k = 0 ; (k < floors) ; k++) {
                for (int j = 0 ; j < columns ; j++) {
                    if (array[i][j][k].getState() == ParkingSpaceState.AVAILABLE) {
                        //insert vehicle
                        array[i][j][k].setState(ParkingSpaceState.OCCUPIED);
                        array[i][j][k].setVehicle(vehicle);
                        array[i][j][k].setReservation(reservation);
                        //sort and return vehicles in "L"
                        return changeArrayInL(i, j, k);
                    }
                    else if (array[i][j][k].getState() == ParkingSpaceState.OCCUPIED && reservation.getDepartureTime().before(array[i][j][k].getDepartureTime())) {
                        //insert & change array(i,j,k,reservation)
                        return changeArrayInsertCase2(i, j, k, vehicle, reservation);
                    }
                }
            }
        }
        return false;
    }
    
    public int[] getIndexesInArray (Vehicle vehicle) {
        int[] n = {-1, -1, -1};
        for (int i = 0 ; i < rows ; i++) {
            for (int j = 0 ; j < columns ; j++) {
                for (int k = 0 ; k < floors ; k++) {
                    if (array[i][j][k].getVehicle() == vehicle) {
                        n[0] = i;
                        n[1] = j;
                        n[2] = k;
                        return n;
                    }
                }
            }
        }
        return n;
    }
    
    public boolean setConditionToPlace (int row, int col, int floor, ParkingSpaceState state) {
        
        if (array[row][col][floor].getState() != ParkingSpaceState.OCCUPIED) {
            array[row][col][floor].setState(state);
            return true;
        }
        else { //occupied parkingSpace
            if (getAvailableCapacity() < 1) {
                return false;
            }
            //save car
            Vehicle vehicle = array[row][col][floor].getVehicle();
            Reservation reservation = array[row][col][floor].getReservation();
            //set condition
            array[row][col][floor].setState(state);
            array[row][col][floor].setVehicle(null);
            array[row][col][floor].setReservation(null);
            //move car
            return insert(vehicle, reservation);
        }
    }
    
    public boolean remove (Vehicle vehicle) {
        //search place's vehicle
        int[] n = getIndexesInArray(vehicle);
        if (n[0] == -1) return false;
        int row = n[0], col = n[1], floor = n[2];
        
        //CHECK OUT
        array[row][col][floor].getReservation().setStatus(ReservationStatus.CHECKED_OUT);
        //remove car
        array[row][col][floor].setState(ParkingSpaceState.AVAILABLE);
        array[row][col][floor].setVehicle(null);
        array[row][col][floor].setReservation(null);
        
        //change array - return cars after sort
        return changeArrayInL(row, col, floor);
    }
}