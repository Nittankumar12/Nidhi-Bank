package com.RWI.Nidhi.otpSendAndVerify;

import org.springframework.http.ResponseEntity;

public interface OtpServiceInterface {

    ResponseEntity<String> sendEmailOtp(String email, String subject, String messageToSend, String otp) throws Exception;

    ResponseEntity<String> verifyEmailOtp(String email, String enteredOtp) throws Exception;

    ResponseEntity<String> sendPhoneOtp(String phoneNumber) throws Exception;

    ResponseEntity<String> verifyPhoneOtp(String phoneNumber, String enteredOtp) throws Exception;
}
