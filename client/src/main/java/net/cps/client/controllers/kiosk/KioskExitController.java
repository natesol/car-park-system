package net.cps.client.controllers.kiosk;

import io.github.palexdev.materialfx.controls.MFXButton;
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
import net.cps.common.utils.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class KioskExitController extends AbstractKioskPageController implements Initializable {
    Customer currentCustomer = null;
    Vehicle currentVehicle = null;
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
        CPSClient.sendRequestToServer(RequestType.GET, Entities.CUSTOMER.getTableName() + "/email=" + emailText, null, (req, res) -> {
            try {
                if (res.getStatus() == ResponseStatus.SUCCESS) {
                    currentCustomer = ((ArrayList<Customer>) res.getData()).get(0);
                }
                else {
                    currentCustomer = new Customer(emailText, "", "", "");
                }
                
                for (Vehicle vehicle : App.allVehicles) {
                    if (vehicle.getNumber().equals(vehicleNumberText)) {
                        currentVehicle = vehicle;
                        break;
                    }
                }
    
                if (parkingLot.removeVehicle(currentVehicle)) {
                    for (Reservation reservation : parkingLotReservations) {
                        if (reservation.getCustomer().getEmail().equals(currentCustomer.getEmail()) && reservation.getStatus() == ReservationStatus.CHECKED_IN) {
                            currentReservation = reservation;
                            break;
                        }
                    }
                    CPSClient.sendRequestToServer(RequestType.UPDATE, Entities.PARKING_SPACE.getTableName(), null, parkingLot.getParkingSpaces(), this::onUpdateParkingLot);
                }
                else {
                    Platform.runLater(() -> {
                        loader.hide();
                        dialog.clear();
                        dialog.setTitleText("Exit Parking Lot");
                        dialog.setBodyText("It seems that something went wrong. Please try again.");
                        dialog.open();
                    });
                }
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
    private void onGetReservations (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.SUCCESS) {
            System.out.println("onGetReservations");
            ArrayList<Reservation> reservations = (ArrayList<Reservation>) response.getData();
            parkingLotReservations = new ArrayList<>(reservations.stream().filter(reservation -> reservation.getParkingLot().getName().equals(parkingLot.getName())).toList());
            parkingLot.setReservations(parkingLotReservations);
        }
    }
    
    @RequestCallback.Method
    private void onUpdateParkingLot (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.FINISHED) {
            
            currentReservation.setStatus(ReservationStatus.CHECKED_OUT);
            CPSClient.sendRequestToServer(RequestType.UPDATE, Entities.RESERVATION.getTableName(), null, currentVehicle, (req, res) -> {
            
                if (res.getStatus() == ResponseStatus.FINISHED) {
                    Platform.runLater(() -> {
                        loader.hide();
                        dialog.clear();
        
                        dialog.setTitleText("Exit Parking Lot");
                        dialog.setBodyText("Your vehicle has been successfully exited from the parking lot.", "You can now leave the parking lot.", "Thank you for using our service.");
                        MFXButton okBtn = new MFXButton("OK");
                        okBtn.getStyleClass().add("button-primary-filled");
                        okBtn.setOnAction(event -> {
                            dialog.close();
                        });
                        dialog.setActionButtons(okBtn);
                        dialog.open();
                    });
                }
            });
        }
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    // ...
    
}
