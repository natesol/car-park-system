package net.cps.client.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import net.cps.client.App;
import net.cps.client.CPSClient;
import net.cps.client.events.KioskEnterEvent;
import net.cps.common.entities.ParkingLot;
import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.Entities;
import net.cps.common.utils.RequestCallback;
import net.cps.common.utils.RequestType;
import net.cps.common.utils.ResponseStatus;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class IndexController implements Initializable {
    @FXML
    public AnchorPane root;
    @FXML
    public VBox body;
    @FXML
    public MFXButton kioskBtn;
    @FXML
    public MFXButton pcAppBtn;
    @FXML
    public MFXComboBox<ParkingLot> parkingLotsCombo;
    @FXML
    public MFXButton toggleThemeBtn;
    @FXML
    public Tooltip toggleThemeBtnTip;
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        Platform.runLater(App::updateAppTheme);
        CPSClient.sendRequestToServer(RequestType.GET, Entities.PARKING_LOT.getTableName(), this::onGetAllParkingLot);
    }
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    void kioskBtnClickHandler (ActionEvent event) throws IOException {
        App.setEntity(parkingLotsCombo.getValue());
        App.setPage("kiosk/KioskHome.fxml");
        EventBus.getDefault().post(new KioskEnterEvent((ParkingLot) App.getEntity()));
    }
    
    @FXML
    public void pcAppBtnClickHandler (ActionEvent event) throws IOException {
        App.setPage("pc/auth/PCLogin.fxml");
    }
    
    @FXML
    public void toggleThemeBtnClickHandler (ActionEvent actionEvent) {
        Platform.runLater(App::toggleTheme);
    }
    
    
    /* ----- EventBus Listeners ------------------------------------- */
    
    // ...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    @RequestCallback.Method
    private void onGetAllParkingLot (RequestMessage request, ResponseMessage response) {
        ObservableList<ParkingLot> parkingLots = FXCollections.observableArrayList((List<ParkingLot>) response.getData());
        
        Platform.runLater(() -> {
            if (response.getStatus() == ResponseStatus.SUCCESS) {
                StringConverter<ParkingLot> converter = FunctionalStringConverter.to(parkingLot -> (parkingLot == null) ? "null" : parkingLot.getName());
                parkingLotsCombo.setConverter(converter);
                parkingLotsCombo.setItems(parkingLots);
                parkingLotsCombo.setItems(parkingLots);
                parkingLotsCombo.setValue(parkingLots.get(0));
                parkingLotsCombo.setValue(parkingLots.get(0));
            }
            else {
                System.out.println("failed to get all parking lots from the server.");
            }
        });
    }
    
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    // ...
    
}
