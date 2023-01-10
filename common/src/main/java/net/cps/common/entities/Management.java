package net.cps.common.entities;

import net.cps.common.utils.AbstractOrganization;
import net.cps.common.utils.OrganizationType;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "managements")
@PrimaryKeyJoinColumn(name = "organization_id")
public class Management extends AbstractOrganization implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;
    @NotNull
    @Column(name = "name")
    private String name;
    
    public static final OrganizationType TYPE = OrganizationType.MANAGEMENT;
    public static final String DEFAULT_NAME = "Management Name";
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public Management () {
        super(TYPE);
        this.name = DEFAULT_NAME;
    }
    
    public Management (@NotNull String name, String street, Integer streetNumber, String city, String state) {
        super(TYPE, street, streetNumber, city, state);
        this.name = name;
    }
    
    
    /* ----- Getters and Setters ------------------------------------ */
    
    public Integer getId () {
        return id;
    }
    
    public void setId (Integer id) {
        this.id = id;
    }
    
    public @NotNull String getName () {
        return name;
    }
    
    public void setName (@NotNull String name) {
        this.name = name;
    }
}
