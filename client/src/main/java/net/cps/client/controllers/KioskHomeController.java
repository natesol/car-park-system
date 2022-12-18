package net.cps.client.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import net.cps.client.App;

import java.io.IOException;


public class KioskHomeController {
    
    @FXML
    private MFXTextField emailField;
    
    @FXML
    private MFXPasswordField passwordField;
    
    @FXML
    private MFXButton loginBtn;
    
    @FXML
    void loginBtnClickHandler(ActionEvent event) throws IOException {
        System.out.println("login !");
        
        if (emailField.getText().equals("test") && passwordField.getText().equals("test")) {
            App.setScene("pc-home");
        }
        else {
            System.out.println("wrong email/password.");
        }
    }
}
