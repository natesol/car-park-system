package net.cps.common.entities;

import net.cps.common.utils.ComplaintStatus;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "complaints")
public class Complaint {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;
    @Column(name = "status", nullable = false)
    private ComplaintStatus status;
    @Column(name = "submission_time", nullable = false)
    private Calendar submissionTime;
    @Column(name = "resolution_time")
    private Calendar resolutionTime;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "resolution", nullable = false)
    private String resolution;
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;
    
    public Complaint (Customer customer, String content) {
        this.customer = customer;
        this.status = ComplaintStatus.ACTIVE;
        this.submissionTime = Calendar.getInstance();
        this.content = content;
    }
    
    public Complaint () {}
    
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
}
