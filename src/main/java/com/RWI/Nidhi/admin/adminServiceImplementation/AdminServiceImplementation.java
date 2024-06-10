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
import com.RWI.Nidhi.user.serviceImplementation.*;
import com.RWI.Nidhi.user.serviceInterface.UserService;
import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
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
    private static final int CODE_LENGTH = 6;
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    @Autowired
    AgentRepo agentRepo;
    @Autowired
    LoanRepo loanRepo;
   // @Autowired
    AccountsServiceImplementation accountsService;
    @Autowired
    EmiService emiService;
    @Autowired
    KycDetailsServiceImp kycDetailsService;
    @Autowired
    KycDetailsRepo kycDetailsRepo;
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
    UserSchemeLoanServiceImplementation userSchemeLoanService;
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
    MisRepo misRepo;
    @Autowired
    RecurringDepositRepo recurringDepositRepo;
    @Autowired
    FixedDepositRepo fixedDepositRepo;
    @Autowired
    AccountsRepo accountsRepo;

    @Override
    public ResponseEntity<?> addAgent(SignupRequest signUpRequest) {
        if (agentRepo.existsByAgentEmail(signUpRequest.getEmail()) || userRepo.existsByEmail(signUpRequest.getEmail())) {

            return new ResponseEntity<>("Email Already taken", HttpStatus.NOT_ACCEPTABLE);
        }
        if (adminRepo.existsByAdminName(signUpRequest.getUsername()) || agentRepo.existsByAgentName(signUpRequest.getEmail()) || userRepo.existsByUserName(signUpRequest.getUsername())) {
            return new ResponseEntity<>("Username Already taken", HttpStatus.NOT_ACCEPTABLE);
        }
        if (adminRepo.existsByPhoneNumber(signUpRequest.getPhoneNumber()) || agentRepo.existsByAgentPhoneNum(signUpRequest.getPhoneNumber()) || userRepo.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
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

        if (agentRole == null) return new ResponseEntity<>("Role Not found ", HttpStatus.NOT_FOUND);

        roles.add(agentRole);
        agent.setRoles(roles);
        newAgent.setRoles(roles);
        newAgent.setReferralCode(generateReferralCode());
        agentRepo.save(newAgent);
        credentialsRepo.save(agent);
        return new ResponseEntity<>(signUpRequest, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> addAdmin(@NotNull SignupRequest signUpRequest, @NotNull String adminPassword) {
        if (agentRepo.existsByAgentEmail(signUpRequest.getEmail()) || userRepo.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>("Email Already taken", HttpStatus.NOT_ACCEPTABLE);
        }
        if (adminRepo.existsByAdminName(signUpRequest.getUsername()) || agentRepo.existsByAgentName(signUpRequest.getEmail()) || userRepo.existsByUserName(signUpRequest.getUsername())) {
            return new ResponseEntity<>("Username already taken", HttpStatus.NOT_ACCEPTABLE);
        }
        if (adminRepo.existsByPhoneNumber(signUpRequest.getPhoneNumber()) || agentRepo.existsByAgentPhoneNum(signUpRequest.getPhoneNumber()) || userRepo.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
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
            return new ResponseEntity<>("Error occurred " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Credentials agent = new Credentials(signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPhoneNumber(),
                newAdmin.getPassword());
//
        Set<Role> roles = new HashSet<>();
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).get();
        if (adminRole == null) return new ResponseEntity<>("Role not found", HttpStatus.NOT_FOUND);

        roles.add(adminRole);
        agent.setRoles(roles);
        newAdmin.setRoles(roles);
        adminRepo.save(newAdmin);

        credentialsRepo.save(agent);
        return new ResponseEntity<>("Admin Registered Successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateAgentName(String agentEmail, String agentName) throws Exception {
        Agent currAgent = agentRepo.findByAgentEmail(agentEmail);
        if (currAgent == null) return new ResponseEntity<>("Agent Not Found", HttpStatus.NOT_FOUND);
        currAgent.setAgentName(agentName);
        agentRepo.save(currAgent);

        AddAgentDto addAgentDto = new AddAgentDto();
        addAgentDto.setAgentName(currAgent.getAgentName());
        addAgentDto.setAgentEmail(currAgent.getAgentEmail());
        addAgentDto.setAgentPhoneNum(currAgent.getAgentPhoneNum());
        return new ResponseEntity<>(addAgentDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteUserById(String userEmail, String agentEmail) {
        User user = userRepo.findByEmail(userEmail);
        Agent agent = user.getAgent();
        if (!agent.getAgentEmail().equals(agentEmail)) {
            return new ResponseEntity<>("This user is not in current agent's list", HttpStatus.NOT_FOUND);
        }
        user.getAccounts().setAccountStatus(Status.CLOSED);
        List<FixedDeposit> fixedDepositList = user.getAccounts().getFdList();
        for (FixedDeposit fixedDeposit : fixedDepositList) {
            fixedDeposit.setFdStatus(Status.FORECLOSED);
            fixedDepositRepo.save(fixedDeposit);
        }
        List<RecurringDeposit> recurringDepositList = user.getAccounts().getRecurringDepositList();
        for (RecurringDeposit fixedDeposit : recurringDepositList) {
            fixedDeposit.setRdStatus(Status.FORECLOSED);
            recurringDepositRepo.save(fixedDeposit);
        }
        List<MIS> misList = user.getAccounts().getMisList();
        for (MIS fixedDeposit : misList) {
            fixedDeposit.setStatus(Status.FORECLOSED);
            misRepo.save(fixedDeposit);
        }
        List<Loan> loanList = user.getAccounts().getLoanList();
        for (Loan fixedDeposit : loanList) {
            fixedDeposit.setStatus(LoanStatus.FORECLOSED);
            loanRepo.save(fixedDeposit);
        }
        Scheme scheme = user.getAccounts().getScheme();
        scheme.setSStatus(SchemeStatus.FORECLOSED);
        schemeRepo.save(scheme);

        userRepo.save(user);

        return new ResponseEntity<>("User Deleted", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateAgentAddress(String agentEmail, String agentAddress) throws Exception {
        Agent currAgent = agentRepo.findByAgentEmail(agentEmail);
        if (currAgent == null) return new ResponseEntity<>("Agent Not Found", HttpStatus.NOT_FOUND);
        currAgent.setAgentAddress(agentAddress);
        agentRepo.save(currAgent);

        AddAgentDto addAgentDto = new AddAgentDto();
        addAgentDto.setAgentName(currAgent.getAgentName());
        addAgentDto.setAgentEmail(currAgent.getAgentEmail());
        addAgentDto.setAgentPhoneNum(currAgent.getAgentPhoneNum());
        return new ResponseEntity<>(addAgentDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateAgentEmail(String agentOldEmail, String agentNewEmail) throws Exception {
        Agent currAgent = agentRepo.findByAgentEmail(agentOldEmail);
        if (currAgent == null) return new ResponseEntity<>("Agent Not Found", HttpStatus.NOT_FOUND);
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
        if (currAgent == null) return new ResponseEntity<>("Agent Not Found", HttpStatus.NOT_FOUND);
        currAgent.setAgentPhoneNum(phoneNum);
        agentRepo.save(currAgent);

        AddAgentDto addAgentDto = new AddAgentDto();
        addAgentDto.setAgentName(currAgent.getAgentName());
        addAgentDto.setAgentEmail(currAgent.getAgentEmail());
        addAgentDto.setAgentPhoneNum(currAgent.getAgentPhoneNum());
        return new ResponseEntity<>(addAgentDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteAgentById(int id) {
        Agent agent = agentRepo.findById(id).get();
        if (agent == null) return new ResponseEntity<>("Agent Not found", HttpStatus.NOT_FOUND);
        agentRepo.deleteById(id);
        return new ResponseEntity<>("Agent Deleted", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllAgents() {
        List<Agent> allAgents = agentRepo.findAll();
        if (allAgents.size() == 0) return new ResponseEntity<>("No agents Found", HttpStatus.NOT_FOUND);
        List<AgentMinimalDto> idUsernameAgent = allAgents.stream()
                .map(agent -> new AgentMinimalDto(agent.getAgentId(), agent.getAgentName(), agent.getAgentEmail(), agent.getReferralCode()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(idUsernameAgent, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> getAgentById(int id) {
        Agent agent = agentRepo.findById(id).get();
        if (agent == null) return new ResponseEntity<>("Agent not found with this id", HttpStatus.NOT_FOUND);

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

        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> getTransactionForCurrentMonth() {
        List<Transactions> currTransactions = transactionRepo.getTransactionBetweenDates(LocalDate.now().withDayOfMonth(1), LocalDate.now());
        if (currTransactions.size() == 0) return new ResponseEntity<>("No Transactions found", HttpStatus.NOT_FOUND);
        List<TransactionsHistoryDto> transactionsHistoryList = new ArrayList<>();
        for (Transactions t : currTransactions) {
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
        List<Transactions> currTransactions = transactionRepo.getTransactionBetweenDates(LocalDate.now().minusDays(7), LocalDate.now());
        if (currTransactions.size() == 0) return new ResponseEntity<>("No Transactions found", HttpStatus.NOT_FOUND);

        List<TransactionsHistoryDto> transactionsHistoryList = new ArrayList<>();
        for (Transactions t : currTransactions) {
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
    public ResponseEntity<?> getTransactionForToday() {
        List<Transactions> currTransactions = transactionRepo.getTransactionBetweenDates(LocalDate.now(), LocalDate.now());
        if (currTransactions.size() == 0) return new ResponseEntity<>("No Transactions found", HttpStatus.NOT_FOUND);
        List<TransactionsHistoryDto> transactionsHistoryList = new ArrayList<>();
        for (Transactions t : currTransactions) {
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
    public ResponseEntity<?> getTransactionBetweenDates(LocalDate startDate, LocalDate endDate) {
        List<Transactions> currTransactions = transactionRepo.getTransactionBetweenDates(startDate, endDate);
        if (currTransactions.size() == 0) return new ResponseEntity<>("No Transactions found", HttpStatus.NOT_FOUND);

        List<TransactionsHistoryDto> transactionsHistoryList = new ArrayList<>();
        for (Transactions t : currTransactions) {
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
        List<Loan> loans = loanRepo.findAll().stream().filter((loan -> loan.getLoanType().equals(loanType))).toList();

        List<LoanHistoryDto> loanDTOList = new ArrayList<>();
        for (Loan loan : loans) {
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
        newTransaction.setTransactionDate(LocalDate.now());
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
        newTransaction.setTransactionDate(LocalDate.now());
        newTransaction.setTransactionType(TransactionType.DEBITED);
        newTransaction.setTransactionAmount(amount);
        newTransaction.setTransactionStatus(TransactionStatus.COMPLETED);

        transactionRepo.save(newTransaction);
        return new ResponseEntity<>(newTransaction, HttpStatus.OK);
    }


    @Override
    public List<LoanHistoryDto> getLoansByLoanStatus(LoanStatus loanStatus) {
        List<Loan> loans = loanRepo.findAll().stream().filter((loan) -> loan.getStatus().equals(loanStatus)).toList();
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

    private String getEncryptedPassword(String password) {
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
        return new ResponseEntity<>("Account deactivated!!", HttpStatus.OK);
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
            if (loanList.isEmpty()) {
                return new ResponseEntity<>("No Loan exists for the given user", HttpStatus.I_AM_A_TEAPOT);
            } else {
                for (Loan loan : loanList) {
                    if (loan.getStatus().equals(previousStatus)) {
                        if(loan.getSignUrl().isEmpty()||loan.getSignVideoUrl().isEmpty())return new ResponseEntity<>("No proof",HttpStatus.NOT_FOUND);
                        if (previousStatus.equals(LoanStatus.APPLIED) && changedStatus.equals(LoanStatus.APPROVED)) {
                            loan.setStatus(changedStatus);
                            loan.setStartDate(LocalDate.now());
                            loan.setTransactionsList(new ArrayList<>());
                            loan.setEmiDate(userLoanService.calcFirstEMIDate(loan.getStartDate()));
                            if (loan.getLoanType().equals(LoanType.Scheme)) {
                                SchLoanCalcDto loanCalcDto = new SchLoanCalcDto();
                                loanCalcDto.setRePaymentTerm(loan.getRePaymentTerm());
                                loanCalcDto.setPrincipalLoanAmount(loan.getPrincipalLoanAmount());
                                loan.setMonthlyEMI(userSchemeLoanService.calculateSchLoanEMI(loanCalcDto));
                                loan.setPayableLoanAmount(userSchemeLoanService.calculateFirstPayableSchLoanAmount(loanCalcDto));

                            } else if (loan.getLoanType().equals(LoanType.Other)) {
                                EmiDetails emiDetails = emiService.calculateEmi(loan.getPrincipalLoanAmount(), loan.getDiscount(), loan.getRePaymentTerm());
                                loan.setPrincipalLoanAmount(emiDetails.getMrpPrice());
                                if (emiDetails.getEmi9Months() == 0) {
                                    loan.setRePaymentTerm(12);
                                    loan.setMonthlyEMI(emiDetails.getEmi12Months());
                                    loan.setPayableLoanAmount(loan.getMonthlyEMI()*loan.getRePaymentTerm());
                                } else if (emiDetails.getEmi12Months() == 0) {
                                    loan.setRePaymentTerm(9);
                                    loan.setMonthlyEMI(emiDetails.getEmi9Months());
                                    loan.setPayableLoanAmount(loan.getMonthlyEMI()*loan.getRePaymentTerm());
                                } else {
                                    throw new IllegalArgumentException("Unsupported EMI duration ");
                                }
                            } else {
                                LoanCalcDto loanCalcDto = new LoanCalcDto();
                                loanCalcDto.setLoanType(loan.getLoanType());
                                loanCalcDto.setRePaymentTerm(loan.getRePaymentTerm());
                                loanCalcDto.setPrincipalLoanAmount(loan.getPrincipalLoanAmount());
                                loanCalcDto.setInterestRate(loan.getLoanType());
                                loan.setMonthlyEMI(userLoanService.calculateEMI(loanCalcDto));
                                loan.setPayableLoanAmount(userLoanService.calculateFirstPayableAmount(loanCalcDto));
                            }
                            loanRepo.save(loan);
                            sendStatusEmail(loan);
                            return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                        } else if (previousStatus.equals(LoanStatus.APPLIED) && changedStatus.equals(LoanStatus.PENDING)) {
                            loan.setStatus(changedStatus);
                            loanRepo.save(loan);
                            sendStatusEmail(loan);
                            return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                        } else if (previousStatus.equals(LoanStatus.APPLIED) && changedStatus.equals(LoanStatus.REJECTED)) {
                            loan.setStatus(changedStatus);
                            loanRepo.save(loan);
                            sendStatusEmail(loan);
                            return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                        } else if (previousStatus.equals(LoanStatus.APPROVED) && changedStatus.equals(LoanStatus.SANCTIONED)) {
                            loan.setStatus(changedStatus);
                            Transactions transactions = new Transactions();
                            transactions.setAccount(accounts);
                            transactions.setLoan(loan);
                            if(loan.getLoanType().equals(LoanType.Scheme)){
                                Scheme scheme = accounts.getScheme();
                                double schemeAmount = scheme.getTotalDepositAmount() + (scheme.getTotalDepositAmount()*scheme.getInterestRate()/scheme.getMonthlyDepositAmount());
                                transactions.setTransactionAmount(loan.getPrincipalLoanAmount() + schemeAmount);
                                Transactions.deductTotalBalance(loan.getPrincipalLoanAmount() + schemeAmount);
                            }else {
                                transactions.setTransactionAmount(loan.getPrincipalLoanAmount());
                                Transactions.deductTotalBalance(loan.getPrincipalLoanAmount());
                            }
                            transactions.setTransactionDate(LocalDate.now());
                            transactions.setTransactionType(TransactionType.DEBITED);
                            transactions.setTransactionStatus(TransactionStatus.COMPLETED);
                            transactionRepo.save(transactions);
                            loan.getTransactionsList().add(transactions);
                            loanRepo.save(loan);
                            sendStatusEmail(loan);
                            return new ResponseEntity<>((("Status updated to " + changedStatus)+userLoanService.getLoanInfo(userEmail)), HttpStatus.OK);
                        } else if (previousStatus.equals(LoanStatus.APPROVED) && changedStatus.equals(LoanStatus.PENDING)) {
                            loan.setStatus(changedStatus);
                            loanRepo.save(loan);
                            sendStatusEmail(loan);
                            return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                        } else if (previousStatus.equals(LoanStatus.APPROVED) && changedStatus.equals(LoanStatus.REJECTED)) {
                            loan.setStatus(changedStatus);
                            loanRepo.save(loan);
                            sendStatusEmail(loan);
                            return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                        } else if (previousStatus.equals(LoanStatus.SANCTIONED) && changedStatus.equals(LoanStatus.CLOSED)) {
                            loan.setStatus(changedStatus);
                            loanRepo.save(loan);
                            sendStatusEmail(loan);
                            return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                        } else if (previousStatus.equals(LoanStatus.SANCTIONED) && changedStatus.equals(LoanStatus.PENDING)) {
                            loan.setStatus(changedStatus);
                            loanRepo.save(loan);
                            sendStatusEmail(loan);
                            return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                        } else if (previousStatus.equals(LoanStatus.PENDING) && changedStatus.equals(LoanStatus.APPROVED)) {
                            loan.setStatus(changedStatus);
                            loanRepo.save(loan);
                            sendStatusEmail(loan);
                            return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                        } else if (previousStatus.equals(LoanStatus.PENDING) && changedStatus.equals(LoanStatus.SANCTIONED)) {
                            loan.setStatus(changedStatus);
                            loanRepo.save(loan);
                            sendStatusEmail(loan);
                            return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                        } else if (previousStatus.equals(LoanStatus.PENDING) && changedStatus.equals(LoanStatus.REJECTED)) {
                            loan.setStatus(changedStatus);
                            loanRepo.save(loan);
                            sendStatusEmail(loan);
                            return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                        } else if (previousStatus.equals(LoanStatus.PENDING) && changedStatus.equals(LoanStatus.CLOSED)) {
                            loan.setStatus(changedStatus);
                            loanRepo.save(loan);
                            sendStatusEmail(loan);
                            return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                        } else if (previousStatus.equals(LoanStatus.REQUESTEDFORFORECLOSURE) && changedStatus.equals(LoanStatus.FORECLOSED)) {
                            loan.setStatus(changedStatus);
                            loanRepo.save(loan);
                            sendStatusEmail(loan);
                            return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                        } else {
                            return new ResponseEntity<>("Invalid Change in status", HttpStatus.I_AM_A_TEAPOT);
                        }
                    }
                }
                return new ResponseEntity<>("Applied Change in status doesn't match recorded status", HttpStatus.I_AM_A_TEAPOT);
            }
        } else
            return new ResponseEntity<>("Invalid Agent", HttpStatus.I_AM_A_TEAPOT);
    }

    private void sendStatusEmail(Loan loan) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(loan.getUser().getEmail());
        mailMessage.setSubject("Change in Loan Status");
        mailMessage.setText("Hello User," + loan.getUser().getUserName() + ",\n\n Your loan status has been changed to " + loan.getStatus() + " Please confirm so with your respective agent.");
        javaMailSender.send(mailMessage);
    }

    public ResponseEntity<?> ChangeSchemeStatus(String userEmail, String agentEmail, SchemeStatus changedStatus, SchemeStatus previousStatus) {
        if (agentRepo.existsByAgentEmail(agentEmail)) {
            if (userRepo.existsByEmail(userEmail)) {
                User user = userService.getByEmail(userEmail);
                Accounts accounts = user.getAccounts();
                if (accounts != null) {
                    Scheme scheme = accounts.getScheme();
                    if (scheme == null) {
                        return new ResponseEntity<>("No Scheme exists for the given user", HttpStatus.I_AM_A_TEAPOT);
                    } else {
                        if (scheme.getSStatus().equals(previousStatus)) {
                            if (previousStatus.equals(SchemeStatus.APPLIED) && changedStatus.equals(SchemeStatus.APPROVED)) {
                                scheme.setSStatus(changedStatus);
                                scheme.setStartDate(LocalDate.now());
                                scheme.setInterestRate(2);
                                scheme.setNextEMIDate(firstDateOfNextMonth(LocalDate.now()));
                                scheme.setAgent(agentRepo.findByAgentEmail(agentEmail));
                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                                return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                            } else if (previousStatus.equals(SchemeStatus.APPLIED) && changedStatus.equals(SchemeStatus.PENDING)) {
                                scheme.setSStatus(changedStatus);
                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                                return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                            } else if (previousStatus.equals(SchemeStatus.APPLIED) && changedStatus.equals(SchemeStatus.REJECTED)) {
                                scheme.setSStatus(changedStatus);
                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                                return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                            } else if (previousStatus.equals(SchemeStatus.APPROVED) && changedStatus.equals(SchemeStatus.SANCTIONED)) {
                                scheme.setSStatus(changedStatus);
                                Transactions transactions = new Transactions();
                                transactions.setAccount(accounts);
                                transactions.setScheme(scheme);
                                transactions.setTransactionAmount(scheme.getMonthlyDepositAmount() * scheme.getTenure());
                                Transactions.deductTotalBalance(scheme.getMonthlyDepositAmount() * scheme.getTenure());
                                transactions.setTransactionDate(LocalDate.now());
                                transactions.setTransactionType(TransactionType.DEBITED);
                                transactions.setTransactionStatus(TransactionStatus.COMPLETED);
                                transactionRepo.save(transactions);
                                scheme.getTransactionsList().add(transactions);
                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                                return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                            } else if (previousStatus.equals(SchemeStatus.APPROVED) && changedStatus.equals(SchemeStatus.PENDING)) {
                                scheme.setSStatus(changedStatus);
                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                                return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                            } else if (previousStatus.equals(SchemeStatus.APPROVED) && changedStatus.equals(SchemeStatus.REJECTED)) {
                                scheme.setSStatus(changedStatus);
                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                                return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                            } else if (previousStatus.equals(SchemeStatus.SANCTIONED) && changedStatus.equals(SchemeStatus.CLOSED)) {
                                scheme.setSStatus(changedStatus);
                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                                return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                            } else if (previousStatus.equals(SchemeStatus.SANCTIONED) && changedStatus.equals(SchemeStatus.PENDING)) {
                                scheme.setSStatus(changedStatus);
                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                                return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                            } else if (previousStatus.equals(SchemeStatus.SANCTIONED) && changedStatus.equals(SchemeStatus.APPLIEDFORLOAN)) {
                                scheme.setSStatus(changedStatus);
                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                                return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                            } else if (previousStatus.equals(SchemeStatus.APPLIEDFORLOAN) && changedStatus.equals(SchemeStatus.APPROVEDLOAN)) {
                                scheme.setSStatus(changedStatus);
                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                                return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                            } else if (previousStatus.equals(SchemeStatus.PENDING) && changedStatus.equals(SchemeStatus.APPROVED)) {
                                scheme.setSStatus(changedStatus);
                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                                return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                            } else if (previousStatus.equals(SchemeStatus.PENDING) && changedStatus.equals(SchemeStatus.SANCTIONED)) {
                                scheme.setSStatus(changedStatus);
                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                                return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                            } else if (previousStatus.equals(SchemeStatus.PENDING) && changedStatus.equals(SchemeStatus.APPLIEDFORLOAN)) {
                                scheme.setSStatus(changedStatus);
                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                                return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                            } else if (previousStatus.equals(SchemeStatus.PENDING) && changedStatus.equals(SchemeStatus.APPROVEDLOAN)) {
                                scheme.setSStatus(changedStatus);
                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                                return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                            } else if (previousStatus.equals(SchemeStatus.PENDING) && changedStatus.equals(SchemeStatus.REJECTED)) {
                                scheme.setSStatus(changedStatus);
                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                                return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
                            } else if (previousStatus.equals(SchemeStatus.PENDING) && changedStatus.equals(SchemeStatus.CLOSED)) {
                                scheme.setSStatus(changedStatus);
                                schemeRepo.save(scheme);
                                sendStatusEmail(scheme);
                                return new ResponseEntity<>(("Status updated to " + changedStatus), HttpStatus.OK);
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
            } else {
                return new ResponseEntity<>("User doesn't exist", HttpStatus.I_AM_A_TEAPOT);
            }
        } else {
            return new ResponseEntity<>("Invalid Agent", HttpStatus.I_AM_A_TEAPOT);
        }
    }

    private void sendStatusEmail(Scheme scheme) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(scheme.getAccount().getUser().getEmail());
        mailMessage.setSubject("Change in Scheme Status");
        mailMessage.setText("Hello User," + scheme.getAccount().getUser().getUserName() + ",\n\n Your Scheme Status has been changed to " + scheme.getSStatus() + " Please confirm so with your respective agent.");
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
            if (scheme.getSStatus().equals(SchemeStatus.CLOSED)) {
                accounts.setScheme(null);
                accountsRepo.save(accounts);
                schemeRepo.delete(scheme);
                return "Record Removed";
            }
            return "Scheme status not closed";
        }
        return "User not found";
    }

    private String generateReferralCode() {
        Random random = new Random();
        StringBuilder referralCode = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(ALPHABET.length());
            referralCode.append(ALPHABET.charAt(index));
        }
        return referralCode.toString();
    }

    @Override
    public ResponseEntity<?> getKycDetails(String userEmail) {
        if (userRepo.existsByEmail(userEmail)) {
            User user = userService.getByEmail(userEmail);
            KycDetails kycDetails = user.getKycDetails();
            if (kycDetails == null) {
                return new ResponseEntity<>("No Kyc exists for the given user", HttpStatus.I_AM_A_TEAPOT);
            } else {
                KycDetails kycDetailss = kycDetailsService.getDetailsByUserEmail(userEmail);
                KycDetailsDto kycDetailsDto = new KycDetailsDto();
                kycDetailsDto.setEmail(kycDetailss.getEmail());
                kycDetailsDto.setCategories(kycDetailss.getCategories());
                kycDetailsDto.setEducation(kycDetailss.getEducation());
                kycDetailsDto.setGender(kycDetailss.getGender());
                kycDetailsDto.setDateOfBirth(kycDetailss.getDateOfBirth());
                kycDetailsDto.setFatherName(kycDetailss.getFatherName());
                kycDetailsDto.setFatherLastName(kycDetailss.getFatherLastName());
                kycDetailsDto.setFirstName(kycDetailss.getFirstName());
                kycDetailsDto.setNationality(kycDetailss.getNationality());
                kycDetailsDto.setEmail(kycDetailss.getEmail());
                kycDetailsDto.setLastName(kycDetailss.getLastName());
                kycDetailsDto.setPhnNo(kycDetailss.getPhnNo());
                kycDetailsDto.setReligion(kycDetailss.getReligion());
                kycDetailsDto.setAlternatePhnNo(kycDetailss.getAlternatePhnNo());
                kycDetailsDto.setNomineeFirstName(kycDetailss.getNomineeFirstName());
                kycDetailsDto.setNomineeLastName(kycDetailss.getNomineeLastName());
                kycDetailsDto.setNomineeContactNumber(kycDetailss.getNomineeContactNumber());
                kycDetailsDto.setRelationWithNominee(kycDetailss.getRelationWithNominee());
                kycDetailsDto.setOccupation(kycDetailss.getOccupation());
                kycDetailsDto.setMonthlyIncome(kycDetailss.getMonthlyIncome());
                kycDetailsDto.setNumberOfFamilyMembers(kycDetailss.getNumberOfFamilyMembers());
                kycDetailsDto.setKycStatus(kycDetailss.getKycStatus());
                return new ResponseEntity<>(kycDetailsDto, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("User doesn't exist", HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @Override
    public ResponseEntity<?> ChangeKycStatus(String userEmail, KycStatus newStatus) {
        KycDetails kycDetails = kycDetailsRepo.findByEmail(userEmail);

        User user = userRepo.findByEmail(userEmail);
        Agent agent = agentRepo.findByReferralCode(kycDetails.getReferralCode());
        if (kycDetails == null) {
            return new ResponseEntity<>("No Kyc exists for the given user", HttpStatus.NOT_FOUND);
        } else {
            if (agent == null) return new ResponseEntity<>("Invalid Referral Code", HttpStatus.NOT_FOUND);
            if (newStatus.equals(KycStatus.Pending)) {
                return new ResponseEntity<>("Invalid change in status", HttpStatus.NOT_ACCEPTABLE);
            } else if (newStatus.equals(KycStatus.Approved)) {
                kycDetails.setKycStatus(newStatus);
                kycDetailsRepo.save(kycDetails);
                sendStatusEmail(kycDetails);
                user.setAgent(agent);
                user.setKycDetails(kycDetails);
                kycDetails.setUser(user);
                user.setReferralCode(kycDetails.getReferralCode());
                userRepo.save(user);
                if (agent.getUserList().isEmpty()) agent.setUserList(new ArrayList<>());
                agent.getUserList().add(user);
                agentRepo.save(agent);
                return new ResponseEntity<>(("Status updated to " + newStatus), HttpStatus.OK);
            } else if (newStatus.equals(KycStatus.Rejected)) {
                kycDetails.setKycStatus(newStatus);
                kycDetailsRepo.save(kycDetails);
                sendStatusEmail(kycDetails);
                return new ResponseEntity<>(("Status updated to " + newStatus), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Problem Occurred", HttpStatus.BAD_REQUEST);
            }
        }
    }

    private void sendStatusEmail(KycDetails kycDetails) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(kycDetails.getEmail());
        mailMessage.setSubject("Change in your Kyc Status");
        mailMessage.setText("Hello " + kycDetails.getFirstName() + " " + kycDetails.getLastName() + ",\n\n Your Kyc Status has been changed to " + kycDetails.getKycStatus() + " Please confirm so with your respective agent.");
        javaMailSender.send(mailMessage);
    }

    @Override
    public ResponseEntity<?> setLoanDiscount(String userEmail, double discount) {
        User user = userService.getByEmail(userEmail);
        if (user == null) return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
        Accounts accounts = user.getAccounts();
        if (accounts == null) return new ResponseEntity<>("account not found", HttpStatus.NOT_FOUND);
        if (accountsService.CheckAccStatus(user.getEmail()) == Boolean.FALSE)
            return new ResponseEntity<>("account not active", HttpStatus.NOT_ACCEPTABLE);
        if (accounts.getLoanList() == null || userLoanService.isLoanNotOpen(userEmail) == Boolean.TRUE)
            return new ResponseEntity<>("no active loan", HttpStatus.NOT_ACCEPTABLE);
        List<Loan> loanList = accounts.getLoanList();
        for (Loan loan : loanList) {
            if (loan.getLoanType().equals(LoanType.Other)) {
                loan.setDiscount(discount);
                loanRepo.save(loan);
                return new ResponseEntity<>("Discount set", HttpStatus.OK);
            } else
                return new ResponseEntity<>("not eligible for discount", HttpStatus.NOT_ACCEPTABLE);
        }
        return null;
    }
}
