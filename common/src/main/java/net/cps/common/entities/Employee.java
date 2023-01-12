package net.cps.common.entities;

import net.cps.common.utils.AbstractUser;
import net.cps.common.utils.EmployeeRole;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "employees")
public class Employee extends AbstractUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "email")
    private String email;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "password_salt")
    private String passwordSalt;
    @Column(name = "password_hash")
    private String passwordHash;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "role")
    private EmployeeRole role;
    @ManyToOne
    @JoinColumn(name = "organization")
    private Organization organization;
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public Employee () {
        super();
    }
    
    public Employee (String email, String firstName, String lastName,  String password, EmployeeRole role, Organization organization) {
        super();
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordSalt = generateSalt();
        this.passwordHash = hashPassword(password, passwordSalt);
        this.role = role;
        this.organization = organization;
        
        this.isActive = false;
    }
    
    
    /* ----- Getters and Setters ------------------------------------ */
    
    public Integer getId () {
        return id;
    }
    public void setId (Integer id) {
        this.id = id;
    }
    
    public String getEmail () {
        return email;
    }
    public void setEmail (String email) {
        this.email = email;
    }
    
    public String getFirstName () {
        return firstName;
    }
    public void setFirstName (String first_name) {
        this.firstName = first_name;
    }
    
    public String getLastName () {
        return lastName;
    }
    public void setLastName (String last_name) {
        this.lastName = last_name;
    }
    
    @Override
    public String getPasswordSalt () {
        return passwordSalt;
    }
    public void setPasswordSalt (String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }
    
    @Override
    public String getPasswordHash () {
        return passwordHash;
    }
    public void setPasswordHash (String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public EmployeeRole getRole () {
        return role;
    }
    public void setRole (EmployeeRole role) {
        this.role = role;
    }

    public Organization getOrganization () {
        return organization;
    }
    public void setOrganization (Organization organization) {
        this.organization = organization;
    }
    
    public Boolean getIsActive () {
        return isActive;
    }
    public void setIsActive (Boolean active) {
        isActive = active;
    }
    

    /* ----- Utility Methods ---------------------------------------- */
    
    @Override
    public String toString () {
        return "Employee {" +
                "id: " + id +
                ", email: " + email +
                ", firstName: '" + firstName + "'" +
                ", lastName: '" + lastName + "'" +
                ", passwordSalt: '" + passwordSalt + "'" +
                ", passwordHash: '" + passwordHash + "'" +
                ", isActive: '" + isActive + "'" +
                ", role: '" + role + "'" +
                ", organization: '" + organization + "'" +
                "}";
    }
}
