package net.cps.client.controllers.pc.auth;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import net.cps.client.App;
import net.cps.client.CPSClient;
import net.cps.client.events.CustomerLoginEvent;
import net.cps.client.events.EmployeeLoginEvent;
import net.cps.client.events.UserAuthEvent;
import net.cps.client.utils.AbstractPageController;
import net.cps.common.entities.Customer;
import net.cps.common.entities.Employee;
import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.Entities;
import net.cps.common.utils.RequestCallback;
import net.cps.common.utils.RequestType;
import net.cps.common.utils.ResponseStatus;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



public class PCForgotPasswordController extends AbstractPageController {
    String resetCode;
    String userType;
    Object user;
    
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
    public void initialize (URL url, ResourceBundle resourceBundle) {}
    
    
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
        Platform.runLater(() -> {
            dialog.close();
            
            MFXTextField resetCodeField = new MFXTextField();
            resetCodeField.setFloatMode(FloatMode.DISABLED);
            resetCodeField.setTextLimit(6);
            resetCodeField.setMinHeight(45);
    
            Text errorText = new Text("Wrong reset code, please try again.");
            errorText.setStyle("-fx-fill: #730721; -fx-text-fill: #730721; -fx-font-size: 12px;");
            errorText.setVisible(false);
            
            TextFlow textFlow = new TextFlow(errorText);
            
            VBox vBox = new VBox(resetCodeField, textFlow);
            vBox.setSpacing(10);
            VBox.setMargin(resetCodeField, new Insets(20, 0, 0, 0));
            
            MFXButton resetBtn = new MFXButton("Reset");
            resetBtn.getStyleClass().add("button-primary");
            resetBtn.setOnAction(event -> {
                if (resetCodeField.getText().equals(this.resetCode)) {
                    changePasswordBtnClickHandler(event);
                }
                else {
                    errorText.setVisible(true);
                }
            });
            MFXButton cancelBtn = new MFXButton("Cancel");
            cancelBtn.getStyleClass().add("button-secondary");
            cancelBtn.setOnAction(event -> dialog.close());

            dialog.setTitleText("Enter Reset Code");
            dialog.setBodyText("Please enter the reset code sent to your email.");
            dialog.setCustomContent(vBox);
            dialog.setActionButtons(cancelBtn, resetBtn);
            dialog.open();
        });
    }
    
    @FXML
    public void changePasswordBtnClickHandler (ActionEvent actionEvent) {
        Platform.runLater(() -> {
            dialog.close();
    
            MFXPasswordField passwordField = new MFXPasswordField();
            passwordField.setFloatMode(FloatMode.BORDER);
            passwordField.setFloatingText("Password");
            passwordField.setMinHeight(45);
            passwordField.setPrefWidth(300);
            
            VBox passwordBox = new VBox(passwordField);
            VBox.setMargin(passwordField, new Insets(10, 0, 10, 0));
            
            MFXButton resetBtn = new MFXButton("Reset");
            resetBtn.getStyleClass().add("button-primary");
            resetBtn.setOnAction(event -> {
                String password = passwordField.getText();
                if (userType.equals(Entities.CUSTOMER.getClassName())) {
                    Customer customer = (Customer) user;
                    customer.setPassword(password);
                    CPSClient.sendRequestToServer(RequestType.UPDATE, Entities.CUSTOMER.getTableName() + "/password=" + password, "null", customer, (req, res) -> {
                        if (res.getStatus() == ResponseStatus.FINISHED) {
                            dialog.close();
                            Platform.runLater(() -> {
                                dialog.setTitleText("Password Reset");
                                dialog.setBodyText("Your password has been reset successfully.");
                                MFXButton confirmBtn = new MFXButton("Confirm");
                                confirmBtn.getStyleClass().add("button-primary");
                                confirmBtn.setOnAction(event1 -> {
                                    dialog.close();
                                    try {
                                        App.setPage("pc/auth/PCLogin.fxml");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                                dialog.setActionButtons(confirmBtn);
                                dialog.open();
                            });
                        }
                        else {
                            Platform.runLater(() -> {
                                dialog.setTitleText("Password Reset");
                                dialog.setBodyText("An error occurred while resetting your password.");
                                MFXButton confirmBtn = new MFXButton("Confirm");
                                confirmBtn.getStyleClass().add("button-secondary");
                                confirmBtn.setOnAction(event1 -> dialog.close());
                                dialog.setActionButtons(confirmBtn);
                                dialog.open();
                            });
                        }
                    });
                }
                else {
                    Employee employee = (Employee) user;
                    employee.setPassword(password);
                    CPSClient.sendRequestToServer(RequestType.UPDATE, Entities.CUSTOMER.getTableName() + "/password=" + password, "null", employee, (req, res) -> {
                        if (res.getStatus() == ResponseStatus.SUCCESS) {
                            dialog.close();
                            Platform.runLater(() -> {
                                dialog.setTitleText("Password Reset");
                                dialog.setBodyText("Your password has been reset successfully.");
                                MFXButton confirmBtn = new MFXButton("Confirm");
                                confirmBtn.getStyleClass().add("button-primary");
                                confirmBtn.setOnAction(event1 -> {
                                    dialog.close();
                                    try {
                                        App.setPage("pc/auth/PCLogin.fxml");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                                dialog.setActionButtons(confirmBtn);
                                dialog.open();
                            });
                        }
                        else {
                            Platform.runLater(() -> {
                                dialog.setTitleText("Password Reset");
                                dialog.setBodyText("An error occurred while resetting your password.");
                                MFXButton confirmBtn = new MFXButton("Confirm");
                                confirmBtn.getStyleClass().add("button-secondary");
                                confirmBtn.setOnAction(event1 -> dialog.close());
                                dialog.setActionButtons(confirmBtn);
                                dialog.open();
                            });
                        }
                    });
                }
            });
            
            dialog.setTitleText("Enter Reset Code");
            dialog.setBodyText("Please enter the reset code sent to your email.");
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
            if (response.getStatus() == ResponseStatus.SUCCESS) {
                userType = response.getMessage().split("/")[0];
                resetCode = response.getMessage().split("/")[1];
                user = response.getData();
                
                MFXButton confirmBtn = new MFXButton("Confirm");
                confirmBtn.getStyleClass().add("button-primary");
                confirmBtn.setOnAction(this::resetCodeBtnClickHandler);
                
                dialog.setTitleText("Success");
                dialog.setBodyText("Reset code has been sent to your email. ", "Please do not close this window, the reset code is temporary.");
                dialog.setActionButtons(confirmBtn);
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
