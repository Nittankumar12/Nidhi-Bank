package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.AddUserDto;
import com.RWI.Nidhi.dto.UserResponseDto;
import com.RWI.Nidhi.entity.User;
import org.springframework.http.ResponseEntity;


public interface UserService {
    UserResponseDto updateUserName(String email, String userName);

    UserResponseDto updateUserEmail(String email, String updateUserEmail);

    UserResponseDto updateUserPhoneNum(String email, String phoneNum);

    AddUserDto updateUserPassword(String email, String password) throws Exception;

    ResponseEntity<String> userForgetPasswordSendVerificationCode(String email) throws Exception;

    ResponseEntity<String> userForgetPasswordVerifyVerificationCode(String email, String enteredOtp) throws Exception;

    User getByEmail(String email);
}







