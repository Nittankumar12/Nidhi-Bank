package com.RWI.Nidhi.admin.adminServiceInterface;

import com.RWI.Nidhi.Security.payload.request.SignupRequest;
import com.RWI.Nidhi.dto.LoanHistoryDto;
import com.RWI.Nidhi.enums.KycStatus;
import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.enums.LoanType;
import com.RWI.Nidhi.enums.SchemeStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface AdminServiceInterface {
    ResponseEntity <?> setLoanDiscount(String userEmail,double discount);
    ResponseEntity<?> addAgent(SignupRequest signupRequest) throws Exception;

    ResponseEntity<?> addAdmin(SignupRequest signUpRequest, String adminPassword);

    ResponseEntity<?> updateAgentName(String agentEmail, String agentName) throws Exception;

    ResponseEntity<?> updateAgentAddress(String agentEmail, String agentAddress) throws Exception;

    ResponseEntity<?> updateAgentEmail(String agentOldEmail, String agentNewEmail) throws Exception;

    ResponseEntity<?> updateAgentPhoneNum(String agentEmail, String phoneNum) throws Exception;

    ResponseEntity<?> deleteUserById(String userEmail, String agentEmail);

    ResponseEntity<?> deleteAgentById(int id) throws Exception;

    ResponseEntity<?> getAllAgents();

    ResponseEntity<?> getAgentById(int id) throws Exception;

    ResponseEntity<?> getTransactionForCurrentMonth();

    ResponseEntity<?> getTransactionForCurrentWeek();

    ResponseEntity<?> getTransactionForToday();

    ResponseEntity<?> getTransactionBetweenDates(LocalDate startDate, LocalDate endDate);

    ResponseEntity<?> findByStatus(LoanStatus status);

    List<LoanHistoryDto> getLoansByLoanStatus(LoanStatus loanStatus);

    List<LoanHistoryDto> getLoansByLoanType(LoanType loanType);

    ResponseEntity<?> addBalanceToAccount(double amount, String paymentId, String orderId);

    ResponseEntity<?> deductBalanceToAccount(double amount,String paymentId, String orderId);

    ResponseEntity<?> deactivateAccount(String accountNumber, String agentEmail);

    ResponseEntity<?> closeAccount(String accountNumber, String agentEmail);

    ResponseEntity<?> changeLoanStatus(String userEmail, String agentEmail, LoanStatus changedStatus, LoanStatus previousStatus);

    ResponseEntity<?> ChangeSchemeStatus(String userEmail, String agentEmail, SchemeStatus changedStatus, SchemeStatus previousStatus);

    String deleteScheme(String email);

    ResponseEntity<?> ChangeKycStatus(String userEmail, KycStatus newStatus);

    ResponseEntity<?> getKycDetails(String userEmail);
}
