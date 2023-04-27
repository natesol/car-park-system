package net.cps.client.utils;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import net.cps.client.App;
import net.cps.client.CPSClient;
import net.cps.client.events.CustomerLoginEvent;
import net.cps.common.entities.Customer;
import net.cps.common.utils.RequestType;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public abstract class AbstractPCCustomerPageController extends AbstractPageController {
    protected Customer customer;
    
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
    public MFXButton menuBtnLogout;
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        EventBus.getDefault().register(this);
        customer = (Customer) App.getEntity();
    
        Platform.runLater(() -> {
            customerName.setText(customer.getFirstName() + " " + customer.getLastName());
        });
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
            confirmBtn.getStyleClass().add("button-danger-filled");
            MFXButton cancelBtn = new MFXButton("Cancel");
            cancelBtn.setOnAction(event -> dialog.close());
            cancelBtn.getStyleClass().add("button-base-outlined");
            
            dialog.setWidth(Dialog.Width.EXTRA_SMALL);
            dialog.setTitleText("Log Out");
            dialog.setBodyText("Are you sure you want to log out?");
            dialog.setActionButtons(cancelBtn, confirmBtn);
            dialog.open();
        });
    }
    
    
    /* ----- Event Bus Listeners ------------------------------------ */
    
    @Subscribe
    public void onCustomerLogin (@NotNull CustomerLoginEvent event) throws IOException {
        customer = event.getCustomer();
        App.setEntity(customer);
        
        Platform.runLater(() -> {
            customerName.setText(customer.getFirstName() + " " + customer.getLastName());
        });
    }
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    // ...
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    // ...
    
}