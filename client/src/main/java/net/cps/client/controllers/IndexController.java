package net.cps.client.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import net.cps.client.App;

import java.io.IOException;


public class IndexController extends PageController {
    @FXML
    private MFXButton kioskBtn;
    @FXML
    public MFXButton pcAppBtn;
    
    /* ----- Event Handlers ----- */
    
    @FXML
    void kioskBtnClickHandler (ActionEvent event) throws IOException {
        System.out.println("kiosk !");
        App.setScene("KioskHome.fxml");
    }
    
    @FXML
    public void pcAppBtnClickHandler (ActionEvent event) throws IOException {
        System.out.println("remote PC !");
        App.setScene("PCLogin.fxml");
    }
}
