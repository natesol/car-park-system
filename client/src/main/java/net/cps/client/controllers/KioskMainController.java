package net.cps.client.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.cps.client.App;
import net.cps.client.utils.ResourcesLoader;
import net.cps.common.entities.ParkingLot;
import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class KioskMainController extends PageController implements Initializable {
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
    public Text parkingLotText;
    
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        //EventBus.getDefault().register(this);
        Platform.runLater(() -> {
            try {
                setSubPage("KioskMainHome.fxml");
                parkingLotText.setText(((ParkingLot) App.entity).getName());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    
    
    // ----- Action Handlers Methods ----------------------
    
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
    
    @FXML
    public void homeBtnClickHandler (ActionEvent actionEvent) {
        Platform.runLater(() -> {
            try {
                activateMenuBtn(homeBtn);
                setSubPage("KioskMainHome.fxml");
                parkingLotText.setText(((ParkingLot) App.entity).getName());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    
    @FXML
    public void subscriptionBtnClickHandler (ActionEvent actionEvent) {
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
        try {
            setSubPage("KioskMainBookNow.fxml");
            activateMenuBtn(bookNowBtn);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
