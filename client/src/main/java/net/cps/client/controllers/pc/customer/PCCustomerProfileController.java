package net.cps.client.controllers.pc.customer;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import net.cps.client.App;
import net.cps.client.CPSClient;
import net.cps.client.utils.AbstractPCCustomerPageController;
import net.cps.common.entities.Customer;
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
    private Customer editedCustomer = null;
    
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
    
    /* Edit Profile Form Controls */
    @FXML
    public HBox editProfileForm;
    @FXML
    public MFXCheckbox nameCheck;
    @FXML
    public MFXTextField firstNameField;
    @FXML
    public MFXTextField lastNameField;
    @FXML
    public Text nameErrorText;
    @FXML
    public MFXCheckbox emailCheck;
    @FXML
    public MFXTextField emailField;
    @FXML
    public Text emailErrorText;
    @FXML
    public MFXCheckbox passwordCheck;
    @FXML
    public MFXTextField passwordField;
    @FXML
    public MFXTextField repeatPasswordField;
    @FXML
    public Text passwordErrorText;
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        
        Platform.runLater(() -> {
            firstName.setText(customer.getFirstName());
            lastName.setText(customer.getLastName());
            email.setText(customer.getEmail());
            
            clearEditProfileForm();
        });
        
        //CPSClient.sendRequestToServer(RequestType.GET, Entities.VEHICLE.getTableName() + "/customer_email=" + customer.getEmail(), this::onGetVehicles);
    }
    
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void editProfileBtnClickHandler (ActionEvent event) {
        dialog.setTitleText("Edit Profile");
        dialog.setBodyText("Choose the fields you want to edit and enter the new values below.");
        
        editProfileForm.setVisible(true);
        editProfileForm.setManaged(true);
        editProfileForm.setDisable(false);
        clearEditProfileForm();
        dialog.setCustomContent(editProfileForm);
        
        MFXButton saveBtn = new MFXButton("Save");
        saveBtn.getStyleClass().add("button-primary-filled");
        saveBtn.setOnAction(e -> {
            boolean isChanged = false;
            editedCustomer = new Customer(customer);
            
            if (nameCheck.isSelected()) {
                String firstName = firstNameField.getText();
                if (firstName.isEmpty()) {
                    nameErrorText.setText("Please enter your first name.");
                    return;
                }
                if (firstName.length() < 2) {
                    nameErrorText.setText("First name must be at least 2 characters long.");
                    return;
                }
                if (firstName.length() > 55) {
                    nameErrorText.setText("First name must be at most 55 characters long.");
                    return;
                }
                if (!firstName.matches("[a-zA-Z]+")) {
                    nameErrorText.setText("First name must contain only letters.");
                    return;
                }
                String lastName = lastNameField.getText();
                if (lastName.isEmpty()) {
                    nameErrorText.setText("Please enter your last name.");
                    return;
                }
                if (lastName.length() < 2) {
                    nameErrorText.setText("Last name must be at least 2 characters long.");
                    return;
                }
                if (lastName.length() > 55) {
                    nameErrorText.setText("Last name must be at most 55 characters long.");
                    return;
                }
                if (!lastName.matches("[a-zA-Z]+")) {
                    nameErrorText.setText("Last name must contain only letters.");
                    return;
                }
                
                editedCustomer.setFirstName(firstName);
                editedCustomer.setLastName(lastName);
                isChanged = true;
            }
            if (emailCheck.isSelected()) {
                String email = emailField.getText();
                if (email.isEmpty()) {
                    nameErrorText.setText("Please enter your email.");
                    return;
                }
                if (!(email.contains("@") && email.contains("."))) {
                    nameErrorText.setText("Please enter a valid email.");
                    return;
                }
                
                editedCustomer.setEmail(email);
                isChanged = true;
            }
            if (passwordCheck.isSelected()) {
                String password = passwordField.getText();
                if (password.isEmpty()) {
                    passwordErrorText.setText("Please enter your password.");
                    return;
                }
                String repeatPassword = repeatPasswordField.getText();
                if (repeatPassword.isEmpty()) {
                    passwordErrorText.setText("Please repeat your password.");
                    return;
                }
                if (!password.equals(repeatPassword)) {
                    passwordErrorText.setText("Passwords do not match.");
                    return;
                }
                
                editedCustomer.setPassword(password);
                isChanged = true;
            }
            if (isChanged) {
                loader.show();
                CPSClient.sendRequestToServer(RequestType.UPDATE, Entities.CUSTOMER.getTableName() + "/" + customer.getId(), null, editedCustomer, this::onProfileUpdate);
            }
            else {
                dialog.setTitleText("Edit Profile");
                dialog.setBodyText("No changes were made.");
                dialog.setCustomContent((Node) null);
                dialog.setActionButtons((Node) null);
                dialog.open();
            }
        });
        MFXButton cancelBtn = new MFXButton("Cancel");
        cancelBtn.getStyleClass().add("button-base-outlined");
        cancelBtn.setOnAction(e -> {
            dialog.close();
        });
        dialog.setActionButtons(cancelBtn, saveBtn);
        
        dialog.open();
    }
    
    @FXML
    public void nameCheckClickHandler (ActionEvent event) {
        if (nameCheck.isSelected()) {
            firstNameField.setDisable(false);
            lastNameField.setDisable(false);
            firstNameField.setOpacity(1);
            lastNameField.setOpacity(1);
            nameErrorText.setText("");
        }
        else {
            firstNameField.setDisable(true);
            lastNameField.setDisable(true);
            firstNameField.setOpacity(0.7);
            lastNameField.setOpacity(0.7);
            nameErrorText.setText("");
        }
    }
    
    @FXML
    public void emailCheckClickHandler (ActionEvent event) {
        if (emailCheck.isSelected()) {
            emailField.setDisable(false);
            emailField.setOpacity(1);
            emailErrorText.setText("");
        }
        else {
            emailField.setDisable(true);
            emailField.setOpacity(0.7);
            emailErrorText.setText("");
        }
    }
    
    @FXML
    public void passwordCheckClickHandler (ActionEvent event) {
        if (passwordCheck.isSelected()) {
            passwordField.setDisable(false);
            repeatPasswordField.setDisable(false);
            passwordField.setOpacity(1);
            repeatPasswordField.setOpacity(1);
            passwordErrorText.setText("");
        }
        else {
            passwordField.setDisable(true);
            repeatPasswordField.setDisable(true);
            passwordField.setOpacity(0.7);
            repeatPasswordField.setOpacity(0.7);
            passwordErrorText.setText("");
        }
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
    
    private void onGetVehicles (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.SUCCESS) {
            Platform.runLater(() -> {
                allCustomerVehicles = (ArrayList<Vehicle>) response.getData();
                
                ObservableList<Vehicle> vehicles = (ObservableList<Vehicle>) allCustomerVehicles;
                vehiclesTable.setItems(vehicles);
            });
        }
    }
    
    @RequestCallback.Method
    private void onProfileUpdate (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.SUCCESS) {
            Platform.runLater(() -> {
                dialog.close();
                loader.hide();
                
                dialog.setTitleText("Edit Profile Success");
                dialog.setBodyText("Your profile has been updated successfully.");
                dialog.open();
                
                customer = editedCustomer;
                App.setEntity(customer);
                firstName.setText(customer.getFirstName());
                lastName.setText(customer.getLastName());
                email.setText(customer.getEmail());
            });
        }
        else {
            Platform.runLater(() -> {
                dialog.close();
                loader.hide();
                dialog.setTitleText("Something went wrong");
                dialog.setBodyText("An error occurred while updating your profile.", response.getMessage());
                dialog.open();
            });
        }
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    private void clearEditProfileForm () {
        nameCheck.setSelected(false);
        firstNameField.setText("");
        lastNameField.setText("");
        firstNameField.setDisable(true);
        lastNameField.setDisable(true);
        nameErrorText.setText("");
        nameErrorText.setVisible(true);
        
        emailCheck.setSelected(false);
        emailField.setText("");
        emailField.setDisable(true);
        emailErrorText.setText("");
        emailErrorText.setVisible(true);
        
        passwordCheck.setSelected(false);
        passwordField.setText("");
        repeatPasswordField.setText("");
        passwordField.setDisable(true);
        repeatPasswordField.setDisable(true);
        passwordErrorText.setText("");
        passwordErrorText.setVisible(true);
    }
}
