package net.cps.common.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reports")
public class Report {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    
    // ???????????????
    
    private Long parkingLotId;
    private String title;
    private String period;
    private String type;
    private String status;
    
    public Report () {}
    

}
