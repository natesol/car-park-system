package net.cps.client.controllers.kiosk;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.cps.client.App;
import net.cps.client.utils.AbstractKioskPageController;
import net.cps.client.utils.AbstractPageController;
import net.cps.common.entities.ParkingLot;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class KioskSubscriptionsController extends AbstractKioskPageController implements Initializable {
    @FXML
    public MFXTextField email;
    @FXML
    public MFXTextField vehicleNumber;
    @FXML
    public MFXTextField subscriptionNumber;
    @FXML
    public MFXButton enterSubscriptionBtn;
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
    }
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void enterSubscriptionBtnClickHandler (ActionEvent event) throws IOException {
        dialog.setTitleText("Enter Subscription");
        dialog.setBodyText("You have successfully entered the subscription.");
        dialog.open();
    }
    
    
    /* ----- Event Bus Listeners ------------------------------------ */
    
    // ...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    // ...
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    // ...
    
}
