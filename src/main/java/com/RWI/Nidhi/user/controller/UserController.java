package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.AddUserDto;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.user.serviceImplementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceImpl userServiceImpl;
    @PutMapping("updateName")
    public User updateUserName(@RequestParam("id") int id, @RequestParam("userName") String userName) throws Exception{
        return userServiceImpl.updateUserName(id, userName);
    }
    @PutMapping("updateEmail")
    public User updateUserEmail(@RequestParam("id") int id,@RequestParam("userEmail") String userEmail) throws Exception{
        return userServiceImpl.updateUserEmail(id, userEmail);
    }
    @PutMapping("updatePhoneNumber")
    public User updateUserPhoneNum(@RequestParam("id") int id,@RequestParam("phoneNum") String phoneNum) throws Exception{
        return userServiceImpl.updateUserPhoneNum(id, phoneNum);
    }
    @PostMapping("/forget/verifyEmail")
    public ResponseEntity<String> verifyEmail(@RequestParam("email") String email) throws Exception {
        return userServiceImpl.userForgetPasswordSendVerificationCode(email);
    }

    @PostMapping("/forget/verifyOtp")
    public ResponseEntity<String> verifyOtp(@RequestParam("email") String email, @RequestParam("enteredOtp") String enteredOtp ) throws Exception {
        return userServiceImpl.userForgetPasswordVerifyVerificationCode(email, enteredOtp);
    }

    @PostMapping("/updateUserPassword")
    public AddUserDto updateUserPassword(@RequestParam("email") String email, @RequestParam("password") String password ) throws Exception {
        return userServiceImpl.updateUserPassword(email, password);
    }
}
