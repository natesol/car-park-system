package net.cps.client.controllers.pc.customer;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import net.cps.client.CPSClient;
import net.cps.client.utils.AbstractPCCustomerPageController;
import net.cps.common.entities.Complaint;
import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;


public class PCCustomerComplaintsController extends AbstractPCCustomerPageController implements Initializable {
    ArrayList<Complaint> allCustomerComplaints = null;
    Complaint lastComplaint = null;
    Complaint selectedComplaint = null;
    
    
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
    
    @FXML
    public HBox newComplaintForm;
    @FXML
    public MFXTextField subjectField;
    @FXML
    public Text subjectErrorText;
    @FXML
    public TextArea complaintField;
    @FXML
    public Text complaintErrorText;
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        
        CPSClient.sendRequestToServer(RequestType.GET, Entities.COMPLAINT.getTableName() + "/customer_email=" + customer.getEmail(), this::onGetComplaints);
    }
    
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void fileComplaintBtnClickHandler (ActionEvent event) {
        dialog.clear();
        dialog.setTitleText("File a Complaint");
        dialog.setBodyText("Please fill in the following form to file a complaint:");
        
        newComplaintForm.setDisable(false);
        newComplaintForm.setVisible(true);
        newComplaintForm.setManaged(true);
        dialog.setCustomContent(newComplaintForm);
        
        MFXButton submitBtn = new MFXButton("Submit");
        submitBtn.getStyleClass().add("button-primary-filled");
        submitBtn.setOnAction(e -> {
            String subject = subjectField.getText();
            subjectErrorText.setText("");
            if (subject.isEmpty()) {
                subjectErrorText.setText("Please enter a subject.");
                return;
            }
            if (subject.length() < 2) {
                subjectErrorText.setText("Please enter a valid subject.");
                return;
            }
            if (subject.length() > 50) {
                subjectErrorText.setText("Subject is too long.");
                return;
            }
            String content = complaintField.getText();
            if (content.isEmpty()) {
                complaintErrorText.setText("Please enter a complaint.");
                return;
            }
            if (content.length() < 2) {
                complaintErrorText.setText("Please enter a valid complaint.");
                return;
            }
            if (content.length() > 500) {
                complaintErrorText.setText("Complaint is too long.");
                return;
            }
            
            loader.show();
            lastComplaint = new Complaint(customer, subject + " - " + content);
            CPSClient.sendRequestToServer(RequestType.CREATE, Entities.COMPLAINT.getTableName(), null, lastComplaint, this::onCreateComplaints);
        });
        
        MFXButton cancelBtn = new MFXButton("Cancel");
        cancelBtn.getStyleClass().add("button-base-outlined");
        cancelBtn.setOnAction(e -> {
            dialog.close();
        });
        
        dialog.setActionButtons(cancelBtn, submitBtn);
        dialog.open();
    }
    
    @FXML
    public void cancelComplaintBtnClickHandler (ActionEvent event) {
        selectedComplaint = complaintsTable.getSelectionModel().getSelectedItem();
        
        if (selectedComplaint == null || selectedComplaint.getStatus() != ComplaintStatus.ACTIVE) {
            dialog.clear();
            dialog.setTitleText("Cancel a Complaint");
            
            if (selectedComplaint == null) {
                dialog.setBodyText("No complaint were selected.", "Please select a complaint to cancel and try again.");
            }
            else if (selectedComplaint.getStatus() == ComplaintStatus.CANCELLED) {
                dialog.setBodyText("This complaint has already been cancelled.", "Please select another complaint and try again.");
            }
            else if (selectedComplaint.getStatus() == ComplaintStatus.RESOLVED) {
                dialog.setBodyText("This complaint has already been resolved.", "Please select an active complaint and try again.");
            }
            else {
                dialog.setBodyText("Please select another complaint and try again later.");
            }
    
            MFXButton okBtn = new MFXButton("OK");
            okBtn.getStyleClass().add("button-base-filled");
            okBtn.setOnAction(e -> {
                dialog.close();
            });
    
            dialog.setActionButtons(okBtn);
            dialog.open();
            return;
        }
        
        dialog.clear();
        dialog.setTitleText("Cancel a Complaint");
        dialog.setBodyText("Are you sure you want to cancel this complaint?");
        
        MFXButton submitBtn = new MFXButton("Yes");
        submitBtn.getStyleClass().add("button-danger-filled");
        submitBtn.setOnAction(e -> {
            loader.show();
            
            selectedComplaint.cancel();
            CPSClient.sendRequestToServer(RequestType.UPDATE, Entities.COMPLAINT.getTableName(), null, selectedComplaint, this::onCancelComplaints);
        });
        
        MFXButton cancelBtn = new MFXButton("No");
        cancelBtn.getStyleClass().add("button-base-outlined");
        cancelBtn.setOnAction(e -> {
            dialog.close();
        });
        
        dialog.setActionButtons(cancelBtn, submitBtn);
        dialog.open();
    }
    
    
    /* ----- EventBus Listeners ------------------------------------- */
    
    // ...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    @RequestCallback.Method
    private void onGetComplaints (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.SUCCESS) {
            Platform.runLater(() -> {
                allCustomerComplaints = (ArrayList<Complaint>) response.getData();
                
                ObservableList<Complaint> complaints = FXCollections.observableArrayList(allCustomerComplaints);
                
                statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
                submittedCol.setCellValueFactory(new PropertyValueFactory<>("submissionTimeFormatted"));
                resolvedCol.setCellValueFactory(new PropertyValueFactory<>("resolutionTimeFormatted"));
                contentCol.setCellValueFactory(new PropertyValueFactory<>("content"));
                responseCol.setCellValueFactory(new PropertyValueFactory<>("resolution"));
                complaintsTable.setItems(complaints);
            });
        }
    }
    
    @RequestCallback.Method
    private void onCreateComplaints (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.FINISHED) {
            Platform.runLater(() -> {
                allCustomerComplaints.add(lastComplaint);
                lastComplaint = null;
                
                ObservableList<Complaint> complaints = FXCollections.observableArrayList(allCustomerComplaints);
                complaintsTable.setItems(null);
                complaintsTable.setItems(complaints);
                
                loader.hide();
                dialog.clear();
                dialog.setTitleText("Complaint Filed Successfully");
                dialog.setBodyText("Your complaint has been filed successfully.", "Any updates will be sent to your email.", "Thank you for your patience.");
                MFXButton okBtn = new MFXButton("OK");
                okBtn.getStyleClass().add("button-base-filled");
                okBtn.setOnAction(e -> {
                    dialog.close();
                });
                dialog.setActionButtons(okBtn);
                dialog.open();
            });
        }
        else {
            Platform.runLater(() -> {
                loader.hide();
                dialog.clear();
                dialog.setTitleText("Something Went Wrong");
                dialog.setBodyText("Your complaint has not been filed.", "Please try again later.", "Thank you for your patience.");
                MFXButton okBtn = new MFXButton("OK");
                okBtn.getStyleClass().add("button-base-filled");
                okBtn.setOnAction(e -> {
                    dialog.close();
                });
                dialog.setActionButtons(okBtn);
                dialog.open();
            });
        }
    }
    
    @RequestCallback.Method
    private void onCancelComplaints (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.FINISHED) {
            Platform.runLater(() -> {
                allCustomerComplaints.remove(selectedComplaint);
                allCustomerComplaints.add(selectedComplaint);
                selectedComplaint = null;
                
                ObservableList<Complaint> complaints = FXCollections.observableArrayList(allCustomerComplaints);
                complaintsTable.setItems(null);
                complaintsTable.setItems(complaints);
                
                loader.hide();
                dialog.clear();
                dialog.setTitleText("Complaint Cancelled Successfully");
                dialog.setBodyText("Your complaint has been cancelled successfully.", "We would be happy to hear from you again.");
                MFXButton okBtn = new MFXButton("OK");
                okBtn.getStyleClass().add("button-base-filled");
                okBtn.setOnAction(e -> {
                    dialog.close();
                });
                dialog.setActionButtons(okBtn);
                dialog.open();
            });
        }
        else {
            Platform.runLater(() -> {
                loader.hide();
                dialog.clear();
                dialog.setTitleText("Something Went Wrong");
                dialog.setBodyText("Your complaint has not been cancelled.", "Please try again later.", "Thank you for your patience.");
                MFXButton okBtn = new MFXButton("OK");
                okBtn.getStyleClass().add("button-base-filled");
                okBtn.setOnAction(e -> {
                    dialog.close();
                });
                dialog.setActionButtons(okBtn);
                dialog.open();
            });
        }
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    // ...
    
}
