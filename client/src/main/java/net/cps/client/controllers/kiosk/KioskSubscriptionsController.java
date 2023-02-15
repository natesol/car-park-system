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
import net.cps.common.entities.Subscription;
import net.cps.common.entities.Vehicle;
import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.*;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;


public class KioskSubscriptionsController extends AbstractKioskPageController implements Initializable {
    Subscription currentSubscription = null;
    String currentSubscriptionNumber = null;
    Vehicle currentVehicle = null;
    String currentVehicleNumber = null;
    Customer currentCustomer = null;
    Reservation currentReservation = null;
    
    @FXML
    public MFXTextField subscriptionNumber;
    @FXML
    public Text subscriptionErrorText;
    @FXML
    public Text vehicleErrorText;
    @FXML
    public MFXTextField vehicleNumber;
    @FXML
    public MFXButton enterParkingLotBtn;
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
    }
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void enterParkingLotBtnClickHandler (ActionEvent event) throws IOException {
        String subscriptionNumberText = subscriptionNumber.getText();
        subscriptionErrorText.setText("");
        if (subscriptionNumberText.isEmpty()) {
            subscriptionErrorText.setText("Please enter your subscription number.");
            return;
        }
        if (!subscriptionNumberText.matches("[0-9]+")) {
            subscriptionErrorText.setText("Please enter a valid subscription number.");
            return;
        }
        String vehicleNumberText = vehicleNumber.getText();
        vehicleErrorText.setText("");
        if (vehicleNumberText.isEmpty()) {
            vehicleErrorText.setText("Please enter your vehicle number.");
            return;
        }
        if (!vehicleNumberText.matches("[0-9]+") || vehicleNumberText.length() != 8) {
            vehicleErrorText.setText("Please enter a valid vehicle number.");
            return;
        }
        
        loader.show();
        
        currentSubscriptionNumber = subscriptionNumberText;
        currentVehicleNumber = vehicleNumberText;
        
        CPSClient.sendRequestToServer(RequestType.GET, Entities.SUBSCRIPTION.getTableName() + "/" + currentSubscriptionNumber, this::onGetSubscription);
    }
    
    
    
    /* ----- Event Bus Listeners ------------------------------------ */
    
    // ...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    @RequestCallback.Method
    private void onGetSubscription (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.SUCCESS && response.getData() != null) {
            currentSubscription = (Subscription) response.getData();
            
            if (currentSubscription.getVehicles().stream().noneMatch(vehicle -> vehicle.getNumber().equals(currentVehicleNumber))) {
                Platform.runLater(() -> {
                    loader.hide();
                    
                    dialog.clear();
                    dialog.setTitleText("Error");
                    dialog.setBodyText("It seems that the vehicle number you entered does not match the subscription number you entered.", "Please check your subscription number and vehicle number and try again.");
                    MFXButton okBtn = new MFXButton("OK");
                    okBtn.getStyleClass().add("button-base-filled");
                    okBtn.setOnAction(event -> {
                        dialog.close();
                    });
                    
                    dialog.setActionButtons(okBtn);
                    dialog.open();
                });
                return;
            }
            
            currentVehicle = currentSubscription.getVehicles().stream().filter(vehicle -> vehicle.getNumber().equals(currentVehicleNumber)).findFirst().get();
            currentCustomer = currentSubscription.getCustomer();
            currentReservation = createReservation();
            
            if (!parkingLot.checkAvailability(currentReservation)) {
                Platform.runLater(() -> {
                    loader.hide();
                    
                    dialog.clear();
                    dialog.setTitleText("Parking Lot Full");
                    dialog.setBodyText("It seems that the parking lot is full right now.", "Please try again later.");
                    MFXButton okBtn = new MFXButton("OK");
                    okBtn.getStyleClass().add("button-base-filled");
                    okBtn.setOnAction(event -> {
                        dialog.close();
                    });
                    
                    dialog.setActionButtons(okBtn);
                    dialog.open();
                });
                return;
            }
            
            CPSClient.sendRequestToServer(RequestType.CREATE, Entities.RESERVATION.getTableName(), null, currentReservation, this::onCreateReservation);
    
            System.out.println("!!!!!!!!!!!!!!!!!!!!!");
        }
        else {
            Platform.runLater(() -> {
                loader.hide();
                
                dialog.clear();
                dialog.setTitleText("Error");
                if (response.getStatus() == ResponseStatus.NOT_FOUND) {
                    dialog.setBodyText("It seems that the subscription number you entered does not exist in our system.", "Please check your subscription number and try again.");
                }
                else {
                    dialog.setBodyText("An error occurred while trying to get your subscription information.", "Please try again later.");
                }
                MFXButton okBtn = new MFXButton("OK");
                okBtn.getStyleClass().add("button-base-filled");
                okBtn.setOnAction(event -> {
                    dialog.close();
                });
                
                dialog.setActionButtons(okBtn);
                dialog.open();
            });
        }
    }
    
    @RequestCallback.Method
    private void onCreateReservation (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.FINISHED) {
            currentReservation.setId((int) response.getData());
            parkingLot.addReservation(currentReservation);
            
            CPSClient.sendRequestToServer(RequestType.UPDATE, Entities.PARKING_LOT.getTableName(), null, parkingLot, this::onUpdateParkingLot);
        }
        else {
            Platform.runLater(() -> {
                loader.hide();
                
                dialog.clear();
                dialog.setTitleText("Something Went Wrong");
                dialog.setBodyText("An error occurred while trying to create your reservation.", "Please try again later.");
                MFXButton okBtn = new MFXButton("OK");
                okBtn.getStyleClass().add("button-base-filled");
                okBtn.setOnAction(event -> {
                    dialog.close();
                });
                
                dialog.setActionButtons(okBtn);
                dialog.open();
            });
        }
    }
    
    @RequestCallback.Method
    private void onUpdateParkingLot (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.FINISHED) {
            Platform.runLater(() -> {
                loader.hide();
                
                dialog.clear();
                dialog.setTitleText("Parking Lot Entry");
                dialog.setBodyText("Please leave your vehicle in the parking lot entrance, and our smart robot will take care of the rest.", "Have a nice day!");
                MFXButton okBtn = new MFXButton("Enter");
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
        else {
            Platform.runLater(() -> {
                loader.hide();
                
                dialog.clear();
                dialog.setTitleText("Something Went Wrong");
                dialog.setBodyText("An error occurred while trying to enter the parking lot.", "Please try again later.");
                MFXButton okBtn = new MFXButton("OK");
                okBtn.getStyleClass().add("button-base-filled");
                okBtn.setOnAction(event -> {
                    dialog.close();
                });
                
                dialog.setActionButtons(okBtn);
                dialog.open();
            });
        }
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    private Reservation createReservation () {
        Calendar arrivalTime = Calendar.getInstance();
        Calendar departureTime = Calendar.getInstance();
        departureTime.add(Calendar.HOUR, currentSubscription.getDepartureTime().getHour());
        departureTime.add(Calendar.MINUTE, currentSubscription.getDepartureTime().getMinute());
        departureTime.add(Calendar.SECOND, currentSubscription.getDepartureTime().getSecond());
        
        return new Reservation(parkingLot, currentCustomer, currentVehicle, arrivalTime, departureTime, ReservationStatus.PENDING, 0.0);
    }
}
