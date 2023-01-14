package net.cps.client.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import net.cps.client.App;
import net.cps.client.events.CustomerLoginEvent;
import net.cps.client.events.EmployeeLoginEvent;
import net.cps.client.events.UserAuthEvent;
import net.cps.common.entities.Customer;
import net.cps.common.entities.Employee;
import net.cps.common.utils.ResponseStatus;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class PCForgotPasswordController extends PageController {
    @FXML
    public HBox goBackBtn;
    @FXML
    public MFXTextField emailField;
    @FXML
    public MFXButton sendBtn;
    @FXML
    public MFXButton resetCodeBtn;
    @FXML
    public Hyperlink loginLink;
    
    
    /* ---- JavaFX Initialization ----------------------------------- */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        EventBus.getDefault().register(this);
    }
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void goBackBtnClickHandler (ActionEvent event) throws IOException {
        App.setPage("PCLogin.fxml");
    }
    
    @FXML
    public void sendBtnClickHandler (ActionEvent actionEvent) {
        String email = emailField.getText();
        
        //CPSClient.sendRequest(RequestType.FORGOT_PASSWORD, emailField.getText());
    }
    
    @FXML
    public void resetCodeBtnClickHandler (ActionEvent actionEvent) {
        dialog.setTitleText("Enter Reset Code");
        dialog.setBodyText("_ _ _ _ _ _");
        dialog.open();
    }
    
    @FXML
    public void loginLinkClickHandler (ActionEvent actionEvent) throws IOException {
        App.setPage("PCLogin.fxml");
    }
    
    
    /* ----- Event Listeners ---------------------------------------- */
    
    @Subscribe
    public void onServerAuth (UserAuthEvent event) {
        Platform.runLater(() -> {
            if (event.getResponse().getStatus() == ResponseStatus.SUCCESS) {
                try {
                    if (event.getResponse().getBody().equals("customer")) {
                        App.setPage("PCCustomerMain.fxml");
                        EventBus.getDefault().post(new CustomerLoginEvent((Customer) event.getResponse().getData()));
                    }
                    else if (event.getResponse().getBody().equals("employee")) {
                        App.setPage("PCEmployeeMain.fxml");
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
                dialog.setBodyText(event.getResponse().getBody());
                dialog.open();
            }
        });
    }
    

    /* ----- Utility Methods ---------------------------------------- */
    
    // ...
}
