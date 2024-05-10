package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.UserLoginDto;
import com.RWI.Nidhi.user.serviceImplementation.UserRegistrationServiceImplementation;
import com.RWI.Nidhi.user.serviceImplementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserRegistrationController {
    @Autowired
    UserRegistrationServiceImplementation userService;
    @Autowired
    UserServiceImpl userServiceImpl;

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto user) {
        String email = user.getEmail();
        String password = user.getPassword();
        boolean isAuthenticated = userServiceImpl.authenticate(email, password);
        if (isAuthenticated) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/sendEmailOtp")
    public ResponseEntity<String> sendEmailOtp(@RequestParam("email") String email) throws Exception {
        System.out.println("in controlleer");
        return userService.sendEmailOtp(email);
    }

    @PostMapping("/verifyEmailOtp")
    public ResponseEntity<String> verifyEmailOtp(@RequestParam("email") String email,
                                                 @RequestParam("enteredOTP") String enteredOTP) throws Exception {
        return userService.verifyEmailOtp(email, enteredOTP);
    }

    @PostMapping("/sendPhoneOtp")
    public ResponseEntity<String> sendPhoneOtp(@RequestParam("phoneNumber") String phoneNumber) throws Exception {
        return userService.sendPhoneOtp(phoneNumber);
    }

    @PostMapping("/verifyPhoneOtp")
    public ResponseEntity<String> verifyPhoneOtp(@RequestParam("phoneNumber") String phoneNumber,
                                                 @RequestParam("receivedOtp") String enteredOtp) throws Exception {
        return userService.verifyPhoneOtp(phoneNumber, enteredOtp);
    }
}
