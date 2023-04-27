package net.cps.client.controllers.kiosk;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import net.cps.client.utils.AbstractKioskPageController;

import java.net.URL;
import java.util.ResourceBundle;


public class KioskHomeController extends AbstractKioskPageController implements Initializable {
    @FXML
    public Text parkingLotName;
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        
        Platform.runLater(() -> {
            parkingLotName.setText(parkingLot.getName());
        });
    }
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    // ...
    
    
    /* ----- Event Bus Listeners ------------------------------------ */
    
    // ...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    // ...
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    // ...
    
}
