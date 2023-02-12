package net.cps.client.controllers.kiosk;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import net.cps.client.utils.AbstractKioskPageController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class KioskReservationsController extends AbstractKioskPageController implements Initializable {
    @FXML
    public MFXTextField email;
    @FXML
    public MFXTextField vehicleNumber;
    @FXML
    public MFXButton enterReservationBtn;
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
    }
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void enterReservationBtnClickHandler (ActionEvent event) throws IOException {
        dialog.setTitleText("Enter Reservation");
        dialog.setBodyText("You have successfully entered the reservation.");
        dialog.open();
    }
    
    
    /* ----- Event Bus Listeners ------------------------------------ */
    
    // ...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    // ...
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    // ...
    
}
