package net.cps.client.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import net.cps.client.CPSClient;
import net.cps.client.events.ParkingLotDataTmpEvent;
import net.cps.client.events.RatesDataTmpEvent;
import net.cps.entities.hibernate.ParkingLot;
import net.cps.entities.hibernate.Rates;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.Collection;

public class PCHomeController {
    // Base elements.
    @FXML
    private MFXButton editDataBtn;
    @FXML
    private MFXButton refreshBtn;
    
    // ParkingLot table elements.
    @FXML
    private MFXLegacyTableView<ParkingLot> parkingLotTable;
    @FXML
    private TableColumn<ParkingLot, String> parkingLotTableIDCol;
    @FXML
    private TableColumn<ParkingLot, String> parkingLotTableNameCol;
    @FXML
    private TableColumn<ParkingLot, String> parkingLotTableLocationCol;
    @FXML
    private TableColumn<ParkingLot, String> parkingLotTableRowWidthCol;
    @FXML
    private TableColumn<ParkingLot, String> parkingLotTableTotalSpaceCol;
    
    // Rates table elements.
    @FXML
    private MFXLegacyTableView<Rates> ratesTable;
    @FXML
    private TableColumn<Rates, String> ratesTableIDCol;
    @FXML
    private TableColumn<Rates, String> ratesTableHOPPCol;
    @FXML
    private TableColumn<Rates, String> ratesTableHOTPPCol;
    @FXML
    private TableColumn<Rates, String> ratesTableRSSVPPCol;
    @FXML
    private TableColumn<Rates, String> ratesTableRSMVPPCol;
    @FXML
    private TableColumn<Rates, String> ratesTableFSSVPPCol;
    
    // Dialog elements.
    @FXML
    private AnchorPane dialogWrapper;
    @FXML
    private MFXComboBox ratesComboBox;
    @FXML
    private MFXButton dialogCancelBtn;
    @FXML
    private MFXButton dialogEditBtn;
    
    @FXML
    void initialize() {
        EventBus.getDefault().register(this);
    
        CPSClient.sendMessageToServer("get-parking-lots");
        CPSClient.sendMessageToServer("get-rates");
        
        ObservableList<String> strings = FXCollections.observableArrayList(
                "Hourly Occasional Parking Price",
                "Hourly Onetime Parking Price",
                "Regular Subscription Single Vehicle",
                "Regular Subscription Multiple Vehicles",
                "Full Subscription Single Vehicle"
        );;
        ratesComboBox.setItems(strings);
    }
    
    @FXML
    void showParkingLotsBtnClickHandler(ActionEvent event) throws IOException {
        System.out.println("show btn blaalala...");
    }
    
    @FXML
    public void refreshBtnClickHandler(ActionEvent event) {
        System.out.println("refresh tables...");
        
        CPSClient.sendMessageToServer("get-parking-lots");
        CPSClient.sendMessageToServer("get-rates");
    }
    
    @Subscribe
    public void parkingLotDataTmpEventHandler(ParkingLotDataTmpEvent event) {
        ObservableList<ParkingLot> parkingLots = FXCollections.observableArrayList();
        parkingLots.addAll((Collection<? extends ParkingLot>) event.getData());
    
        parkingLotTableIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        parkingLotTableNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        parkingLotTableLocationCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        parkingLotTableRowWidthCol.setCellValueFactory(new PropertyValueFactory<>("floorWidth"));
        parkingLotTableTotalSpaceCol.setCellValueFactory(new PropertyValueFactory<>("totalSpace"));
    
        parkingLotTable.setItems(parkingLots);
    }
    
    @Subscribe
    public void ratesDataTmpEventHandler(RatesDataTmpEvent event) {
        ObservableList<Rates> rates = FXCollections.observableArrayList();
        rates.addAll((Collection<? extends Rates>) event.getData());
        
        ratesTableIDCol.setCellValueFactory(new PropertyValueFactory<>("parkingLotId"));
        ratesTableHOPPCol.setCellValueFactory(new PropertyValueFactory<>("hourlyOccasionalParking"));
        ratesTableHOTPPCol.setCellValueFactory(new PropertyValueFactory<>("hourlyOnetimeParking"));
        ratesTableRSSVPPCol.setCellValueFactory(new PropertyValueFactory<>("regularSubscriptionSingleVehicle"));
        ratesTableRSMVPPCol.setCellValueFactory(new PropertyValueFactory<>("regularSubscriptionMultipleVehicles"));
        ratesTableFSSVPPCol.setCellValueFactory(new PropertyValueFactory<>("fullSubscriptionSingleVehicle"));
        
        ratesTable.setItems(rates);
    }
    
    public void editDataBtnClickHandler(ActionEvent actionEvent) {
        openDialog();
    }
    
    // -----------------
    private void openDialog() {
        dialogWrapper.setVisible(true);
        dialogWrapper.setDisable(false);
    
        
    }
    
    private void closeDialog() {
        dialogWrapper.setVisible(false);
        dialogWrapper.setDisable(true);
        
    }
    
}
