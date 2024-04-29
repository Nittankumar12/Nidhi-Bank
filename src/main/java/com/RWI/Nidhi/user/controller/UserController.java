package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.otpSendAndVerify.OtpServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
    OtpServiceImplementation userService;

	@PostMapping("/sendEmailOtp")
	public ResponseEntity<String> sendEmailOtp(@RequestParam("email") String email) throws Exception {
		return userService.sendEmailOtp(email, "Email verification OTP", "Your OTP for email verification is: ");
	}

	@PostMapping("/verifyEmailOtp")
	public ResponseEntity<String> verifyEmailOtp(@RequestParam("email") String email, @RequestParam("enteredOTP") String enteredOTP) throws Exception {
		return userService.verifyEmailOtp(email, enteredOTP);
	}

	@PostMapping("/sendPhoneOtp")
	public ResponseEntity<String> sendPhoneOtp(@RequestParam("phoneNumber") String phoneNumber) throws Exception {
		return userService.sendPhoneOtp(phoneNumber);
	}

	@PostMapping("/verifyPhoneOtp")
	public ResponseEntity<String> verifyPhoneOtp(@RequestParam("phoneNumber") String phoneNumber, @RequestParam("receivedOtp") String enteredOtp) throws Exception {
		return userService.verifyPhoneOtp(phoneNumber, enteredOtp);
	}
}
