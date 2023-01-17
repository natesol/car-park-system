package net.cps.client.controllers.pc.employee;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import net.cps.client.App;
import net.cps.client.CPSClient;
import net.cps.client.utils.AbstractPageController;
import net.cps.common.entities.Employee;
import net.cps.common.utils.EmployeeRole;
import net.cps.common.utils.RequestType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class PCEmployeeComplaintsController extends AbstractPageController {
    private Employee employee;
    private EmployeeRole role;
    
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
    public MFXButton menuBtnReports;
    @FXML
    public MFXButton menuBtnStatistics;
    @FXML
    public MFXButton menuBtnComplaints;
    @FXML
    public MFXButton menuBtnProfile;
    @FXML
    public MFXButton menuBtnLogout;
    
    @FXML
    public MFXButton mainActionBtn;
    @FXML
    public Text bodyText;
    @FXML
    public MFXButton secondaryActionBtn;
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            employee = (Employee) App.getEntity();
            role = employee.getRole();
    
            setPageForRole(role);
            employeeName.setText(employee.getFirstName() + " " + employee.getLastName() + " (" + EmployeeRole.toString(role) + ")");
        });
    }
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void menuBtnHomeClickHandler (MouseEvent mouseEvent) throws IOException {
        if (menuBtnHome.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                App.setPage("pc/employee/PCEmployeeHome.fxml");
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
                App.setPage("pc/employee/PCEmployeeParkingLots.fxml");
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
                App.setPage("pc/employee/PCEmployeeRates.fxml");
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
                App.setPage("pc/employee/PCEmployeeParkingSpaces.fxml");
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
                App.setPage("pc/employee/PCEmployeeReports.fxml");
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
                App.setPage("pc/employee/PCEmployeeStatistics.fxml");
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
                App.setPage("pc/employee/PCEmployeeComplaints.fxml");
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
                App.setPage("pc/employee/PCEmployeeProfile.fxml");
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
    
    
    /* ----- Event Bus Listeners ------------------------------------ */
    
    // ...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    //...
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    private void hideMenuButton (@NotNull Node button) {
        button.setDisable(true);
        button.setVisible(false);
        button.setManaged(false);
    }
    
    private void showMenuButton (@NotNull Node button) {
        button.setDisable(false);
        button.setVisible(true);
        button.setManaged(true);
    }
    
    private void setPageForRole (@NotNull EmployeeRole role) {
        Platform.runLater(() -> {
            switch (role) {
                case NETWORK_MANAGER -> {
                    hideMenuButton(menuBtnParkingLots);
                    showMenuButton(menuBtnRates);
                    showMenuButton(menuBtnParkingSpaces);
                    showMenuButton(menuBtnReports);
                    showMenuButton(menuBtnStatistics);
                    hideMenuButton(menuBtnComplaints);
                }
                case CUSTOMER_SERVICE_EMPLOYEE -> {
                    hideMenuButton(menuBtnParkingLots);
                    hideMenuButton(menuBtnRates);
                    showMenuButton(menuBtnParkingSpaces);
                    hideMenuButton(menuBtnReports);
                    hideMenuButton(menuBtnStatistics);
                    showMenuButton(menuBtnComplaints);
                }
                case PARKING_LOT_MANAGER -> {
                    hideMenuButton(menuBtnParkingLots);
                    showMenuButton(menuBtnRates);
                    showMenuButton(menuBtnParkingSpaces);
                    showMenuButton(menuBtnReports);
                    hideMenuButton(menuBtnStatistics);
                    hideMenuButton(menuBtnComplaints);
                }
                case PARKING_LOT_EMPLOYEE -> {
                    hideMenuButton(menuBtnParkingLots);
                    hideMenuButton(menuBtnRates);
                    showMenuButton(menuBtnParkingSpaces);
                    hideMenuButton(menuBtnReports);
                    hideMenuButton(menuBtnStatistics);
                    hideMenuButton(menuBtnComplaints);
                }
            }
        });
    }
    
}

