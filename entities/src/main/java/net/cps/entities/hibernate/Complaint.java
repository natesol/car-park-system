package net.cps.entities.hibernate;

import java.util.Date;

public class Complaint {

    private String customerId;
    private String status;
    private String content;
    private Date submissionDate;
    private String assignedEmployeeId;
    private String notes;

    public Complaint(String customerId, String status, String content, String assignedEmployeeId, String notes) {
        this.customerId = customerId;
        this.status = status;
        this.content = content;
        this.assignedEmployeeId = assignedEmployeeId;
        this.notes = notes;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAssignedEmployeeId() {
        return assignedEmployeeId;
    }

    public void setAssignedEmployeeId(String assignedEmployeeId) {
        this.assignedEmployeeId = assignedEmployeeId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
