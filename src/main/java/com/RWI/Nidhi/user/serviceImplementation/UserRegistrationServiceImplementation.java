package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.otpSendAndVerify.OtpServiceImplementation;
import com.RWI.Nidhi.user.serviceInterface.UserRegistrationInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationServiceImplementation implements UserRegistrationInterface {
    @Autowired
    OtpServiceImplementation otpServiceImplementation;

    @Override
    public ResponseEntity<String> sendEmailOtp(String email) throws Exception {

        String otp = otpServiceImplementation.generateOTP();
        String subject = "Email Verification OTP";
        String messageToSend = "Your OTP for email verification is: ";
        System.out.println("Sending email");
        otpServiceImplementation.sendEmailOtp(email, subject, messageToSend, otp);
        System.out.println("Email Sent");
        return new ResponseEntity<>("OTP sent successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> verifyEmailOtp(String email, String enteredOtp) throws Exception {
        return otpServiceImplementation.verifyEmailOtp(email, enteredOtp);
    }

    @Override
    public ResponseEntity<String> sendPhoneOtp(String phoneNumber) throws Exception {
        return otpServiceImplementation.sendPhoneOtp(phoneNumber);
    }

    @Override
    public ResponseEntity<String> verifyPhoneOtp(String phoneNumber, String enteredOtp) throws Exception {
        return otpServiceImplementation.verifyPhoneOtp(phoneNumber, enteredOtp);
    }
}
