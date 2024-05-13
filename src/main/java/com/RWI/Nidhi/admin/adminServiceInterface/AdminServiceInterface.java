package com.RWI.Nidhi.admin.adminServiceInterface;

import com.RWI.Nidhi.Security.payload.request.SignupRequest;
import com.RWI.Nidhi.admin.ResponseDto.AdminViewsAgentDto;
import com.RWI.Nidhi.admin.ResponseDto.AgentMinimalDto;
import com.RWI.Nidhi.dto.AddAgentDto;
import com.RWI.Nidhi.dto.TransactionsHistoryDto;
import com.RWI.Nidhi.entity.Admin;
import com.RWI.Nidhi.entity.Agent;
import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.enums.LoanStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface AdminServiceInterface {

    ResponseEntity<?> addAgent(SignupRequest signupRequest) throws Exception;

    ResponseEntity<?> addAdmin(SignupRequest signUpRequest, String adminPassword);

    ResponseEntity<?> updateAgentName(String agentEmail, String agentName) throws Exception;

    ResponseEntity<?> updateAgentAddress(String agentEmail , String agentAddress) throws Exception;

    ResponseEntity<?> updateAgentEmail(String agentOldEmail, String agentNewEmail) throws Exception;

    ResponseEntity<?> updateAgentPhoneNum(String agentEmail, String phoneNum) throws Exception;

    ResponseEntity<?> deleteAgentById(int id) throws Exception;

    ResponseEntity<?> getAllAgents();

    ResponseEntity<?> getAgentById(int id) throws Exception;

    ResponseEntity<?> getTransactionForCurrentMonth();
    ResponseEntity<?> getTransactionForCurrentWeek();
    ResponseEntity<?> getTransactionForToday();
    ResponseEntity<?> getTransactionBetweenDates(LocalDate startDate, LocalDate endDate);
    ResponseEntity<?> findByStatus(LoanStatus status);
}
