package com.RWI.Nidhi.agent.serviceInterface;

import com.RWI.Nidhi.dto.AddUserDto;
import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.dto.LoanInfoDto;
import com.RWI.Nidhi.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AgentServiceInterface {
    User addUser(AddUserDto addUserDto, String agentEmail) throws Exception;
    boolean deleteUserById(int id) throws Exception;
    List<User> getAllUsers();
    User findUserById(int id) throws Exception;
    Accounts deactivateAccount(String accountNumber) throws Exception;
    Accounts closeAccount(String accountNumber) throws Exception;
    // Loan Related Methods
    LoanInfoDto LoanApproved(String email);
    LoanInfoDto LoanOnSanction(String email);
    LoanInfoDto LoanOnPending(String email);
    LoanInfoDto LoanRejected(String email);
    LoanInfoDto LoanForeclosed(String email);
    LoanInfoDto LoanClosed(String email);
}

