package net.cps.client.controllers.pc.auth;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import net.cps.client.App;
import net.cps.client.CPSClient;
import net.cps.client.utils.AbstractPageController;
import net.cps.common.entities.Customer;
import net.cps.common.entities.Employee;
import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.Entities;
import net.cps.common.utils.RequestCallback;
import net.cps.common.utils.RequestType;
import net.cps.common.utils.ResponseStatus;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class PCForgotPasswordController extends AbstractPageController implements Initializable {
    @FXML
    public MFXButton goBackBtn;
    @FXML
    public MFXButton toggleThemeBtn;
    @FXML
    public MFXTextField emailField;
    @FXML
    public MFXButton sendBtn;
    @FXML
    public MFXButton resetCodeBtn;
    @FXML
    public Hyperlink loginLink;
    
    private String resetCode;
    private String userType;
    private Object user;
    
    
    /* ---- JavaFX Initialization ----------------------------------- */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {}
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void goBackBtnClickHandler (ActionEvent actionEvent) throws IOException {
        App.setPage("pc/auth/PCLogin.fxml");
    }
    
    @FXML
    public void sendBtnClickHandler (ActionEvent actionEvent) {
        String email = emailField.getText();
        
        if (email.isEmpty()) {
            dialog.setTitleText("Reset Password Error");
            dialog.setBodyText("Please fill your email address.");
            dialog.open();
            return;
        }
        if (!(email.contains("@") && email.contains("."))) {
            dialog.setTitleText("Reset Password Error");
            dialog.setBodyText("Please enter a valid email address.");
            dialog.open();
            return;
        }
        
        loader.show();
        CPSClient.sendRequestToServer(RequestType.AUTH, "forgot-password/email=" + email, this::onForgotPassword);
    }
    
    @FXML
    public void resetCodeBtnClickHandler (ActionEvent actionEvent) {
        Platform.runLater(() -> {
            final String EMPTY_RESET_CODE = "Please enter your reset code.";
            final String WRONG_RESET_CODE = "Wrong reset code, please try again.";
            
            Text errorText = new Text(EMPTY_RESET_CODE);
            errorText.getStyleClass().add("fs-xs");
            errorText.getStyleClass().add("text-danger");
            errorText.setVisible(false);
            
            MFXTextField resetCodeField = new MFXTextField();
            resetCodeField.setFloatMode(FloatMode.INLINE);
            resetCodeField.setFloatingText("Reset Code");
            resetCodeField.setTextLimit(6);
            resetCodeField.setMinHeight(45);
            resetCodeField.setOnAction((event) -> {
                if (errorText.isVisible()) {
                    errorText.setVisible(false);
                }
            });

            TextFlow textFlow = new TextFlow(errorText);
            VBox vBox = new VBox(resetCodeField, textFlow);
            vBox.setAlignment(javafx.geometry.Pos.CENTER);
            vBox.setSpacing(10);
            VBox.setMargin(resetCodeField, new Insets(20, 0, 0, 0));
            VBox.setMargin(textFlow, new Insets(0, 0, 20, 0));
            
            MFXButton resetBtn = new MFXButton("Verify Code");
            resetBtn.getStyleClass().add("button-primary-filled");
            resetBtn.setOnAction(event -> {
                errorText.setVisible(false);
                if (resetCodeField.getText().isEmpty()) {
                    errorText.setText(EMPTY_RESET_CODE);
                    errorText.setVisible(true);
                }
                else if (resetCodeField.getText().equals(this.resetCode)) {
                    changePasswordBtnClickHandler(event);
                }
                else {
                    errorText.setText(WRONG_RESET_CODE);
                    errorText.setVisible(true);
                }
            });
            MFXButton cancelBtn = new MFXButton("Cancel");
            cancelBtn.getStyleClass().add("button-base-outlined");
            cancelBtn.setOnAction(event -> dialog.close());
            
            dialog.setTitleText("Enter Reset Code");
            dialog.setBodyText("Please enter the reset code we sent to your email.", "If you didn't receive the code yet, try to check your spam folder.");
            dialog.setCustomContent(vBox);
            dialog.setActionButtons(cancelBtn, resetBtn);
            dialog.open();
        });
    }
    
    @FXML
    public void changePasswordBtnClickHandler (ActionEvent actionEvent) {
        Platform.runLater(() -> {
            MFXPasswordField passwordField = new MFXPasswordField();
            passwordField.setFloatMode(FloatMode.BORDER);
            passwordField.setFloatingText("Password");
            passwordField.setMinHeight(45);
            passwordField.setPrefWidth(300);
            
            VBox passwordBox = new VBox(passwordField);
            VBox.setMargin(passwordField, new Insets(10, 0, 10, 0));
            
            MFXButton resetBtn = new MFXButton("Reset Password");
            resetBtn.getStyleClass().add("button-primary-filled");
            resetBtn.setOnAction(event -> {
                String password = passwordField.getText();
                if (userType.equals(Entities.CUSTOMER.getClassName())) {
                    Customer customer = (Customer) user;
                    customer.setPassword(password);
                    loader.show();
                    
                    CPSClient.sendRequestToServer(RequestType.UPDATE, Entities.CUSTOMER.getTableName() + "/password=" + password, "null", customer, this::onPasswordReset);
                }
                else {
                    Employee employee = (Employee) user;
                    employee.setPassword(password);
                    loader.show();
                    
                    CPSClient.sendRequestToServer(RequestType.UPDATE, Entities.EMPLOYEE.getTableName() + "/password=" + password, "null", employee, this::onPasswordReset);
                }
            });
            
            dialog.setTitleText("Enter New Password");
            dialog.setBodyText("Please enter your new password.", "Make sure you remember it, so you can login next time.");
            dialog.setCustomContent(passwordBox);
            dialog.setActionButtons(resetBtn);
            dialog.open();
        });
    }
    
    @FXML
    public void loginLinkClickHandler (ActionEvent actionEvent) throws IOException {
        App.setPage("pc/auth/PCLogin.fxml");
    }
    
    
    /* ----- Event Bus Listeners ------------------------------------ */
    
    // ...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    @RequestCallback.Method
    public void onForgotPassword (RequestMessage request, ResponseMessage response) {
        Platform.runLater(() -> {
            loader.hide();
            
            if (response.getStatus() == ResponseStatus.SUCCESS) {
                userType = response.getMessage().split("/")[0];
                resetCode = response.getMessage().split("/")[1];
                user = response.getData();
                
                MFXButton confirmBtn = new MFXButton("Confirm");
                confirmBtn.getStyleClass().add("button-primary-filled");
                confirmBtn.setOnAction(this::resetCodeBtnClickHandler);
                
                dialog.setTitleText("Reset Code Sent");
                dialog.setBodyText("Reset code has been successfully sent to your email. ", "Please do not close this window, the reset code is temporary.");
                dialog.setActionButtons(confirmBtn);
                dialog.open();
            }
            else {
                dialog.setTitleText("Something Went Wrong");
                dialog.setBodyText(response.getMessage());
                dialog.setActionButtons((Node) null);
                dialog.open();
            }
        });
    }
    
    @RequestCallback.Method
    public void onPasswordReset (RequestMessage request, ResponseMessage response) {
        Platform.runLater(() -> {
            loader.hide();
            
            if (response.getStatus() == ResponseStatus.FINISHED) {
                dialog.close();
                dialog.setTitleText("Password Reset");
                dialog.setBodyText("Your password has been reset successfully.", "You can now login with your new password.");
                MFXButton confirmBtn = new MFXButton("Confirm");
                confirmBtn.getStyleClass().add("button-primary-filled");
                confirmBtn.setOnAction(e -> {
                    dialog.close();
                    try {
                        App.setPage("pc/auth/PCLogin.fxml");
                    }
                    catch (IOException err) {
                        err.printStackTrace();
                    }
                });
                MFXButton closeBtn = new MFXButton("Close");
                closeBtn.getStyleClass().add("button-base-outlined");
                closeBtn.setOnAction(e -> dialog.close());
                dialog.setActionButtons(closeBtn, confirmBtn);
                dialog.open();
            }
            else {
                dialog.setTitleText("Password Reset");
                dialog.setBodyText("An error occurred while resetting your password.", "Please try again later.");
                MFXButton confirmBtn = new MFXButton("Confirm");
                confirmBtn.getStyleClass().add("button-primary-filled");
                confirmBtn.setOnAction(e -> dialog.close());
                dialog.setActionButtons(confirmBtn);
                dialog.open();
            }
        });
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    // ...
    
}
