package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.user.serviceImplementation.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserServiceImplementation userService;

	@PostMapping("/sendEmailOtp")
	public ResponseEntity<String> sendEmailOtp(@RequestParam("email") String email) throws Exception {
		return userService.sendEmailOtp(email);
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
