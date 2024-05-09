package com.RWI.Nidhi.Security.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.Set;
public class SignupRequest {

    @Size(min = 3, max = 20)
    private String username;
    private String firstName;
    private String lastName;

    @Size(max = 50)
    @Email
    private String email;

    private Set<String> role;


//    @Size(min = 6, max = 40)
//    private String password;

    @Size(max = 10) // Assuming phone number is 10 digits
    private String phoneNumber;

//    private String designation;

    public SignupRequest() {
    }
//removed not blank for all arguments
    public SignupRequest( @Size(min = 3, max = 20) String username,  @Size(max = 50) @Email String email,
            Set<String> role  /*@Size(min = 6, max = 40)*//* String password*/, @Size(max = 10) String phoneNumber) {
        super();
        this.username = username;
        this.email = email;
        this.role = role;
//        this.password = password;
        this.phoneNumber = phoneNumber;
    }
    


	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRole() {
        return role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

//    public String getPassword() {
//        return password;
//    }

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

//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

//    public String getDesignation() {
//        return designation;
//    }
//
//    public void setDesignation(String designation) {
//        this.designation = designation;
//    }

    @Override
    public String toString() {
        return "SignupRequest [username=" + username + ", email=" + email + ", role=" + role  +
//                " password=" + password+
              ", phoneNumber=" + phoneNumber + "]";
    }

}
