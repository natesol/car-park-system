package net.cps.common.entities;

import net.cps.common.utils.ComplaintStatus;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;

@Entity
@Table(name = "complaints")
public class Complaint implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_email", referencedColumnName = "email")
    private Customer customer;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ComplaintStatus status;
    @NotNull
    @Column(name = "submission_time", nullable = false)
    private Calendar submissionTime;
    @Column(name = "resolution_time")
    private Calendar resolutionTime;
    @NotNull
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "resolution")
    private String resolution;
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public Complaint () {}
    
    public Complaint (@NotNull Customer customer, @NotNull String content) {
        this.customer = customer;
        this.status = ComplaintStatus.ACTIVE;
        this.submissionTime = Calendar.getInstance();
        this.content = content;
        this.employee = null;
        this.resolution = null;
        this.resolutionTime = null;
    }
    
    
    /* ----- Getters and Setters ------------------------------------ */
    
    public Integer getId () {
        return id;
    }
    
    public void setId (Integer id) {
        this.id = id;
    }
    
    public Customer getCustomer () {
        return customer;
    }
    
    public void setCustomer (Customer customer) {
        this.customer = customer;
    }
    
    public ComplaintStatus getStatus () {
        return status;
    }
    
    public void setStatus (ComplaintStatus status) {
        this.status = status;
    }
    
    public Calendar getSubmissionTime () {
        return submissionTime;
    }
    
    public void setSubmissionTime (Calendar submissionTime) {
        this.submissionTime = submissionTime;
    }
    
    public Calendar getResolutionTime () {
        return resolutionTime;
    }
    
    public void setResolutionTime (Calendar resolutionTime) {
        this.resolutionTime = resolutionTime;
    }
    
    public String getContent () {
        return content;
    }
    
    public void setContent (String content) {
        this.content = content;
    }
    
    public String getResolution () {
        return resolution;
    }
    
    public void setResolution (String resolution) {
        this.resolution = resolution;
    }
    
    public Employee getEmployee () {
        return employee;
    }
    
    public void setEmployee (Employee employee) {
        this.employee = employee;
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    public void resolve (String resolution) {
        this.resolution = resolution;
        this.resolutionTime = Calendar.getInstance();
        this.status = ComplaintStatus.RESOLVED;
    }
    
    
    @Override
    public String toString () {
        return "Complaint{" +
                "id: " + id +
                ", customer: " + customer.getEmail() +
                ", status: " + status +
                ", submissionTime: " + submissionTime.getTime() +
                ", resolutionTime: " + (resolutionTime == null ? "null" : resolutionTime.getTime()) +
                ", content: " + content +
                ", resolution: " + resolution +
                ", employee: " + employee +
                '}';
    }
}
