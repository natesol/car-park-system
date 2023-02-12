package net.cps.client.controllers.pc.customer;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import net.cps.client.utils.AbstractPCCustomerPageController;
import net.cps.common.entities.Complaint;
import net.cps.common.utils.ComplaintStatus;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;


public class PCCustomerComplaintsController extends AbstractPCCustomerPageController implements Initializable {
    @FXML
    public MFXButton fileComplaintBtn;
    @FXML
    public MFXLegacyTableView<Complaint> complaintsTable;
    @FXML
    public TableColumn<String, ComplaintStatus> statusCol;
    @FXML
    public TableColumn<String, Calendar> submittedCol;
    @FXML
    public TableColumn<String, Calendar> resolvedCol;
    @FXML
    public TableColumn<String, String> contentCol;
    @FXML
    public TableColumn<String, String> responseCol;
    @FXML
    public MFXButton cancelComplaintBtn;
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
    }
    
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void fileComplaintBtnClickHandler (ActionEvent event) {
        dialog.setTitleText("File a Complaint");
        dialog.setBodyText("Please fill in the following fields to file a complaint:");
        dialog.open();
    }
    
    @FXML
    public void cancelComplaintBtnClickHandler (ActionEvent event) {
        dialog.setTitleText("Cancel a Complaint");
        dialog.setBodyText("Please select a complaint to cancel:");
        dialog.open();
    }
    
    
    /* ----- EventBus Listeners ------------------------------------- */
    
    // ...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    // ...
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    // ...
    
}
