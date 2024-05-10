package com.RWI.Nidhi.admin.adminServiceImplementation;

import com.RWI.Nidhi.Security.models.Credentials;
import com.RWI.Nidhi.Security.models.ERole;
import com.RWI.Nidhi.Security.models.Role;
import com.RWI.Nidhi.Security.payload.request.SignupRequest;
import com.RWI.Nidhi.Security.repository.CredentialsRepo;
import com.RWI.Nidhi.Security.repository.RoleRepository;
import com.RWI.Nidhi.admin.ResponseDto.AdminViewsAgentDto;
import com.RWI.Nidhi.admin.ResponseDto.AgentMinimalDto;
import com.RWI.Nidhi.admin.adminServiceInterface.AdminServiceInterface;
import com.RWI.Nidhi.dto.AddAgentDto;
import com.RWI.Nidhi.dto.TransactionsHistoryDto;
import com.RWI.Nidhi.entity.*;
import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.otpSendAndVerify.OtpServiceImplementation;
import com.RWI.Nidhi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    CredentialsRepo credentialsRepo;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    AdminRepo adminRepo;
    @Autowired
    UserRepo userRepo;
    @Override
    public SignupRequest addAgent(SignupRequest signUpRequest) throws Exception{
//        if(adminRepo.existsByAdminName(signUpRequest.getUsername())||agentRepo.existsByAgentName(signUpRequest.getEmail())||userRepo.existsByUserName(signUpRequest.getEmail())){
//            throw new Exception("Admin already exists");
//        }
//
//        if(adminRepo.existsByEmail(signUpRequest.getEmail())||agentRepo.existsByAgentEmail(signUpRequest.getEmail())||userRepo.existsByEmail(signUpRequest.getEmail())){
//            throw new Exception("Admin already exists");
//        }
//
//        if (userRepo.existsByPhoneNumber(signUpRequest.getPhoneNumber())||adminRepo.existsByPhoneNumber(signUpRequest.getEmail())||agentRepo.existsByAgentPhoneNum(signUpRequest.getEmail())) {
//            throw new Exception("User already exists");
//        }
         if(agentRepo.existsByAgentEmail(signUpRequest.getEmail()) || userRepo.existsByEmail(signUpRequest.getEmail())){
            throw new Exception("Email already exists");
        }
        if(adminRepo.existsByAdminName(signUpRequest.getUsername()) || agentRepo.existsByAgentName(signUpRequest.getEmail()) || userRepo.existsByUserName(signUpRequest.getUsername())){
            throw new Exception("Username already taken");
        }

        Agent newAgent = new Agent();
        newAgent.setAgentName(signUpRequest.getUsername());
        newAgent.setAgentEmail(signUpRequest.getEmail());
        newAgent.setAgentPhoneNum(signUpRequest.getPhoneNumber());
        try {
            String tempPassword = otpServiceImplementation.generateOTP();
            String subject = "Your temporary password";
            String messageToSend = "Your temporary system generated password is: ";

            otpServiceImplementation.sendEmailOtp(newAgent.getAgentEmail(), subject, messageToSend, tempPassword);
            newAgent.setAgentPassword(encoder.encode(tempPassword));
            agentRepo.save(newAgent);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        Credentials agent = new Credentials(signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPhoneNumber(),
                newAgent.getAgentPassword());
//
        Set<Role> roles = new HashSet<>();
        Role agentRole = roleRepository.findByName(ERole.ROLE_AGENT)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(agentRole);
        agent.setRoles(roles);
        newAgent.setRoles(roles);
        agentRepo.save(newAgent);
        credentialsRepo.save(agent);
        return signUpRequest;
    }
    @Override
    public Admin addAdmin(SignupRequest signUpRequest) throws Exception{
        if(adminRepo.existsByAdminName(signUpRequest.getUsername())||agentRepo.existsByAgentName(signUpRequest.getEmail())||userRepo.existsByUserName(signUpRequest.getEmail())){
            throw new Exception("Admin already exists");
        }

        if(adminRepo.existsByEmail(signUpRequest.getEmail())||agentRepo.existsByAgentEmail(signUpRequest.getEmail())||userRepo.existsByEmail(signUpRequest.getEmail())){
            throw new Exception("Admin already exists");
        }

        if (userRepo.existsByPhoneNumber(signUpRequest.getPhoneNumber())||adminRepo.existsByPhoneNumber(signUpRequest.getEmail())||agentRepo.existsByAgentPhoneNum(signUpRequest.getEmail())) {
            throw new Exception("User already exists");
        }
        Admin newAdmin = new Admin();
        newAdmin.setAdminName(signUpRequest.getUsername());
        newAdmin.setEmail(signUpRequest.getEmail());
        newAdmin.setPhoneNumber(signUpRequest.getPhoneNumber());
        try {
            String tempPassword = "admin21";
//            String subject = "Your temporary password";
//            String messageToSend = "Your temporary system generated password is: ";

//            otpServiceImplementation.sendEmailOtp(newAdmin.getEmail(), subject, messageToSend, tempPassword);
            newAdmin.setPassword(encoder.encode(tempPassword));
            adminRepo.save(newAdmin);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        Credentials agent = new Credentials(signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPhoneNumber(),
                newAdmin.getPassword());
//
        Set<Role> roles = new HashSet<>();
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(adminRole);
        agent.setRoles(roles);
        newAdmin.setRoles(roles);
        adminRepo.save(newAdmin);

        credentialsRepo.save(agent);
        return newAdmin;

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
    public List<TransactionsHistoryDto> getTransactionForCurrentMonth() {
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
    public List<TransactionsHistoryDto> getTransactionForCurrentWeek() {
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
    public List<TransactionsHistoryDto> getTransactionForToday() {
        List<Transactions> currTransactions = transactionRepo.getTransactionBetweenDates(LocalDate.now(), LocalDate.now());
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
    public List<TransactionsHistoryDto> getTransactionBetweenDates(LocalDate startDate, LocalDate endDate) {
        List<Transactions> currTransactions = transactionRepo.getTransactionBetweenDates(startDate, endDate);
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
