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
import com.RWI.Nidhi.dto.*;
import com.RWI.Nidhi.entity.*;
import com.RWI.Nidhi.enums.*;
import com.RWI.Nidhi.otpSendAndVerify.OtpServiceImplementation;
import com.RWI.Nidhi.repository.*;
import com.RWI.Nidhi.user.serviceImplementation.UserLoanServiceImplementation;
import com.RWI.Nidhi.user.serviceInterface.UserService;
import com.amazonaws.services.xray.model.Http;
import com.amazonaws.waiters.HttpSuccessStatusAcceptor;
import org.apache.http.protocol.ResponseServer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    @Autowired
    SchemeRepo schemeRepo;
    @Autowired
    UserService userService;
    @Autowired
    UserLoanServiceImplementation userLoanService;
    @Autowired
    JavaMailSender javaMailSender;


    @Autowired
    AccountsRepo accountsRepo;

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
    public ResponseEntity<?> deleteUserById(String userEmail, String agentEmail) {
        User user = userRepo.findByEmail(userEmail);
        Agent agent = user.getAgent();
        if (!agent.getAgentEmail().equals(agentEmail)) {
            return new ResponseEntity<>("This user is not in current agent's list", HttpStatus.NOT_FOUND);
        }
        userRepo.deleteById(user.getUserId());
        return new ResponseEntity<>("User Deleted", HttpStatus.OK);
    }
@Override
    public ResponseEntity<?> addUser(SignupRequest signUpRequest, String agentEmail) {

        if (agentRepo.existsByAgentEmail(signUpRequest.getEmail()) || userRepo.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>("Email already taken", HttpStatus.NOT_ACCEPTABLE);
        }
        if (adminRepo.existsByAdminName(signUpRequest.getUsername()) || agentRepo.existsByAgentName(signUpRequest.getUsername()) || userRepo.existsByUserName(signUpRequest.getUsername())) {
            return new ResponseEntity<>("Username already taken", HttpStatus.NOT_ACCEPTABLE);
        }
        if (adminRepo.existsByPhoneNumber(signUpRequest.getPhoneNumber()) || agentRepo.existsByAgentPhoneNum(signUpRequest.getPhoneNumber()) || userRepo.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
            return new ResponseEntity<>("Phone number already taken", HttpStatus.NOT_ACCEPTABLE);
        }

        //Getting the agent from repo by email
        Agent agent = agentRepo.findByAgentEmail(agentEmail);

        //Check if agent exists or not
        if (agent == null) {
            return new ResponseEntity<>("Agent doesn't exists", HttpStatus.NOT_FOUND);
        }
        //creation of new user
        User newUser = new User();
        newUser.setUserName(signUpRequest.getUsername());
        newUser.setEmail(signUpRequest.getEmail());
        newUser.setPhoneNumber(signUpRequest.getPhoneNumber());
        newUser.setAgent(agent);
        agent.getUserList().add(newUser);
        try {
//            String tempPassword = "user21";
//                    otpServiceImplementation.generateOTP();
//            String subject = newUser.getUserName();
//            String messageToSend = "Welcome to Nidhi Bank,Your temporary system generated password is: ";

            String  tempPassword = otpServiceImplementation.generateOTP();
            String   subject = "Your temporary password";
            String   messageToSend = "Your temporary system generated password is: ";
            System.out.println("Sending email");
            otpServiceImplementation.sendEmailOtp(newUser.getEmail(), subject, messageToSend, tempPassword);
            newUser.setPassword(encoder.encode(tempPassword));
            userRepo.save(newUser);
        } catch (Exception e) {
            return new ResponseEntity<>("Email Error" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Credentials credentials = new Credentials(signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPhoneNumber(),
                newUser.getPassword());

        // Set default role as USER for user
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER).get();
        if (userRole == null) {
            return new ResponseEntity<>("User role not found", HttpStatus.NOT_FOUND);
        }
//                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);
        credentials.setRoles(roles);
        newUser.setRoles(roles);
        userRepo.save(newUser);
        credentialsRepo.save(credentials);
        agentRepo.save(agent);
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUserName(newUser.getUserName());
        userResponseDto.setEmail(newUser.getEmail());
        userResponseDto.setPhoneNumber(newUser.getPhoneNumber());
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
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
        Transactions.addTotalBalance(amount);
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
        Transactions.addTotalBalance(amount);
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


    // METHODS FROM AGENT SERVICE


    @Override
    public ResponseEntity<?> deactivateAccount(String accountNumber, String agentEmail) {
        Accounts currentAcc = accountsRepo.findByAccountNumber(accountNumber).get();
        if (currentAcc == null) return new ResponseEntity<>("Account number doesn't exists", HttpStatus.NOT_FOUND);

        Agent agent = agentRepo.findByAgentEmail(agentEmail);
        User user = currentAcc.getUser();
        if (agent == null) return new ResponseEntity<>("Agent doesn't exists", HttpStatus.NOT_FOUND);
        if (!user.getAgent().getAgentEmail().equals(agentEmail))
            return new ResponseEntity<>("This agent is not associated with this account's owner", HttpStatus.NOT_FOUND);


        currentAcc.setAccountStatus(Status.INACTIVE);
        accountsRepo.save(currentAcc);
        return new ResponseEntity<>("Account decativated!!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> closeAccount(String accountNumber, String agentEmail) {
        Accounts currentAcc = accountsRepo.findByAccountNumber(accountNumber).get();
        if (currentAcc == null) return new ResponseEntity<>("Account number doesn't exists", HttpStatus.NOT_FOUND);

        Agent agent = agentRepo.findByAgentEmail(agentEmail);
        User user = currentAcc.getUser();
        if (agent == null) return new ResponseEntity<>("Agent doesn't exists", HttpStatus.NOT_FOUND);
        if (!user.getAgent().getAgentEmail().equals(agentEmail))
            return new ResponseEntity<>("This agent is not associated with this account's owner", HttpStatus.NOT_FOUND);

        currentAcc.setAccountStatus(Status.CLOSED);
        accountsRepo.save(currentAcc);
        return new ResponseEntity<>("Account closed!!", HttpStatus.OK);
    }



    @Override
    public ResponseEntity<?> changeLoanStatus(String userEmail, String agentEmail, LoanStatus changedStatus, LoanStatus previousStatus) {
        if (agentRepo.existsByAgentEmail(agentEmail)) {
            User user = userService.getByEmail(userEmail);
            Accounts accounts = user.getAccounts();
            List<Loan> loanList = accounts.getLoanList();
            if(loanList.isEmpty()){
                return new ResponseEntity<>("No Loan exists for the given user",HttpStatus.I_AM_A_TEAPOT);
            }
            else {
                for (Loan loan : loanList) {
                    if(loan.getStatus()==previousStatus){
                        if(previousStatus == LoanStatus.APPLIED && changedStatus == LoanStatus.APPROVED){
                            loan.setStatus(changedStatus);
                            loan.setStartDate(LocalDate.now());
                            loan.setEmiDate(userLoanService.calcFirstEMIDate(loan.getStartDate()));
                            LoanCalcDto loanCalcDto = new LoanCalcDto();
                            loanCalcDto.setLoanType(loan.getLoanType());
                            loanCalcDto.setRePaymentTerm(loan.getRePaymentTerm());
                            loanCalcDto.setPrincipalLoanAmount(loan.getPrincipalLoanAmount());
                            loanCalcDto.setInterestRate(loan.getLoanType());
                            loan.setMonthlyEMI(userLoanService.calculateEMI(loanCalcDto));
                            loan.setPayableLoanAmount(userLoanService.calculateFirstPayableAmount(loanCalcDto));
                            loanRepo.save(loan);
                            sendStatusEmail(loan);
                        } else if (previousStatus == LoanStatus.APPLIED && changedStatus == LoanStatus.PENDING){
                            loan.setStatus(changedStatus);
                            loanRepo.save(loan);
                            sendStatusEmail(loan);
                        } else if (previousStatus == LoanStatus.APPLIED && changedStatus == LoanStatus.REJECTED){
                            loan.setStatus(changedStatus);
                            loanRepo.save(loan);
                            sendStatusEmail(loan);
                        } else if (previousStatus == LoanStatus.APPROVED && changedStatus == LoanStatus.SANCTIONED){
                            loan.setStatus(changedStatus);
                            
                            Transactions transactions = new Transactions();
                            transactions.setAccount(accounts);
                            transactions.setLoan(loan);
                            transactions.setTransactionAmount(loan.getPrincipalLoanAmount());
                            Transactions.deductTotalBalance(loan.getPrincipalLoanAmount());
                            transactions.setTransactionDate(new Date());
                            transactions.setTransactionType(TransactionType.DEBITED);
                            transactions.setTransactionStatus(TransactionStatus.COMPLETED);
                            transactionRepo.save(transactions);
                            loan.getTransactionsList().add(transactions);

                            loanRepo.save(loan);
                            sendStatusEmail(loan);
                        } else if (previousStatus == LoanStatus.APPROVED && changedStatus == LoanStatus.PENDING){
                            loan.setStatus(changedStatus);
                            loanRepo.save(loan);
                            sendStatusEmail(loan);
                        } else if (previousStatus == LoanStatus.SANCTIONED && changedStatus == LoanStatus.CLOSED){
                            loan.setStatus(changedStatus);
                            loanRepo.save(loan);
                            sendStatusEmail(loan);
                        } else if (previousStatus == LoanStatus.SANCTIONED && changedStatus == LoanStatus.PENDING){
                            loan.setStatus(changedStatus);
                            loanRepo.save(loan);
                            sendStatusEmail(loan);
                        } else if (previousStatus == LoanStatus.REQUESTEDFORFORECLOSURE && changedStatus == LoanStatus.FORECLOSED){
                            loan.setStatus(changedStatus);
                            loanRepo.save(loan);
                            sendStatusEmail(loan);
                        }else {
                            return new ResponseEntity<>("Invalid Change in status",HttpStatus.I_AM_A_TEAPOT);
                        }
                    }else
                        return new ResponseEntity<>("Applied Change in status doesn't match recorded status",HttpStatus.I_AM_A_TEAPOT);
                }
                return new ResponseEntity<>("Invalid Agent",HttpStatus.I_AM_A_TEAPOT);
            }
        } else
            return new ResponseEntity<>("Invalid Agent",HttpStatus.I_AM_A_TEAPOT);
    }

    private void sendStatusEmail(Loan loan) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(loan.getUser().getEmail());
        mailMessage.setSubject("Change in Loan Status");
        mailMessage.setText("Hello User," + loan.getUser().getUserName() + ",\n\n Your loan status has been changed to" + loan.getStatus() + "Please confirm so with your respective agent.");
        javaMailSender.send(mailMessage);
    }
    public ResponseEntity<?> ChangeSchemeStatus(String userEmail, String agentEmail, SchemeStatus changedStatus, SchemeStatus previousStatus){
        if (agentRepo.existsByAgentEmail(agentEmail)) {
            if (userRepo.existsByEmail(userEmail)) {
                User user = userService.getByEmail(userEmail);
                Accounts accounts = user.getAccounts();
                if (accounts != null) {
                    Scheme scheme = accounts.getScheme();
                    if (scheme == null) {
                        return new ResponseEntity<>("No Scheme exists for the given user", HttpStatus.I_AM_A_TEAPOT);
                    } else {
                        if (scheme.getSStatus() == previousStatus) {
                            if (previousStatus == SchemeStatus.APPLIED && changedStatus == SchemeStatus.APPROVED) {
                                scheme.setSStatus(changedStatus);
                                scheme.setStartDate(LocalDate.now());
                                scheme.setInterestRate(10);
                                scheme.setNextEMIDate(firstDateOfNextMonth(LocalDate.now()));
                                scheme.setAgent(agentRepo.findByAgentEmail(agentEmail));
                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                            } else if (previousStatus == SchemeStatus.APPLIED && changedStatus == SchemeStatus.PENDING) {
                                scheme.setSStatus(changedStatus);
                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                            } else if (previousStatus == SchemeStatus.APPLIED && changedStatus == SchemeStatus.REJECTED) {
                                scheme.setSStatus(changedStatus);
                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                            } else if (previousStatus == SchemeStatus.APPROVED && changedStatus == SchemeStatus.SANCTIONED) {
                                scheme.setSStatus(changedStatus);

                                Transactions transactions = new Transactions();
                                transactions.setAccount(accounts);
                                transactions.setScheme(scheme);
                                transactions.setTransactionAmount(scheme.getMonthlyDepositAmount()*scheme.getTenure());
                                Transactions.deductTotalBalance(scheme.getMonthlyDepositAmount()*scheme.getTenure());
                                transactions.setTransactionDate(new Date());
                                transactions.setTransactionType(TransactionType.DEBITED);
                                transactions.setTransactionStatus(TransactionStatus.COMPLETED);
                                transactionRepo.save(transactions);
                                scheme.getTransactionsList().add(transactions);

                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                            } else if (previousStatus == SchemeStatus.APPROVED && changedStatus == SchemeStatus.PENDING) {
                                scheme.setSStatus(changedStatus);
                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                            } else if (previousStatus == SchemeStatus.SANCTIONED && changedStatus == SchemeStatus.CLOSED) {
                                scheme.setSStatus(changedStatus);
                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                            } else if (previousStatus == SchemeStatus.SANCTIONED && changedStatus == SchemeStatus.PENDING) {
                                scheme.setSStatus(changedStatus);
                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                            } else if (previousStatus == SchemeStatus.APPLIEDFORLOAN && changedStatus == SchemeStatus.APPROVEDLOAN) {
                                scheme.setSStatus(changedStatus);
                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                            } else {
                                return new ResponseEntity<>("Invalid Change in status", HttpStatus.I_AM_A_TEAPOT);
                            }
                        } else {
                            return new ResponseEntity<>("Applied Change in status doesn't match recorded status", HttpStatus.I_AM_A_TEAPOT);
                        }
                    }
                } else {
                    return new ResponseEntity<>("Account doesn't exist", HttpStatus.I_AM_A_TEAPOT);
                }
            }else {
                return  new ResponseEntity<>("User doesn't exist", HttpStatus.I_AM_A_TEAPOT);
            }
        }
        else {
            return new ResponseEntity<>("Invalid Agent", HttpStatus.I_AM_A_TEAPOT);
        }
        return null;
    }
    private void sendStatusEmail(Scheme scheme) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(scheme.getAccount().getUser().getEmail());
        mailMessage.setSubject("Change in Scheme Status");
        mailMessage.setText("Hello User," + scheme.getAccount().getUser().getUserName() + ",\n\n Your Scheme Status has been changed to" + scheme.getSStatus() + "Please confirm so with your respective agent.");
        javaMailSender.send(mailMessage);
    }
    private LocalDate firstDateOfNextMonth(LocalDate date) {
        LocalDate nextMonth = date.plusMonths(1);
        return nextMonth.withDayOfMonth(1);
    }
    @Override
    public String deleteScheme(String email) {// scheme delete for when scheme has ended
        if (userRepo.existsByEmail(email)) {
            User user = userService.getByEmail(email);
            Accounts accounts = user.getAccounts();
            Scheme scheme = accounts.getScheme();
            if (scheme.getSStatus() == SchemeStatus.CLOSED) {
                accounts.setScheme(null);
                accountsRepo.save(accounts);
                schemeRepo.delete(scheme);
                return "Record Removed";
            }
            return "Scheme status not closed";
        }
        return "User not found";
    }

}
