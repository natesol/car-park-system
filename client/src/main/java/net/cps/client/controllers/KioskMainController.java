package net.cps.client.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class KioskMainController extends PageController implements Initializable {
    @FXML
    public VBox kioskMenu;
    @FXML
    public MFXButton homeBtn;
    @FXML
    public MFXButton subscriptionBtn;
    @FXML
    public MFXButton reservationBtn;
    @FXML
    public MFXButton bookNowBtn;
    @FXML
    public VBox subPageWrapper;

    
    
    
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
