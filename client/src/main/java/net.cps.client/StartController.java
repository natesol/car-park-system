package net.cps.client;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import net.cps.entities.Message;

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
    void remotePCBtnClickHandler(ActionEvent event) {
        System.out.println("remote PC !");
    }
}
