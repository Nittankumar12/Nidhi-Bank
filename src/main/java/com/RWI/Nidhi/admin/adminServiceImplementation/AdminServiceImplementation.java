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
import com.RWI.Nidhi.dto.LoanHistoryDto;
import com.RWI.Nidhi.dto.TransactionsHistoryDto;
import com.RWI.Nidhi.entity.*;
import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.enums.LoanType;
import com.RWI.Nidhi.enums.TransactionStatus;
import com.RWI.Nidhi.enums.TransactionType;
import com.RWI.Nidhi.otpSendAndVerify.OtpServiceImplementation;
import com.RWI.Nidhi.repository.*;
import com.amazonaws.services.xray.model.Http;
import com.amazonaws.waiters.HttpSuccessStatusAcceptor;
import org.apache.http.protocol.ResponseServer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.*;
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
    public ResponseEntity<?> addAgent(SignupRequest signUpRequest){

         if(agentRepo.existsByAgentEmail(signUpRequest.getEmail()) || userRepo.existsByEmail(signUpRequest.getEmail())){
            return new ResponseEntity<>("Email Already taken", HttpStatus.NOT_ACCEPTABLE);
        }
        if(adminRepo.existsByAdminName(signUpRequest.getUsername()) || agentRepo.existsByAgentName(signUpRequest.getEmail()) || userRepo.existsByUserName(signUpRequest.getUsername())){
            return new ResponseEntity<>("Username Already taken", HttpStatus.NOT_ACCEPTABLE);
        }
        if(adminRepo.existsByPhoneNumber(signUpRequest.getPhoneNumber()) || agentRepo.existsByAgentPhoneNum(signUpRequest.getPhoneNumber()) || userRepo.existsByPhoneNumber(signUpRequest.getPhoneNumber())){
            return new ResponseEntity<>("Phone number already taken", HttpStatus.NOT_ACCEPTABLE);
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
            return new ResponseEntity<>("Error Occurred " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Credentials agent = new Credentials(signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPhoneNumber(),
                newAgent.getAgentPassword());

        Set<Role> roles = new HashSet<>();
        Role agentRole = roleRepository.findByName(ERole.ROLE_AGENT).get();

        if(agentRole == null) return new ResponseEntity<>("Role Not found ", HttpStatus.NOT_FOUND);

        roles.add(agentRole);
        agent.setRoles(roles);
        newAgent.setRoles(roles);
        agentRepo.save(newAgent);
        credentialsRepo.save(agent);
        return new ResponseEntity<>(signUpRequest,HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> addAdmin(@NotNull SignupRequest signUpRequest,@NotNull String adminPassword){
        if(agentRepo.existsByAgentEmail(signUpRequest.getEmail()) || userRepo.existsByEmail(signUpRequest.getEmail())){
            return new ResponseEntity<>("Email Already taken" , HttpStatus.NOT_ACCEPTABLE);
        }
        if(adminRepo.existsByAdminName(signUpRequest.getUsername()) || agentRepo.existsByAgentName(signUpRequest.getEmail()) || userRepo.existsByUserName(signUpRequest.getUsername())){
            return new ResponseEntity<>("Username already taken", HttpStatus.NOT_ACCEPTABLE);
        }
        if(adminRepo.existsByPhoneNumber(signUpRequest.getPhoneNumber()) || agentRepo.existsByAgentPhoneNum(signUpRequest.getPhoneNumber()) || userRepo.existsByPhoneNumber(signUpRequest.getPhoneNumber())){
            return new ResponseEntity<>("Phone number already taken", HttpStatus.NOT_ACCEPTABLE);
        }
        Admin newAdmin = new Admin();
        newAdmin.setAdminName(signUpRequest.getUsername());
        newAdmin.setEmail(signUpRequest.getEmail());
        newAdmin.setPhoneNumber(signUpRequest.getPhoneNumber());
        try {
            String tempPassword = adminPassword;
            newAdmin.setPassword(encoder.encode(tempPassword));
            adminRepo.save(newAdmin);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occured " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Credentials agent = new Credentials(signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPhoneNumber(),
                newAdmin.getPassword());
//
        Set<Role> roles = new HashSet<>();
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).get();
        if(adminRole == null) return new ResponseEntity<>("Role not found", HttpStatus.NOT_FOUND);

        roles.add(adminRole);
        agent.setRoles(roles);
        newAdmin.setRoles(roles);
        adminRepo.save(newAdmin);

        credentialsRepo.save(agent);
        return new ResponseEntity<>("Admin Registered Successfully",HttpStatus.OK);

    }
    @Override
    public ResponseEntity<?> updateAgentName(String agentEmail, String agentName) throws Exception{
        Agent currAgent = agentRepo.findByAgentEmail(agentEmail);
        if(currAgent == null) return new ResponseEntity<>("Agent Not Found", HttpStatus.NOT_FOUND);
        currAgent.setAgentName(agentName);
        agentRepo.save(currAgent);

        AddAgentDto addAgentDto = new AddAgentDto();
        addAgentDto.setAgentName(currAgent.getAgentName());
        addAgentDto.setAgentEmail(currAgent.getAgentEmail());
        addAgentDto.setAgentPhoneNum(currAgent.getAgentPhoneNum());
        return new ResponseEntity<>(addAgentDto,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateAgentAddress(String agentEmail, String agentAddress) throws Exception {
        Agent currAgent = agentRepo.findByAgentEmail(agentEmail);
        if(currAgent == null) return new ResponseEntity<>("Agent Not Found", HttpStatus.NOT_FOUND);
        currAgent.setAgentAddress(agentAddress);
        agentRepo.save(currAgent);

        AddAgentDto addAgentDto = new AddAgentDto();
        addAgentDto.setAgentName(currAgent.getAgentName());
        addAgentDto.setAgentEmail(currAgent.getAgentEmail());
        addAgentDto.setAgentPhoneNum(currAgent.getAgentPhoneNum());
        return new ResponseEntity<>(addAgentDto,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateAgentEmail(String agentOldEmail, String agentNewEmail) throws Exception{
        Agent currAgent = agentRepo.findByAgentEmail(agentOldEmail);
        if(currAgent == null) return new ResponseEntity<>("Agent Not Found", HttpStatus.NOT_FOUND);
        currAgent.setAgentEmail(agentNewEmail);
        agentRepo.save(currAgent);

        AddAgentDto addAgentDto = new AddAgentDto();
        addAgentDto.setAgentName(currAgent.getAgentName());
        addAgentDto.setAgentEmail(currAgent.getAgentEmail());
        addAgentDto.setAgentPhoneNum(currAgent.getAgentPhoneNum());
        return new ResponseEntity<>(addAgentDto, HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> updateAgentPhoneNum(String agentEmail, String phoneNum) throws Exception {
        Agent currAgent = agentRepo.findByAgentEmail(agentEmail);
        if(currAgent == null) return new ResponseEntity<>("Agent Not Found", HttpStatus.NOT_FOUND);
        currAgent.setAgentPhoneNum(phoneNum);
        agentRepo.save(currAgent);

        AddAgentDto addAgentDto = new AddAgentDto();
        addAgentDto.setAgentName(currAgent.getAgentName());
        addAgentDto.setAgentEmail(currAgent.getAgentEmail());
        addAgentDto.setAgentPhoneNum(currAgent.getAgentPhoneNum());
        return new ResponseEntity<>(addAgentDto, HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> deleteAgentById(int id){
        Agent agent = agentRepo.findById(id).get();
        if(agent == null) return new ResponseEntity<>("Agent Not found", HttpStatus.NOT_FOUND);
        agentRepo.deleteById(id);
        return new ResponseEntity<>("Agent Deleted" , HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> getAllAgents() {
        List<Agent> allAgents = agentRepo.findAll();
        if(allAgents.size() == 0) return new ResponseEntity<>("No agents Found", HttpStatus.NOT_FOUND);
        List<AgentMinimalDto> idUsernameAgent = allAgents.stream()
                .map(agent -> new AgentMinimalDto(agent.getAgentId(),agent.getAgentName()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(idUsernameAgent,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAgentById(int id){
        Agent agent = agentRepo.findById(id).get();
        if(agent == null) return new ResponseEntity<>("Agent not found with this id",HttpStatus.NOT_FOUND);

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

        return new ResponseEntity<>(responseDto,HttpStatus.OK);

    }
    @Override
    public ResponseEntity<?> getTransactionForCurrentMonth() {
        List<Transactions> currTransactions = transactionRepo.getTransactionBetweenDates(LocalDate.now().withDayOfMonth(1),LocalDate.now());
        if(currTransactions.size() == 0) return new ResponseEntity<>("No Transactions found", HttpStatus.NOT_FOUND);
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
        return new ResponseEntity<>(transactionsHistoryList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getTransactionForCurrentWeek() {
        List<Transactions> currTransactions = transactionRepo.getTransactionBetweenDates(LocalDate.now().minusDays(7),LocalDate.now());
        if(currTransactions.size() == 0) return new ResponseEntity<>("No Transactions found", HttpStatus.NOT_FOUND);

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
        return new ResponseEntity<>(transactionsHistoryList,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getTransactionForToday() {
        List<Transactions> currTransactions = transactionRepo.getTransactionBetweenDates(LocalDate.now(), LocalDate.now());
        if(currTransactions.size() == 0) return new ResponseEntity<>("No Transactions found", HttpStatus.NOT_FOUND);
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
        return new ResponseEntity<>(transactionsHistoryList,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getTransactionBetweenDates(LocalDate startDate, LocalDate endDate) {
        List<Transactions> currTransactions = transactionRepo.getTransactionBetweenDates(startDate, endDate);
        if(currTransactions.size() == 0) return new ResponseEntity<>("No Transactions found", HttpStatus.NOT_FOUND);

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
        return new ResponseEntity<>(transactionsHistoryList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> findByStatus(LoanStatus status) {
        return new ResponseEntity<>(loanRepo.findByStatus(status), HttpStatus.OK);
    }

    @Override
    public List<LoanHistoryDto> getLoansByLoanType(LoanType loanType) {
        List<Loan> loans = loanRepo.findAll().stream().filter((loan ->loan.getLoanType().equals(loanType))).toList();

        List<LoanHistoryDto> loanDTOList = new ArrayList<>();
        for(Loan loan:loans){
            LoanHistoryDto loanHistoryDto = new LoanHistoryDto();
            loanHistoryDto.setLoanId(loan.getLoanId());
            loanHistoryDto.setUserName(loan.getAccount().getUser().getUserName());
            loanHistoryDto.setRequestedLoanAmount(loan.getPrincipalLoanAmount());
            loanHistoryDto.setInterestRate(loan.getInterestRate());
            loanHistoryDto.setMonthlyEmi(loan.getMonthlyEMI());
            loanHistoryDto.setStatus(loan.getStatus().toString());
            loanHistoryDto.setLoanType(loan.getLoanType().toString());
            loanDTOList.add(loanHistoryDto);
            System.out.println(loanDTOList);
        }
        return loanDTOList;
    }

    @Override
    public ResponseEntity<?> addBalanceToAccount(double amount) {
        Transactions.setTotalBalance(Transactions.getTotalBalance() + amount);
        Transactions newTransaction = new Transactions();
        newTransaction.setTransactionDate(new Date());
        newTransaction.setTransactionType(TransactionType.CREDITED);
        newTransaction.setTransactionAmount(amount);
        newTransaction.setTransactionStatus(TransactionStatus.COMPLETED);

        transactionRepo.save(newTransaction);
        return new ResponseEntity<>(newTransaction, HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> deductBalanceToAccount(double amount) {
        Transactions.setTotalBalance(Transactions.getTotalBalance() + amount);
        Transactions newTransaction = new Transactions();
        newTransaction.setTransactionDate(new Date());
        newTransaction.setTransactionType(TransactionType.DEBITED);
        newTransaction.setTransactionAmount(amount);
        newTransaction.setTransactionStatus(TransactionStatus.COMPLETED);

        transactionRepo.save(newTransaction);
        return new ResponseEntity<>(newTransaction, HttpStatus.OK);
    }

    @Override
    public List<LoanHistoryDto> getLoansByLoanStatus(LoanStatus loanStatus) {
        List<Loan> loans = loanRepo.findAll().stream().filter((loan) ->loan.getStatus().equals(loanStatus)).toList();
        List<LoanHistoryDto> loanDTOList = new ArrayList<>();
        loans.forEach(loan -> {
            LoanHistoryDto loanHistoryDto = new LoanHistoryDto();
            loanHistoryDto.setLoanId(loan.getLoanId());
            loanHistoryDto.setUserName(loan.getAccount().getUser().getUserName());
            loanHistoryDto.setRequestedLoanAmount(loan.getPrincipalLoanAmount());
            loanHistoryDto.setInterestRate(loan.getInterestRate());
            loanHistoryDto.setStatus(loan.getStatus().toString());
            loanHistoryDto.setMonthlyEmi(loan.getMonthlyEMI());
            loanHistoryDto.setInterestRate(loan.getInterestRate());
            loanDTOList.add(loanHistoryDto);
        });

        return loanDTOList;
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
