package com.RWI.Nidhi.admin.adminServiceImplementation;

import com.RWI.Nidhi.admin.ResponseDto.AdminViewsAgentDto;
import com.RWI.Nidhi.admin.ResponseDto.AgentMinimalDto;
import com.RWI.Nidhi.admin.adminServiceInterface.AdminServiceInterface;
import com.RWI.Nidhi.dto.AddAgentDto;
import com.RWI.Nidhi.dto.TransactionsHistoryDto;
import com.RWI.Nidhi.entity.*;
import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.otpSendAndVerify.OtpServiceImplementation;
import com.RWI.Nidhi.repository.AgentRepo;
import com.RWI.Nidhi.repository.LoanRepo;
import com.RWI.Nidhi.repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
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
    @Autowired
    OtpServiceImplementation otpServiceImplementation;
    @Override
    public AddAgentDto addAgent(AddAgentDto addAgentDto) throws Exception{
        if(agentRepo.existsByAgentEmail(addAgentDto.getAgentEmail())){
            throw new Exception("Agent already exists");
        }
        Agent newAgent = new Agent();
        newAgent.setAgentName(addAgentDto.getAgentName());
        newAgent.setAgentEmail(addAgentDto.getAgentEmail());
        newAgent.setAgentPhoneNum(addAgentDto.getAgentPhoneNum());
        try {
            String tempPassword = otpServiceImplementation.generateOTP();
            String subject = "Your temporary password";
            String messageToSend = "Your temporary system generated password is: ";

            otpServiceImplementation.sendEmailOtp(newAgent.getAgentEmail(), subject, messageToSend, tempPassword);
            newAgent.setAgentPassword(getEncryptedPassword(tempPassword));
            agentRepo.save(newAgent);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return addAgentDto;
    }

    @Override
    public AddAgentDto updateAgentName(String agentEmail, String agentName) throws Exception{
        Agent currAgent = agentRepo.findByAgentEmail(agentEmail);

        currAgent.setAgentName(agentName);
        try {
            agentRepo.save(currAgent);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        AddAgentDto addAgentDto = new AddAgentDto();
        addAgentDto.setAgentName(currAgent.getAgentName());
        addAgentDto.setAgentEmail(currAgent.getAgentEmail());
        addAgentDto.setAgentPhoneNum(currAgent.getAgentPhoneNum());
        return addAgentDto;
    }

    @Override
    public AddAgentDto updateAgentAddress(String agentEmail, String agentAddress) throws Exception {
        Agent currAgent = agentRepo.findByAgentEmail(agentEmail);

        currAgent.setAgentAddress(agentAddress);
        try {
            agentRepo.save(currAgent);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        AddAgentDto addAgentDto = new AddAgentDto();
        addAgentDto.setAgentName(currAgent.getAgentName());
        addAgentDto.setAgentEmail(currAgent.getAgentEmail());
        addAgentDto.setAgentPhoneNum(currAgent.getAgentPhoneNum());
        return addAgentDto;
    }

    @Override
    public AddAgentDto updateAgentEmail(String agentOldEmail, String agentNewEmail) throws Exception{
        Agent currAgent = agentRepo.findByAgentEmail(agentOldEmail);

        currAgent.setAgentEmail(agentNewEmail);
        try {
            agentRepo.save(currAgent);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        AddAgentDto addAgentDto = new AddAgentDto();
        addAgentDto.setAgentName(currAgent.getAgentName());
        addAgentDto.setAgentEmail(currAgent.getAgentEmail());
        addAgentDto.setAgentPhoneNum(currAgent.getAgentPhoneNum());
        return addAgentDto;
    }
    @Override
    public AddAgentDto updateAgentPhoneNum(String agentEmail, String phoneNum) throws Exception {
        Agent currAgent = agentRepo.findByAgentEmail(agentEmail);

        currAgent.setAgentPhoneNum(phoneNum);
        try {
            agentRepo.save(currAgent);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        AddAgentDto addAgentDto = new AddAgentDto();
        addAgentDto.setAgentName(currAgent.getAgentName());
        addAgentDto.setAgentEmail(currAgent.getAgentEmail());
        addAgentDto.setAgentPhoneNum(currAgent.getAgentPhoneNum());
        return addAgentDto;
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
    private byte[] getSHA(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getEncryptedPassword (String password){
        String encryptedPassword = "";
        try {
            BigInteger number = new BigInteger(1, getSHA(password));
            return number.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
