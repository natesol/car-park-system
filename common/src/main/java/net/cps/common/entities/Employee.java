package net.cps.common.entities;

import net.cps.common.utils.EmployeeRole;

import javax.persistence.*;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

@Entity
@Table(name = "employees")
public class Employee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "role")
    private EmployeeRole role;
    
    @Column(name = "password_hash")
    private String passwordHash;
    
    @Column(name = "password_salt")
    private String passwordSalt;
    
    public Employee () {}
    
    public Employee (String firstName, String lastName, String email, String password, EmployeeRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        setPassword(password);
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
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
    
    public String getEmail () {
        return email;
    }
    
    public void setEmail (String email) {
        this.email = email;
    }
    
    public EmployeeRole getRole () {
        return role;
    }
    
    public void setRole (EmployeeRole role) {
        this.role = role;
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
        return "Employee {" +
                "id: " + id +
                ", firstName: '" + firstName + "'" +
                ", lastName: '" + lastName + "'" +
                ", role: '" + role + "'" +
                "}";
    }
}
