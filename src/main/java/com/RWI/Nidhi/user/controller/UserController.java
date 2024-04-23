package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.user.serviceImplementation.UserOtpServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserOtpServiceImplementation userService;

	@PostMapping("/sendEmailOtp")
	public ResponseEntity<String> sendEmailOtp(String email) throws Exception {
		return userService.sendEmailOtp(email);
	}

	@PostMapping("/verifyEmailOtp")
	public ResponseEntity<String> verifyEmailOtp(String email, String sentOtp, String enteredOtp) throws Exception {
		return userService.verifyEmailOtp(email, sentOtp, enteredOtp);
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
