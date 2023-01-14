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
import net.cps.client.events.EmployeeLoginEvent;
import net.cps.client.utils.ResourcesLoader;
import net.cps.common.entities.Employee;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class PCEmployeeMainController extends PageController {
    private Employee employee;
    
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
                setSubPage("PCEmployeeMainHome.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            
            activateMenuBtn(menuBtnHome);
        });
    }
    
    
    
    /* ----- Event Handlers ----------------------------------------- */
    
    @Subscribe
    public void onEmployeeLogin (EmployeeLoginEvent event) throws IOException {
        employee = event.getEmployee();
        String role = employee.getRole().toString();
        
        Platform.runLater(() -> {
            customerFirstName.setText(employee.getFirstName());
            customerLastName.setText(employee.getLastName() + " (" + role + ")");
    
            switch (role) {
                case "ADMIN" -> {
                    menuBtnSubscriptions.setVisible(false);
                    menuBtnReservations.setVisible(false);
                    menuBtnComplaints.setVisible(false);
                }
                case "MANAGER" -> {
                    menuBtnSubscriptions.setVisible(false);
                    menuBtnReservations.setVisible(false);
                }
                case "EMPLOYEE" -> menuBtnSubscriptions.setVisible(false);
            }
        });
        
    }
    
    @FXML
    public void menuBtnHomeClickHandler (MouseEvent mouseEvent) throws IOException {
        setSubPage("PCEmployeeMainHome.fxml");
        activateMenuBtn(menuBtnHome);
    }
    
    @FXML
    public void menuBtnSubscriptionsClickHandler (MouseEvent mouseEvent) throws IOException {
        setSubPage("PCEmployeeMainSubscriptions.fxml");
        activateMenuBtn(menuBtnSubscriptions);
    }
    
    @FXML
    public void menuBtnReservationsClickHandler (MouseEvent mouseEvent) throws IOException {
        setSubPage("PCEmployeeMainReservations.fxml");
        activateMenuBtn(menuBtnReservations);
    }
    
    @FXML
    public void menuBtnComplaintsClickHandler (MouseEvent mouseEvent) throws IOException {
        setSubPage("PCEmployeeMainComplaints.fxml");
        activateMenuBtn(menuBtnComplaints);
    }
    
    @FXML
    public void menuBtnProfileClickHandler (MouseEvent mouseEvent) throws IOException {
        setSubPage("PCEmployeeMainProfile.fxml");
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
                    App.setPage("PCLogin.fxml");
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
