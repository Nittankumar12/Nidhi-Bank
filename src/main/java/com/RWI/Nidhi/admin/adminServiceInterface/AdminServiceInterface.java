package com.RWI.Nidhi.admin.adminServiceInterface;

import com.RWI.Nidhi.admin.ResponseDto.AdminViewsAgentDto;
import com.RWI.Nidhi.admin.ResponseDto.AgentMinimalDto;
import com.RWI.Nidhi.dto.AddAgentDto;
import com.RWI.Nidhi.dto.TransactionsHistoryDto;
import com.RWI.Nidhi.entity.Agent;
<<<<<<< HEAD
import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.enums.LoanStatus;

=======
>>>>>>> ca3fbfa0cf31b6c0a657b04ed4603f421733082a

import java.util.List;

public interface AdminServiceInterface {

    Agent addAgent(AddAgentDto addAgentDto) throws Exception;

    Agent updateAgentName(int id, String agentName) throws Exception;

    Agent updateAgentAddress(int id, String agentAddress) throws Exception;

    Agent updateAgentEmail(int id, String agentEmail) throws Exception;

    Agent updateAgentPhoneNum(int id, String phoneNum) throws Exception;

    boolean deleteAgentById(int id) throws Exception;

    List<AgentMinimalDto> getAllAgents();

    AdminViewsAgentDto getAgentById(int id) throws Exception;
<<<<<<< HEAD
    List<TransactionsHistoryDto> getTransactionForCurrentMonth(TransactionsHistoryDto transactionsHistoryDto);
    List<TransactionsHistoryDto> getTransactionForCurrentWeek(TransactionsHistoryDto transactionsHistoryDto);
    List<TransactionsHistoryDto> getTransactionForToday(TransactionsHistoryDto transactionsHistoryDto);
    List<Loan> findByStatus(LoanStatus status);
=======

>>>>>>> ca3fbfa0cf31b6c0a657b04ed4603f421733082a

}
