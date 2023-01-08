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
import net.cps.client.events.CustomerLoginEvent;
import net.cps.client.utils.ResourcesLoader;
import net.cps.common.entities.Customer;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class PCCustomerMainController extends PageController {
    private Customer customer;
    
    @FXML
    public Text customerFirstName;
    @FXML
    public Text customerLastName;
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
    
    
    
    /* ----- Event Handlers ----------------------------------------- */
    
    @Subscribe
    public void onCustomerLogin (CustomerLoginEvent event) throws IOException {
        customer = (Customer) event.getResponse().getData();
        
        customerFirstName.setText(customer.getFirstName());
        customerLastName.setText(customer.getLastName());
    }
    
    @FXML
    public void menuBtnHomeClickHandler (MouseEvent mouseEvent) throws IOException {
        setSubPage("PCCustomerMainHome.fxml");
        activateMenuBtn(menuBtnHome);
    }
    
    @FXML
    public void menuBtnSubscriptionsClickHandler (MouseEvent mouseEvent) throws IOException {
        setSubPage("PCCustomerMainSubscriptions.fxml");
        activateMenuBtn(menuBtnSubscriptions);
    }
    
    @FXML
    public void menuBtnReservationsClickHandler (MouseEvent mouseEvent) throws IOException {
        setSubPage("PCCustomerMainReservations.fxml");
        activateMenuBtn(menuBtnReservations);
    }
    
    @FXML
    public void menuBtnComplaintsClickHandler (MouseEvent mouseEvent) throws IOException {
        setSubPage("PCCustomerMainComplaints.fxml");
        activateMenuBtn(menuBtnComplaints);
    }
    
    @FXML
    public void menuBtnProfileClickHandler (MouseEvent mouseEvent) throws IOException {
        setSubPage("PCCustomerMainProfile.fxml");
        activateMenuBtn(menuBtnProfile);
    }
    
    @FXML
    public void menuBtnSignOutClickHandler (MouseEvent mouseEvent) throws IOException {
        Platform.runLater(() -> {
            dialog.setTitleText("Log Out");
            dialog.setBodyText("Are you sure you want to log out?");
            dialog.setWidth("xs");
            MFXButton confirmBtn = new MFXButton("Log Out");
            confirmBtn.setOnAction(event -> {
                dialog.close();
                try {
                    App.setScene("PCLogin.fxml");
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            confirmBtn.getStyleClass().add("button-primary");
            MFXButton cancelBtn = new MFXButton("Cancel");
            cancelBtn.setOnAction(event -> dialog.close());
            dialog.setActionButtons(cancelBtn, confirmBtn);
            dialog.open();
        });
    }
    
    
    /* ----- Utility Methods ----------------------- */
    
    public void setSubPage (String fxml) throws IOException {
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
    
    public void setSubPage (Pane pane) {
        Platform.runLater(() -> {
            subPageWrapper.getChildren().clear();
            subPageWrapper.getChildren().add(pane);
        });
    }
    
    public void activateMenuBtn (@NotNull MFXButton btn) {
        Platform.runLater(() -> {
            dashboardLeft.lookupAll(".dashboard-menu-button.active").forEach(node -> node.getStyleClass().remove("active"));
            btn.getStyleClass().add("active");
        });
    }
}