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
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;


public class KioskBookNowController extends AbstractKioskPageController implements Initializable {
    Reservation reservation = null;
    Customer customer = null;
    
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
        
        CPSClient.sendRequestToServer(RequestType.GET, Entities.CUSTOMER.getTableName() + "/email=" + emailText, null, (req, res) -> {
            loader.show();
            try {
                
                if (res.getStatus() == ResponseStatus.SUCCESS) {
                    customer = ((ArrayList<Customer>) res.getData()).get(0);
                }
                else {
                    customer = new Customer(emailText, "", "", "");
                }
                
                Vehicle vehicle = new Vehicle(vehicleNumberText, customer);
                reservation = createReservationObject(customer, vehicle, departureCalendar);
                
                CPSClient.sendRequestToServer(RequestType.CREATE, Entities.RESERVATION.getTableName(), null, reservation, this::onCreateReservation);
            }
            catch (Throwable e) {
                e.printStackTrace();
            }
        });
    }
    
    
    /* ----- Event Bus Listeners ------------------------------------ */
    
    // ...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    
    private void onCreateReservation (RequestMessage request, ResponseMessage response) {
        System.out.println("onCreateReservation");
        
        if (response.getStatus() == ResponseStatus.FINISHED) {
            parkingLot.insertVehicle(reservation);
            
            CPSClient.sendRequestToServer(RequestType.UPDATE, Entities.PARKING_SPACE.getTableName(), null, parkingLot.getParkingSpaces(), this::onUpdateParkingLot);
        }
    }
    
    @RequestCallback.Method
    private void onUpdateParkingLot (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.FINISHED) {
            
            System.out.println("onUpdateParkingLot");
            
            Platform.runLater(() -> {
                loader.hide();
                dialog.clear();
                
                dialog.setTitleText("Enter Parking Lot");
                dialog.setBodyText("Your reservation has been created successfully.", "You can now leave your vehicle in the parking lot entrance, and our smart robot will take care of the rest.", "Thank you for choosing us!");
                MFXButton okBtn = new MFXButton("OK");
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
    
    //private @Nullable Reservation createReservationObject (Customer customer, Vehicle vehicle, Calendar departureTime) {
    //    try {
    //        ParkingLot parkingLot = parkingLotsListCombo.getSelectionModel().getSelectedItem();
    //        String vehicleNumber = vehicleNumberField.getText();
    //        Vehicle vehicle = allCustomerVehicles.stream().filter(v -> v.getNumber().equals(vehicleNumber)).findFirst().orElse(null);
    //        boolean isNewVehicle = vehicle == null;
    //        if (isNewVehicle) {
    //            vehicle = new Vehicle(vehicleNumber, customer);
    //            Vehicle finalVehicle = vehicle;
    //            CPSClient.sendRequestToServer(RequestType.CREATE, Entities.VEHICLE.getTableName(), null, vehicle, (req, res) -> {
    //                if (res.getStatus() == ResponseStatus.FINISHED) {
    //                    finalVehicle.setId((Integer) res.getData());
    //                    allCustomerVehicles.add(finalVehicle);
    //                }
    //            });
    //        }
    //        Calendar arrivalTime = Calendar.getInstance();
    //        arrivalTime.setTime(Date.from(arrivalDate.getValue().atTime(Integer.parseInt(arrivalTimeHourField.getText()), Integer.parseInt(arrivalTimeMinutesField.getText())).atZone(ZoneId.systemDefault()).toInstant()));
    //        Calendar departureTime = Calendar.getInstance();
    //        departureTime.setTime(Date.from(departureDate.getValue().atTime(Integer.parseInt(departureTimeHourField.getText()), Integer.parseInt(departureTimeMinutesField.getText())).atZone(ZoneId.systemDefault()).toInstant()));
    //
    //        return new Reservation(parkingLot, customer, vehicle, arrivalTime, departureTime);
    //    }
    //    catch (Throwable e) {
    //        return null;
    //    }
    //}
    
    private @NotNull Reservation createReservationObject (Customer customer, Vehicle vehicle, Calendar departureTime) {
        Reservation reservation = new Reservation(parkingLot, customer, vehicle, Calendar.getInstance(), departureTime);
        return reservation;
    }
    
}
