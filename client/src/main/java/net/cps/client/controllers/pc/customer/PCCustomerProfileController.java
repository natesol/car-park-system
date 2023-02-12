package net.cps.client.controllers.pc.customer;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import net.cps.client.CPSClient;
import net.cps.client.utils.AbstractPCCustomerPageController;
import net.cps.common.entities.Vehicle;
import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.Entities;
import net.cps.common.utils.RequestCallback;
import net.cps.common.utils.RequestType;
import net.cps.common.utils.ResponseStatus;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class PCCustomerProfileController extends AbstractPCCustomerPageController implements Initializable {
    private ArrayList<Vehicle> allCustomerVehicles = new ArrayList<>();
    
    /* Header Controls */
    @FXML
    public MFXButton editProfileBtn;
    
    /* Profile Controls */
    @FXML
    public MFXTextField firstName;
    @FXML
    public MFXTextField lastName;
    @FXML
    public MFXTextField email;
    
    /* Vehicles Table Controls */
    @FXML
    public MFXLegacyTableView<Vehicle> vehiclesTable;
    @FXML
    public TableColumn<String, Integer> vehicleIdCol;
    @FXML
    public TableColumn<String, String> vehicleNumCol;
    @FXML
    public MFXButton removeVehicleBtn;
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        
        Platform.runLater(() -> {
            firstName.setText(customer.getFirstName());
            lastName.setText(customer.getLastName());
            email.setText(customer.getEmail());
        });
        
        CPSClient.sendRequestToServer(RequestType.GET, Entities.VEHICLE.getTableName() + "/customer_email=" + customer.getEmail(), this::onGetVehicles);
    }
    
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void editProfileBtnClickHandler (ActionEvent event) {
        dialog.setTitleText("Edit Profile");
        dialog.setBodyText("Are you sure you want to edit your profile?");
        dialog.open();
    }
    
    @FXML
    public void removeVehicleBtnClickHandler (ActionEvent event) {
        dialog.setTitleText("Remove Vehicle");
        dialog.setBodyText("Are you sure you want to remove this vehicle?");
        dialog.open();
    }
    
    /* ----- EventBus Listeners ------------------------------------- */
    
    // ...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    @RequestCallback.Method
    private void onGetVehicles (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.SUCCESS) {
            Platform.runLater(() -> {
                allCustomerVehicles = (ArrayList<Vehicle>) response.getData();
            });
        }
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    // ...
    
}
