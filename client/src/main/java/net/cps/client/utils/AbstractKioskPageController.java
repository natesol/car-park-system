package net.cps.client.utils;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import net.cps.client.App;
import net.cps.client.events.KioskEnterEvent;
import net.cps.common.entities.ParkingLot;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;


public abstract class AbstractKioskPageController extends AbstractPageController {
    protected ParkingLot parkingLot;
    
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
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        
        EventBus.getDefault().register(this);
        parkingLot = (ParkingLot) App.getEntity();
        
        if (parkingLot != null) {
            Platform.runLater(() -> {
                parkingLotNameTitle.setText(parkingLot.getName());
            });
        }
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
    
    @Subscribe
    public void onKioskEnterEvent (KioskEnterEvent event) {
        this.parkingLot = event.getParkingLot();
        App.setEntity(parkingLot);
        
        Platform.runLater(() -> {
            parkingLotNameTitle.setText(parkingLot.getName());
        });
    }
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    // ...
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    // ...
}
