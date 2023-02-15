package net.cps.client.controllers.pc.employee.customerService;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import net.cps.client.CPSClient;
import net.cps.client.utils.AbstractPCEmployeePageController;
import net.cps.common.entities.Complaint;
import net.cps.common.entities.Customer;
import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;


public class PCEmployeeCSComplaintsController extends AbstractPCEmployeePageController implements Initializable {
    ArrayList<Complaint> allCustomerComplaints = null;
    Complaint selectedComplaint = null;
    Double refundAmount = null;
    
    @FXML
    public MFXComboBox<String> statusCombo;
    @FXML
    public MFXButton showDataBtn;
    
    @FXML
    public MFXLegacyTableView<Complaint> complaintTable;
    @FXML
    public TableColumn<String, String> customerCol;
    @FXML
    public TableColumn<String, ComplaintStatus> statusCol;
    @FXML
    public TableColumn<String, Calendar> submitCol;
    @FXML
    public TableColumn<String, Calendar> resolveCol;
    @FXML
    public TableColumn<String, String> contentCol;
    @FXML
    public TableColumn<String, String> resolutionCol;
    @FXML
    public MFXButton handleBtn;
    
    @FXML
    public HBox handleComplaintForm;
    @FXML
    public TextArea complaintText;
    @FXML
    public TextArea responseField;
    @FXML
    public Text responseErrorText;
    @FXML
    public MFXToggleButton refundToggle;
    @FXML
    public MFXTextField refundIlsField;
    @FXML
    public Text refundErrorText;
    
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        
        Platform.runLater(() -> {
            statusCombo.setItems(FXCollections.observableArrayList("All", "Active", "Resolved", "Canceled"));
            statusCombo.setValue("All");
        });
        
        CPSClient.sendRequestToServer(RequestType.GET, Entities.COMPLAINT.getTableName(), this::onGetComplaints);
    }
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void showDataBtnClickHandler (ActionEvent event) {
        String status = statusCombo.getValue();
        
        if (status == null) {
            return;
        }
        if (status.equals("All")) {
            complaintTable.setItems(FXCollections.observableArrayList(allCustomerComplaints));
            return;
        }
        if (status.equals("Active")) {
            ObservableList<Complaint> activeComplaints = FXCollections.observableArrayList();
            for (Complaint complaint : allCustomerComplaints) {
                if (complaint.getStatus() == ComplaintStatus.ACTIVE) {
                    activeComplaints.add(complaint);
                }
            }
            complaintTable.setItems(activeComplaints);
            return;
        }
        if (status.equals("Resolved")) {
            ObservableList<Complaint> resolvedComplaints = FXCollections.observableArrayList();
            for (Complaint complaint : allCustomerComplaints) {
                if (complaint.getStatus() == ComplaintStatus.RESOLVED) {
                    resolvedComplaints.add(complaint);
                }
            }
            complaintTable.setItems(resolvedComplaints);
            return;
        }
        if (status.equals("Canceled")) {
            ObservableList<Complaint> canceledComplaints = FXCollections.observableArrayList();
            for (Complaint complaint : allCustomerComplaints) {
                if (complaint.getStatus() == ComplaintStatus.CANCELLED) {
                    canceledComplaints.add(complaint);
                }
            }
            complaintTable.setItems(canceledComplaints);
            return;
        }
        
        complaintTable.setItems(FXCollections.observableArrayList(allCustomerComplaints));
    }
    
    @FXML
    public void handleBtnClickHandler (ActionEvent event) {
        selectedComplaint = complaintTable.getSelectionModel().getSelectedItem();
        
        if (selectedComplaint == null) {
            dialog.clear();
            dialog.setTitleText("Handle Complaint");
            dialog.setBodyText("No complaint were selected.", "Please select a complaint to handle.");
    
            MFXButton okBtn = new MFXButton("OK");
            okBtn.getStyleClass().add("button-base-filled");
            okBtn.setOnAction(e -> {
                dialog.close();
            });
    
            dialog.setActionButtons(okBtn);
            dialog.open();
            
            return;
        }
        if (selectedComplaint.getStatus() == ComplaintStatus.RESOLVED) {
            dialog.clear();
            dialog.setTitleText("Handle Complaint");
            dialog.setBodyText("Complaint has already been resolved.", "Please select a complaint that is not resolved yet.");
            
            MFXButton okBtn = new MFXButton("OK");
            okBtn.getStyleClass().add("button-base-filled");
            okBtn.setOnAction(e -> {
                dialog.close();
            });
    
            dialog.setActionButtons(okBtn);
            dialog.open();
            return;
        }
        if (selectedComplaint.getStatus() == ComplaintStatus.CANCELLED) {
            dialog.clear();
            dialog.setTitleText("Handle Complaint");
            dialog.setBodyText("Complaint was cancelled by the customer.", "Please select a complaint that is not cancelled.");
            
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
        dialog.setTitleText("Handle Complaint");
        dialog.setBodyText("Please fill in the following form to resolve the complaint.");
        
        handleComplaintForm.setDisable(false);
        handleComplaintForm.setVisible(true);
        handleComplaintForm.setManaged(true);
        complaintText.setText(selectedComplaint.getContent());
        dialog.setCustomContent(handleComplaintForm);
    
        MFXButton resolveBtn = new MFXButton("Resolve");
        resolveBtn.getStyleClass().add("button-primary-filled");
        resolveBtn.setOnAction(e -> {
            String response = responseField.getText();
            responseErrorText.setText("");
            if (response.isEmpty()) {
                responseErrorText.setText("Please enter a response.");
                return;
            }
            if (response.length() < 2) {
                responseErrorText.setText("Please enter a valid response.");
                return;
            }
            if (response.length() > 500) {
                responseErrorText.setText("Response is too long.");
                return;
            }
            double refundIls = 0.0;
            if (refundToggle.isSelected()) {
                try {
                    refundIls = Double.parseDouble(refundIlsField.getText());
                } catch (NumberFormatException ex) {
                    refundErrorText.setText("Please enter a valid amount.");
                    return;
                }
                if (refundIls < 0) {
                    refundErrorText.setText("Refund amount must be positive.");
                    return;
                }
            }
            
            loader.show();
            selectedComplaint.resolve(response, employee);
            refundAmount = refundIls;
            CPSClient.sendRequestToServer(RequestType.UPDATE, Entities.COMPLAINT.getTableName(), null, selectedComplaint, this::onResolveComplaints);
        });
    
        MFXButton cancelBtn = new MFXButton("Cancel");
        cancelBtn.getStyleClass().add("button-base-outlined");
        cancelBtn.setOnAction(e -> {
            dialog.close();
        });
    
        dialog.setActionButtons(cancelBtn, resolveBtn);
        dialog.open();
    }
    
    @FXML
    public void refundToggleHandler (ActionEvent event) {
        refundIlsField.setDisable(!refundToggle.isSelected());
    }
    
    
    /* ----- Event Bus Listeners ------------------------------------ */
    
    // ...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    @RequestCallback.Method
    private void onGetComplaints (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.SUCCESS) {
            Platform.runLater(() -> {
                allCustomerComplaints = (ArrayList<Complaint>) response.getData();
                
                ObservableList<Complaint> complaints = FXCollections.observableArrayList(allCustomerComplaints);
    
                customerCol.setCellValueFactory(new PropertyValueFactory<>("customerEmail"));
                statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
                submitCol.setCellValueFactory(new PropertyValueFactory<>("submissionTimeFormatted"));
                resolveCol.setCellValueFactory(new PropertyValueFactory<>("resolutionTimeFormatted"));
                contentCol.setCellValueFactory(new PropertyValueFactory<>("content"));
                resolutionCol.setCellValueFactory(new PropertyValueFactory<>("resolution"));
                complaintTable.setItems(complaints);
            });
        }
    }
    
    @RequestCallback.Method
    private void onResolveComplaints (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.FINISHED) {
            Platform.runLater(() -> {
                Customer customer = selectedComplaint.getCustomer();
                
                allCustomerComplaints.remove(selectedComplaint);
                allCustomerComplaints.add(selectedComplaint);
                selectedComplaint = null;
    
                ObservableList<Complaint> complaints = FXCollections.observableArrayList(allCustomerComplaints);
                complaintTable.setItems(null);
                complaintTable.setItems(complaints);
    
                if (refundAmount > 0) {
                    customer.creditBalance(refundAmount);
                    CPSClient.sendRequestToServer(RequestType.UPDATE, Entities.CUSTOMER.getTableName(), null, customer, this::onRefundCustomer);
                }
                else {
                    loader.hide();
                    
                    dialog.clear();
                    dialog.setTitleText("Complaint Resolved Successfully");
                    dialog.setBodyText("Complaint was resolved successfully.", "No refund was issued.", "The customer will be notified.");
                    
                    MFXButton okBtn = new MFXButton("OK");
                    okBtn.getStyleClass().add("button-base-filled");
                    okBtn.setOnAction(e -> {
                        dialog.close();
                    });
                    
                    dialog.setActionButtons(okBtn);
                    dialog.open();
                }
            });
        }
    }
    
    
    @RequestCallback.Method
    private void onRefundCustomer (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.FINISHED) {
            Platform.runLater(() -> {
                loader.hide();
                
                dialog.clear();
                dialog.setTitleText("Complaint Resolved Successfully");
                dialog.setBodyText("Complaint was resolved successfully.", "The customer balance was refund of " + refundAmount + "₪.", "The customer will be notified.");
                
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
