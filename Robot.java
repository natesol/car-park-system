package org.example;

//import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

public class Robot implements Serializable {

    private Integer id;
    private ParkingSpaceSmartArray parkingSpaceSmartArray;
    private ParkingLot parkingLot;

    public Integer getId () {return id;}

    public Robot (ParkingLot parkingLot) {
        parkingSpaceSmartArray = new ParkingSpaceSmartArray(3,5,3,parkingLot);
        setParkingLot(parkingLot);
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
    public void printParkingLotMap(){
        System.out.println("PRINT MAP BY DEPARTURE TIME:");
        for (int k = 0; k < parkingSpaceSmartArray.getFloors(); k++) {
            System.out.println("floor "+k+":");
            for (int j = 0; j < parkingSpaceSmartArray.getColumns(); j++) {
                for (int i = 0; i < parkingSpaceSmartArray.getRows(); i++) {
                    if(parkingSpaceSmartArray.getParkingSpace(i,j,k).getCondition()==ParkingSpaceCondition.OCCUPIED){
                        System.out.print(parkingSpaceSmartArray.getParkingSpace(i,j,k).getVehicle().getId()+": "+parkingSpaceSmartArray.getParkingSpace(i,j,k).getDepartureTime().getTime() +"| ");
                    }else{
                        System.out.print(parkingSpaceSmartArray.getParkingSpace(i,j,k).getCondition() + "| ");
                    }
                }
                System.out.println("");
            }
        }
    }
    public ParkingSpaceSmartArray getParkingSpaceSmartArray() {
        return parkingSpaceSmartArray;
    }
    public void setParkingSpace (int row, int column, int floor, ParkingSpaceCondition condition) {
        parkingSpaceSmartArray.getParkingSpace(row, column, floor).setCondition(condition);
    }
    public ParkingSpace getParkingSpace (int row, int column, int floor) {
        return parkingSpaceSmartArray.getParkingSpace(row, column, floor);
    }

   /* public Integer[][][] getParkingSpaceSmartArray () {
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
    */

    public void insertVehicle (Vehicle vehicle, Reservation reservation) {
        parkingSpaceSmartArray.insert(vehicle, reservation);
    }
    public void removeVehicle (Vehicle vehicle) {
        parkingSpaceSmartArray.remove(vehicle);
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }
    public Integer getAvailableCapacityByTime (Calendar entranceTime, Calendar departureTime){
        return parkingSpaceSmartArray.getAvailableCapacityByTime(entranceTime,departureTime);
    }
}


// TODO: add methods - and any other needed fields or methods to this class.
class ParkingSpaceSmartArray {

    private ParkingSpace[][][] array;
    private Integer rows;
    private Integer columns;
    private Integer floors;
    private Integer totalCapacity;
    private ParkingLot parkingLot;

    public ParkingSpaceSmartArray(Integer x, Integer y, Integer z,ParkingLot parkingLot) { //get dims
        this.array = new ParkingSpace[x][y][z];
        this.rows = x; //3
        this.columns = y; //5
        this.floors = z; //3
        this.totalCapacity = x * y * z;
        this.parkingLot = parkingLot;
        init();
    }

    public void init() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                for (int k = 0; k < floors; k++) {
                    array[i][j][k] = new ParkingSpace(i,j,k,parkingLot); //AVAILABLE automatically
                }
            }
        }
    }
    public boolean swap (ParkingSpace p1,ParkingSpace p2){
        int row1=p1.getRow();int column1=p1.getColumn();int floor1=p1.getFloor();
        int row2=p2.getRow();int column2=p2.getColumn(); int floor2=p2.getFloor();
        //send OCCUPIED/AVAILABLE indexes
        if ((array[row1][column1][floor1].getCondition() != ParkingSpaceCondition.AVAILABLE)&&(array[row1][column1][floor1].getCondition() != ParkingSpaceCondition.OCCUPIED)){
            System.out.println("error swap\n");
            return false;
        }
        if ((array[row2][column2][floor2].getCondition() != ParkingSpaceCondition.AVAILABLE)&&(array[row2][column2][floor2].getCondition() != ParkingSpaceCondition.OCCUPIED)){
            System.out.println("error swap\n");
            return false;
        }
        ParkingSpaceCondition condition = array[row1][column1][floor1].getCondition();
        Vehicle vehicle = array[row1][column1][floor1].getVehicle();
        Calendar departureTime = array[row1][column1][floor1].getDepartureTime();
        Calendar arrivalTime = array[row1][column1][floor1].getArrivalTime();
        array[row1][column1][floor1].setArrivalTime(array[row2][column2][floor2].getArrivalTime());
        array[row1][column1][floor1].setDepartureTime(array[row2][column2][floor2].getDepartureTime());
        array[row1][column1][floor1].setVehicle(array[row2][column2][floor2].getVehicle());
        array[row1][column1][floor1].setCondition(array[row2][column2][floor2].getCondition());
        array[row2][column2][floor2].setDepartureTime(departureTime);
        array[row2][column2][floor2].setArrivalTime(arrivalTime);
        array[row2][column2][floor2].setVehicle(vehicle);
        array[row2][column2][floor2].setCondition(condition);
        return true;
    }
    public boolean bubbleSort(ArrayList<ParkingSpace> myList){
        //PLEASE send OCCUPIED/AVAILABLE indexes
        // A function to implement bubble sort, and swap the places in array
        int size = myList.size();
        int i, j;
        for (i = 0; i < size - 1; i++) {
            // Last i elements are already in place
            for (j = 0; j < size - i - 1; j++) {
                //case empty parking (we don't want to compare null)
                if(myList.get(j).getCondition()==ParkingSpaceCondition.AVAILABLE){
                    //j before j+1: dont swap
                }else if(myList.get(j+1).getCondition()==ParkingSpaceCondition.AVAILABLE){
                    //j+1 is empty - need to swap
                    if(swap(myList.get(j), myList.get(j + 1))==false) return false; //error - return 1
                }
                //case full parking
                //j>j+1: need to swap
                else if (myList.get(j).getDepartureTime().after(myList.get(j + 1).getDepartureTime())) {
                    swap(myList.get(j), myList.get(j + 1));
                }else{
                    //j before j+1: dont swap
                }
            }
        }
        return true;

    }

    public boolean changeArrayInL (int row, int column, int floor){
        //change sort "L"
        //1) create a list of places you want to sort.
        // the places are in "L" shape, because the car need to go to the zero row and go to the zero floor.
        ArrayList<ParkingSpace> myList = new ArrayList<ParkingSpace>();
        //the places are sorted from the min place - close to the enter of parking

        //moves in same row&column (change floors) - go to the zero floor(move up: ^).
        for (int i=0; i<floor ; i++){
            if (array[0][column][i].getCondition()==ParkingSpaceCondition.AVAILABLE || array[row][column][i].getCondition()==ParkingSpaceCondition.OCCUPIED)
                myList.add(array[row][column][i]);
        }
        //moves in same floor (change rows) - go to the zero row (move: <-).
        for (int i=0; i<=row ; i++){
            if (array[i][column][floor].getCondition()==ParkingSpaceCondition.AVAILABLE || array[i][column][floor].getCondition()==ParkingSpaceCondition.OCCUPIED)
                myList.add(array[i][column][floor]);
        }
        System.out.println("moves: "+myList.size());
        //2) sort the list - create a new map after exit/enter.
        return bubbleSort(myList);
    }
    public boolean changeArrayInsertCase2 (int row, int column, int floor,Vehicle vehicle, Reservation reservation) {
        //case2 - between cars
        //search next available index2
        int i=0; int j=0; int k=floor; int flag=0;
        ArrayList<ParkingSpace> myList = new ArrayList<ParkingSpace>();
        //priority to columns because we dont need many moves in ths parkingLot
        for (;k<floors;k++) {
            if (k==floor){ //first time
                i=row;
            }else {
                i=0;
            }
            for (;i<rows;i++) {
                if (k==floor && i==row){ //first time
                    j=column;
                }else {
                    j=0;
                }
                for (;j < columns;j++)  {
                    if (array[i][j][k].getCondition() == ParkingSpaceCondition.AVAILABLE) { //found index2
                        //index 2 = index 1
                        array[i][j][k].setCondition(ParkingSpaceCondition.OCCUPIED);
                        array[i][j][k].setVehicle(array[row][column][floor].getVehicle());
                        array[i][j][k].setDepartureTime(array[row][column][floor].getDepartureTime());
                        array[i][j][k].setArrivalTime(array[row][column][floor].getArrivalTime());
                        //insert car to index 1
                        array[row][column][floor].setVehicle(vehicle);
                        array[row][column][floor].setDepartureTime(reservation.getDepartureTime());
                        array[row][column][floor].setArrivalTime(reservation.getArrivalTime());

                        flag = 1;
                        break;
                    }
                    if (flag==1) break;
                }
                if (flag==1) break;
            }
            if (flag==1) break;
        }
        //if "L"1 in "L"2 we want to compute 1 time
        if((column==j && floor==k)){
            return (changeArrayInL(Math.max(row,i),column,floor));
        }else if(row==0 && row==i && column==j){
            return (changeArrayInL(0,column,Math.max(floor,k)));
        }else {
            // "L" to index1==row,column,floor
            // "L" to index2==i,j,k
            return(changeArrayInL(row, column, floor) && changeArrayInL(i, j, k));
        }
    }

    public boolean insert (Vehicle vehicle, Reservation reservation) {
        //search the true place in the array
        if (getAvailableCapacity()==0){
            System.out.println("sorry, the parking lot is full\n");
            return false;
        }
        //loop: run on the array. condition: (available||out over range||find place between cars (exit time))
        //priority to minimum rows because we don't need many moves in ths parkingLot (enter in zero row in zero floor)
        for (int i=0; (i < rows); i++) {
            for (int k = 0; (k < floors); k++) {
                for (int j = 0; j < columns; j++) {
                    if (array[i][j][k].getCondition() == ParkingSpaceCondition.AVAILABLE) {
                        //insert vehicle
                        array[i][j][k].setCondition(ParkingSpaceCondition.OCCUPIED);
                        array[i][j][k].setVehicle(vehicle);
                        array[i][j][k].setDepartureTime(reservation.getDepartureTime());
                        array[i][j][k].setArrivalTime(reservation.getArrivalTime());

                        //sort and return vehicles in "L"
                        return changeArrayInL(i,j,k);
                    }
                    else if(array[i][j][k].getCondition() == ParkingSpaceCondition.OCCUPIED && reservation.getDepartureTime().before(array[i][j][k].getDepartureTime())){
                        //insert & change array(i,j,k,reservation)
                        return changeArrayInsertCase2(i,j,k,vehicle,reservation);
                    }
                }
            }
        }
        return false;
    }
    public int[] getIndexesInArray(Vehicle vehicle){
        int n[]={-1,-1,-1};
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                for (int k = 0; k < floors; k++) {
                    if (array[i][j][k].getVehicle() == vehicle) {
                        n[0]=i; n[1]=j; n[2]=k;
                        return n;
                    }
                }
            }
        }
        return n;
    }
    //PLEASE send reservation of customer in array[row][col][floor], if it's empty send null
    public boolean setConditionToPlace(int row, int col, int floor, ParkingSpaceCondition condition, Reservation reservation){

        if (array[row][col][floor].getCondition() != ParkingSpaceCondition.OCCUPIED){
            array[row][col][floor].setCondition(condition);
            return true;
        }else{
            if (getAvailableCapacity()<1){
                return false;
            }
            //save car
            Vehicle vehicle = array[row][col][floor].getVehicle();
            Calendar departureTime = array[row][col][floor].getDepartureTime();
            Calendar arrivalTime = array[row][col][floor].getArrivalTime();
            ParkingSpaceCondition conditionCar = array[row][col][floor].getCondition();
            //set condition
            array[row][col][floor].setCondition(condition);
            array[row][col][floor].setVehicle(null);
            array[row][col][floor].setDepartureTime(null);
            array[row][col][floor].setArrivalTime(null);

            //move car
            return insert(vehicle, reservation);
        }
    }
    public boolean remove (Vehicle vehicle) {
        //search place's vehicle
        int [] n =getIndexesInArray(vehicle);
        if (n[0]==-1) return false;
        int row = n[0], col = n[1], floor = n[2];
        //remove car
        array[row][col][floor].setDepartureTime(null);
        array[row][col][floor].setArrivalTime(null);
        array[row][col][floor].setCondition(ParkingSpaceCondition.AVAILABLE);
        array[row][col][floor].setVehicle(null);
        //change array - return cars after sort
        return changeArrayInL(row,col,floor);
    }

    ParkingSpace getParkingSpace (Integer x, Integer y, Integer z) {return array[x][y][z];}

    public ParkingSpace[][][] getArray() {return array;}

    public Integer getRows () {return rows;}

    public void setRows (Integer rows) {this.rows = rows;}

    public Integer getColumns () {return columns;}

    public void setColumns (Integer columns) {this.columns = columns;}

    public Integer getFloors () {return floors;}

    public void setFloors (Integer floors) {this.floors = floors;}

    public void setTotalCapacity (Integer totalCapacity) {this.totalCapacity = totalCapacity;}

    public Integer getTotalCapacity() {return totalCapacity;}

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
    //reservation created after checking with this function. also : must delete reservation after vehicle leaves the parking lot
    public Integer getAvailableCapacityByTime(Calendar entranceTime, Calendar departureTime) {
        Integer numOfParkingSpaces = rows * columns * floors;
        Integer numOfUnavailablePS = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                for (int k = 0; k < floors; k++) {
                    if (array[i][j][k].getCondition() == ParkingSpaceCondition.DISABLED || array[i][j][k].getCondition() == ParkingSpaceCondition.OUT_OF_ORDER) {
                        numOfUnavailablePS++;
                    }
                }
            }
        }
        List<Reservation> reservations = parkingLot.getReservationsList();
        for (Reservation reservation : reservations) {
            //if someone is late to take his vehicle, then the departure time is wrong.
            Calendar vehicleDepartureTime = reservation.getDepartureTime();
            if (vehicleDepartureTime.before(Calendar.getInstance()))
                vehicleDepartureTime = Calendar.getInstance();
            if (!(vehicleDepartureTime.before(entranceTime) || reservation.getArrivalTime().after(departureTime)))
                numOfUnavailablePS++;
        }

        return numOfParkingSpaces - numOfUnavailablePS;
    }
    //if client enters, we want to check if he has reservation.
    public void UpdateReservation (Vehicle vehicle, Long idReservation){
        List<Reservation> reservations = vehicle.getReservationsList();
        for(Reservation reservation:reservations){
            if(reservation.getId()==idReservation)
                reservation.setEnterParkingLot(true);
        }
    }

}