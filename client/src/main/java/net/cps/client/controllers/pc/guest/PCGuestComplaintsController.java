package net.cps.client.controllers.pc.guest;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import net.cps.client.utils.AbstractPCGuestPageController;

import java.net.URL;
import java.util.ResourceBundle;


public class PCGuestComplaintsController extends AbstractPCGuestPageController implements Initializable {
    @FXML
    public MFXButton fileComplaintBtn;
    @FXML
    public MFXTextField emailField;
    @FXML
    public MFXTextField complaintField;
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