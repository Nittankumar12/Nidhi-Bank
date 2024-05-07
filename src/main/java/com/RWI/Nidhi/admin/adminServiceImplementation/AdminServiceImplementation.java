package com.RWI.Nidhi.admin.adminServiceImplementation;

import com.RWI.Nidhi.admin.ResponseDto.AdminViewsAgentDto;
import com.RWI.Nidhi.admin.ResponseDto.AgentMinimalDto;
import com.RWI.Nidhi.admin.adminServiceInterface.AdminServiceInterface;
import com.RWI.Nidhi.dto.AddAgentDto;
import com.RWI.Nidhi.dto.TransactionsHistoryDto;
import com.RWI.Nidhi.entity.*;
import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.repository.AgentRepo;
import com.RWI.Nidhi.repository.LoanRepo;
import com.RWI.Nidhi.repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImplementation implements AdminServiceInterface {
    @Autowired
    AgentRepo agentRepo;
    @Autowired
    LoanRepo loanRepo;
    @Autowired
    TransactionRepo transactionRepo;
    @Override
    public Agent addAgent(AddAgentDto addAgentDto) throws Exception{
        Agent newAgent = new Agent();
        newAgent.setAgentName(addAgentDto.getAgentName());
        newAgent.setAgentEmail(addAgentDto.getAgentEmail());
        newAgent.setAgentPhoneNum(addAgentDto.getAgentPhoneNum());
        newAgent.setAgentPassword(addAgentDto.getAgentPassword());
        newAgent.setAgentAddress(addAgentDto.getAgentAddress());

        try {
            agentRepo.save(newAgent);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return newAgent;
    }

    @Override
    public Agent updateAgentName(int id, String agentName) throws Exception{
        Agent currAgent = agentRepo.findById(id).orElseThrow(() -> {return new Exception("agent not found");});

        currAgent.setAgentName(agentName);
        try {
            agentRepo.save(currAgent);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return currAgent;
    }

    @Override
    public Agent updateAgentAddress(int id, String agentAddress) throws Exception {
        Agent currAgent = agentRepo.findById(id).orElseThrow(() -> {return new Exception("agent not found");});

        currAgent.setAgentAddress(agentAddress);
        try {
            agentRepo.save(currAgent);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return currAgent;
    }

    @Override
    public Agent updateAgentEmail(int id, String agentEmail) throws Exception{
        Agent currAgent = agentRepo.findById(id).orElseThrow(() -> {return new Exception("agent not found");});

        currAgent.setAgentEmail(agentEmail);
        try {
            agentRepo.save(currAgent);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return currAgent;
    }
    @Override
    public Agent updateAgentPhoneNum(int id, String phoneNum) throws Exception {
        Agent currAgent = agentRepo.findById(id).orElseThrow(() -> {return new Exception("agent not found");});

        currAgent.setAgentPhoneNum(phoneNum);
        try {
            agentRepo.save(currAgent);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return currAgent;
    }
    @Override
    public boolean deleteAgentById(int id) throws Exception {
        try{
            agentRepo.deleteById(id);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }
    @Override
    public List<AgentMinimalDto> getAllAgents() {
        List<Agent> allAgents = agentRepo.findAll();
        List<AgentMinimalDto> idUsernameAgent = allAgents.stream()
                .map(agent -> new AgentMinimalDto(agent.getAgentId(),agent.getAgentName()))
                .collect(Collectors.toList());
        return idUsernameAgent;
    }

    @Override
    public AdminViewsAgentDto getAgentById(int id) throws Exception{
        Agent agent = agentRepo.findById(id).orElseThrow(() -> {return new Exception("agent not found");});
        AdminViewsAgentDto responseDto = new AdminViewsAgentDto();
        responseDto.setAgentId(agent.getAgentId());
        responseDto.setAgentName(agent.getAgentName());
        responseDto.setAgentPhoneNum(agent.getAgentPhoneNum());
        responseDto.setAgentEmail(agent.getAgentEmail());
        responseDto.setAgentAddress(agent.getAgentAddress());

        List<FixedDeposit> fdList = agent.getFixedDepositList();
        responseDto.setNumberOfFd(fdList.size());

        List<MIS> misList = agent.getMisList();
        responseDto.setNumberOfMis(misList.size());

        List<RecurringDeposit> rdList = agent.getRecurringDepositList();
        responseDto.setNumberOfRd(rdList.size());

        List<Scheme> schemeList = agent.getSchemeList();
        responseDto.setNumberOfScheme(schemeList.size());

        return responseDto;

    }

    @Override
    public List<TransactionsHistoryDto> getTransactionForCurrentMonth(TransactionsHistoryDto transactionsHistoryDto) {
        List<Transactions> currTransactions = transactionRepo.getTransactionBetweenDates(LocalDate.now().withDayOfMonth(1),LocalDate.now());
        List<TransactionsHistoryDto> transactionsHistoryList = new ArrayList<>();
        for(Transactions t : currTransactions){
            TransactionsHistoryDto temp = new TransactionsHistoryDto();
            temp.setTransactionId(t.getTransactionId());
            temp.setAmount(t.getTransactionAmount());
            temp.setDate(t.getTransactionDate());
            temp.setTransactionStatus(t.getTransactionStatus());
            temp.setAccountNumber(t.getAccount().getAccountNumber());

            transactionsHistoryList.add(temp);
        }
        return transactionsHistoryList;
    }

    @Override
    public List<TransactionsHistoryDto> getTransactionForCurrentWeek(TransactionsHistoryDto transactionsHistoryDto) {
        List<Transactions> currTransactions = transactionRepo.getTransactionBetweenDates(LocalDate.now().minusDays(7),LocalDate.now());

        List<TransactionsHistoryDto> transactionsHistoryList = new ArrayList<>();
        for(Transactions t : currTransactions){
            TransactionsHistoryDto temp = new TransactionsHistoryDto();
            temp.setTransactionId(t.getTransactionId());
            temp.setAmount(t.getTransactionAmount());
            temp.setDate(t.getTransactionDate());
            temp.setTransactionStatus(t.getTransactionStatus());
            temp.setAccountNumber(t.getAccount().getAccountNumber());

            transactionsHistoryList.add(temp);
        }
        return transactionsHistoryList;
    }

    @Override
    public List<TransactionsHistoryDto> getTransactionForToday(TransactionsHistoryDto transactionsHistoryDto) {
        List<Transactions> currTransactions = transactionRepo.getTransactionForDate(LocalDate.now());
        List<TransactionsHistoryDto> transactionsHistoryList = new ArrayList<>();
        for(Transactions t : currTransactions){
            TransactionsHistoryDto temp = new TransactionsHistoryDto();
            temp.setTransactionId(t.getTransactionId());
            temp.setAmount(t.getTransactionAmount());
            temp.setDate(t.getTransactionDate());
            temp.setTransactionStatus(t.getTransactionStatus());
            temp.setAccountNumber(t.getAccount().getAccountNumber());

            transactionsHistoryList.add(temp);
        }
        return transactionsHistoryList;
    }

    @Override
    public List<Loan> findByStatus(LoanStatus status) {
        return loanRepo.findByStatus(status);
    }

}
