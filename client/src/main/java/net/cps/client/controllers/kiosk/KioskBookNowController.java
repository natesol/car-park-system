package net.cps.client.controllers.kiosk;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import net.cps.client.App;
import net.cps.client.CPSClient;
import net.cps.client.utils.AbstractKioskPageController;
import net.cps.common.entities.Customer;
import net.cps.common.entities.Reservation;
import net.cps.common.entities.Vehicle;
import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.Entities;
import net.cps.common.utils.RequestCallback;
import net.cps.common.utils.RequestType;
import net.cps.common.utils.ResponseStatus;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;


public class KioskBookNowController extends AbstractKioskPageController implements Initializable {
    Reservation currentReservation = null;
    Customer currentCustomer = null;
    Vehicle currentVehicle = null;
    
    @FXML
    public MFXTextField email;
    @FXML
    public Text emailErrorText;
    @FXML
    public MFXTextField vehicleNumber;
    @FXML
    public Text vehicleErrorText;
    @FXML
    public MFXDatePicker departureDate;
    @FXML
    public MFXTextField departureHour;
    @FXML
    public MFXTextField departureMinute;
    @FXML
    public Text departureErrorText;
    @FXML
    public MFXButton enterParkingLotBtn;
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
    }
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void enterParkingLotBtnClickHandler (ActionEvent event) {
        String emailText = email.getText();
        emailErrorText.setText("");
        if (emailText.isEmpty()) {
            emailErrorText.setText("Please enter your email.");
            return;
        }
        if (!(emailText.contains("@") && emailText.contains("."))) {
            emailErrorText.setText("Please enter a valid email.");
            return;
        }
        String vehicleNumberText = vehicleNumber.getText();
        vehicleErrorText.setText("");
        if (vehicleNumberText.isEmpty()) {
            vehicleErrorText.setText("Please enter your vehicle number.");
            return;
        }
        if (!vehicleNumberText.matches("\\d+")) {
            vehicleErrorText.setText("Vehicle number can only contain numbers.");
            return;
        }
        if (vehicleNumberText.length() < 8) {
            vehicleErrorText.setText("Vehicle number must be 8 digits long.");
            return;
        }
        LocalDate departureDateValue = departureDate.getValue();
        departureErrorText.setText("");
        if (departureDateValue == null) {
            departureErrorText.setText("Please enter estimated departure date.");
            return;
        }
        if (departureDateValue.isBefore(LocalDate.now())) {
            departureErrorText.setText("Departure time cannot be in the past.");
            return;
        }
        if (departureDateValue.isAfter(LocalDate.now().plusDays(14))) {
            departureErrorText.setText("Departure time cannot be more than 14 days from now.");
            return;
        }
        String departureHourValue = departureHour.getText();
        if (departureHourValue.isEmpty()) {
            departureErrorText.setText("Please enter an estimated departure hour.");
            return;
        }
        if (!departureHourValue.matches("\\d+")) {
            departureErrorText.setText("Departure hour can only contain numbers.");
            return;
        }
        int departureHourInt = Integer.parseInt(departureHourValue);
        if (departureHourInt < 0 || departureHourInt > 23) {
            departureErrorText.setText("Departure hour must be between 0 and 23.");
            return;
        }
        String departureMinuteValue = departureMinute.getText();
        if (departureMinuteValue.isEmpty()) {
            departureErrorText.setText("Please enter an estimated departure minute.");
            return;
        }
        if (!departureMinuteValue.matches("\\d+")) {
            departureErrorText.setText("Departure minute can only contain numbers.");
            return;
        }
        int departureMinuteInt = Integer.parseInt(departureMinuteValue);
        if (departureMinuteInt < 0 || departureMinuteInt > 59) {
            departureErrorText.setText("Departure minute must be between 0 and 59.");
            return;
        }
        Calendar departureCalendar = Calendar.getInstance();
        departureCalendar.set(departureDateValue.getYear(), departureDateValue.getMonthValue() - 1, departureDateValue.getDayOfMonth(), departureHourInt, departureMinuteInt);
        
        loader.show();
        CPSClient.sendRequestToServer(RequestType.GET, Entities.CUSTOMER.getTableName() + "/email=" + emailText, null, (req, res) -> {
            try {
                if (res.getStatus() == ResponseStatus.SUCCESS) {
                    currentCustomer = ((ArrayList<Customer>) res.getData()).get(0);
                }
                else {
                    currentCustomer = new Customer(emailText, "", "", "");
                }
                
                Vehicle vehicle = new Vehicle(vehicleNumberText, currentCustomer);
                currentReservation = createReservationObject(currentCustomer, vehicle, departureCalendar);
            }
            catch (Throwable e) {
                e.printStackTrace();
            }
        });
    }
    
    
    /* ----- Event Bus Listeners ------------------------------------ */
    
    // ...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    
    @RequestCallback.Method
    private void onCreateReservation (RequestMessage request, ResponseMessage response) {
        Vehicle vehicle = currentVehicle;
        if (App.allVehicles.stream().anyMatch(v -> v.getNumber().equals(currentVehicle.getNumber()))) {
            vehicle = App.allVehicles.stream().filter(v -> v.getNumber().equals(currentVehicle.getNumber())).findFirst().get();
        }
        else {
            App.allVehicles.add(vehicle);
        }
        currentVehicle = vehicle;
        
        if (response.getStatus() == ResponseStatus.FINISHED) {
            currentReservation.setId((Integer) response.getData());
            Boolean result = parkingLot.insertVehicle(currentReservation);
            if (!result) {
                System.out.println("Error: parking lot is full");
                return;
            }
            
            CPSClient.sendRequestToServer(RequestType.UPDATE, Entities.PARKING_SPACE.getTableName(), null, parkingLot.getParkingSpaces(), this::onUpdateParkingLot);
        }
    }
    
    @RequestCallback.Method
    private void onUpdateParkingLot (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.FINISHED) {
            Platform.runLater(() -> {
                loader.hide();
                dialog.clear();
                
                dialog.setTitleText("Enter Parking Lot");
                dialog.setBodyText("Your reservation has been created successfully.", " ", "You can now leave your vehicle in the parking lot entrance, and our smart robot will take care of the rest.", "Thank you for choosing us!");
                MFXButton okBtn = new MFXButton("OK");
                okBtn.getStyleClass().add("button-primary-filled");
                okBtn.setOnAction(event -> {
                    dialog.close();
                    try {
                        App.setPage("kiosk/KioskExit.fxml");
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                dialog.setActionButtons(okBtn);
                dialog.open();
            });
        }
    }
    
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    private @Nullable Reservation createReservationObject (Customer customer, Vehicle vehicle, Calendar departureTime) {
        try {
            vehicle = new Vehicle(vehicle.getNumber(), customer);
            Vehicle finalVehicle = vehicle;
            
            CPSClient.sendRequestToServer(RequestType.CREATE, Entities.VEHICLE.getTableName(), null, vehicle, (req, res) -> {
                if (res.getStatus() == ResponseStatus.FINISHED) {
                    finalVehicle.setId((Integer) res.getData());
                }
                else {
                    switch (finalVehicle.getNumber()) {
                        case "12345678" -> finalVehicle.setId(1);
                        case "87654321" -> finalVehicle.setId(2);
                        case "14725836" -> finalVehicle.setId(3);
                        case "96385274" -> finalVehicle.setId(4);
                        case "25836974" -> finalVehicle.setId(5);
                        case "74185296" -> finalVehicle.setId(6);
                        case "85274196" -> finalVehicle.setId(7);
                        default -> finalVehicle.setId(8);
                    }
                }
                currentVehicle = finalVehicle;
                currentReservation.getVehicle().setId(currentVehicle.getId());
                
                CPSClient.sendRequestToServer(RequestType.CREATE, Entities.RESERVATION.getTableName(), null, currentReservation, this::onCreateReservation);
            });
            
            Calendar arrivalTime = Calendar.getInstance();
            Reservation reservation = new Reservation(parkingLot, customer, vehicle, arrivalTime, departureTime);
            currentReservation = reservation;
            return reservation;
        }
        catch (Throwable e) {
            return null;
        }
    }
}
