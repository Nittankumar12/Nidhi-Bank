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
import java.util.List;

public interface AdminServiceInterface {

    Agent addAgent(SignupRequest signupRequest) throws Exception;

    Admin addAdmin(SignupRequest signUpRequest) throws Exception;

    AddAgentDto updateAgentName(String agentEmail, String agentName) throws Exception;

    AddAgentDto updateAgentAddress(String agentEmail , String agentAddress) throws Exception;

    AddAgentDto updateAgentEmail(String agentOldEmail, String agentNewEmail) throws Exception;

    AddAgentDto updateAgentPhoneNum(String agentEmail, String phoneNum) throws Exception;

    boolean deleteAgentById(int id) throws Exception;

    List<AgentMinimalDto> getAllAgents();

    AdminViewsAgentDto getAgentById(int id) throws Exception;

    List<TransactionsHistoryDto> getTransactionForCurrentMonth(TransactionsHistoryDto transactionsHistoryDto);
    List<TransactionsHistoryDto> getTransactionForCurrentWeek(TransactionsHistoryDto transactionsHistoryDto);
    List<TransactionsHistoryDto> getTransactionForToday(TransactionsHistoryDto transactionsHistoryDto);
    List<Loan> findByStatus(LoanStatus status);
}
