package net.cps.client.controllers.pc.auth;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import net.cps.client.App;
import net.cps.client.CPSClient;
import net.cps.client.events.CustomerLoginEvent;
import net.cps.client.events.EmployeeLoginEvent;
import net.cps.client.events.UserAuthEvent;
import net.cps.client.utils.AbstractPageController;
import net.cps.common.entities.Customer;
import net.cps.common.entities.Employee;
import net.cps.common.utils.EmployeeRole;
import net.cps.common.utils.Entities;
import net.cps.common.utils.RequestType;
import net.cps.common.utils.ResponseStatus;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class PCLoginController extends AbstractPageController implements Initializable {
    @FXML
    public MFXButton goBackBtn;
    @FXML
    public MFXButton toggleThemeBtn;
    @FXML
    public MFXButton guestBtn;
    @FXML
    public MFXTextField emailField;
    @FXML
    public MFXPasswordField passwordField;
    @FXML
    public Hyperlink forgotPasswordLink;
    @FXML
    public MFXButton loginBtn;
    @FXML
    public Hyperlink signUpLink;
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        EventBus.getDefault().register(this);
    }
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    void guestBtnClickHandler (ActionEvent actionEvent) throws IOException {
        App.setPage("pc/guest/PCGuestHome.fxml");
        App.setEntity(null);
    }
    
    @FXML
    void loginBtnClickHandler (ActionEvent event) throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();
        
        if (email.isEmpty() && password.isEmpty()) {
            dialog.setTitleText("Login Error");
            dialog.setBodyText("Please fill your email and password to login.");
            dialog.open();
            return;
        }
        if (email.isEmpty()) {
            dialog.setTitleText("Login Error");
            dialog.setBodyText("Please insert your email to login.");
            dialog.open();
            return;
        }
        if (password.isEmpty()) {
            dialog.setTitleText("Login Error");
            dialog.setBodyText("Please insert your password to login.");
            dialog.open();
            return;
        }
        if (!(email.contains("@") && email.contains("."))) {
            dialog.setTitleText("Login Error");
            dialog.setBodyText("Please enter a valid email address.");
            dialog.open();
            return;
        }
        
        loader.show();
        CPSClient.sendRequestToServer(RequestType.AUTH, "login/email=" + email + "&password=" + password, "user login authentication", null);
    }
    
    @FXML
    public void forgotPasswordLinkClickHandler (ActionEvent event) throws IOException {
        App.setPage("pc/auth/PCForgotPassword.fxml");
    }
    
    @FXML
    void signUpLinkClickHandler (ActionEvent event) throws IOException {
        App.setPage("pc/auth/PCSignUp.fxml");
    }
    
    
    /* ----- Event Bus Listeners ------------------------------------ */
    
    @Subscribe
    public void onServerAuth (UserAuthEvent event) {
        ResponseStatus status = event.getResponse().getStatus();
        String message = event.getMessage();
        
        Platform.runLater(() -> {
            loader.hide();
            
            if (status == ResponseStatus.SUCCESS) {
                try {
                    if (message.equals(Entities.CUSTOMER.getClassName())) {
                        Customer customer = (Customer) event.getResponse().getData();
                        App.setPage("pc/customer/PCCustomerHome.fxml");
                        App.setEntity(customer);
                        EventBus.getDefault().post(new CustomerLoginEvent(customer));
                    }
                    else if (message.equals(Entities.EMPLOYEE.getClassName())) {
                        Employee employee = (Employee) event.getResponse().getData();
                        EmployeeRole role = employee.getRole();
                        App.setPage("pc/employee/" + role.toPath() + "/PCEmployee" + role.toInitials() + "Home.fxml");
                        App.setEntity(employee);
                        EventBus.getDefault().post(new EmployeeLoginEvent(employee));
                    }
                    else {
                        dialog.setTitleText("Login Error");
                        dialog.setBodyText("Something went wrong, please try again later.", "If the problem persists, please contact our support.");
                        dialog.open();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                dialog.setTitleText("Login Error");
                dialog.setBodyText(message);
                dialog.open();
            }
        });
    }
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    // ...
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    // ...
    
}
