package com.RWI.Nidhi.Security.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.Set;


public class SignupRequest {

    @Size(min = 3, max = 20)
    private String username;
    @Size(max = 50)
    @Email
    private String email;
    @Size(max = 10) // Assuming phone number is 10 digits
    private String phoneNumber;

    public SignupRequest() {
    }

    //removed not blank for all arguments
    public SignupRequest(@Size(min = 3, max = 20) String username, @Size(max = 50) @Email String email
                         /*Set<String> role*/  /*@Size(min = 6, max = 40)*//* String password*/, @Size(max = 10) String phoneNumber) {
        super();
        this.username = username;
        this.email = email;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    @Override
    public String toString() {
        return "SignupRequest [username=" + username + ", email=" + email +
//                " password=" + password+
                "phoneNumber=" + phoneNumber + "]";
    }

}
