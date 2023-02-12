package net.cps.client.controllers.pc.customer;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import net.cps.client.CPSClient;
import net.cps.client.utils.AbstractPCCustomerPageController;
import net.cps.common.entities.ParkingLot;
import net.cps.common.entities.Subscription;
import net.cps.common.entities.Vehicle;
import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.*;

import java.net.URL;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;


public class PCCustomerSubscriptionsController extends AbstractPCCustomerPageController implements Initializable {
    private ArrayList<ParkingLot> allParkingLots = new ArrayList<>();
    private ArrayList<Subscription> allCustomerSubscriptions = new ArrayList<>();
    private Subscription selectedSubscription = null;
    private ArrayList<Vehicle> allCustomerVehicles = new ArrayList<>();
    
    /* Header Controls */
    @FXML
    public MFXComboBox<ParkingLot> allParkingLotsCombo;
    @FXML
    public MFXButton addSubscriptionBtn;
    
    /* Subscriptions Table Controls */
    @FXML
    public MFXLegacyTableView<Subscription> subscriptionsTable;
    @FXML
    public TableColumn<String, Subscription> subIdColSubTable;
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
    public Text priceField;
    public MFXButton calcPriceBtn;
    
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        
        Platform.runLater(() -> {
            addSubscriptionForm.setManaged(false);
            addSubscriptionForm.setVisible(false);
            addSubscriptionForm.setDisable(true);
        });
        
        CPSClient.sendRequestToServer(RequestType.GET, Entities.PARKING_LOT.getTableName(), this::onGetParkingLots);
        CPSClient.sendRequestToServer(RequestType.GET, Entities.VEHICLE.getTableName() + "/customer_email=" + customer.getEmail(), this::onGetVehicles);
        CPSClient.sendRequestToServer(RequestType.GET, Entities.SUBSCRIPTION.getTableName() + "/customer_email=" + customer.getEmail(), this::onGetSubscriptions);
    }
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void addSubscriptionBtnClickHandler (ActionEvent actionEvent) {
        Platform.runLater(() -> {
            StringConverter<ParkingLot> converter = FunctionalStringConverter.to(parkingLot -> (parkingLot == null) ? "-" : parkingLot.getName());
            parkingLotsListCombo.setConverter(converter);
            parkingLotsListCombo.setItems(FXCollections.observableArrayList(allParkingLots));
            
            MFXButton confirmBtn = new MFXButton("Add");
            confirmBtn.setOnAction(event -> {
                selectedSubscription = createSubscriptionObject();
                CPSClient.sendRequestToServer(RequestType.CREATE, Entities.SUBSCRIPTION.getTableName(), "create a new subscription for customer: " + customer.getEmail(), selectedSubscription, this::onCreateSubscription);
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
        vehicleNumber.setTextLimit(8);
        children.add(vehicleNumber);
        
        if (children.size() > 1) {
            removeVehicleLink.setDisable(false);
            removeVehicleLink.setVisible(true);
            removeVehicleLink.setManaged(true);
        }
    }
    
    @FXML
    private void removeVehicleLinkClickHandler (ActionEvent actionEvent) {
        ObservableList<Node> children = vehicleNumbersFields.getChildren();
        if (children.size() > 1) {
            children.remove(children.size() - 1);
        }
        if (children.size() == 1) {
            removeVehicleLink.setDisable(true);
            removeVehicleLink.setVisible(false);
            removeVehicleLink.setManaged(false);
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
    
    @FXML
    public void calcPriceBtnClickHandler (ActionEvent actionEvent) {
        Platform.runLater(() -> {
            Subscription subscription = createSubscriptionObject();
            if (subscription == null) return;
            Double price = subscription.calculatePrice();
            if (price == null) return;
            priceField.setText(String.format("%.2f", price));
        });
    }
    
    
    /* ----- EventBus Listeners ------------------------------------- */
    
    //...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    @RequestCallback.Method
    private void onGetSubscriptions (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.SUCCESS) {
            Platform.runLater(() -> {
                allCustomerSubscriptions = (ArrayList<Subscription>) response.getData();
                ObservableList<Subscription> subscriptions = FXCollections.observableArrayList((ArrayList<Subscription>) response.getData());
                
                subIdColSubTable.setCellValueFactory(new PropertyValueFactory<>("id"));
                parkingLotColSubTable.setCellValueFactory(new PropertyValueFactory<>("parkingLotName"));
                subTypeColSubTable.setCellValueFactory(new PropertyValueFactory<>("type"));
                createTimeColSubTable.setCellValueFactory(new PropertyValueFactory<>("createdAtFormatted"));
                expireTimeColSubTable.setCellValueFactory(new PropertyValueFactory<>("expiresAtFormatted"));
                stateColSubTable.setCellValueFactory(new PropertyValueFactory<>("state"));
                subscriptionsTable.setItems(subscriptions);
                subscriptionsTable.refresh();
            });
        }
    }
    
    @RequestCallback.Method
    private void onGetVehicles (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.SUCCESS) {
            Platform.runLater(() -> {
                allCustomerVehicles = (ArrayList<Vehicle>) response.getData();
            });
        }
    }
    
    @RequestCallback.Method
    private void onGetParkingLots (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.SUCCESS) {
            Platform.runLater(() -> {
                allParkingLots = (ArrayList<ParkingLot>) response.getData();
            });
        }
    }
    
    @RequestCallback.Method
    private void onCreateSubscription (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.FINISHED) {
            Integer id = (Integer) response.getData();
            selectedSubscription.setId(id);
            Double price = selectedSubscription.calculatePrice();
            
            Platform.runLater(() -> {
                dialog.close();
                
                MFXButton okBtn = new MFXButton("OK");
                okBtn.setOnAction(event -> {
                    dialog.close();
                    subscriptionsTable.setItems(FXCollections.observableArrayList(allCustomerSubscriptions));
                    subscriptionsTable.refresh();
                });
                okBtn.getStyleClass().add("button-primary");
                
                dialog.setWidth(Dialog.Width.EXTRA_SMALL);
                dialog.setTitleText("Subscription Created");
                dialog.setBodyText("Your subscription has been created successfully, and your account where charged: " + price + "ILS.", " ", "Your subscription ID is: '" + id + "', keep it safe.", "Please note, your subscription id is required for any future actions - such as parking lot entrance.");
                dialog.setActionButtons(okBtn);
                dialog.open();
                
                allCustomerSubscriptions.add(selectedSubscription);
                ObservableList<Subscription> subscriptions = FXCollections.observableArrayList(allCustomerSubscriptions);
                subscriptionsTable.setItems(subscriptions);
                subscriptionsTable.refresh();
                
                customer.chargeBalance(price);
                CPSClient.sendRequestToServer(RequestType.UPDATE, Entities.CUSTOMER.getTableName(), null, customer, (req, res) -> {
                    if (res.getStatus() == ResponseStatus.SUCCESS) {
                        System.out.println("Customer balance updated successfully.");
                    }
                });
            });
        }
        else {
            Platform.runLater(() -> {
                dialog.close();
                
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
        selectedSubscription = null;
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    private Subscription createSubscriptionObject () {
        try {
            ParkingLot parkingLot;
            Calendar startAt = Calendar.getInstance();
            SubscriptionType type;
            ArrayList<Vehicle> vehicles = new ArrayList<>();
            ArrayList<Vehicle> newVehicles = new ArrayList<>();
            LocalTime departureTime = LocalTime.of(0, 0);
            
            if (subscriptionType.getSelectedToggle() == regularSubRadio) {
                parkingLot = parkingLotsListCombo.getSelectionModel().getSelectedItem();
                startAt.setTime(Date.from(startAtDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                startAt.clear(Calendar.HOUR_OF_DAY);
                type = SubscriptionType.BASIC;
                vehicleNumbersFields.getChildren().forEach(node -> {
                    if (node instanceof MFXTextField) {
                        String vehicleNumber = ((MFXTextField) node).getText();
                        vehicles.add(new Vehicle(vehicleNumber, customer));
                        
                        if (allCustomerVehicles.stream().noneMatch(vehicle -> vehicle.getNumber().equals(vehicleNumber))) {
                            newVehicles.add(new Vehicle(vehicleNumber, customer));
                        }
                    }
                });
            }
            else {
                parkingLot = null;
                startAt.setTime(Date.from(startAtDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                startAt.clear(Calendar.HOUR_OF_DAY);
                type = SubscriptionType.PREMIUM;
                String vehicleNumber = vehicleNumberField.getText();
                vehicles.add(new Vehicle(vehicleNumber, customer));
                if (allCustomerVehicles.stream().noneMatch(vehicle -> vehicle.getNumber().equals(vehicleNumber))) {
                    newVehicles.add(new Vehicle(vehicleNumber, customer));
                }
            }
            if (departureTimeToggle.isSelected()) {
                departureTime = LocalTime.of(Integer.parseInt(departureTimeHourField.getText()), Integer.parseInt(departureTimeMinutesField.getText()));
            }
            if (!newVehicles.isEmpty()) {
                CPSClient.sendRequestToServer(RequestType.CREATE, Entities.VEHICLE.getTableName(), null, newVehicles, (req, res) -> {
                    if (res.getStatus() == ResponseStatus.SUCCESS) {
                        allCustomerVehicles.addAll(newVehicles);
                    }
                });
            }
            
            return new Subscription(customer, parkingLot, startAt, type, vehicles, departureTime);
        }
        catch (Throwable e) {
            return null;
        }
    }
}
