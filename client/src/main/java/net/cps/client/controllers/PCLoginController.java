package net.cps.client.controllers;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXNotificationCell;
import io.github.palexdev.materialfx.enums.NotificationPos;
import io.github.palexdev.materialfx.enums.NotificationState;
import io.github.palexdev.materialfx.factories.InsetsFactory;
import io.github.palexdev.materialfx.font.FontResources;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import io.github.palexdev.materialfx.notifications.MFXNotificationCenterSystem;
import io.github.palexdev.materialfx.notifications.MFXNotificationSystem;
import io.github.palexdev.materialfx.notifications.base.INotification;
import io.github.palexdev.materialfx.utils.RandomUtils;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.cps.client.App;

import java.io.IOException;


public class PCLoginController {
    
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
