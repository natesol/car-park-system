package net.cps.client.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.enums.FloatMode;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import net.cps.client.App;
import net.cps.client.CPSClient;
import net.cps.common.entities.ParkingLot;
import javafx.collections.ObservableList;
import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.Entities;
import net.cps.common.utils.RequestCallback;
import net.cps.common.utils.RequestType;
import net.cps.common.utils.ResponseStatus;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;


public class IndexController extends PageController {
    @FXML
    public MFXButton kioskBtn;
    @FXML
    public MFXButton pcAppBtn;
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {}
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    void kioskBtnClickHandler (ActionEvent event) throws IOException {
        CPSClient.sendRequestToServer(RequestType.GET, Entities.PARKING_LOT.getTableName(), this::onGetAllParkingLot);
    }
    
    @FXML
    public void pcAppBtnClickHandler (ActionEvent event) throws IOException {
        App.setPage("PCLogin.fxml");
    }
    
    
    /* ----- EventBus Listeners ------------------------------------- */
    
    // ...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    @RequestCallback.Method
    private void onGetAllParkingLot (RequestMessage request, ResponseMessage response) {
        ObservableList<ParkingLot> parkingLots = FXCollections.observableArrayList((List<ParkingLot>) response.getData());
        
        Platform.runLater(() -> {
            if (response.getStatus() == ResponseStatus.SUCCESS) {
                // Create the filter combo-box and set its items.
                MFXFilterComboBox<ParkingLot> filterCombo = new MFXFilterComboBox<>();
                filterCombo.setFloatMode(FloatMode.DISABLED);
                filterCombo.setPrefWidth(400);
                StringConverter<ParkingLot> converter = FunctionalStringConverter.to(parkingLot -> (parkingLot == null) ? "" : parkingLot.getName());
                Function<String, Predicate<ParkingLot>> filterFunction = str -> parkingLot -> StringUtils.containsIgnoreCase(converter.toString(parkingLot), str);
                filterCombo.setItems(parkingLots);
                filterCombo.setValue(parkingLots.get(0));
                filterCombo.setConverter(converter);
                filterCombo.setFilterFunction(filterFunction);
                
                // Create a HBox as wrapper to hold the filter combo-box.
                HBox wrapper = new HBox();
                wrapper.getChildren().add(filterCombo);
                
                // Create the dialog buttons.
                MFXButton confirmBtn = new MFXButton("Confirm");
                confirmBtn.getStyleClass().add("button-primary");
                confirmBtn.setOnAction(actionEvent -> {
                    try {
                        App.setEntity(filterCombo.getValue());
                        App.setPage("KioskMain.fxml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                MFXButton cancelBtn = new MFXButton("Cancel");
                cancelBtn.getStyleClass().add("button-secondary");
                cancelBtn.setOnAction(actionEvent -> dialog.close());
    
                // Set the dialog content and open it.
                dialog.setTitleText("Open Kiosk App");
                dialog.setBodyText("Choose which parking lot you want to open the app for.");
                dialog.setCustomContent(wrapper);
                dialog.setActionButtons(cancelBtn, confirmBtn);
                dialog.open();
            }
            else {
                System.out.println("failed to get all parking lots from the server.");
            }
        });
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    // ...
    
}
