package net.cps.client.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import net.cps.client.App;

import java.io.IOException;


public class StartController {
    
    @FXML
    private MFXButton kioskBtn;
    
    @FXML
    private MFXButton remotePCBtn;
    
    @FXML
    void kioskBtnClickHandler(ActionEvent event) {
        System.out.println("kiosk !");
    }
    
    @FXML
    void remotePCBtnClickHandler(ActionEvent event) throws IOException {
        System.out.println("remote PC !");
        App.setScene("pc-login");
    }
}
