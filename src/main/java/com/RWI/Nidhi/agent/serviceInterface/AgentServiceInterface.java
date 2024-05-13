package com.RWI.Nidhi.agent.serviceInterface;

import com.RWI.Nidhi.Security.payload.request.SignupRequest;
import com.RWI.Nidhi.dto.AddUserDto;
import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.LoanStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AgentServiceInterface {
    ResponseEntity<?> addUser(SignupRequest signupRequest, String agentEmail);
    public ResponseEntity<?> deleteUserById(String userEmail, String agentEmail);
    ResponseEntity<?> getAllUsers(String email);
    ResponseEntity<?> findUserById(int id,String agentEmail);
    public ResponseEntity<?> deactivateAccount(String accountNumber, String agentEmail);
    public ResponseEntity<?> closeAccount(String accountNumber, String agentEmail);
    ResponseEntity<?> changeLoanStatus(String userEmail, String agentEmail, LoanStatus changedStatus, LoanStatus previousStatus);
}

