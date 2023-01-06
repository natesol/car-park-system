package net.cps.entities.hibernate;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;
    @NotNull
    String role;
    @NotNull
    String email;
    @NotNull
    String firstName;
    @NotNull
    String lastName;
    @NotNull
    Long organization;

    public Employee(@NotNull String role, @NotNull String email, @NotNull String firstName,
                    @NotNull String lastName, @NotNull Long organization) {
        this.role = role;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.organization = organization;
    }

    public Employee() {

    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Long getOrganization() {
        return organization;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setOrganization(Long organization) {
        this.organization = organization;
    }
}
