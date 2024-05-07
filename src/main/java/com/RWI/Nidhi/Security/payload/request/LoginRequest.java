package com.RWI.Nidhi.Security.payload.request;


public class LoginRequest {

//    @NotBlank
    private String usernameOrEmailOrPhoneNumber;

//    @NotBlank
    private String password;

    public String getUsernameOrEmailOrPhoneNumber() {
        return usernameOrEmailOrPhoneNumber;
    }

    public void setUsernameOrEmailOrPhoneNumber(String usernameOrEmailOrPhoneNumber) {
        this.usernameOrEmailOrPhoneNumber = usernameOrEmailOrPhoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
