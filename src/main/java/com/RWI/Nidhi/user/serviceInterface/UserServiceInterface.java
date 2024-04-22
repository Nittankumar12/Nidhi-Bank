package com.RWI.Nidhi.user.serviceInterface;

import org.springframework.http.ResponseEntity;

public interface UserServiceInterface {

    ResponseEntity<String> sendEmailOtp(String email) throws Exception;
    ResponseEntity<String> verifyEmailOtp(String email, String sentOtp, String enteredOtp) throws Exception;
    ResponseEntity<String> sendPhoneOtp(String phoneNumber) throws Exception;
    ResponseEntity<String> verifyPhoneOtp(String phoneNumber, String sentOtp, String enteredOtp) throws Exception;
}
