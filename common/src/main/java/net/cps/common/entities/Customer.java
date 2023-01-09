package net.cps.common.entities;

import net.cps.common.utils.AbstractUser;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "customers")
public class Customer extends AbstractUser implements Serializable {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "id")
    //private Integer id;

    @Id
    @Column(name = "email")
    private String email;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "password_hash")
    private String passwordHash;
    
    @Column(name = "password_salt")
    private String passwordSalt;
    
    public Customer () {}
    
    public Customer (String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordSalt = generateSalt();
        this.passwordHash = hashPassword(password, passwordSalt);
    }
    
    //public int getId () {
    //    return id;
    //}
    //
    //public void setId (int id) {
    //    this.id = id;
    //}
    
    public String getFirstName () {
        return firstName;
    }
    
    public void setFirstName (String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName () {
        return lastName;
    }
    
    public void setLastName (String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail () {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }
    @Override
    public String getPasswordHash () {
        return passwordHash;
    }

    public void setPasswordHash (String passwordHash) {
        this.passwordHash = passwordHash;
    }
    @Override
    public String getPasswordSalt () {
        return passwordSalt;
    }

    public void setPasswordSalt (String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    @Override
    public String toString () {
        return "Customer {" +
                //"id: " + id +
                ", firstName: '" + firstName + "'" +
                ", lastName: '" + lastName + "'" +
                ", email: '" + email + "'" +
                ", passwordHash: '" + passwordHash + "'" +
                ", passwordSalt: '" + passwordSalt + "'" +
                "}";
    }
}
