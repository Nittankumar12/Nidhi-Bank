package com.RWI.Nidhi.agent.serviceInterface;

import com.RWI.Nidhi.Security.payload.request.SignupRequest;
import com.RWI.Nidhi.dto.AddAgentDto;
import com.RWI.Nidhi.dto.AddUserDto;
import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.enums.SchemeStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AgentServiceInterface {
    ResponseEntity<?> addUser(SignupRequest signupRequest, String agentEmail);
    public ResponseEntity<?> deleteUserById(String userEmail, String agentEmail);
    ResponseEntity<?> getAllUsers(String email);
    ResponseEntity<?> findUserById(int id,String agentEmail);
    public ResponseEntity<?> deactivateAccount(String accountNumber, String agentEmail);
    public ResponseEntity<?> closeAccount(String accountNumber, String agentEmail);
    ResponseEntity<?> ChangeLoanStatus(String userEmail, String agentEmail, LoanStatus changedStatus, LoanStatus previousStatus);
    ResponseEntity<?> ChangeSchemeStatus(String userEmail, String agentEmail, SchemeStatus changedStatus, SchemeStatus previousStatus);
    String deleteScheme(String email);
    ResponseEntity<String> agentForgetPasswordSendVerificationCode(String agentEmail) throws Exception;
    ResponseEntity<String> agentForgetPasswordVerifyVerificationCode(String agentEmail, String enteredOtp) throws Exception;
    AddAgentDto updateAgentPassword(String agentEmail, String agentPassword) throws Exception;
}

