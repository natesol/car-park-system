package net.cps.client.controllers.pc.customer;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.cps.client.App;
import net.cps.client.CPSClient;
import net.cps.client.utils.AbstractPageController;
import net.cps.common.entities.Customer;
import net.cps.common.entities.ParkingLot;
import net.cps.common.entities.Subscription;
import net.cps.common.entities.Vehicle;
import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;


public class PCCustomerSubscriptionsController extends AbstractPageController {
    private Customer customer;
    private ArrayList<ParkingLot> allParkingLots;
    private ArrayList<Subscription> allSubscriptions;
    
    @FXML
    public Text customerName;
    @FXML
    public MFXButton menuBtnHome;
    @FXML
    public MFXButton menuBtnSubscriptions;
    @FXML
    public MFXButton menuBtnReservations;
    @FXML
    public MFXButton menuBtnComplaints;
    @FXML
    public MFXButton menuBtnProfile;
    @FXML
    public MFXButton menuBtnSignOut;
    
    @FXML
    public MFXComboBox<ParkingLot> allParkingLotsCombo;
    @FXML
    public MFXButton addSubscriptionBtn;
    @FXML
    public MFXLegacyTableView<Subscription> subscriptionsTable;
    @FXML
    public TableColumn<String, ParkingLot> parkingLotColSubTable;
    @FXML
    public TableColumn<String, SubscriptionType> subTypeColSubTable;
    @FXML
    public TableColumn<String, Calendar> createTimeColSubTable;
    @FXML
    public TableColumn<String, Calendar> expireTimeColSubTable;
    @FXML
    public TableColumn<String, SubscriptionState> stateColSubTable;
    
    /* Add Subscription Form Controls */
    public HBox addSubscriptionForm;
    public ToggleGroup subscriptionType;
    public MFXRadioButton regularSubRadio;
    public MFXRadioButton premiumSubRadio;
    public MFXComboBox<ParkingLot> parkingLotsListCombo;
    public MFXTextField vehicleNumberField;
    public VBox vehicleNumbersFieldsWrapper;
    public VBox vehicleNumbersFields;
    public MFXTextField vehicleNumberField1;
    public Hyperlink addVehicleLink;
    public Hyperlink removeVehicleLink;
    public MFXDatePicker startAtDate;
    public HBox departureTimeFieldsWrapper;
    public MFXToggleButton departureTimeToggle;
    public MFXTextField departureTimeHourField;
    public MFXTextField departureTimeMinutesField;
    
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        //EventBus.getDefault().register(this);
        
        customer = (Customer) App.getEntity();
        Platform.runLater(() -> {
            customerName.setText(customer.getFirstName() + " " + customer.getLastName());
            addSubscriptionForm.setManaged(false);
            addSubscriptionForm.setVisible(false);
            addSubscriptionForm.setDisable(true);
        });
        
        CPSClient.sendRequestToServer(RequestType.GET, Entities.SUBSCRIPTION.getTableName() + "/customer_email=" + customer.getEmail(), this::onGetSubscriptions);
        CPSClient.sendRequestToServer(RequestType.GET, Entities.PARKING_LOT.getTableName(), ((request, response) -> allParkingLots = (ArrayList<ParkingLot>) response.getData()));
    }
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void menuBtnHomeClickHandler (MouseEvent mouseEvent) throws IOException {
        if (menuBtnHome.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                App.setPage("pc/customer/PCCustomerHome.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    @FXML
    public void menuBtnSubscriptionsClickHandler (MouseEvent mouseEvent) throws IOException {
        if (menuBtnSubscriptions.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                App.setPage("pc/customer/PCCustomerSubscriptions.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    @FXML
    public void menuBtnReservationsClickHandler (MouseEvent mouseEvent) throws IOException {
        if (menuBtnReservations.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                App.setPage("pc/customer/PCCustomerReservations.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    @FXML
    public void menuBtnComplaintsClickHandler (MouseEvent mouseEvent) throws IOException {
        if (menuBtnComplaints.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                App.setPage("pc/customer/PCCustomerComplaints.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    @FXML
    public void menuBtnProfileClickHandler (MouseEvent mouseEvent) throws IOException {
        if (menuBtnProfile.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                App.setPage("pc/customer/PCCustomerProfile.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    @FXML
    public void menuBtnLogoutClickHandler (MouseEvent mouseEvent) throws IOException {
        Platform.runLater(() -> {
            MFXButton confirmBtn = new MFXButton("Log Out");
            confirmBtn.setOnAction(event -> {
                dialog.close();
                try {
                    CPSClient.sendRequestToServer(RequestType.AUTH, "logout/email=" + customer.getEmail(), "customer initialized logout.", customer, null);
                    App.setEntity(null);
                    App.setPage("pc/auth/PCLogin.fxml");
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            confirmBtn.getStyleClass().add("button-primary");
            MFXButton cancelBtn = new MFXButton("Cancel");
            cancelBtn.setOnAction(event -> dialog.close());
            cancelBtn.getStyleClass().add("button-secondary");
            
            dialog.setWidth(Dialog.Width.EXTRA_SMALL);
            dialog.setTitleText("Log Out");
            dialog.setBodyText("Are you sure you want to log out?");
            dialog.setActionButtons(cancelBtn, confirmBtn);
            dialog.open();
        });
    }
    
    @FXML
    public void addSubscriptionBtnClickHandler (ActionEvent actionEvent) {
        Platform.runLater(() -> {
            
            
            MFXButton confirmBtn = new MFXButton("Add");
            confirmBtn.setOnAction(event -> {
                CPSClient.sendRequestToServer(RequestType.CREATE, Entities.SUBSCRIPTION.getTableName(), "create a new subscription for customer: " + customer.getEmail(), createSubscriptionObject(), null);
                dialog.close();
            });
            confirmBtn.getStyleClass().add("button-primary");
            MFXButton cancelBtn = new MFXButton("Cancel");
            cancelBtn.setOnAction(event -> dialog.close());
            cancelBtn.getStyleClass().add("button-secondary");
            
            dialog.setWidth(Dialog.Width.EXTRA_SMALL);
            dialog.setTitleText("Add Subscription");
            dialog.setBodyText("Fill the form below to create a new subscription.");
            
            dialog.setCustomContent(addSubscriptionForm);
            addSubscriptionForm.setManaged(true);
            addSubscriptionForm.setVisible(true);
            addSubscriptionForm.setDisable(false);
            
            dialog.setActionButtons(cancelBtn, confirmBtn);
            
            dialog.open();
        });
    }
    
    @FXML
    private void addSubscriptionFormStateToggleHandler (ActionEvent actionEvent) {
        Platform.runLater(() -> {
            if (subscriptionType.getSelectedToggle() == regularSubRadio) {
                parkingLotsListCombo.setDisable(false);
                parkingLotsListCombo.setVisible(true);
                parkingLotsListCombo.setManaged(true);
                VBox.setMargin(parkingLotsListCombo, new Insets(0, 0, 0, 0));
                
                vehicleNumberField.setDisable(true);
                vehicleNumberField.setVisible(false);
                vehicleNumberField.setManaged(false);
                VBox.setMargin(vehicleNumberField, new Insets(-65, 0, 0, 0));
                
                vehicleNumbersFieldsWrapper.setDisable(false);
                vehicleNumbersFieldsWrapper.setVisible(true);
                vehicleNumbersFieldsWrapper.setManaged(true);
                VBox.setMargin(vehicleNumbersFieldsWrapper, new Insets(0, 0, 0, 0));
            }
            else {
                parkingLotsListCombo.setDisable(true);
                parkingLotsListCombo.setVisible(false);
                parkingLotsListCombo.setManaged(false);
                VBox.setMargin(parkingLotsListCombo, new Insets(-65, 0, 0, 0));
                
                vehicleNumberField.setDisable(false);
                vehicleNumberField.setVisible(true);
                vehicleNumberField.setManaged(true);
                VBox.setMargin(vehicleNumberField, new Insets(0, 0, 0, 0));
                
                vehicleNumbersFieldsWrapper.setDisable(true);
                vehicleNumbersFieldsWrapper.setVisible(false);
                vehicleNumbersFieldsWrapper.setManaged(false);
                VBox.setMargin(vehicleNumbersFieldsWrapper, new Insets((vehicleNumbersFields.getChildren().size() * -65) - 29, 0, 0, 0));
            }
        });
    }
    
    @FXML
    private void addVehicleLinkClickHandler (ActionEvent actionEvent) {
        ObservableList<Node> children = vehicleNumbersFields.getChildren();
        int newVehicleNumberFieldIndex = children.size() + 1;
        MFXTextField vehicleNumber = new MFXTextField();
        vehicleNumber.setId("vehicleNumberField" + newVehicleNumberFieldIndex);
        vehicleNumber.setFloatingText("Vehicle Number #" + newVehicleNumberFieldIndex);
        vehicleNumber.setPrefWidth(300.0);
        children.add(vehicleNumber);
    }
    
    @FXML
    private void removeVehicleLinkClickHandler (ActionEvent actionEvent) {
        ObservableList<Node> children = vehicleNumbersFields.getChildren();
        if (children.size() > 1) {
            children.remove(children.size() - 1);
        }
    }
    
    @FXML
    private void departureTimeToggleHandler (ActionEvent actionEvent) {
        Platform.runLater(() -> {
            if (departureTimeToggle.isSelected()) {
                departureTimeHourField.setDisable(false);
                departureTimeHourField.setEditable(true);
                departureTimeMinutesField.setDisable(false);
                departureTimeMinutesField.setEditable(true);
            }
            else {
                departureTimeHourField.setDisable(true);
                departureTimeHourField.setEditable(false);
                departureTimeHourField.setText("0");
                departureTimeMinutesField.setDisable(true);
                departureTimeMinutesField.setEditable(false);
                departureTimeMinutesField.setText("0");
            }
        });
    }
    
    
    
    /* ----- EventBus Listeners ------------------------------------- */
    
    //...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    @RequestCallback.Method
    private void onGetSubscriptions (RequestMessage request, ResponseMessage response) {
        ObservableList<Subscription> subscriptions = (ObservableList<Subscription>) response.getData();
        
        System.out.println("onGetSubscriptions !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
        System.out.println("subscriptions: " + subscriptions);
        
        if (response.getStatus() == ResponseStatus.SUCCESS) {
            Platform.runLater(() -> {
                
                parkingLotColSubTable.setCellValueFactory(new PropertyValueFactory<>("parkingLotId"));
                //subTypeColSubTable.setCellValueFactory(new PropertyValueFactory<>("name"));
                //createTimeColSubTable.setCellValueFactory(new PropertyValueFactory<>("address"));
                //expireTimeColSubTable.setCellValueFactory(new PropertyValueFactory<>("floorWidth"));
                //stateColSubTable.setCellValueFactory(new PropertyValueFactory<>("totalSpace"));
                
                subscriptionsTable.setItems(subscriptions);
                
                parkingLotColSubTable.setText("Parking Lot!!!!!");
                
            });
        }
    }
    
    @RequestCallback.Method
    private void onCreateSubscription (RequestMessage request, ResponseMessage response) {
        dialog.close();
        
        if (response.getStatus() == ResponseStatus.FINISHED) {
            Subscription subscription = (Subscription) response.getData();
            
            Platform.runLater(() -> {
                allSubscriptions.add(subscription);
                subscriptionsTable.getItems().add(subscription);
            });
        }
        else {
            Platform.runLater(() -> {
                MFXButton okBtn = new MFXButton("OK");
                okBtn.setOnAction(event -> dialog.close());
                okBtn.getStyleClass().add("button-secondary");
                
                dialog.setWidth(Dialog.Width.EXTRA_SMALL);
                dialog.setTitleText("Error");
                dialog.setBodyText("An error occurred while creating the subscription.", response.getMessage());
                dialog.setActionButtons(okBtn);
                
                dialog.open();
            });
        }
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    private @NotNull Subscription createSubscriptionObject () {
        Subscription subscription;
        ParkingLot parkingLot;
        Calendar startAt = Calendar.getInstance();
        List<Vehicle> vehicles = new ArrayList<>();
        LocalTime departureTime = LocalTime.of(0, 0);
        
        if (subscriptionType.getSelectedToggle() == regularSubRadio) {
            parkingLot = parkingLotsListCombo.getSelectionModel().getSelectedItem();
            startAt.setTime(Date.from(startAtDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            startAt.clear(Calendar.HOUR_OF_DAY);
            vehicleNumbersFields.getChildren().forEach(node -> {
                if (node instanceof MFXTextField) {
                    vehicles.add(new Vehicle(((MFXTextField) node).getText(), customer));
                }
            });
            if (departureTimeToggle.isSelected()) {
                departureTime = LocalTime.of(Integer.parseInt(departureTimeHourField.getText()), Integer.parseInt(departureTimeMinutesField.getText()));
            }
            
            subscription = new Subscription(customer, parkingLot, startAt, SubscriptionType.BASIC, vehicles, departureTime);
        }
        else {
            startAt.setTime(Date.from(startAtDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            startAt.clear(Calendar.HOUR_OF_DAY);
            vehicles.add(new Vehicle(vehicleNumberField.getText(), customer));
            if (departureTimeToggle.isSelected()) {
                departureTime = LocalTime.of(Integer.parseInt(departureTimeHourField.getText()), Integer.parseInt(departureTimeMinutesField.getText()));
            }
            
            subscription = new Subscription(customer, null, startAt, SubscriptionType.PREMIUM, vehicles, departureTime);
        }
        
        return subscription;
    }
    
}
