package net.cps.client.controllers.pc.customer;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import net.cps.client.CPSClient;
import net.cps.client.utils.AbstractPCCustomerPageController;
import net.cps.common.entities.ParkingLot;
import net.cps.common.entities.Reservation;
import net.cps.common.entities.Vehicle;
import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.*;

import java.net.URL;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;


public class PCCustomerReservationsController extends AbstractPCCustomerPageController implements Initializable {
    private ArrayList<ParkingLot> allParkingLots = new ArrayList<>();
    private ArrayList<Reservation> allCustomerReservations = new ArrayList<>();
    private Reservation selectedReservation = null;
    private ArrayList<Vehicle> allCustomerVehicles = new ArrayList<>();
    
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
        super.initialize(url, resourceBundle);
        
        Platform.runLater(() -> {
            newReservationForm.setManaged(false);
            newReservationForm.setVisible(false);
            newReservationForm.setDisable(true);
        });
        
        CPSClient.sendRequestToServer(RequestType.GET, Entities.PARKING_LOT.getTableName(), this::onGetParkingLots);
        CPSClient.sendRequestToServer(RequestType.GET, Entities.VEHICLE.getTableName() + "/customer_email=" + customer.getEmail(), this::onGetVehicles);
        CPSClient.sendRequestToServer(RequestType.GET, Entities.RESERVATION.getTableName() + "/customer_email=" + customer.getEmail(), this::onGetReservations);
    }
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
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
    
    @FXML
    public void cancelSelectedBtnClickHandler (ActionEvent actionEvent) {
        Platform.runLater(() -> {
            Reservation selected = reservationsTable.getSelectionModel().getSelectedItems().get(0);
            Double refund = selected.calculateCancellationFee();
            
            MFXButton confirmBtn = new MFXButton("Confirm");
            confirmBtn.setOnAction(event -> {
                selected.setStatus(ReservationStatus.CANCELLED);
                customer.creditBalance(refund);
                CPSClient.sendRequestToServer(RequestType.UPDATE, Entities.RESERVATION.getTableName(), "cancel reservation: " + selected.getId(), selected, (req, res) -> {
                    if (res.getStatus() == ResponseStatus.FINISHED) {
                        CPSClient.sendRequestToServer(RequestType.UPDATE, Entities.CUSTOMER.getTableName(), "update customer: " + customer.getEmail(), customer, (req1, res1) -> {
                            if (res1.getStatus() == ResponseStatus.FINISHED) {
                                Platform.runLater(() -> {
                                    dialog.close();
                                    MFXButton okBtn = new MFXButton("OK");
                                    okBtn.setOnAction(event1 -> dialog.close());
                                    okBtn.getStyleClass().add("button-primary");
                                    dialog.setTitleText("Success");
                                    dialog.setBodyText("Reservation cancelled successfully.", "Your credit balance has been updated by " + refund + "₪.");
                                    dialog.setActionButtons(okBtn);
                                    dialog.open();
                                    reservationsTable.refresh();
                                });
                            }
                        });
                    }
                });
                dialog.close();
            });
            confirmBtn.getStyleClass().add("button-primary");
            MFXButton cancelBtn = new MFXButton("Cancel");
            cancelBtn.setOnAction(event -> dialog.close());
            cancelBtn.getStyleClass().add("button-secondary");
            
            dialog.setWidth(Dialog.Width.EXTRA_SMALL);
            dialog.setTitleText("Cancel Reservation");
            dialog.setBodyText("You will be refunded for " + refund + "₪.", "Are you sure you want to cancel the selected reservation?");
            dialog.setActionButtons(cancelBtn, confirmBtn);
            dialog.open();
        });
    }
    
    
    /* ----- Event Bus Listeners ------------------------------------ */
    
    //...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    @RequestCallback.Method
    private void onGetReservations (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.SUCCESS) {
            Platform.runLater(() -> {
                allCustomerReservations = (ArrayList<Reservation>) response.getData();
                ObservableList<Reservation> reservations = FXCollections.observableArrayList(allCustomerReservations);
                
                parkingLotColResTable.setCellValueFactory(new PropertyValueFactory<>("parkingLotName"));
                vehicleColResTable.setCellValueFactory(new PropertyValueFactory<>("vehicleNumber"));
                arrivalTimeColResTable.setCellValueFactory(new PropertyValueFactory<>("arrivalTimeFormatted"));
                departureTimeColResTable.setCellValueFactory(new PropertyValueFactory<>("departureTimeFormatted"));
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
            Double price = selectedReservation.calculatePrice();
            
            customer.chargeBalance(price);
            CPSClient.sendRequestToServer(RequestType.UPDATE, Entities.CUSTOMER.getTableName(), "update customer: " + customer.getEmail(), customer, (req, res) -> {
                if (res.getStatus() == ResponseStatus.FINISHED) {
                    Platform.runLater(() -> {
                        dialog.close();
                        
                        MFXButton okBtn = new MFXButton("OK");
                        okBtn.setOnAction(event -> {
                            dialog.close();
                            reservationsTable.setItems(FXCollections.observableArrayList(allCustomerReservations));
                            reservationsTable.refresh();
                        });
                        okBtn.getStyleClass().add("button-primary");
                        
                        dialog.setWidth(Dialog.Width.EXTRA_SMALL);
                        dialog.setTitleText("Success");
                        dialog.setBodyText("Your reservation created successfully.", "Your credit balance has been charged by " + price + "₪.");
                        dialog.setActionButtons(okBtn);
                        dialog.open();
                        reservationsTable.refresh();
                    });
                }
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
    
    private Reservation createReservationObject () {
        try {
            ParkingLot parkingLot = parkingLotsListCombo.getSelectionModel().getSelectedItem();
            String vehicleNumber = vehicleNumberField.getText();
            Vehicle vehicle = allCustomerVehicles.stream().filter(v -> v.getNumber().equals(vehicleNumber)).findFirst().orElse(null);
            boolean isNewVehicle = vehicle == null;
            if (isNewVehicle) {
                vehicle = new Vehicle(vehicleNumber, customer);
                Vehicle finalVehicle = vehicle;
                CPSClient.sendRequestToServer(RequestType.CREATE, Entities.VEHICLE.getTableName(), null, vehicle, (req, res) -> {
                    if (res.getStatus() == ResponseStatus.SUCCESS) {
                        allCustomerVehicles.add(finalVehicle);
                    }
                });
            }
            Calendar arrivalTime = Calendar.getInstance();
            arrivalTime.setTime(Date.from(arrivalDate.getValue().atTime(Integer.parseInt(arrivalTimeHourField.getText()), Integer.parseInt(arrivalTimeMinutesField.getText())).atZone(ZoneId.systemDefault()).toInstant()));
            Calendar departureTime = Calendar.getInstance();
            departureTime.setTime(Date.from(departureDate.getValue().atTime(Integer.parseInt(departureTimeHourField.getText()), Integer.parseInt(departureTimeMinutesField.getText())).atZone(ZoneId.systemDefault()).toInstant()));
            
            return new Reservation(parkingLot, customer, vehicle, arrivalTime, departureTime);
        }
        catch (Throwable e) {
            return null;
        }
    }
    
}
