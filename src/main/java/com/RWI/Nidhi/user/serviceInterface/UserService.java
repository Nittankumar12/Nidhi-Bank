package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.AddUserDto;
import com.RWI.Nidhi.entity.User;
import org.springframework.http.ResponseEntity;


public interface UserService {
    User updateUserName(int id, String userName) throws Exception;
    User updateUserEmail(int id, String userEmail) throws Exception;
    User updateUserPhoneNum(int id, String phoneNum) throws Exception;
    AddUserDto updateUserPassword(String email, String password) throws Exception;
    ResponseEntity<String> userForgetPasswordSendVerificationCode(String email) throws Exception;
    ResponseEntity<String> userForgetPasswordVerifyVerificationCode(String email, String enteredOtp) throws Exception;
    User getByEmail(String email);


}







