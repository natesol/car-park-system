package net.cps.client.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class KioskHomeController {
    @FXML
    private MFXButton testBtn;
    @FXML
    private MFXButton test2Btn;
    
    // ----- Action Handlers Methods ----------------------
    
    @FXML
    public void testBtnClickHandler(ActionEvent actionEvent) {
        System.out.println("Test Button !");
    }
    
    @FXML
    public void test2BtnClickHandler(ActionEvent actionEvent) {
        System.out.println("Test 2 Button !");
    }
}
