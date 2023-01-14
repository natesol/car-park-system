package net.cps.common.entities;

import net.cps.common.utils.ParkingSpaceState;
import net.cps.common.utils.ReservationStatus;

import java.util.ArrayList;
import java.util.Calendar;


public class Robot {
    private final ParkingLot parkingLot;
    private final ParkingSpace[][][] Matrix3D;
    private final Integer floors;
    private final Integer rows;
    private final Integer cols;
    private final Integer totalCapacity;
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public Robot (ParkingLot parkingLot /*, ArrayList<ParkingSpace> parkingSpaces */) {
        this.parkingLot = parkingLot;
        this.floors = parkingLot.getNumOfFloors(); //3
        this.rows = parkingLot.getNumOfRows(); //3
        this.cols = parkingLot.getNumOfCols(); //5
        this.totalCapacity = floors * rows * cols;
        //this.Matrix3D = new ParkingSpace[floors][rows][cols];
        this.Matrix3D = new ParkingSpace[rows][cols][floors]; // ???
        initParkingSpaces();
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    public void initParkingSpaces () {
        for (int i = 0 ; i < cols ; i++) {
            for (int j = 0 ; j < rows ; j++) {
                for (int k = 0 ; k < floors ; k++) {
                    Matrix3D[i][j][k] = new ParkingSpace(parkingLot, i, j, k); // AVAILABLE automatically
                }
            }
        }
    }
    
    public void printAvailableParkingSpaces () {
        for (int i = 0 ; i < rows ; i++) {
            for (int j = 0 ; j < cols ; j++) {
                for (int k = 0 ; k < floors ; k++) {
                    if (Matrix3D[i][j][k].getState() == ParkingSpaceState.AVAILABLE) {
                        System.out.println("Parking space available at: " + i + ", " + j + ", " + k);
                    }
                }
            }
        }
    }
    
    public void printOccupiedParkingSpaces () {
        for (int i = 0 ; i < rows ; i++) {
            for (int j = 0 ; j < cols ; j++) {
                for (int k = 0 ; k < floors ; k++) {
                    if (Matrix3D[i][j][k].getState() == ParkingSpaceState.OCCUPIED) {
                        System.out.println("Parking space occupied at: " + i + ", " + j + ", " + k);
                    }
                }
            }
        }
    }
    
    public void printParkingSpace (int row, int col, int floor) {
        System.out.println("Parking space at: " + row + ", " + col + ", " + floor + " is " + Matrix3D[row][col][floor].getState());
    }
    
    public void printParkingLotMap () {
        System.out.println("PRINT MAP BY DEPARTURE TIME:");
        for (int k = 0 ; k < floors ; k++) {
            System.out.println("floor " + k + ":");
            for (int j = 0 ; j < cols ; j++) {
                for (int i = 0 ; i < rows ; i++) {
                    if (Matrix3D[i][j][k].getState() == ParkingSpaceState.OCCUPIED) {
                        System.out.print(Matrix3D[i][j][k].getVehicle().getLicensePlate() + ": " + Matrix3D[i][j][k].getReservation().getDepartureTime().getTime() + "| ");
                    }
                    else {
                        System.out.print(Matrix3D[i][j][k].getState() + "| ");
                    }
                }
                System.out.println();
            }
        }
    }
    
    // if client enters, we want to check if he has reservation.
    public void UpdateReservation (Vehicle vehicle, Integer idReservation) {
        ArrayList<Reservation> reservations = (ArrayList<Reservation>) vehicle.getReservations();
        for (Reservation reservation : reservations) {
            if (idReservation.equals(reservation.getId())) {
                reservation.setStatus(ReservationStatus.CHECKED_IN);
            }
        }
    }
    
    
    
    /* ----- Getters & Setters -------------------------------------- */
    
    ParkingSpace getParkingSpace (Integer x, Integer y, Integer z) {
        return Matrix3D[x][y][z];
    }
    
    public ParkingSpace[][][] getMatrix3D () {
        return Matrix3D;
    }
    
    public Integer getFloors () {
        return floors;
    }
    
    public Integer getRows () {
        return rows;
    }
    
    public Integer getCols () {
        return cols;
    }
    
    public Integer getTotalCapacity () {
        return totalCapacity;
    }
    
    public Integer getAvailableCapacity () {
        Integer availableCapacity = 0;
        for (int i = 0 ; i < rows ; i++) {
            for (int j = 0 ; j < cols ; j++) {
                for (int k = 0 ; k < floors ; k++) {
                    if (Matrix3D[i][j][k].getState() == ParkingSpaceState.AVAILABLE) {
                        availableCapacity++;
                    }
                }
            }
        }
        return availableCapacity;
    }
    
    // reservation created after checking with this function. also : must delete reservation after vehicle leaves the parking lot
    public Integer getAvailableCapacityByTime (Calendar entranceTime, Calendar departureTime) {
        Integer numOfParkingSpaces = rows * cols * floors;
        Integer numOfUnavailablePS = 0;
        
        for (int i = 0 ; i < rows ; i++) {
            for (int j = 0 ; j < cols ; j++) {
                for (int k = 0 ; k < floors ; k++) {
                    if (Matrix3D[i][j][k].getState() == ParkingSpaceState.DISABLED || Matrix3D[i][j][k].getState() == ParkingSpaceState.OUT_OF_ORDER) {
                        numOfUnavailablePS++;
                    }
                }
            }
        }
        ArrayList<Reservation> reservations = parkingLot.getReservations();
        for (Reservation reservation : reservations) {
            // if someone is late to take his vehicle, then the departure time is wrong.
            Calendar vehicleDepartureTime = reservation.getDepartureTime();
            
            if (vehicleDepartureTime.before(Calendar.getInstance())) {
                vehicleDepartureTime = Calendar.getInstance();
            }
            if (!(vehicleDepartureTime.before(entranceTime) || reservation.getArrivalTime().after(departureTime))) {
                numOfUnavailablePS++;
            }
        }
        
        return numOfParkingSpaces - numOfUnavailablePS;
    }
    
    
    
    /* ----- Algorithm Methods -------------------------------------- */
    
    public boolean swap (ParkingSpace p1, ParkingSpace p2) {
        int floor1 = p1.getFloor();
        int row1 = p1.getRow();
        int column1 = p1.getCol();
        int floor2 = p2.getFloor();
        int row2 = p2.getRow();
        int column2 = p2.getCol();
        
        // send OCCUPIED/AVAILABLE indexes
        if ((Matrix3D[row1][column1][floor1].getState() != ParkingSpaceState.AVAILABLE) && (Matrix3D[row1][column1][floor1].getState() != ParkingSpaceState.OCCUPIED)) {
            System.out.println("error swap\n");
            return false;
        }
        if ((Matrix3D[row2][column2][floor2].getState() != ParkingSpaceState.AVAILABLE) && (Matrix3D[row2][column2][floor2].getState() != ParkingSpaceState.OCCUPIED)) {
            System.out.println("error swap\n");
            return false;
        }
        
        ParkingSpaceState state = Matrix3D[floor1][row1][column1].getState();
        Vehicle vehicle = Matrix3D[floor1][row1][column1].getVehicle();
        Calendar arrivalTime = Matrix3D[floor1][row1][column1].getArrivalTime();
        Calendar departureTime = Matrix3D[floor1][row1][column1].getDepartureTime();
        
        Matrix3D[row1][column1][floor1].setArrivalTime(Matrix3D[row2][column2][floor2].getArrivalTime());
        Matrix3D[row1][column1][floor1].setDepartureTime(Matrix3D[row2][column2][floor2].getDepartureTime());
        Matrix3D[row1][column1][floor1].setVehicle(Matrix3D[row2][column2][floor2].getVehicle());
        Matrix3D[row1][column1][floor1].setState(Matrix3D[row2][column2][floor2].getState());
        
        Matrix3D[row2][column2][floor2].setDepartureTime(departureTime);
        Matrix3D[row2][column2][floor2].setArrivalTime(arrivalTime);
        Matrix3D[row2][column2][floor2].setVehicle(vehicle);
        Matrix3D[row2][column2][floor2].setState(state);
        
        return true;
    }
    
    public boolean bubbleSort (ArrayList<ParkingSpace> myList) {
        // PLEASE send OCCUPIED/AVAILABLE indexes
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
                    if (!swap(myList.get(j), myList.get(j + 1))) return false; //error - return 1
                }
                //case full parking
                //j>j+1: need to swap
                else if (myList.get(j).getDepartureTime().after(myList.get(j + 1).getDepartureTime())) {
                    swap(myList.get(j), myList.get(j + 1));
                }
                else {
                    //j before j+1: dont swap
                }
            }
        }
        return true;
    }
    
    public boolean changeArrayInL (int row, int column, int floor) {
        // change sort "L"
        // 1) create a list of places you want to sort.
        // the places are in "L" shape, because the car need to go to the zero row and go to the zero floor.
        ArrayList<ParkingSpace> myList = new ArrayList<ParkingSpace>();
        // the places are sorted from the min place - close to the entrance of parking
        
        // moves in same row&column (change floors) - go to the zero floor (move up: ^).
        for (int i = 0 ; i < floor ; i++) {
            if (Matrix3D[0][column][i].getState() == ParkingSpaceState.AVAILABLE || Matrix3D[row][column][i].getState() == ParkingSpaceState.OCCUPIED)
                myList.add(Matrix3D[row][column][i]);
        }
        // moves in same floor (change rows) - go to the zero row (move: <-).
        for (int i = 0 ; i <= row ; i++) {
            if (Matrix3D[i][column][floor].getState() == ParkingSpaceState.AVAILABLE || Matrix3D[i][column][floor].getState() == ParkingSpaceState.OCCUPIED)
                myList.add(Matrix3D[i][column][floor]);
        }
        System.out.println("moves: " + myList.size());
        // 2) sort the list - create a new map after exit/enter.
        return bubbleSort(myList);
    }
    
    public boolean changeArrayInsertCase2 (int row, int column, int floor, Vehicle vehicle, Reservation reservation) {
        // case 2 - between cars
        // search next available index2
        int i = 0;
        int j = 0;
        int k = floor;
        int flag = 0;
        ArrayList<ParkingSpace> myList = new ArrayList<ParkingSpace>();
        // priority to columns because we don't need many moves in ths parkingLot
        for ( ; k < floors ; k++) {
            if (k == floor) { // first time
                i = row;
            }
            else {
                i = 0;
            }
            for ( ; i < rows ; i++) {
                if (k == floor && i == row) { // first time
                    j = column;
                }
                else {
                    j = 0;
                }
                for ( ; j < cols ; j++) {
                    if (Matrix3D[i][j][k].getState() == ParkingSpaceState.AVAILABLE) { // found index2
                        // index 2 = index 1
                        Matrix3D[i][j][k].setState(ParkingSpaceState.OCCUPIED);
                        Matrix3D[i][j][k].setVehicle(Matrix3D[row][column][floor].getVehicle());
                        Matrix3D[i][j][k].setDepartureTime(Matrix3D[row][column][floor].getDepartureTime());
                        Matrix3D[i][j][k].setArrivalTime(Matrix3D[row][column][floor].getArrivalTime());
                        // insert car to index 1
                        Matrix3D[row][column][floor].setVehicle(vehicle);
                        Matrix3D[row][column][floor].setDepartureTime(reservation.getDepartureTime());
                        Matrix3D[row][column][floor].setArrivalTime(reservation.getArrivalTime());
                        
                        flag = 1;
                        break;
                    }
                }
                if (flag == 1) break;
            }
            if (flag == 1) break;
        }
        // if "L"1 in "L"2 we want to compute 1 time
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
        // search the true place in the array
        if (getAvailableCapacity() == 0) {
            System.out.println("sorry, the parking lot is full\n");
            return false;
        }
        // loop: run on the array. condition: (available||out over range||find place between cars (exit time))
        // priority to minimum rows because we don't need many moves in ths parkingLot (enter in zero row in zero floor)
        for (int i = 0 ; (i < rows) ; i++) {
            for (int k = 0 ; (k < floors) ; k++) {
                for (int j = 0 ; j < cols ; j++) {
                    if (Matrix3D[i][j][k].getState() == ParkingSpaceState.AVAILABLE) {
                        // insert vehicle
                        Matrix3D[i][j][k].setState(ParkingSpaceState.OCCUPIED);
                        Matrix3D[i][j][k].setVehicle(vehicle);
                        Matrix3D[i][j][k].setDepartureTime(reservation.getDepartureTime());
                        Matrix3D[i][j][k].setArrivalTime(reservation.getArrivalTime());
                        
                        // sort and return vehicles in "L"
                        return changeArrayInL(i, j, k);
                    }
                    else if (Matrix3D[i][j][k].getState() == ParkingSpaceState.OCCUPIED && reservation.getDepartureTime().before(Matrix3D[i][j][k].getDepartureTime())) {
                        // insert & change array(i,j,k,reservation)
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
            for (int j = 0 ; j < cols ; j++) {
                for (int k = 0 ; k < floors ; k++) {
                    if (Matrix3D[i][j][k].getVehicle() == vehicle) {
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
    
    // PLEASE send reservation of customer in array[row][col][floor], if it's empty send null
    public boolean setConditionToPlace (int row, int col, int floor, ParkingSpaceState condition, Reservation reservation) {
        if (Matrix3D[row][col][floor].getState() != ParkingSpaceState.OCCUPIED) {
            Matrix3D[row][col][floor].setState(condition);
            return true;
        }
        else {
            if (getAvailableCapacity() < 1) {
                return false;
            }
            //save car
            Vehicle vehicle = Matrix3D[row][col][floor].getVehicle();
            Calendar departureTime = Matrix3D[row][col][floor].getDepartureTime();
            Calendar arrivalTime = Matrix3D[row][col][floor].getArrivalTime();
            ParkingSpaceState conditionCar = Matrix3D[row][col][floor].getState();
            //set condition
            Matrix3D[row][col][floor].setState(condition);
            Matrix3D[row][col][floor].setVehicle(null);
            Matrix3D[row][col][floor].setDepartureTime(null);
            Matrix3D[row][col][floor].setArrivalTime(null);
            
            //move car
            return insert(vehicle, reservation);
        }
    }
    
    public boolean remove (Vehicle vehicle) {
        // search place's vehicle
        int[] n = getIndexesInArray(vehicle);
        if (n[0] == -1) return false;
        int row = n[0], col = n[1], floor = n[2];
        // remove car
        Matrix3D[row][col][floor].setDepartureTime(null);
        Matrix3D[row][col][floor].setArrivalTime(null);
        Matrix3D[row][col][floor].setState(ParkingSpaceState.AVAILABLE);
        Matrix3D[row][col][floor].setVehicle(null);
        // change array - return cars after sort
        return changeArrayInL(row, col, floor);
    }
}