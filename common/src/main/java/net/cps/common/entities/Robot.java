package net.cps.common.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "robots")
public class Robot implements Serializable {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    
    public Integer getId () {return id;}
    
    
    
}
