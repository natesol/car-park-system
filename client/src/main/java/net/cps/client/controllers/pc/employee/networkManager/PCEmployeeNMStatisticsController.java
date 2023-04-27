package net.cps.client.controllers.pc.employee.networkManager;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import net.cps.client.App;
import net.cps.client.CPSClient;
import net.cps.client.utils.AbstractPCEmployeePageController;
import net.cps.client.utils.AbstractPageController;
import net.cps.common.entities.Employee;
import net.cps.common.utils.EmployeeRole;
import net.cps.common.utils.RequestType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class PCEmployeeNMStatisticsController extends AbstractPCEmployeePageController implements Initializable {
    // ...
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
    }
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    // ...
    
    
    /* ----- Event Bus Listeners ------------------------------------ */
    
    // ...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    //...
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    // ...
    
}
