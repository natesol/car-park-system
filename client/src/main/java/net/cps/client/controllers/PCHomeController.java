package net.cps.client.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
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

import java.util.Collection;

public class PCHomeController {
    // Base elements.
    @FXML
    private MFXButton editRatesBtn;
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
    private MFXTextField idTextField;
    @FXML
    private MFXComboBox ratesComboBox;
    @FXML
    private MFXTextField valueTextField;
    @FXML
    private MFXButton dialogCancelBtn;
    @FXML
    private MFXButton dialogEditBtn;
    
    //-----------
    
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
        ratesComboBox.selectIndex(1);
    }
    
    @FXML
    public void refreshBtnClickHandler(ActionEvent event) {
        CPSClient.sendMessageToServer("get-parking-lots");
        CPSClient.sendMessageToServer("get-rates");
    }
    
    @FXML
    public void editRatesBtnClickHandler(ActionEvent actionEvent) {
        openDialog();
    }
    
    @FXML
    public void dialogCancelBtnClickHandler(ActionEvent actionEvent) {
        closeDialog();
    }
    
    @FXML
    public void dialogEditBtnClickHandler(ActionEvent actionEvent) {
        int id = Integer.parseInt(idTextField.getText());
        String rates = ratesComboBox.getText();
        double value = Double.parseDouble(valueTextField.getText());
        Rates obj = null;
    
        closeDialog();
        
        ObservableList<Rates> ratesList = ratesTable.getItems();
        for (Rates rate: ratesList) {
            if (rate.getId() == id) {
                obj = rate;
                break;
            }
        }
        
        if (rates.equals("Hourly Occasional Parking Price")) {
            obj.setHourlyOccasionalParking(value);
        }
        else if (rates.equals("Hourly Onetime Parking Price")) {
            obj.setHourlyOnetimeParking(value);
        }
        else if (rates.equals("Regular Subscription Single Vehicle")) {
            obj.setRegularSubscriptionSingleVehicle(value);
        }
        else if (rates.equals("Regular Subscription Multiple Vehicles")) {
            obj.setRegularSubscriptionMultipleVehicles(value);
        }
        else if (rates.equals("Full Subscription Single Vehicle")) {
            obj.setFullSubscriptionSingleVehicle(value);
        }
        
        System.out.println("edit: " + id + ", " + rates + ", " + value);
    
        CPSClient.sendMessageToServer("update-rates", obj);
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
        
        ratesTableIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        ratesTableHOPPCol.setCellValueFactory(new PropertyValueFactory<>("hourlyOccasionalParking"));
        ratesTableHOTPPCol.setCellValueFactory(new PropertyValueFactory<>("hourlyOnetimeParking"));
        ratesTableRSSVPPCol.setCellValueFactory(new PropertyValueFactory<>("regularSubscriptionSingleVehicle"));
        ratesTableRSMVPPCol.setCellValueFactory(new PropertyValueFactory<>("regularSubscriptionMultipleVehicles"));
        ratesTableFSSVPPCol.setCellValueFactory(new PropertyValueFactory<>("fullSubscriptionSingleVehicle"));
        
        ratesTable.setItems(rates);
    }
    
    // -----------------
    private void openDialog() {
        dialogWrapper.setVisible(true);
        dialogWrapper.setDisable(false);
    }
    
    @FXML
    private void closeDialog() {
        dialogWrapper.setVisible(false);
        dialogWrapper.setDisable(true);
    }
}
