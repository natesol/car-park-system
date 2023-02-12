package net.cps.client.controllers.kiosk;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import net.cps.client.utils.AbstractKioskPageController;

import java.net.URL;
import java.util.ResourceBundle;


public class KioskBookNowController extends AbstractKioskPageController implements Initializable {
    @FXML
    public MFXTextField email;
    @FXML
    public MFXTextField vehicleNumber;
    @FXML
    public MFXDatePicker departureDate;
    @FXML
    public MFXTextField departureHour;
    @FXML
    public MFXTextField departureMinute;
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
        dialog.setTitleText("Enter Parking Lot");
        dialog.setBodyText("You have successfully entered the parking lot.");
        dialog.open();
    }
    
    /* ----- Event Bus Listeners ------------------------------------ */
    
    // ...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    // ...
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    // ...
    
}
