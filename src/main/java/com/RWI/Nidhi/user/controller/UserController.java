package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.AddUserDto;
import com.RWI.Nidhi.dto.UserResponseDto;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.repository.UserRepo;
import com.RWI.Nidhi.user.serviceImplementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceImpl userServiceImpl;
    @PutMapping("updateName")
    public ResponseEntity<?> updateUserName(@RequestParam("userEmail")  String email, @RequestParam("userName") String userName){
        UserResponseDto user = userServiceImpl.updateUserName(email, userName);
        if(user == null){
            return new ResponseEntity<>("Error occurred", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(user,HttpStatus.OK);
    }
    @PutMapping("updateEmail")
    public ResponseEntity<?> updateUserEmail(@RequestParam("userEmail") String email, @RequestParam("updateUserEmail") String userEmail){
        UserResponseDto user = userServiceImpl.updateUserEmail(email, userEmail);
        if(user == null){
            return new ResponseEntity<>("Error occurred", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(user,HttpStatus.OK);
    }
    @PutMapping("updatePhoneNumber")
    public ResponseEntity<?> updateUserPhoneNum(@RequestParam("userEmail") String email,@RequestParam("phoneNum") String phoneNum){
        UserResponseDto user = userServiceImpl.updateUserPhoneNum(email, phoneNum);
        if(user == null){
            return new ResponseEntity<>("Error occurred", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(user,HttpStatus.OK);
    }
    @PostMapping("/verifyEmail")
    public ResponseEntity<String> verifyEmail(@RequestParam("email") String email) throws Exception {
        return userServiceImpl.userForgetPasswordSendVerificationCode(email);
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<String> verifyOtp(@RequestParam("email") String email, @RequestParam("enteredOtp") String enteredOtp ) throws Exception {
        return userServiceImpl.userForgetPasswordVerifyVerificationCode(email, enteredOtp);
    }

    @PutMapping("/updateUserPassword")
    public AddUserDto updateUserPassword(@RequestParam("email") String email, @RequestParam("password") String password ) throws Exception {
        return userServiceImpl.updateUserPassword(email, password);
    }
}
