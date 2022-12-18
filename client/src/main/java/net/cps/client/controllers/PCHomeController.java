package net.cps.client.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import net.cps.client.App;

import java.io.IOException;


public class PCHomeController {
    
    @FXML
    private MFXButton showParkingLotsBtn;
    
    @FXML
    private MFXButton showRatesBtn;
    
    @FXML
    void showParkingLotsBtnClickHandler(ActionEvent event) throws IOException {
        System.out.println("show ParkingLots !");
    }
    
    @FXML
    void showRatesBtnClickHandler(ActionEvent event) throws IOException {
        System.out.println("show Rates !");
    }
}
