package net.cps.client.controllers.pc.auth;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;

import javafx.application.Platform;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import net.cps.client.App;
import net.cps.client.CPSClient;
import net.cps.client.utils.AbstractPageController;
import net.cps.common.entities.Customer;
import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.RequestCallback;
import net.cps.common.utils.ResponseStatus;
import net.cps.common.utils.RequestType;


public class PCSignUpController extends AbstractPageController {
    @FXML
    public HBox goBackButton;
    @FXML
    public MFXTextField firstNameField;
    @FXML
    public MFXTextField lastNameField;
    @FXML
    private MFXTextField emailField;
    @FXML
    private MFXPasswordField passwordField;
    @FXML
    public MFXPasswordField passwordRepeatField;
    @FXML
    public MFXCheckbox termsOfServiceCheckBox;
    @FXML
    public MFXButton signUpBtn;
    @FXML
    public Hyperlink loginLink;
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {}
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void goBackButtonClickHandler (MouseEvent mouseEvent) throws IOException {
        App.setPage("pc/auth/PCLogin.fxml");
    }
    
    @FXML
    public void termsOfServiceLinkClickHandler (MouseEvent mouseEvent) {
        dialog.setTitleText("CityPark Terms of Service");
        dialog.setBodyText("Welcome to CityPark's online application service (the \"Service\"). The Service is provided by CityPark and its affiliates (collectively, \"we,\" \"us,\" or \"our\"). These terms of service (these \"Terms\") govern your access to and use of the Service, including any content, functionality, and services offered on or through the Service.\n" +
                "\n" +
                "Please read the Terms carefully before you start to use the Service. By using the Service or by clicking to accept or agree to the Terms when this option is made available to you, you accept and agree to be bound and abide by these Terms and our Privacy Policy, found at [insert URL], incorporated herein by reference. If you do not want to agree to these Terms or the Privacy Policy, you must not access or use the Service.\n" +
                "\n" +
                "We reserve the right to change or modify these Terms or any aspect of the Service at any time and in our sole discretion. Any changes or modifications will be effective immediately upon posting the revisions to the Service, and you waive any right you may have to receive specific notice of such changes or modifications. Your continued use of the Service following the posting of changes or modifications will confirm your acceptance of such changes or modifications. Therefore, you should frequently review these Terms and any other applicable policies, including their dates, to understand the terms and conditions that apply to your use of the Service. If you do not agree to the amended terms, you must stop using the Service.\n" +
                "\n" +
                "The Service is intended for use by individuals who are at least 18 years old. By using the Service, you represent and warrant that you are at least 18 years old and that you have the legal capacity to enter into these Terms. If you are using the Service on behalf of an organization, you represent and warrant that you are authorized to accept these Terms on behalf of such organization and to bind such organization to these Terms.\n" +
                "\n" +
                "The Service is a platform that enables you to locate, reserve, and pay for parking spaces at parking lots owned or operated by us or our affiliates (each a \"Parking Lot\") through the Service. All transactions for parking spaces made through the Service are subject to availability and the terms and conditions set forth in these Terms.\n" +
                "\n" +
                "The Service may include certain communications from us, such as service announcements and administrative messages. You understand and agree that these communications shall be considered part of using the Service. As part of our policy to provide you total privacy, we also provide you the option of opting out of receiving newsletters or any other promotional communications from us.\n" +
                "\n" +
                "You may not use the Service or any content contained in the Service for any illegal or unauthorized purpose nor may you, in the use of the Service, violate any laws in your jurisdiction (including but not limited to copyright laws).\n" +
                "\n" +
                "You are solely responsible for your conduct and any data, text, files, information, usernames, images, graphics, photos, profiles, audio and video clips, sounds, musical works, works of authorship, applications, links, and other content or materials (collectively, \"Content\") that you submit, post, or display on or via the Service.\n" +
                "\n" +
                "You must not transmit any worms or viruses or any code of a destructive nature.\n" +
                "\n" +
                "We reserve the right to modify or terminate the Service or your access to the Service for any reason, without notice and without liability to you.\n" +
                "\n" +
                "We reserve the right to refuse service to anyone for any reason at any time.\n" +
                "\n" +
                "A breach or violation of any of the Terms will result in an immediate termination of your Services.\n");
        dialog.setWidth("lg");
        MFXButton closeBtn = new MFXButton("Close");
        closeBtn.setOnAction(event -> dialog.close());
        dialog.setActionButtons(closeBtn);
        dialog.open();
    }
    
    @FXML
    public void signUpBtnClickHandler (ActionEvent actionEvent) throws IOException {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String passwordRepeat = passwordRepeatField.getText();
        Boolean acceptedTerms = (Boolean) termsOfServiceCheckBox.isSelected();
        
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || passwordRepeat.isEmpty()) {
            dialog.setTitleText("Error");
            dialog.setBodyText("Please fill all the fields on the form.");
            dialog.open();
            return;
        }
        if (!(email.contains("@") && email.contains("."))) {
            dialog.setTitleText("Error");
            dialog.setBodyText("Please enter a valid email.");
            dialog.open();
            return;
        }
        if (!password.equals(passwordRepeat)) {
            dialog.setTitleText("Error");
            dialog.setBodyText("The passwords you entered do not match.");
            dialog.open();
            return;
        }
        if (!acceptedTerms) {
            dialog.setTitleText("Error");
            dialog.setBodyText("Please read and accept the terms of service to continue.");
            dialog.open();
            return;
        }
        
        Customer customer = new Customer(email, firstName, lastName, password);
        CPSClient.sendRequestToServer(RequestType.AUTH, "register/email=" + email + "&password=" + password, "create a new customer account", customer, this::onCustomerCreation);
    }
    
    @FXML
    public void loginLinkClickHandler (MouseEvent mouseEvent) throws IOException {
        App.setPage("pc/auth/PCLogin.fxml");
    }
    
    
    /* ----- EventBus Listeners ------------------------------------- */
    
    @RequestCallback.Method
    public void onCustomerCreation (RequestMessage request, ResponseMessage response) {
        ResponseStatus status = response.getStatus();
        
        Platform.runLater(() -> {
            if (status == ResponseStatus.FINISHED) {
                // Create the dialog buttons.
                MFXButton confirmBtn = new MFXButton("Confirm");
                confirmBtn.getStyleClass().add("button-primary");
                confirmBtn.setOnAction(actionEvent -> {
                    try {
                        App.setPage("pc/auth/PCLogin.fxml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                MFXButton closeBtn = new MFXButton("Close");
                closeBtn.getStyleClass().add("button-secondary");
                closeBtn.setOnAction(actionEvent -> dialog.close());
                
                dialog.setTitleText("Success");
                dialog.setBodyText("Your account has been created successfully");
                dialog.setActionButtons(closeBtn, confirmBtn);
                dialog.open();
            } else {
                dialog.setTitleText("Something went wrong");
                dialog.setBodyText(response.getMessage());
                dialog.open();
            }
        });
    }
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    // ...
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    // ...
}
