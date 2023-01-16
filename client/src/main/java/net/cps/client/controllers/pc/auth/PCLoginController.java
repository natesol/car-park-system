package net.cps.client.controllers.pc.auth;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import net.cps.client.App;
import net.cps.client.CPSClient;
import net.cps.client.utils.AbstractPageController;
import net.cps.client.events.CustomerLoginEvent;
import net.cps.client.events.EmployeeLoginEvent;
import net.cps.client.events.UserAuthEvent;
import net.cps.common.entities.Customer;
import net.cps.common.entities.Employee;
import net.cps.common.utils.Entities;
import net.cps.common.utils.RequestType;
import net.cps.common.utils.ResponseStatus;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class PCLoginController extends AbstractPageController {
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
    }
    
    @FXML
    void loginBtnClickHandler (ActionEvent event) throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();
        
        if (email.isEmpty() || password.isEmpty()) {
            dialog.setTitleText("Error");
            dialog.setBodyText("Please fill all the fields");
            dialog.open();
            return;
        }
        if (!(email.contains("@") && email.contains("."))) {
            dialog.setTitleText("Error");
            dialog.setBodyText("Please enter a valid email");
            dialog.open();
            return;
        }
        
        CPSClient.sendRequestToServer(RequestType.AUTH, "login/email=" + email + "&password=" + password, "user login authentication", null);
    }
    
    @FXML
    public void forgotPasswordLinkClickHandler (MouseEvent mouseEvent) throws IOException {
        App.setPage("pc/auth/PCForgotPassword.fxml");
    }
    
    @FXML
    void signUpLinkClickHandler (ActionEvent event) throws IOException {
        App.setPage("pc/auth/PCSignUp.fxml");
    }
    
    
    /* ----- EventBus Listeners ------------------------------------- */
    
    @Subscribe
    public void onServerAuth (UserAuthEvent event) {
        ResponseStatus status = event.getResponse().getStatus();
        String message = event.getMessage();
        
        Platform.runLater(() -> {
            if (status == ResponseStatus.SUCCESS) {
                try {
                    if (message.equals(Entities.CUSTOMER.getClassName())) {
                        App.setPage("pc/customer/PCCustomerHome.fxml");
                        App.setEntity(event.getResponse().getData());
                        EventBus.getDefault().post(new CustomerLoginEvent((Customer) event.getResponse().getData()));
                    }
                    else if (message.equals(Entities.EMPLOYEE.getClassName())) {
                        App.setPage("pc/employee/PCEmployeeHome.fxml");
                        App.setEntity(event.getResponse().getData());
                        EventBus.getDefault().post(new EmployeeLoginEvent((Employee) event.getResponse().getData()));
                    }
                    else {
                        dialog.setTitleText("Error");
                        dialog.setBodyText("Something went wrong");
                        dialog.open();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                dialog.setTitleText("Error");
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
