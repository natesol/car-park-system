package net.cps.client.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class PCLoginController {
    
    @FXML
    private MFXButton kioskBtn;
    
    @FXML
    private MFXButton remotePCBtn;
    
    @FXML
    void kioskBtnClickHandler(ActionEvent event) {
        System.out.println("kiosk !");
    }
    
    @FXML
    void remotePCBtnClickHandler(ActionEvent event) {
        System.out.println("remote PC !");
    }
}
