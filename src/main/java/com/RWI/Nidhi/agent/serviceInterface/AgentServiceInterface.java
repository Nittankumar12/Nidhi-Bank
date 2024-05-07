package com.RWI.Nidhi.agent.serviceInterface;

import com.RWI.Nidhi.dto.AddUserDto;
<<<<<<< HEAD
import com.RWI.Nidhi.entity.Accounts;
=======
import com.RWI.Nidhi.dto.LoanInfoDto;
>>>>>>> ca3fbfa0cf31b6c0a657b04ed4603f421733082a
import com.RWI.Nidhi.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AgentServiceInterface {
    User addUser(AddUserDto addUserDto) throws Exception;

    User updateUserName(int id, String userName) throws Exception;

    User updateUserEmail(int id, String userEmail) throws Exception;

    User updateUserPhoneNum(int id, String phoneNum) throws Exception;

    User updateUserPassword(String email, String password) throws Exception;

    boolean deleteUserById(int id) throws Exception;

    List<User> getAllUsers();

    User findUserById(int id) throws Exception;

    ResponseEntity<String> forgetPasswordSendVerificationCode(String email) throws Exception;

    ResponseEntity<String> forgetPasswordVerifyVerificationCode(String email, String otp) throws Exception;
<<<<<<< HEAD
    Accounts deactivateAccount(String accountNumber) throws Exception;
    Accounts closeAccount(String accountNumber) throws Exception;

=======

    // Loan Related Methods
    LoanInfoDto LoanApproved(String email);

    LoanInfoDto LoanOnSanction(String email);

    LoanInfoDto LoanOnPending(String email);

    LoanInfoDto LoanRejected(String email);

    LoanInfoDto LoanForeclosed(String email);

    LoanInfoDto LoanClosed(String email);
>>>>>>> ca3fbfa0cf31b6c0a657b04ed4603f421733082a
}

