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
import net.cps.client.utils.AbstractPageController;
import net.cps.common.entities.ParkingLot;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class KioskSubscriptionsController extends AbstractPageController implements Initializable {
    private ParkingLot parkingLot;
    
    @FXML
    public Text parkingLotNameTitle;
    
    @FXML
    public VBox kioskMenu;
    @FXML
    public MFXButton homeMenuBtn;
    @FXML
    public MFXButton bookNowMenuBtn;
    @FXML
    public MFXButton subscriptionMenuBtn;
    @FXML
    public MFXButton reservationMenuBtn;
    @FXML
    public MFXButton exitMenuBtn;
    
    
    public MFXTextField email;
    public MFXTextField vehicleNumber;
    public MFXTextField subscriptionNumber;
    public MFXButton enterSubscriptionBtn;
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        parkingLot = (ParkingLot) App.getEntity();
        
        Platform.runLater(() -> {
            parkingLotNameTitle.setText(parkingLot.getName());
        });
    }
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void homeMenuBtnClickHandler (ActionEvent event) throws IOException {
        if (homeMenuBtn.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                App.setPage("kiosk/KioskHome.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    @FXML
    public void bookNowMenuBtnClickHandler (ActionEvent event) throws IOException {
        if (bookNowMenuBtn.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                App.setPage("kiosk/KioskBookNow.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    @FXML
    public void subscriptionMenuBtnClickHandler (ActionEvent event) throws IOException {
        if (subscriptionMenuBtn.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                App.setPage("kiosk/KioskSubscriptions.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    @FXML
    public void reservationMenuBtnClickHandler (ActionEvent event) throws IOException {
        if (reservationMenuBtn.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                App.setPage("kiosk/KioskReservations.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    @FXML
    public void exitMenuBtnClickHandler (ActionEvent event) throws IOException {
        if (exitMenuBtn.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                App.setPage("kiosk/KioskExit.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    
    
    /* ----- Event Bus Listeners ------------------------------------ */
    
    // ...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    // ...
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    // ...
}
