package net.cps.client.controllers.pc.customer;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import net.cps.client.App;
import net.cps.client.CPSClient;
import net.cps.client.utils.AbstractPageController;
import net.cps.common.entities.*;
import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;


public class PCCustomerReservationsController extends AbstractPageController {
    private Customer customer;
    private ArrayList<ParkingLot> allParkingLots = new ArrayList<>();
    private ArrayList<Reservation> allCustomerReservations = new ArrayList<>();
    private Reservation selectedReservation = null;
    private ArrayList<Vehicle> allCustomerVehicles = new ArrayList<>();
    
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
    public MFXButton newReservationBtn;
    @FXML
    public MFXButton cancelSelectedBtn;
    @FXML
    public MFXLegacyTableView<Reservation> reservationsTable;
    @FXML
    public TableColumn<String, ParkingLot> parkingLotColResTable;
    @FXML
    public TableColumn<String, Vehicle> vehicleColResTable;
    @FXML
    public TableColumn<String, Calendar> arrivalTimeColResTable;
    @FXML
    public TableColumn<String, Calendar> departureTimeColResTable;
    @FXML
    public TableColumn<String, ReservationStatus> statusColResTable;
    
    /* New Reservation Form Controls */
    public HBox newReservationForm;
    public MFXComboBox<ParkingLot> parkingLotsListCombo;
    public MFXTextField vehicleNumberField;
    public MFXDatePicker arrivalDate;
    public MFXTextField arrivalTimeHourField;
    public MFXTextField arrivalTimeMinutesField;
    public MFXDatePicker departureDate;
    public MFXTextField departureTimeHourField;
    public MFXTextField departureTimeMinutesField;
    public Text priceField;
    
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        customer = (Customer) App.getEntity();
        
        Platform.runLater(() -> {
            customerName.setText(customer.getFirstName() + " " + customer.getLastName());
            newReservationForm.setManaged(false);
            newReservationForm.setVisible(false);
            newReservationForm.setDisable(true);
        });
        
        CPSClient.sendRequestToServer(RequestType.GET, Entities.PARKING_LOT.getTableName(), this::onGetParkingLots);
        CPSClient.sendRequestToServer(RequestType.GET, Entities.VEHICLE.getTableName() + "/customer_email=" + customer.getEmail(), this::onGetVehicles);
        CPSClient.sendRequestToServer(RequestType.GET, Entities.RESERVATION.getTableName() + "/customer_email=" + customer.getEmail(), this::onGetReservation);
    }
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void menuBtnHomeClickHandler (MouseEvent mouseEvent) throws IOException {
        if (menuBtnHome.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                App.setPage("pc/customer/PCEmployeeHome.fxml");
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
    public void newReservationBtnClickHandler (ActionEvent actionEvent) {
        Platform.runLater(() -> {
            StringConverter<ParkingLot> converter = FunctionalStringConverter.to(parkingLot -> (parkingLot == null) ? "-" : parkingLot.getName());
            parkingLotsListCombo.setConverter(converter);
            parkingLotsListCombo.setItems(FXCollections.observableArrayList(allParkingLots));
            
            MFXButton confirmBtn = new MFXButton("Book");
            confirmBtn.setOnAction(event -> {
                selectedReservation = createReservationObject();
                CPSClient.sendRequestToServer(RequestType.CREATE, Entities.RESERVATION.getTableName(), "create a new reservation for customer: " + customer.getEmail(), selectedReservation, this::onCreateReservation);
                dialog.close();
            });
            confirmBtn.getStyleClass().add("button-primary");
            MFXButton cancelBtn = new MFXButton("Cancel");
            cancelBtn.setOnAction(event -> dialog.close());
            cancelBtn.getStyleClass().add("button-secondary");
            
            dialog.setWidth(Dialog.Width.EXTRA_SMALL);
            dialog.setTitleText("Book New Reservation");
            dialog.setBodyText("Fill the form below to create a new reservation.");
            
            dialog.setCustomContent(newReservationForm);
            newReservationForm.setManaged(true);
            newReservationForm.setVisible(true);
            newReservationForm.setDisable(false);
            
            dialog.setActionButtons(cancelBtn, confirmBtn);
            
            dialog.open();
        });
    }
    
    
    
    /* ----- EventBus Listeners ------------------------------------- */
    
    //...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    @RequestCallback.Method
    private void onGetReservation (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.SUCCESS) {
            Platform.runLater(() -> {
                allCustomerReservations = (ArrayList<Reservation>) response.getData();
                ObservableList<Reservation> reservations = FXCollections.observableArrayList((ArrayList<Reservation>) response.getData());
    
                parkingLotColResTable.setCellValueFactory(new PropertyValueFactory<>("parkingLotName"));
                vehicleColResTable.setCellValueFactory(new PropertyValueFactory<>("number"));
                arrivalTimeColResTable.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
                departureTimeColResTable.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
                statusColResTable.setCellValueFactory(new PropertyValueFactory<>("status"));
                reservationsTable.setItems(reservations);
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
    private void onCreateReservation (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.FINISHED) {
            Integer id = (Integer) response.getData();
            selectedReservation.setId(id);
            
            Platform.runLater(() -> {
                dialog.close();
                
                MFXButton okBtn = new MFXButton("OK");
                okBtn.setOnAction(event -> {
                    dialog.close();
                    reservationsTable.setItems(FXCollections.observableArrayList(allCustomerReservations));
                });
                okBtn.getStyleClass().add("button-primary");
                
                dialog.setWidth(Dialog.Width.EXTRA_SMALL);
                dialog.setTitleText("Subscription Created");
                dialog.setBodyText("Your subscription has been created successfully.", "Your subscription ID is: '" + id + "', keep it safe.", "Please note, your subscription id is required for any future actions - such as parking lot entrance.");
                dialog.setActionButtons(okBtn);
                dialog.open();
                
                allCustomerReservations.add(selectedReservation);
                ObservableList<Reservation> reservations = FXCollections.observableArrayList(allCustomerReservations);
                reservationsTable.setItems(reservations);
                
                customer.chargeBalance(calculatePrice(selectedReservation));
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
        selectedReservation = null;
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    private @NotNull Reservation createReservationObject () {
        ParkingLot parkingLot;
        Calendar startAt = Calendar.getInstance();
        SubscriptionType type;
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        ArrayList<Vehicle> newVehicles = new ArrayList<>();
        LocalTime departureTime = LocalTime.of(0, 0);

        
        return new Reservation();
    }
    
    private Double calculatePrice (@NotNull Reservation subscription) {
        Double price = 0.0;
        //SubscriptionType type = subscription.getType();
        //ParkingLot parkingLot = subscription.getParkingLot();
        //Integer numOfVehicles = subscription.getVehicles().size();
        //
        //if (type == SubscriptionType.BASIC) {
        //    if (numOfVehicles == 1) {
        //        price = parkingLot.getRates().getRegularSubscriptionSingleVehicle() * parkingLot.getRates().getHourlyOnetimeParking();
        //    }
        //    else {
        //        price = parkingLot.getRates().getRegularSubscriptionMultipleVehicles() * parkingLot.getRates().getHourlyOnetimeParking() * numOfVehicles;
        //    }
        //}
        //else if (type == SubscriptionType.PREMIUM) {
        //    price = parkingLot.getRates().getFullSubscriptionSingleVehicle() * parkingLot.getRates().getHourlyOnetimeParking();
        //}
        //
        return price;
    }
}
