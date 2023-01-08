package net.cps.common.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

@Entity
@Table(name = "customers")
public class Customer implements Serializable {
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
        setPassword(password);
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
    
    public String getPasswordHash () {
        return passwordHash;
    }
    
    public void setPasswordHash (String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public String getPasswordSalt () {
        return passwordSalt;
    }
    
    public void setPasswordSalt (String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }
    
    public void setPassword (String password) {
        passwordSalt = generateSalt();
        passwordHash = hashPassword(password, passwordSalt);
    }
    
    private String generateSalt () {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    private String hashPassword (String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean verifyPassword (String password) {
        return Objects.equals(hashPassword(password, passwordSalt), passwordHash);
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
