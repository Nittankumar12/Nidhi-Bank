package com.RWI.Nidhi.user.serviceInterface;

import org.springframework.http.ResponseEntity;

public interface UserOtpServiceInterface {

    ResponseEntity<String> sendEmailOtp(String email) throws Exception;
    ResponseEntity<String> verifyEmailOtp(String email, String enteredOtp) throws Exception;
    ResponseEntity<String> sendPhoneOtp(String phoneNumber) throws Exception;
    ResponseEntity<String> verifyPhoneOtp(String phoneNumber, String enteredOtp) throws Exception;
}
