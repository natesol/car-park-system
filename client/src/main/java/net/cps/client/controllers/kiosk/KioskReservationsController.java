package net.cps.client.controllers.kiosk;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import net.cps.client.App;
import net.cps.client.CPSClient;
import net.cps.client.utils.AbstractKioskPageController;
import net.cps.common.entities.ParkingLot;
import net.cps.common.entities.Reservation;
import net.cps.common.entities.Robot;
import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;


public class KioskReservationsController extends AbstractKioskPageController implements Initializable {
    ArrayList<Reservation> parkingLotReservations = null;
    Reservation currentReservation = null;
    
    @FXML
    public MFXTextField email;
    @FXML
    public Text emailErrorText;
    @FXML
    public MFXTextField vehicleNumber;
    @FXML
    public Text vehicleErrorText;
    @FXML
    public MFXButton enterParkingLotBtn;
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        
        CPSClient.sendRequestToServer(RequestType.GET, Entities.RESERVATION.getTableName(), this::onGetReservations);
    }
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void enterParkingLotBtnClickHandler (ActionEvent event) throws IOException {
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
    
        loader.show();
        currentReservation = null;
        for (Reservation reservation : parkingLotReservations) {
            if (reservation.getCustomer().getEmail().equals(emailText) && reservation.getVehicleNumber().equals(vehicleNumberText) && reservation.getStatus() == ReservationStatus.PENDING) {
                if (currentReservation == null) {
                    currentReservation = reservation;
                }
                else if (currentReservation.getDepartureTime().after(reservation.getDepartureTime())) {
                    currentReservation = reservation;
                }
            }
        }
        
        if (currentReservation == null) {
            Platform.runLater(() -> {
                loader.hide();
                dialog.clear();
                dialog.setTitleText("Enter Reservation");
                dialog.setBodyText("It seems like that you don't have any pending reservation for this parking lot.", "Please check the details you entered and try again.");
                MFXButton okBtn = new MFXButton("OK");
                okBtn.getStyleClass().add("button-base-filled");
                okBtn.setOnAction(e -> {
                    dialog.close();
                });
                dialog.setActionButtons(okBtn);
                
                dialog.open();
            });
            return;
        }
        if (currentReservation.getArrivalTime().after(Calendar.getInstance().getTime())) {
            Platform.runLater(() -> {
                loader.hide();
                dialog.clear();
                dialog.setTitleText("Enter Reservation");
                dialog.setBodyText("Your next reservation is not yet due.", "Your next reservation is due at " + currentReservation.getDepartureTimeFormatted() + ".", "Please check your reservations and try again.");
                MFXButton okBtn = new MFXButton("OK");
                okBtn.getStyleClass().add("button-base-filled");
                okBtn.setOnAction(e -> {
                    dialog.close();
                });
                dialog.setActionButtons(okBtn);
                
                dialog.open();
            });
            return;
        }
        if (currentReservation.getDepartureTime().before(Calendar.getInstance().getTime())) {
            Platform.runLater(() -> {
                loader.hide();
                dialog.clear();
                dialog.setTitleText("Enter Reservation");
                dialog.setBodyText("Your reservation is already expired.", "Your reservation was due at " + currentReservation.getDepartureTimeFormatted() + ".", "Please check your reservations and try again.");
                MFXButton okBtn = new MFXButton("OK");
                okBtn.getStyleClass().add("button-base-filled");
                okBtn.setOnAction(e -> {
                    dialog.close();
                });
                dialog.setActionButtons(okBtn);
                
                dialog.open();
            });
            
            currentReservation.setStatus(ReservationStatus.CHECKED_OUT);
            currentReservation.setArrivalTime(Calendar.getInstance());
            currentReservation.setDepartureTime(Calendar.getInstance());
            CPSClient.sendRequestToServer(RequestType.UPDATE, Entities.RESERVATION.getTableName(), null, currentReservation, null);
            
            return;
        }
        if (!parkingLot.checkAvailability(currentReservation)) {
            Platform.runLater(() -> {
                loader.hide();
                dialog.clear();
                dialog.setTitleText("Enter Reservation");
                dialog.setBodyText("We are terribly sorry, but the parking lot is currently full.", "If we caused you any damage, please file a complaint, and we will do our best to compensate you.", "Thank you for your understanding.");
                MFXButton okBtn = new MFXButton("OK");
                okBtn.getStyleClass().add("button-base-filled");
                okBtn.setOnAction(e -> {
                    dialog.close();
                });
                dialog.setActionButtons(okBtn);
                
                dialog.open();
            });
        }
        
        parkingLot.insertVehicle(currentReservation);
        CPSClient.sendRequestToServer(RequestType.UPDATE, Entities.PARKING_SPACE.getTableName(), null, parkingLot.getParkingSpaces(), this::onUpdateParkingLot);
    }
    
    
    /* ----- Event Bus Listeners ------------------------------------ */
    
    // ...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    @RequestCallback.Method
    private void onGetReservations (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.SUCCESS) {
            ArrayList<Reservation> reservations = (ArrayList<Reservation>) response.getData();
            parkingLotReservations = new ArrayList<>(reservations.stream().filter(reservation -> reservation.getParkingLot().getName().equals(parkingLot.getName())).toList());
            parkingLot.setReservations(parkingLotReservations);
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
    
    // ...
    
}
