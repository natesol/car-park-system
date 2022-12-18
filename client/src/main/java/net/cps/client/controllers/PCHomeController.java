package net.cps.client.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.util.Duration;
import net.cps.client.CPSClient;
import net.cps.client.events.ParkingLotDataTmpEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PCHomeController {
    
    @FXML
    private MFXButton showParkingLotsBtn;
    
    @FXML
    private MFXButton showRatesBtn;
    
    @FXML
    void showParkingLotsBtnClickHandler(ActionEvent event) throws IOException {
        System.out.println("show ParkingLots !");
        
        CPSClient.sendMessageToServer("get parking-lots");
    }
    
    @FXML
    void showRatesBtnClickHandler(ActionEvent event) throws IOException {
        System.out.println("show Rates !");
    }
    
    @Subscribe
    public void parkingLotDataTmpEventHandler(ParkingLotDataTmpEvent event) {
        System.out.println("!!!!!!!!!!!!!");
        System.out.println(event.getMessage());
    }
    
    @FXML
    void initialize() {
        EventBus.getDefault().register(this);
    }
}
