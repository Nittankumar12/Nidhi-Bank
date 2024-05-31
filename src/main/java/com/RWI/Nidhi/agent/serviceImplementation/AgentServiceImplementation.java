package com.RWI.Nidhi.agent.serviceImplementation;

import com.RWI.Nidhi.Security.models.Credentials;
import com.RWI.Nidhi.Security.models.ERole;
import com.RWI.Nidhi.Security.models.Role;
import com.RWI.Nidhi.Security.payload.request.SignupRequest;
import com.RWI.Nidhi.Security.repository.CredentialsRepo;
import com.RWI.Nidhi.Security.repository.RoleRepository;
import com.RWI.Nidhi.agent.serviceInterface.AgentServiceInterface;
import com.RWI.Nidhi.dto.AddAgentDto;
import com.RWI.Nidhi.dto.AgentForgetPassword;
import com.RWI.Nidhi.dto.CommissionDto;
import com.RWI.Nidhi.dto.UserResponseDto;
import com.RWI.Nidhi.entity.Agent;
import com.RWI.Nidhi.entity.Commission;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.CommissionType;
import com.RWI.Nidhi.otpSendAndVerify.OtpServiceImplementation;
import com.RWI.Nidhi.repository.*;
import com.RWI.Nidhi.user.serviceImplementation.UserLoanServiceImplementation;
import com.RWI.Nidhi.user.serviceInterface.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

@Service
public class AgentServiceImplementation implements AgentServiceInterface {

    @Autowired
    UserRepo userRepo;
    @Autowired
    OtpServiceImplementation otpServiceImplementation;
    @Autowired
    AccountsRepo accountsRepo;
    @Autowired
    UserService userService;
    @Autowired
    UserLoanServiceImplementation userLoanService;
    @Autowired
    LoanRepo loanRepo;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    AgentRepo agentRepo;
    @Autowired
    TransactionRepo transactionRepo;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    CredentialsRepo credentialsRepo;
    @Autowired
    AdminRepo adminRepo;
    @Autowired
    CredentialsRepo credRepo;

//
//    @Override
//    public ResponseEntity<?> deleteUserById(String userEmail, String agentEmail) {
//        User user = userRepo.findByEmail(userEmail);
//        Agent agent = user.getAgent();
//        if (!agent.getAgentEmail().equals(agentEmail)) {
//            return new ResponseEntity<>("This user is not in current agent's list", HttpStatus.NOT_FOUND);
//        }
//        userRepo.deleteById(user.getUserId());
//        return new ResponseEntity<>("User Deleted", HttpStatus.OK);
//    }

    @Override
    public ResponseEntity<?> getAllUsers(String email) {
        Agent agent = agentRepo.findByAgentEmail(email);
        List<User> users = agent.getUserList();
        if (users.size() == 0) return new ResponseEntity<>("No users found", HttpStatus.NOT_FOUND);
        List<UserResponseDto> userResponseDtoList = new ArrayList<>();
        for (User user : users) {
            UserResponseDto userResponseDto = new UserResponseDto();
            userResponseDto.setUserName(user.getUserName());
            userResponseDto.setEmail(user.getEmail());
            userResponseDto.setPhoneNumber(user.getPhoneNumber());
            userResponseDtoList.add(userResponseDto);
        }
        return new ResponseEntity<>(userResponseDtoList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> addUser(@NotNull SignupRequest signUpRequest) {

        if (agentRepo.existsByAgentEmail(signUpRequest.getEmail()) || userRepo.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>("Email already taken", HttpStatus.NOT_ACCEPTABLE);
        }
        if (adminRepo.existsByAdminName(signUpRequest.getUsername()) || agentRepo.existsByAgentName(signUpRequest.getUsername()) || userRepo.existsByUserName(signUpRequest.getUsername())) {
            return new ResponseEntity<>("Username already taken", HttpStatus.NOT_ACCEPTABLE);
        }
        if (adminRepo.existsByPhoneNumber(signUpRequest.getPhoneNumber()) || agentRepo.existsByAgentPhoneNum(signUpRequest.getPhoneNumber()) || userRepo.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
            return new ResponseEntity<>("Phone number already taken", HttpStatus.NOT_ACCEPTABLE);
        }

        //creation of new user
        User newUser = new User();
        newUser.setUserName(signUpRequest.getUsername());
        newUser.setEmail(signUpRequest.getEmail());
        newUser.setPhoneNumber(signUpRequest.getPhoneNumber());
        try {
//            String tempPassword = "user21";
//                    otpServiceImplementation.generateOTP();
//            String subject = newUser.getUserName();
//            String messageToSend = "Welcome to Nidhi Bank,Your temporary system generated password is: ";

            String tempPassword = otpServiceImplementation.generateOTP();
            String subject = "Your temporary password";
            String messageToSend = "Your temporary system generated password is: ";
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
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUserName(newUser.getUserName());
        userResponseDto.setEmail(newUser.getEmail());
        userResponseDto.setPhoneNumber(newUser.getPhoneNumber());
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> findUserById(int id, String agentEmail) {
        Agent agent = agentRepo.findByAgentEmail(agentEmail);
        User user = userRepo.findById(id).get();
        if (!user.getAgent().getAgentEmail().equals(agentEmail)) {
            return new ResponseEntity<>("This user is not associated with this agent", HttpStatus.NOT_FOUND);
        }
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUserName(user.getUserName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setPhoneNumber(user.getPhoneNumber());
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }


//    @Override
//    public ResponseEntity<?> deactivateAccount(String accountNumber, String agentEmail) {
//        Accounts currentAcc = accountsRepo.findByAccountNumber(accountNumber).get();
//        if (currentAcc == null) return new ResponseEntity<>("Account number doesn't exists", HttpStatus.NOT_FOUND);
//
//        Agent agent = agentRepo.findByAgentEmail(agentEmail);
//        User user = currentAcc.getUser();
//        if (agent == null) return new ResponseEntity<>("Agent doesn't exists", HttpStatus.NOT_FOUND);
//        if (!user.getAgent().getAgentEmail().equals(agentEmail))
//            return new ResponseEntity<>("This agent is not associated with this account's owner", HttpStatus.NOT_FOUND);
//
//
//        currentAcc.setAccountStatus(Status.INACTIVE);
//        accountsRepo.save(currentAcc);
//        return new ResponseEntity<>("Account deactivated!!", HttpStatus.OK);
//    }
//
//    @Override
//    public ResponseEntity<?> closeAccount(String accountNumber, String agentEmail) {
//        Accounts currentAcc = accountsRepo.findByAccountNumber(accountNumber).get();
//        if (currentAcc == null) return new ResponseEntity<>("Account number doesn't exists", HttpStatus.NOT_FOUND);
//
//        Agent agent = agentRepo.findByAgentEmail(agentEmail);
//        User user = currentAcc.getUser();
//        if (agent == null) return new ResponseEntity<>("Agent doesn't exists", HttpStatus.NOT_FOUND);
//        if (!user.getAgent().getAgentEmail().equals(agentEmail))
//            return new ResponseEntity<>("This agent is not associated with this account's owner", HttpStatus.NOT_FOUND);
//
//        currentAcc.setAccountStatus(Status.CLOSED);
//        accountsRepo.save(currentAcc);
//        return new ResponseEntity<>("Account closed!!", HttpStatus.OK);
//    }

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

    @Override
    public ResponseEntity<String> agentForgetPasswordSendVerificationCode(String agentEmail) throws Exception {
        //check if agent already exists
        if (!agentRepo.existsByAgentEmail(agentEmail)) {
            throw new Exception("This email is not registered with us");
        }
        //
        try {
            String otp = otpServiceImplementation.generateOTP();
            String subject = "Forgot password attempted";
            String messageToSend = "Your verification OTP is: ";
            otpServiceImplementation.sendEmailOtp(agentEmail, subject, messageToSend, otp);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return new ResponseEntity("OTP send", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> agentForgetPasswordVerifyVerificationCode(String agentEmail, String enteredOtp) throws Exception {
        try {
            otpServiceImplementation.verifyEmailOtp(agentEmail, enteredOtp);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return new ResponseEntity("Email Verify Successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateAgentPassword(AgentForgetPassword agentforgetpassword) throws Exception {
        Agent currAgent = agentRepo.findByAgentEmail(agentforgetpassword.getEmail());

        AddAgentDto agentDto = new AddAgentDto();

        currAgent.setAgentPassword(encoder.encode(agentforgetpassword.getPassword()));
        try {
            Optional<Credentials> currAgent1 = credRepo.findByEmail(agentforgetpassword.getEmail());
            if (currAgent1.isPresent()) {
                currAgent1.get().setPassword(encoder.encode(agentforgetpassword.getPassword()));

                credRepo.save(currAgent1.get());
                agentRepo.save(currAgent);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            agentDto.setAgentId(currAgent.getAgentId());
            agentDto.setAgentName(currAgent.getAgentName());
            agentDto.setAgentEmail(currAgent.getAgentEmail());
            agentDto.setAgentPhoneNum(currAgent.getAgentPhoneNum());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return new ResponseEntity<>(agentDto, HttpStatus.OK);

    }

    @Override
    public List<CommissionDto> getCommissionListByType(String agentEmail) {
        Agent agent = agentRepo.findByAgentEmail(agentEmail);
        List<Commission> commissionList = agent.getCommissionList();
        if (commissionList.size() == 0) return null;
        List<CommissionDto> commissionDtoList = new ArrayList<>();
        for (Commission commission : commissionList) {
            CommissionDto commissionDto = new CommissionDto();
            commissionDto.setCommissionRate(commission.getCommissionRate());
            commissionDto.setCommissionAmount(commission.getCommissionAmount());
            commissionDto.setCommissionType(commission.getCommissionType());
            commissionDto.setCommDate(commission.getCommDate());
            commissionDto.setUserName(commission.getUser().getUserName());
            commissionDtoList.add(commissionDto);
        }
        return commissionDtoList;
    }

    @Override
    public List<CommissionDto> getCommissionListByType(String agentEmail, CommissionType commissionType) {
        List<CommissionDto> commissionDtoList = getCommissionListByType(agentEmail);
        if (commissionDtoList == null) return null;
        List<CommissionDto> commissionDtoListByType = new ArrayList<>();

        for (CommissionDto commissionDto : commissionDtoList) {
            if (commissionDto.getCommissionType() == commissionType)
                commissionDtoListByType.add(commissionDto);
        }
        return commissionDtoListByType;
    }

//Shifted methods from Agent to Admin


//    @Override
//    public ResponseEntity<?> ChangeLoanStatus(String userEmail, String agentEmail, LoanStatus changedStatus, LoanStatus previousStatus) {
//        if (agentRepo.existsByAgentEmail(agentEmail)) {
//            User user = userService.getByEmail(userEmail);
//            Accounts accounts = user.getAccounts();
//            List<Loan> loanList = accounts.getLoanList();
//            if(loanList.isEmpty()){
//                return new ResponseEntity<>("No Loan exists for the given user",HttpStatus.I_AM_A_TEAPOT);
//            }
//            else {
//                for (Loan loan : loanList) {
//                    if(loan.getStatus()==previousStatus){
//                        if(previousStatus == LoanStatus.APPLIED && changedStatus == LoanStatus.APPROVED){
//                            loan.setStatus(changedStatus);
//                            loan.setStartDate(LocalDate.now());
//                            loan.setEmiDate(userLoanService.calcFirstEMIDate(loan.getStartDate()));
//                            LoanCalcDto loanCalcDto = new LoanCalcDto();
//                            loanCalcDto.setLoanType(loan.getLoanType());
//                            loanCalcDto.setRePaymentTerm(loan.getRePaymentTerm());
//                            loanCalcDto.setPrincipalLoanAmount(loan.getPrincipalLoanAmount());
//                            loanCalcDto.setInterestRate(loan.getLoanType());
//                            loan.setMonthlyEMI(userLoanService.calculateEMI(loanCalcDto));
//                            loan.setPayableLoanAmount(userLoanService.calculateFirstPayableAmount(loanCalcDto));
//                            loanRepo.save(loan);
//                            sendStatusEmail(loan);
//                        } else if (previousStatus == LoanStatus.APPLIED && changedStatus == LoanStatus.PENDING){
//                            loan.setStatus(changedStatus);
//                            loanRepo.save(loan);
//                            sendStatusEmail(loan);
//                        } else if (previousStatus == LoanStatus.APPLIED && changedStatus == LoanStatus.REJECTED){
//                            loan.setStatus(changedStatus);
//                            loanRepo.save(loan);
//                            sendStatusEmail(loan);
//                        } else if (previousStatus == LoanStatus.APPROVED && changedStatus == LoanStatus.SANCTIONED){
//                            loan.setStatus(changedStatus);
//
//                            Transactions transactions = new Transactions();
//                            transactions.setAccount(accounts);
//                            transactions.setLoan(loan);
//                            transactions.setTransactionAmount(loan.getPrincipalLoanAmount());
//                            Transactions.deductTotalBalance(loan.getPrincipalLoanAmount());
//                            transactions.setTransactionDate(new Date());
//                            transactions.setTransactionType(TransactionType.DEBITED);
//                            transactions.setTransactionStatus(TransactionStatus.COMPLETED);
//                            transactionRepo.save(transactions);
//                            loan.getTransactionsList().add(transactions);
//
//                            loanRepo.save(loan);
//                            sendStatusEmail(loan);
//                        } else if (previousStatus == LoanStatus.APPROVED && changedStatus == LoanStatus.PENDING){
//                            loan.setStatus(changedStatus);
//                            loanRepo.save(loan);
//                            sendStatusEmail(loan);
//                        } else if (previousStatus == LoanStatus.SANCTIONED && changedStatus == LoanStatus.CLOSED){
//                            loan.setStatus(changedStatus);
//                            loanRepo.save(loan);
//                            sendStatusEmail(loan);
//                        } else if (previousStatus == LoanStatus.SANCTIONED && changedStatus == LoanStatus.PENDING){
//                            loan.setStatus(changedStatus);
//                            loanRepo.save(loan);
//                            sendStatusEmail(loan);
//                        } else if (previousStatus == LoanStatus.REQUESTEDFORFORECLOSURE && changedStatus == LoanStatus.FORECLOSED){
//                            loan.setStatus(changedStatus);
//                            loanRepo.save(loan);
//                            sendStatusEmail(loan);
//                        }else {
//                            return new ResponseEntity<>("Invalid Change in status",HttpStatus.I_AM_A_TEAPOT);
//                        }
//                    }else
//                        return new ResponseEntity<>("Applied Change in status doesn't match recorded status",HttpStatus.I_AM_A_TEAPOT);
//                }
//                return new ResponseEntity<>("Invalid Agent",HttpStatus.I_AM_A_TEAPOT);
//            }
//        } else
//            return new ResponseEntity<>("Invalid Agent",HttpStatus.I_AM_A_TEAPOT);
//    }
//
//    private void sendStatusEmail(Loan loan) {
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo(loan.getUser().getEmail());
//        mailMessage.setSubject("Change in Loan Status");
//        mailMessage.setText("Hello User," + loan.getUser().getUserName() + ",\n\n Your loan status has been changed to" + loan.getStatus() + "Please confirm so with your respective agent.");
//        javaMailSender.send(mailMessage);
//    }
//    public ResponseEntity<?> ChangeSchemeStatus(String userEmail, String agentEmail, SchemeStatus changedStatus, SchemeStatus previousStatus){
//        if (agentRepo.existsByAgentEmail(agentEmail)) {
//            if (userRepo.existsByEmail(userEmail)) {
//                User user = userService.getByEmail(userEmail);
//                Accounts accounts = user.getAccounts();
//                if (accounts != null) {
//                    Scheme scheme = accounts.getScheme();
//                    if (scheme == null) {
//                        return new ResponseEntity<>("No Scheme exists for the given user", HttpStatus.I_AM_A_TEAPOT);
//                    } else {
//                        if (scheme.getSStatus() == previousStatus) {
//                            if (previousStatus == SchemeStatus.APPLIED && changedStatus == SchemeStatus.APPROVED) {
//                                scheme.setSStatus(changedStatus);
//                                scheme.setStartDate(LocalDate.now());
//                                scheme.setInterestRate(10);
//                                scheme.setNextEMIDate(firstDateOfNextMonth(LocalDate.now()));
//                                scheme.setAgent(agentRepo.findByAgentEmail(agentEmail));
//                                schemeRepo.save(scheme);
//                                sendStatusEmail(scheme);
//                            } else if (previousStatus == SchemeStatus.APPLIED && changedStatus == SchemeStatus.PENDING) {
//                                scheme.setSStatus(changedStatus);
//                                schemeRepo.save(scheme);
//                                sendStatusEmail(scheme);
//                            } else if (previousStatus == SchemeStatus.APPLIED && changedStatus == SchemeStatus.REJECTED) {
//                                scheme.setSStatus(changedStatus);
//                                schemeRepo.save(scheme);
//                                sendStatusEmail(scheme);
//                            } else if (previousStatus == SchemeStatus.APPROVED && changedStatus == SchemeStatus.SANCTIONED) {
//                                scheme.setSStatus(changedStatus);
//
//                                Transactions transactions = new Transactions();
//                                transactions.setAccount(accounts);
//                                transactions.setScheme(scheme);
//                                transactions.setTransactionAmount(scheme.getMonthlyDepositAmount()*scheme.getTenure());
//                                Transactions.deductTotalBalance(scheme.getMonthlyDepositAmount()*scheme.getTenure());
//                                transactions.setTransactionDate(new Date());
//                                transactions.setTransactionType(TransactionType.DEBITED);
//                                transactions.setTransactionStatus(TransactionStatus.COMPLETED);
//                                transactionRepo.save(transactions);
//                                scheme.getTransactionsList().add(transactions);
//
//                                schemeRepo.save(scheme);
//                                sendStatusEmail(scheme);
//                            } else if (previousStatus == SchemeStatus.APPROVED && changedStatus == SchemeStatus.PENDING) {
//                                scheme.setSStatus(changedStatus);
//                                schemeRepo.save(scheme);
//                                sendStatusEmail(scheme);
//                            } else if (previousStatus == SchemeStatus.SANCTIONED && changedStatus == SchemeStatus.CLOSED) {
//                                scheme.setSStatus(changedStatus);
//                                schemeRepo.save(scheme);
//                                sendStatusEmail(scheme);
//                            } else if (previousStatus == SchemeStatus.SANCTIONED && changedStatus == SchemeStatus.PENDING) {
//                                scheme.setSStatus(changedStatus);
//                                schemeRepo.save(scheme);
//                                sendStatusEmail(scheme);
//                            } else if (previousStatus == SchemeStatus.APPLIEDFORLOAN && changedStatus == SchemeStatus.APPROVEDLOAN) {
//                                scheme.setSStatus(changedStatus);
//                                schemeRepo.save(scheme);
//                                sendStatusEmail(scheme);
//                            } else {
//                                return new ResponseEntity<>("Invalid Change in status", HttpStatus.I_AM_A_TEAPOT);
//                            }
//                        } else {
//                            return new ResponseEntity<>("Applied Change in status doesn't match recorded status", HttpStatus.I_AM_A_TEAPOT);
//                        }
//                    }
//                } else {
//                    return new ResponseEntity<>("Account doesn't exist", HttpStatus.I_AM_A_TEAPOT);
//                }
//            }else {
//                return  new ResponseEntity<>("User doesn't exist", HttpStatus.I_AM_A_TEAPOT);
//            }
//        }
//        else {
//            return new ResponseEntity<>("Invalid Agent", HttpStatus.I_AM_A_TEAPOT);
//        }
//        return null;
//    }
//    private void sendStatusEmail(Scheme scheme) {
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo(scheme.getAccount().getUser().getEmail());
//        mailMessage.setSubject("Change in Scheme Status");
//        mailMessage.setText("Hello User," + scheme.getAccount().getUser().getUserName() + ",\n\n Your Scheme Status has been changed to" + scheme.getSStatus() + "Please confirm so with your respective agent.");
//        javaMailSender.send(mailMessage);
//    }
//    private LocalDate firstDateOfNextMonth(LocalDate date) {
//        LocalDate nextMonth = date.plusMonths(1);
//        return nextMonth.withDayOfMonth(1);
//    }
//    @Override
//    public String deleteScheme(String email) {// scheme delete for when scheme has ended
//        if (userRepo.existsByEmail(email)) {
//            User user = userService.getByEmail(email);
//            Accounts accounts = user.getAccounts();
//            Scheme scheme = accounts.getScheme();
//            if (scheme.getSStatus() == SchemeStatus.CLOSED) {
//                accounts.setScheme(null);
//                accountsRepo.save(accounts);
//                schemeRepo.delete(scheme);
//                return "Record Removed";
//            }
//            return "Scheme status not closed";
//        }
//        return "User not found";
//    }

}