package net.cps.common.entities;

import net.cps.common.utils.ComplaintStatus;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.TimeZone;

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
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Jerusalem"));
        
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
    
    public @NotNull Customer getCustomer () {
        return customer;
    }
    
    public void setCustomer (@NotNull Customer customer) {
        this.customer = customer;
    }
    
    public @NotNull ComplaintStatus getStatus () {
        return status;
    }
    
    public void setStatus (@NotNull ComplaintStatus status) {
        this.status = status;
    }
    
    public @NotNull Calendar getSubmissionTime () {
        return submissionTime;
    }
    
    public @NotNull String getSubmissionTimeFormatted () {
        return calendarToFormattedString(submissionTime);
    }
    
    public void setSubmissionTime (@NotNull Calendar submissionTime) {
        this.submissionTime = submissionTime;
    }
    
    public Calendar getResolutionTime () {
        return resolutionTime;
    }
    
    public @NotNull String getResolutionTimeFormatted () {
        if (resolutionTime == null) {
            return "Its been " + millisToHours(Calendar.getInstance().getTimeInMillis() - submissionTime.getTimeInMillis()) + " hours since the complaint was submitted.";
        }
        return calendarToFormattedString(resolutionTime);
    }
    
    public void setResolutionTime (Calendar resolutionTime) {
        this.resolutionTime = resolutionTime;
    }
    
    public @NotNull String getContent () {
        return content;
    }
    
    public void setContent (@NotNull String content) {
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
    
    public void cancel () {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Jerusalem"));
        
        this.resolution = "Complaint cancelled by the customer.";
        this.resolutionTime = Calendar.getInstance();
        this.status = ComplaintStatus.CANCELLED;
    }
    
    public void resolve (String resolution) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Jerusalem"));
        
        this.resolution = resolution;
        this.resolutionTime = Calendar.getInstance();
        this.status = ComplaintStatus.RESOLVED;
    }
    
    private String millisToHours (long millis) {
        return String.format("%02d:%02d", millis / 3600000, (millis % 3600000) / 60000);
    }
    
    private String calendarToFormattedString (@NotNull Calendar calendar) {
        return String.format("%02d/%02d/%02d %02d:%02d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }
    
    @Override
    public String toString () {
        return "Complaint {" +
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
