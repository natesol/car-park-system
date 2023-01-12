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
    private MFXButton kioskBtn;
    @FXML
    public MFXButton pcAppBtn;
    
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        //EventBus.getDefault().register(this);
    }
    
    
    /* ----- Event Handlers ----------------------------------------- */
    
    @FXML
    void kioskBtnClickHandler (ActionEvent event) throws IOException {
        CPSClient.sendRequestToServer(RequestType.GET, Entities.PARKING_LOT.getTableName(), null, "get all parking lots from the server.", this::onGetAllParkingLot);
    }
    
    @FXML
    public void pcAppBtnClickHandler (ActionEvent event) throws IOException {
        App.setScene("PCLogin.fxml");
    }
    
    public void onGetAllParkingLot (RequestMessage request, ResponseMessage response) {
        System.out.println("onGetAllParkingLot ???");
        
        ObservableList<ParkingLot> parkingLots = FXCollections.observableArrayList((List<ParkingLot>) response.getData());
        
        Platform.runLater(() -> {
            if (response.getStatus() == ResponseStatus.SUCCESS) {
                dialog.setTitleText("Open Kiosk App");
                dialog.setBodyText("Choose which parking lot you want to open the app for.");

                MFXFilterComboBox<ParkingLot> filterCombo = new MFXFilterComboBox<>();
                filterCombo.setFloatMode(FloatMode.DISABLED);
                filterCombo.setPrefWidth(400);
                StringConverter<ParkingLot> converter = FunctionalStringConverter.to(parkingLot -> (parkingLot == null) ? "" : parkingLot.getName());
                Function<String, Predicate<ParkingLot>> filterFunction = str -> parkingLot -> StringUtils.containsIgnoreCase(converter.toString(parkingLot), str);
                filterCombo.setItems(parkingLots);
                filterCombo.setValue(parkingLots.get(0));
                filterCombo.setConverter(converter);
                filterCombo.setFilterFunction(filterFunction);

                HBox wrapper = new HBox();
                wrapper.getChildren().add(filterCombo);

                dialog.setCustomContent(wrapper);

                MFXButton confirmBtn = new MFXButton("Confirm");
                confirmBtn.getStyleClass().add("button-primary");
                confirmBtn.setOnAction(actionEvent -> {
                    try {
                        App.setEntity(filterCombo.getValue());
                        App.setScene("KioskMain.fxml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                MFXButton cancelBtn = new MFXButton("Cancel");
                cancelBtn.getStyleClass().add("button-secondary");
                cancelBtn.setOnAction(actionEvent -> dialog.close());

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
