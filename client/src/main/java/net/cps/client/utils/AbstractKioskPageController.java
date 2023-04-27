package net.cps.client.utils;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import net.cps.client.App;
import net.cps.client.CPSClient;
import net.cps.client.events.KioskEnterEvent;
import net.cps.common.entities.Customer;
import net.cps.common.entities.ParkingLot;
import net.cps.common.entities.ParkingSpace;
import net.cps.common.entities.Vehicle;
import net.cps.common.utils.Entities;
import net.cps.common.utils.RequestType;
import net.cps.common.utils.ResponseStatus;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;


public abstract class AbstractKioskPageController extends AbstractPageController {
    protected ParkingLot parkingLot;
    protected ArrayList<ParkingSpace> allParkingSpaces = new ArrayList<>();
    
    @FXML
    public Text parkingLotNameTitle;
    
    @FXML
    public VBox kioskMenu;
    @FXML
    public MFXButton homeMenuBtn;
    @FXML
    public MFXButton bookNowMenuBtn;
    @FXML
    public MFXButton subscriptionMenuBtn;
    @FXML
    public MFXButton reservationMenuBtn;
    @FXML
    public MFXButton exitMenuBtn;
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        
        EventBus.getDefault().register(this);
        parkingLot = (ParkingLot) App.getEntity();
        
        if (parkingLot != null) {
            Platform.runLater(() -> {
                parkingLotNameTitle.setText(parkingLot.getName());
            });
        }
    
        CPSClient.sendRequestToServer(RequestType.GET, Entities.PARKING_SPACE.getTableName(), null, (req, res) -> {
            if (res.getStatus() == ResponseStatus.SUCCESS) {
                allParkingSpaces = (ArrayList<ParkingSpace>) res.getData();
                allParkingSpaces.removeIf(Objects::isNull);
                allParkingSpaces.removeIf(parkingSpace -> !parkingSpace.getParkingLot().getName().equals(parkingLot.getName()));
                parkingLot.setParkingSpaces(allParkingSpaces);
            }
        });
    }
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void homeMenuBtnClickHandler (ActionEvent event) throws IOException {
        if (homeMenuBtn.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                App.setPage("kiosk/KioskHome.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    @FXML
    public void bookNowMenuBtnClickHandler (ActionEvent event) throws IOException {
        if (bookNowMenuBtn.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                App.setPage("kiosk/KioskBookNow.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    @FXML
    public void subscriptionMenuBtnClickHandler (ActionEvent event) throws IOException {
        if (subscriptionMenuBtn.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                App.setPage("kiosk/KioskSubscriptions.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    @FXML
    public void reservationMenuBtnClickHandler (ActionEvent event) throws IOException {
        if (reservationMenuBtn.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                App.setPage("kiosk/KioskReservations.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    @FXML
    public void exitMenuBtnClickHandler (ActionEvent event) throws IOException {
        if (exitMenuBtn.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                App.setPage("kiosk/KioskExit.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    
    
    /* ----- Event Bus Listeners ------------------------------------ */
    
    @Subscribe
    public void onKioskEnterEvent (@NotNull KioskEnterEvent event) {
        this.initV();
        this.parkingLot = event.getParkingLot();
        App.setEntity(parkingLot);
        
        Platform.runLater(() -> {
            parkingLotNameTitle.setText(parkingLot.getName());
        });
    }
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    // ...
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    public void initV () {
        App.allCustomers.add(new Customer("netanelshlomo@gmail.com", "Netanel", "Shlomo", "123456"));
        App.allCustomers.get(0).setId(1);
        App.allCustomers.add(new Customer("john.doe@gmail.com", "John", "Doe", "123456"));
        App.allCustomers.get(1).setId(2);
        App.allCustomers.add(new Customer("jane.doe@gmail.com", "Jane", "Doe", "123456"));
        App.allCustomers.get(2).setId(3);
        App.allCustomers.add(new Customer("bob.smith@gmail.com", "Bob", "Smith", "123456"));
        App.allCustomers.get(3).setId(4);
        App.allCustomers.add(new Customer("alice.smith@gmail.com", "Alice", "Smith", "123456"));
        App.allCustomers.get(4).setId(5);
        App.allCustomers.add(new Customer("foo.bar@gmail.com", "Foo", "Bar", "123456"));
    
        Vehicle vehicle = new Vehicle("12345678", App.allCustomers.get(0));
        vehicle.setId(1);
        App.allVehicles.add(vehicle);
        App.allCustomers.get(0).addVehicle(vehicle);
        vehicle = new Vehicle("87654321", App.allCustomers.get(0));
        vehicle.setId(2);
        App.allVehicles.add(vehicle);
        App.allCustomers.get(0).addVehicle(vehicle);
        vehicle = new Vehicle("14725836", App.allCustomers.get(1));
        vehicle.setId(3);
        App.allVehicles.add(vehicle);
        App.allCustomers.get(1).addVehicle(vehicle);
        vehicle = new Vehicle("96385274", App.allCustomers.get(2));
        vehicle.setId(4);
        App.allVehicles.add(vehicle);
        App.allCustomers.get(2).addVehicle(vehicle);
        vehicle = new Vehicle("25836974", App.allCustomers.get(3));
        vehicle.setId(5);
        App.allVehicles.add(vehicle);
        App.allCustomers.get(3).addVehicle(vehicle);
        vehicle = new Vehicle("74185296", App.allCustomers.get(4));
        vehicle.setId(6);
        App.allVehicles.add(vehicle);
        App.allCustomers.get(4).addVehicle(vehicle);
        vehicle = new Vehicle("85274196", App.allCustomers.get(5));
        vehicle.setId(7);
        App.allVehicles.add(vehicle);
        App.allCustomers.get(5).addVehicle(vehicle);
    }
}
