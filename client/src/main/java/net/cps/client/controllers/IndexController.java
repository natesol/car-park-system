package net.cps.client.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import net.cps.client.App;
import net.cps.common.entities.ParkingLot;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.net.URL;
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
        dialog.setWidth("sm");
        dialog.setTitleText("Open Kiosk App");
        dialog.setBodyText("Choose which parking lot you want to open the app for.");
        
        MFXFilterComboBox<ParkingLot> filterCombo = new MFXFilterComboBox<>();
        ObservableList<ParkingLot> parkingLots = FXCollections.observableArrayList();
        parkingLots.add(new ParkingLot("parking #1", "haifa 123", 5));
        parkingLots.add(new ParkingLot("parking #2", "haifa 61/4", 1));
        parkingLots.add(new ParkingLot("parking #3", "tel-aviv 99", 2));
        parkingLots.add(new ParkingLot("parking #4", "eilat 7", 11));
        StringConverter<ParkingLot> converter = FunctionalStringConverter.to(parkingLot -> (parkingLot == null) ? "" : "#" + "parkingLot.getId()" + " " + parkingLot.getName());
        Function<String, Predicate<ParkingLot>> filterFunction = str -> parkingLot -> StringUtils.containsIgnoreCase(converter.toString(parkingLot), str);
        filterCombo.setItems(parkingLots);
        filterCombo.setConverter(converter);
        filterCombo.setFilterFunction(filterFunction);
        
        HBox wrapper = new HBox();
        wrapper.getChildren().add(filterCombo);
        
        dialog.setCustomContent(wrapper);
    
        MFXButton confirmBtn = new MFXButton("Confirm");
        confirmBtn.getStyleClass().add("button-primary");
        confirmBtn.setOnAction(actionEvent -> {
            try {
                App.setScene("PCLogin.fxml");
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
    
    @FXML
    public void pcAppBtnClickHandler (ActionEvent event) throws IOException {
        App.setScene("PCLogin.fxml");
    }
    
    
    /* ----- Eventbus Listeners ------------------------------------- */
    
    // ...
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    // ...
    
}
