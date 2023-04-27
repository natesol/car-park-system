package net.cps.client.utils;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import net.cps.client.App;
import net.cps.client.CPSClient;
import net.cps.client.events.EmployeeLoginEvent;
import net.cps.common.entities.Employee;
import net.cps.common.utils.EmployeeRole;
import net.cps.common.utils.RequestType;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public abstract class AbstractPCEmployeePageController extends AbstractPageController {
    protected Employee employee;
    protected EmployeeRole role;
    
    @FXML
    public Text employeeName;
    @FXML
    public MFXButton menuBtnHome;
    @FXML
    public MFXButton menuBtnParkingLots;
    @FXML
    public MFXButton menuBtnRates;
    @FXML
    public MFXButton menuBtnParkingSpaces;
    @FXML
    public MFXButton menuBtnEmployees;
    @FXML
    public MFXButton menuBtnReports;
    @FXML
    public MFXButton menuBtnStatistics;
    @FXML
    public MFXButton menuBtnComplaints;
    @FXML
    public MFXButton menuBtnProfile;
    @FXML
    public MFXButton menuBtnLogout;
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        
        EventBus.getDefault().register(this);
        
        Platform.runLater(() -> {
            employee = (Employee) App.getEntity();
            role = employee.getRole();
            
            employeeName.setText(employee.getFirstName() + " " + employee.getLastName() + " (" + EmployeeRole.toString(role) + ")");
        });
    }
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void menuBtnHomeClickHandler (MouseEvent mouseEvent) throws IOException {
        if (menuBtnHome.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                App.setPage("pc/employee/" + role.toPath() + "/PCEmployee" + role.toInitials() + "Home.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    @FXML
    public void menuBtnParkingLotsClickHandler (MouseEvent mouseEvent) {
        if (menuBtnParkingLots.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                App.setPage("pc/employee/" + role.toPath() + "/PCEmployee" + role.toInitials() + "ParkingLots.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    @FXML
    public void menuBtnRatesClickHandler (MouseEvent mouseEvent) {
        if (menuBtnRates.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                App.setPage("pc/employee/" + role.toPath() + "/PCEmployee" + role.toInitials() + "Rates.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    @FXML
    public void menuBtnParkingSpacesClickHandler (MouseEvent mouseEvent) {
        if (menuBtnParkingSpaces.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                App.setPage("pc/employee/" + role.toPath() + "/PCEmployee" + role.toInitials() + "ParkingSpaces.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    @FXML
    public void menuBtnReportsClickHandler (MouseEvent mouseEvent) {
        if (menuBtnReports.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                App.setPage("pc/employee/" + role.toPath() + "/PCEmployee" + role.toInitials() + "Reports.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    @FXML
    public void menuBtnStatisticsClickHandler (MouseEvent mouseEvent) {
        if (menuBtnStatistics.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                App.setPage("pc/employee/" + role.toPath() + "/PCEmployee" + role.toInitials() + "Statistics.fxml");
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
                App.setPage("pc/employee/" + role.toPath() + "/PCEmployee" + role.toInitials() + "Complaints.fxml");
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
                App.setPage("pc/employee/" + role.toPath() + "/PCEmployee" + role.toInitials() + "Profile.fxml");
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
                    CPSClient.sendRequestToServer(RequestType.AUTH, "logout/email=" + employee.getEmail(), "employee initialized logout.", employee, null);
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
    public void onEmployeeLogin (EmployeeLoginEvent event) throws IOException {
        employee = event.getEmployee();
        
        Platform.runLater(() -> {
            App.setEntity(employee);
            role = employee.getRole();
            
            employeeName.setText(employee.getFirstName() + " " + employee.getLastName() + " (" + EmployeeRole.toString(role) + ")");
        });
    }
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    // ...
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    // ...
    
}