package net.cps.client.controllers.pc.guest;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import net.cps.client.utils.AbstractPCGuestPageController;
import net.cps.common.entities.ParkingLot;

import java.net.URL;
import java.util.ResourceBundle;


public class PCGuestReservationsController extends AbstractPCGuestPageController implements Initializable {
    @FXML
    public MFXTextField emailField;
    @FXML
    public MFXTextField vehicleField;
    @FXML
    public MFXComboBox<ParkingLot> parkingLotCombo;
    @FXML
    public MFXDatePicker arrivalField;
    @FXML
    public MFXButton submitBtn;
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {}
    
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    // ...
    
    
    /* ----- EventBus Listeners ------------------------------------- */
    
    // ...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    // ...
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    // ...
    
}