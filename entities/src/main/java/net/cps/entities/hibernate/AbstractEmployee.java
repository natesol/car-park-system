package net.cps.entities.hibernate;

public abstract class AbstractEmployee {
    private String id;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String role;
    private String organization;

    public AbstractEmployee(){}
    public AbstractEmployee(String id, String emailAddress, String firstName, String lastName, String role, String organization) {
        this.id = id;
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.organization = organization;
    }

    public void logIn(String password){

    }
    public void logOut(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
}
