package net.cps.client.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import net.cps.client.CPSClient;
import net.cps.client.events.ParkingLotDataTmpEvent;
import net.cps.entities.hibernate.ParkingLot;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

public class PCHomeController {
    
    @FXML
    private MFXButton showParkingLotsBtn;
    
    @FXML
    private MFXButton showRatesBtn;
    
    @FXML
    private MFXLegacyTableView<ParkingLot> parkingLotTable;
    @FXML
    private TableColumn<ParkingLot, String> parkingLotTableNameCol;
    @FXML
    private TableColumn<ParkingLot, String> parkingLotTableLocationCol;
    @FXML
    private TableColumn<ParkingLot, String> parkingLotTableSpaceCol;

    
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
        ObservableList<ParkingLot> parkingLots = FXCollections.observableArrayList();
        parkingLots.addAll((Collection<? extends ParkingLot>) event.getMessage());
    
        for (ParkingLot parkingLot : parkingLots) {
            System.out.println(parkingLot.getName() + " " + parkingLot.getAddress());
        }
    
        parkingLotTableNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        parkingLotTableLocationCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        parkingLotTableSpaceCol.setCellValueFactory(new PropertyValueFactory<>("rates"));
    
        parkingLotTable.setItems(parkingLots);
    }
    
    @FXML
    void initialize() {
        EventBus.getDefault().register(this);
    }
}
