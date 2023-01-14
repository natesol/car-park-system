package net.cps.client.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.cps.client.App;
import net.cps.client.CPSClient;
import net.cps.client.events.CustomerLoginEvent;
import net.cps.client.utils.ResourcesLoader;
import net.cps.common.entities.Customer;
import net.cps.common.utils.RequestType;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class PCCustomerMainController extends PageController {
    private Customer customer;
    
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
    public Pane subPageWrapper;
    @FXML
    public HBox subPageBody;
    @FXML
    public VBox dashboardLeft;
    
    /* Home Sub Page */
    
    /* Subscriptions Sub Page */
    
    /* Reservations Sub Page */
    @FXML
    public MFXButton newReservationBtn;
    
    /* Complaints Sub Page */
    @FXML
    public MFXButton fileComplaintBtn;
    
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        EventBus.getDefault().register(this);
        
        Platform.runLater(() -> {
            try {
                setSubPage("PCCustomerMainHome.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            
            activateMenuBtn(menuBtnHome);
        });
    }
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void menuBtnHomeClickHandler (MouseEvent mouseEvent) throws IOException {
        if (menuBtnHome.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                setSubPage("PCCustomerMainHome.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            activateMenuBtn(menuBtnHome);
        });
    }
    
    @FXML
    public void menuBtnSubscriptionsClickHandler (MouseEvent mouseEvent) throws IOException {
        if (menuBtnSubscriptions.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                setSubPage("PCCustomerMainSubscriptions.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            activateMenuBtn(menuBtnSubscriptions);
        });
    }
    
    @FXML
    public void menuBtnReservationsClickHandler (MouseEvent mouseEvent) throws IOException {
        if (menuBtnReservations.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                setSubPage("PCCustomerMainReservations.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            activateMenuBtn(menuBtnReservations);
        });
    }
    
    @FXML
    public void menuBtnComplaintsClickHandler (MouseEvent mouseEvent) throws IOException {
        if (menuBtnComplaints.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                setSubPage("PCCustomerMainComplaints.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            activateMenuBtn(menuBtnComplaints);
        });
    }
    
    @FXML
    public void menuBtnProfileClickHandler (MouseEvent mouseEvent) throws IOException {
        if (menuBtnProfile.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                setSubPage("PCCustomerMainProfile.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            activateMenuBtn(menuBtnProfile);
        });
    }
    
    @FXML
    public void menuBtnLogoutClickHandler (MouseEvent mouseEvent) throws IOException {
        Platform.runLater(() -> {
            MFXButton confirmBtn = new MFXButton("Log Out");
            confirmBtn.setOnAction(event -> {
                dialog.close();
                try {
                    CPSClient.sendRequestToServer(RequestType.AUTH, "logout/" + customer.getEmail(), "customer initialized logout.", customer, null);
                    App.setEntity(null);
                    App.setPage("PCLogin.fxml");
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
    
    
    /* ----- EventBus Listeners ------------------------------------- */
    
    @Subscribe
    public void onCustomerLogin (CustomerLoginEvent event) throws IOException {
        customer = event.getCustomer();
        App.setEntity(customer);
        
        Platform.runLater(() -> {
            customerName.setText(customer.getFirstName() + " " + customer.getLastName());
        });
    }
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    // ...
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    private void setSubPage (String fxml) throws IOException {
        Platform.runLater(() -> {
            try {
                subPageWrapper.getChildren().clear();
                subPageWrapper.getChildren().add(ResourcesLoader.loadFXML(fxml));
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    private void setSubPage (Pane pane) {
        Platform.runLater(() -> {
            subPageWrapper.getChildren().clear();
            subPageWrapper.getChildren().add(pane);
        });
    }
    
    private void activateMenuBtn (@NotNull MFXButton btn) {
        Platform.runLater(() -> {
            dashboardLeft.lookupAll(".dashboard-menu-button.active").forEach(node -> node.getStyleClass().remove("active"));
            btn.getStyleClass().add("active");
        });
    }
}
