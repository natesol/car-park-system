package net.cps.client.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import net.cps.client.App;
import net.cps.client.CPSClient;
import net.cps.client.events.CustomerLoginEvent;
import net.cps.client.events.EmployeeLoginEvent;
import net.cps.client.events.ServerAuthEvent;
import net.cps.common.utils.RequestType;
import net.cps.common.utils.ResponseStatus;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class PCLoginController extends PageController {
    @FXML
    public MFXButton guestBtn;
    @FXML
    private MFXTextField emailField;
    @FXML
    private MFXPasswordField passwordField;
    @FXML
    public Hyperlink forgotPasswordLink;
    @FXML
    private MFXButton loginBtn;
    @FXML
    public Hyperlink signUpLink;
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        EventBus.getDefault().register(this);
    }
    
    
    /* ----- Event Handlers ----------------------------------------- */
    
    @FXML
    void guestBtnClickHandler (ActionEvent actionEvent) throws IOException {
        App.setScene("PCGuestMain.fxml");
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
        
        CPSClient.sendRequestToServer(RequestType.AUTH, "auth/" + email + "/" + password, "user login authentication", null);
    }
    
    @FXML
    void signUpLinkClickHandler (ActionEvent event) throws IOException {
        App.setScene("PCSignUp.fxml");
    }
    
    /* ----- Event Listeners ---------------------------------------- */
    
    @Subscribe
    public void onServerAuth (ServerAuthEvent event) {
        Platform.runLater(() -> {
            if (event.getResponse().getStatus() == ResponseStatus.SUCCESS) {
                try {
                    if (event.getResponse().getBody().equals("customer")) {
                        App.setScene("PCCustomerMain.fxml");
                        EventBus.getDefault().post(new CustomerLoginEvent(event.getResponse()));
                    }
                    else if (event.getResponse().getBody().equals("employee")) {
                        App.setScene("PCEmployeeMain.fxml");
                        EventBus.getDefault().post(new EmployeeLoginEvent(event.getResponse()));
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
                dialog.setBodyText(event.getResponse().getBody());
                dialog.open();
            }
        });
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    // ...
}
