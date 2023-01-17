package net.cps.client.controllers.pc.auth;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import net.cps.client.App;
import net.cps.client.utils.AbstractPageController;
import net.cps.client.events.CustomerLoginEvent;
import net.cps.client.events.EmployeeLoginEvent;
import net.cps.client.events.UserAuthEvent;
import net.cps.client.CPSClient;
import net.cps.common.entities.Customer;
import net.cps.common.entities.Employee;
import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.RequestType;
import net.cps.common.utils.RequestCallback;
import net.cps.common.utils.ResponseStatus;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class PCForgotPasswordController extends AbstractPageController {
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
        App.setPage("pc/auth/PCLogin.fxml");
    }
    
    @FXML
    public void sendBtnClickHandler (ActionEvent actionEvent) {
        String email = emailField.getText();
        CPSClient.sendRequestToServer(RequestType.AUTH, "forgot-password/email=" + email, this::onForgotPassword);
    }
    
    @FXML
    public void resetCodeBtnClickHandler (ActionEvent actionEvent) {
        dialog.setTitleText("Enter Reset Code");
        dialog.setBodyText("_ _ _ _ _ _");
        dialog.open();
    }
    
    @FXML
    public void loginLinkClickHandler (ActionEvent actionEvent) throws IOException {
        App.setPage("pc/auth/PCLogin.fxml");
    }
    
    
    /* ----- Event Listeners ---------------------------------------- */
    
    @Subscribe
    public void onServerAuth (UserAuthEvent event) {
        Platform.runLater(() -> {
            if (event.getResponse().getStatus() == ResponseStatus.SUCCESS) {
                try {
                    if (event.getResponse().getBody().equals("customer")) {
                        App.setPage("pc/auth/PCCustomerHome.fxml");
                        EventBus.getDefault().post(new CustomerLoginEvent((Customer) event.getResponse().getData()));
                    }
                    else if (event.getResponse().getBody().equals("employee")) {
                        App.setPage("pc/auth/PCEmployeeHome.fxml");
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
    
    
    @RequestCallback.Method
    public void onForgotPassword (RequestMessage request, ResponseMessage response) {
        Platform.runLater(() -> {
            if (response.getStatus() == ResponseStatus.SUCCESS) {
                dialog.setTitleText("Success");
                dialog.setBodyText("Reset code has been sent to your email. ", response.getMessage());
                dialog.open();
            }
            else {
                dialog.setTitleText("Error");
                dialog.setBodyText(response.getBody());
                dialog.open();
            }
        });
    }
    
    

    /* ----- Utility Methods ---------------------------------------- */
    
    // ...
}
