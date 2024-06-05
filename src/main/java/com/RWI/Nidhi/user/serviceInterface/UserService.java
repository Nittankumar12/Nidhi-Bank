package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.*;
import com.RWI.Nidhi.entity.Transactions;
import com.RWI.Nidhi.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


public interface UserService {
    UserResponseDto updateUserName(String email, String userName);

    UserResponseDto updateUserEmail(String email, String updateUserEmail);

    UserResponseDto updateUserPhoneNum(String email, String phoneNum);

    AddUserDto updateUserPassword(String email, String password) throws Exception;

    ResponseEntity<String> userForgetPasswordSendVerificationCode(String email) throws Exception;

    ResponseEntity<String> userForgetPasswordVerifyVerificationCode(String email, String enteredOtp) throws Exception;

    User getByEmail(String email);
    ResponseEntity<?> updateUser(UpdateUserDTO updateUserDTO);

    List<UserTransactionsHistoryDto> getTransactionsBetweenDateByUserEmail(String userEmail, LocalDate startDate, LocalDate endDate);
//    List<Transactions> getTransactionOfToday(String userEmail, LocalDate startDate, LocalDate endDate) throws Exception;
}







