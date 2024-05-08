package com.RWI.Nidhi.agent.serviceInterface;

import com.RWI.Nidhi.dto.AddUserDto;
import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.dto.LoanInfoDto;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.LoanStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AgentServiceInterface {
    User addUser(AddUserDto addUserDto, String agentEmail) throws Exception;
    User updateUserName(int id, String userName) throws Exception;
    User updateUserEmail(int id, String userEmail) throws Exception;
    User updateUserPhoneNum(int id, String phoneNum) throws Exception;
    User updateUserPassword(String email, String password) throws Exception;
    boolean deleteUserById(int id) throws Exception;
    List<User> getAllUsers();
    User findUserById(int id) throws Exception;
    ResponseEntity<String> forgetPasswordSendVerificationCode(String email) throws Exception;
    ResponseEntity<String> forgetPasswordVerifyVerificationCode(String email, String otp) throws Exception;
    Accounts deactivateAccount(String accountNumber) throws Exception;
    Accounts closeAccount(String accountNumber) throws Exception;
    // Loan Related Methods
    ResponseEntity<?> LoanApproved(String email);
    ResponseEntity<?> LoanOnSanction(String email);
    ResponseEntity<?>LoanOnPending(String email);
    ResponseEntity<?> LoanRejected(String email);
    ResponseEntity<?> LoanForeclosed(String email);
    ResponseEntity<?> LoanClosed(String email);
    ResponseEntity<?> ChangeLoanStatus(String userEmail, String agentEmail, LoanStatus changedStatus, LoanStatus previousStatus);
}

