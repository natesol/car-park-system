package net.cps.common.entities;

import java.util.Date;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "complaints")
public class Complaint {
    static final String TABLE_NAME = "complaints";
    static final String ENTITY_NAME = "complaint";
    
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName= "id")
    Customer customer;
    String status;
    String content;
    String notes;
    LocalDateTime submissionDate;
    @ManyToOne
    @JoinColumn(name = "assigned_employee_id", referencedColumnName = "id")
    Employee assignedEmployee;
    
    public Complaint(Customer customer, String content, LocalDateTime submissionDate, Employee assignedEmployee) {
        this.customer = customer;
        this.content = content;
        this.submissionDate = submissionDate;
        this.assignedEmployee = assignedEmployee;
        status= "active";
    }
    
    public Complaint() {
    
    }
    
    public Long getId() {
        return id;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public String getStatus() {
        return status;
    }
    
    public String getContent() {
        return content;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }
    
    public Employee getAssignedEmployee() {
        return assignedEmployee;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }
    
    public void setAssignedEmployee(Employee assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }
}
