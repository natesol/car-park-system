package net.cps.client.controllers.kiosk;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.cps.client.App;
import net.cps.client.utils.AbstractPageController;
import net.cps.client.utils.ResourcesLoader;
import net.cps.common.entities.ParkingLot;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class KioskMainController extends AbstractPageController implements Initializable {

    private ParkingLot parkingLot;
    
    @FXML
    public VBox kioskMenu;
    @FXML
    public MFXButton homeBtn;
    @FXML
    public MFXButton subscriptionBtn;
    @FXML
    public MFXButton reservationBtn;
    @FXML
    public MFXButton bookNowBtn;
    @FXML
    public VBox subPageWrapper;
    
    /* Home Sub Page */
    @FXML
    public Text parkingLotName;
    
    /* Subscription Sub Page */
    @FXML
    public MFXButton enterSubscriptionBtn;
    @FXML
    public MFXTextField vehicleNumber;
    @FXML
    public MFXTextField subscriptionNumber;
    
    /* Reservation Sub Page */
    @FXML
    public MFXButton enterReservationBtn;
    
    /* Book Now Sub Page */
    @FXML
    public MFXTextField email;
    @FXML
    public MFXTextField departureHour;
    @FXML
    public MFXTextField departureMinute;
    
    
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        parkingLot = (ParkingLot) App.getEntity();
        
        Platform.runLater(() -> {
            try {
                setSubPage("KioskMainHome.fxml");
                parkingLotName.setText(parkingLot.getName());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void homeBtnClickHandler (ActionEvent actionEvent) {
        if (homeBtn.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                activateMenuBtn(homeBtn);
                setSubPage("KioskMainHome.fxml");
                parkingLotName.setText(parkingLot.getName());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    
    @FXML
    public void subscriptionBtnClickHandler (ActionEvent actionEvent) {
        if (subscriptionBtn.getStyleClass().contains("active")) return;
        
        try {
            setSubPage("KioskMainSubscription.fxml");
            activateMenuBtn(subscriptionBtn);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void reservationBtnClickHandler (ActionEvent actionEvent) {
        if (reservationBtn.getStyleClass().contains("active")) return;
        
        try {
            setSubPage("KioskMainReservation.fxml");
            activateMenuBtn(reservationBtn);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void bookNowBtnClickHandler (ActionEvent actionEvent) {
        if (bookNowBtn.getStyleClass().contains("active")) return;
        
        try {
            setSubPage("KioskMainBookNow.fxml");
            activateMenuBtn(bookNowBtn);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /* ----- EventBus Listeners ------------------------------------- */
    
    // ...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    // ...
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    public void setSubPage (String fxml) throws IOException {
        Platform.runLater(() -> {
            try {
                subPageWrapper.getChildren().clear();
                subPageWrapper.getChildren().add(ResourcesLoader.loadFXML(fxml));
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    public void setSubPage (Pane pane) {
        Platform.runLater(() -> {
            subPageWrapper.getChildren().clear();
            subPageWrapper.getChildren().add(pane);
        });
    }
    
    public void activateMenuBtn (@NotNull MFXButton btn) {
        Platform.runLater(() -> {
            kioskMenu.lookupAll(".kiosk-menu-button.active").forEach(node -> node.getStyleClass().remove("active"));
            btn.getStyleClass().add("active");
        });
    }
}
