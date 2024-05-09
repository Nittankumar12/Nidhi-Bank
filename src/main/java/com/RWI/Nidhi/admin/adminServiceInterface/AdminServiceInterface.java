package com.RWI.Nidhi.admin.adminServiceInterface;

import com.RWI.Nidhi.admin.ResponseDto.AdminViewsAgentDto;
import com.RWI.Nidhi.admin.ResponseDto.AgentMinimalDto;
import com.RWI.Nidhi.dto.AddAgentDto;
import com.RWI.Nidhi.dto.TransactionsHistoryDto;
import com.RWI.Nidhi.entity.Agent;
import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.enums.LoanStatus;

import java.time.LocalDate;
import java.util.List;

public interface AdminServiceInterface {

    AddAgentDto addAgent(AddAgentDto addAgentDto) throws Exception;

    AddAgentDto updateAgentName(String agentEmail, String agentName) throws Exception;

    AddAgentDto updateAgentAddress(String agentEmail , String agentAddress) throws Exception;

    AddAgentDto updateAgentEmail(String agentOldEmail, String agentNewEmail) throws Exception;

    AddAgentDto updateAgentPhoneNum(String agentEmail, String phoneNum) throws Exception;

    boolean deleteAgentById(int id) throws Exception;

    List<AgentMinimalDto> getAllAgents();

    AdminViewsAgentDto getAgentById(int id) throws Exception;

    List<TransactionsHistoryDto> getTransactionForCurrentMonth();
    List<TransactionsHistoryDto> getTransactionForCurrentWeek();
    List<TransactionsHistoryDto> getTransactionForToday();
    List<TransactionsHistoryDto> getTransactionBetweenDates(LocalDate startDate, LocalDate endDate);
    List<Loan> findByStatus(LoanStatus status);
}
