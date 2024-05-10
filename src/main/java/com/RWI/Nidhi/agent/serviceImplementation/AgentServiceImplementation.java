package com.RWI.Nidhi.agent.serviceImplementation;

import com.RWI.Nidhi.Security.models.Credentials;
import com.RWI.Nidhi.Security.models.ERole;
import com.RWI.Nidhi.Security.models.Role;
import com.RWI.Nidhi.Security.payload.request.SignupRequest;
import com.RWI.Nidhi.Security.payload.response.MessageResponse;
import com.RWI.Nidhi.Security.repository.CredentialsRepo;
import com.RWI.Nidhi.Security.repository.RoleRepository;
import com.RWI.Nidhi.agent.serviceInterface.AgentServiceInterface;
import com.RWI.Nidhi.dto.AddUserDto;
import com.RWI.Nidhi.entity.*;
import com.RWI.Nidhi.enums.Status;
import com.RWI.Nidhi.otpSendAndVerify.OtpServiceImplementation;
import com.RWI.Nidhi.repository.*;
import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.user.serviceImplementation.UserLoanServiceImplementation;
import com.RWI.Nidhi.user.serviceInterface.UserService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    PasswordEncoder encoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    CredentialsRepo credentialsRepo;
    @Autowired
    AdminRepo adminRepo;

    @Override
    public User addUser(SignupRequest signUpRequest, String agentEmail) throws Exception {

        //check if user already exists
//        if (userRepo.existsByEmail(addUserDto.getEmail())) {
//            throw new Exception("User already exists");
//        }

        if(agentRepo.existsByAgentEmail(signUpRequest.getEmail()) || userRepo.existsByEmail(signUpRequest.getEmail())){
            throw new Exception("Email already exists");
        }
        if(adminRepo.existsByAdminName(signUpRequest.getUsername()) || agentRepo.existsByAgentName(signUpRequest.getEmail()) || userRepo.existsByUserName(signUpRequest.getUsername())){
            throw new Exception("Username already taken");
        }

        //Getting the agent from repo by email
        Agent agent = agentRepo.findByAgentEmail(agentEmail);

        //Check if agent exists or not
        if (agent == null) {
            throw new Exception("Agent does not exists");
        }
        //creation of new user
        User newUser = new User();
        newUser.setUserName(signUpRequest.getUsername());
        newUser.setEmail(signUpRequest.getEmail());
        newUser.setPhoneNumber(signUpRequest.getPhoneNumber());
        newUser.setAgent(agent);
        agent.getUserList().add(newUser);
        try {
            String tempPassword = otpServiceImplementation.generateOTP();
            String subject = "Your temporary password";
            String messageToSend = "Your temporary system generated password is: ";
            System.out.println("Sending email");
            otpServiceImplementation.sendEmailOtp(newUser.getEmail(), subject, messageToSend, tempPassword);
            newUser.setPassword(encoder.encode(tempPassword));
            userRepo.save(newUser);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        Credentials credentials = new Credentials(signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPhoneNumber(),
                newUser.getPassword());

        // Set default role as USER for user
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);
        credentials.setRoles(roles);
        newUser.setRoles(roles);
        userRepo.save(newUser);
        credentialsRepo.save(newUser);
        agentRepo.save(agent);
        return newUser;
    }

    @Override
    public boolean deleteUserById(int id) throws Exception {
        try {
            userRepo.deleteById(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User findUserById(int id) throws Exception {
        return userRepo.findById(id).orElseThrow(() -> {
            return new Exception("User not found");
        });
    }


    @Override
    public Accounts deactivateAccount(String accountNumber) throws Exception {
        Accounts currentAcc = accountsRepo.findByAccountNumber(accountNumber).orElseThrow(() -> {
            return new Exception("Account Number Not Found");
        });
        currentAcc.setAccountStatus(Status.INACTIVE);
        return accountsRepo.save(currentAcc);
    }

    @Override
    public Accounts closeAccount(String accountNumber) throws Exception {
        Accounts currentAcc = accountsRepo.findByAccountNumber(accountNumber).orElseThrow(() -> {
            return new Exception("Account Number Not Found");
        });
        currentAcc.setAccountStatus(Status.CLOSED);
        return accountsRepo.save(currentAcc);
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

    private void sendStatusEmail(Loan loan) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(loan.getUser().getEmail());
        mailMessage.setSubject("Change in Loan Status");
        mailMessage.setText("Hello User," + loan.getUser().getUserName() + ",\n\n Your loan has been changed to" + loan.getStatus() + "Please confirm so with your respective agent.");
        javaMailSender.send(mailMessage);
    }

    @Override
    public ResponseEntity<?> ChangeLoanStatus(String userEmail, String agentEmail, LoanStatus changedStatus, LoanStatus previousStatus) {
        ResponseEntity<?> asd = new ResponseEntity<>("Invalid Agent",HttpStatus.I_AM_A_TEAPOT);
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
}
