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
import com.RWI.Nidhi.dto.UserResponseDto;
import com.RWI.Nidhi.entity.*;
import com.RWI.Nidhi.enums.SchemeStatus;
import com.RWI.Nidhi.enums.Status;
import com.RWI.Nidhi.otpSendAndVerify.OtpServiceImplementation;
import com.RWI.Nidhi.repository.*;
import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.user.serviceImplementation.UserLoanServiceImplementation;
import com.RWI.Nidhi.user.serviceInterface.UserService;
import com.amazonaws.services.xray.model.Http;
import org.apache.http.protocol.ResponseServer;
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
import java.util.ArrayList;
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
    @Autowired
    SchemeRepo schemeRepo;

    @Override
    public ResponseEntity<?> addUser(SignupRequest signUpRequest, String agentEmail){

        if(agentRepo.existsByAgentEmail(signUpRequest.getEmail()) || userRepo.existsByEmail(signUpRequest.getEmail())){
            return new ResponseEntity<>("Email already taken", HttpStatus.NOT_ACCEPTABLE);
        }
        if(adminRepo.existsByAdminName(signUpRequest.getUsername()) || agentRepo.existsByAgentName(signUpRequest.getUsername()) || userRepo.existsByUserName(signUpRequest.getUsername())){
            return new ResponseEntity<>("Username already taken", HttpStatus.NOT_ACCEPTABLE);
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
            String tempPassword = "user21";
//                    otpServiceImplementation.generateOTP();
            String subject = "Your temporary password";
            String messageToSend = "Your temporary system generated password is: ";
            System.out.println("Sending email");
            otpServiceImplementation.sendEmailOtp(newUser.getEmail(), subject, messageToSend, tempPassword);
            newUser.setPassword(encoder.encode(tempPassword));
            userRepo.save(newUser);
        } catch (Exception e) {
            return new ResponseEntity<>(  "Email Error" + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Credentials credentials = new Credentials(signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPhoneNumber(),
                newUser.getPassword());

        // Set default role as USER for user
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER).get();
        if(userRole == null){
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
        return new ResponseEntity<>(userResponseDto,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteUserById(String userEmail, String agentEmail){
        User user = userRepo.findByEmail(userEmail);
        Agent agent = user.getAgent();
        if(!agent.getAgentEmail().equals(agentEmail)){
            return new ResponseEntity<>("This user is not in current agent's list",HttpStatus.NOT_FOUND);
        }
        userRepo.deleteById(user.getUserId());
        return new ResponseEntity<>("User Deleted",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllUsers(String email) {
        Agent agent = agentRepo.findByAgentEmail(email);
        List<User> users =  agent.getUserList();
        if(users.size() == 0) return new ResponseEntity<>("No users found", HttpStatus.NOT_FOUND);
        List<UserResponseDto> userResponseDtos = new ArrayList<>();
        for(User user: users){
            UserResponseDto userResponseDto = new UserResponseDto();
            userResponseDto.setUserName(user.getUserName());
            userResponseDto.setEmail(user.getEmail());
            userResponseDto.setPhoneNumber(user.getPhoneNumber());
            userResponseDtos.add(userResponseDto);
        }
        return new ResponseEntity<>(userResponseDtos,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> findUserById(int id,String agentEmail){
        Agent agent = agentRepo.findByAgentEmail(agentEmail);
        User user = userRepo.findById(id).get();
        if(!user.getAgent().getAgentEmail().equals(agentEmail)){
            return new ResponseEntity<>("This user is not associated with this agent", HttpStatus.NOT_FOUND);
        }
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUserName(user.getUserName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setPhoneNumber(user.getPhoneNumber());
        return new ResponseEntity<>(userResponseDto,HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> deactivateAccount(String accountNumber, String agentEmail) {
        Accounts currentAcc = accountsRepo.findByAccountNumber(accountNumber).get();
        if(currentAcc == null) return new ResponseEntity<>("Account number doesn't exists", HttpStatus.NOT_FOUND);

        Agent agent = agentRepo.findByAgentEmail(agentEmail);
        User user = currentAcc.getUser();
        if(agent == null) return new ResponseEntity<>("Agent doesn't exists",HttpStatus.NOT_FOUND);
        if(!user.getAgent().getAgentEmail().equals(agentEmail)) return new ResponseEntity<>("This agent is not associated with this account's owner", HttpStatus.NOT_FOUND);


        currentAcc.setAccountStatus(Status.INACTIVE);
        accountsRepo.save(currentAcc);
        return new ResponseEntity<>("Account decativated!!" , HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> closeAccount(String accountNumber, String agentEmail){
        Accounts currentAcc = accountsRepo.findByAccountNumber(accountNumber).get();
        if(currentAcc == null) return new ResponseEntity<>("Account number doesn't exists", HttpStatus.NOT_FOUND);

        Agent agent = agentRepo.findByAgentEmail(agentEmail);
        User user = currentAcc.getUser();
        if(agent == null) return new ResponseEntity<>("Agent doesn't exists",HttpStatus.NOT_FOUND);
        if(!user.getAgent().getAgentEmail().equals(agentEmail)) return new ResponseEntity<>("This agent is not associated with this account's owner", HttpStatus.NOT_FOUND);

        currentAcc.setAccountStatus(Status.CLOSED);
        accountsRepo.save(currentAcc);
        return new ResponseEntity<>("Account closed!!" , HttpStatus.OK);
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

    @Override
    public ResponseEntity<?> ChangeLoanStatus(String userEmail, String agentEmail, LoanStatus changedStatus, LoanStatus previousStatus) {
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

    private void sendStatusEmail(Loan loan) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(loan.getUser().getEmail());
        mailMessage.setSubject("Change in Loan Status");
        mailMessage.setText("Hello User," + loan.getUser().getUserName() + ",\n\n Your loan status has been changed to" + loan.getStatus() + "Please confirm so with your respective agent.");
        javaMailSender.send(mailMessage);
    }
    public ResponseEntity<?> ChangeSchemeStatus(String userEmail, String agentEmail, SchemeStatus changedStatus, SchemeStatus previousStatus){
        if (agentRepo.existsByAgentEmail(agentEmail)) {
            User user = userService.getByEmail(userEmail);
            Accounts accounts = user.getAccounts();
            Scheme scheme = accounts.getScheme();
            if(scheme==null){
                return new ResponseEntity<>("No Scheme exists for the given user",HttpStatus.I_AM_A_TEAPOT);
            }
            else {
                if(scheme.getSStatus()==previousStatus){
                    if(previousStatus == SchemeStatus.APPLIED && changedStatus == SchemeStatus.APPROVED){
                        scheme.setSStatus(changedStatus);
                        scheme.setStartDate(LocalDate.now());
                        scheme.setAgent(agentRepo.findByAgentEmail(agentEmail));
                        schemeRepo.save(scheme);
                        sendStatusEmail(scheme);
                    } else if (previousStatus == SchemeStatus.APPLIED && changedStatus == SchemeStatus.PENDING){
                        scheme.setSStatus(changedStatus);
                        schemeRepo.save(scheme);
                        sendStatusEmail(scheme);
                    } else if (previousStatus == SchemeStatus.APPLIED && changedStatus == SchemeStatus.REJECTED){
                        scheme.setSStatus(changedStatus);
                        schemeRepo.save(scheme);
                        sendStatusEmail(scheme);
                    } else if (previousStatus == SchemeStatus.APPROVED && changedStatus == SchemeStatus.SANCTIONED){
                        scheme.setSStatus(changedStatus);
                        schemeRepo.save(scheme);
                        sendStatusEmail(scheme);
                    } else if (previousStatus == SchemeStatus.APPROVED && changedStatus == SchemeStatus.PENDING){
                        scheme.setSStatus(changedStatus);
                        schemeRepo.save(scheme);
                        sendStatusEmail(scheme);
                    } else if (previousStatus == SchemeStatus.SANCTIONED && changedStatus == SchemeStatus.CLOSED){
                        scheme.setSStatus(changedStatus);
                        schemeRepo.save(scheme);
                        sendStatusEmail(scheme);
                    } else if (previousStatus == SchemeStatus.SANCTIONED && changedStatus == SchemeStatus.PENDING){
                        scheme.setSStatus(changedStatus);
                        schemeRepo.save(scheme);
                        sendStatusEmail(scheme);
                    } else if (previousStatus == SchemeStatus.APPLIEDFORLOAN && changedStatus == SchemeStatus.APPROVEDLOAN){
                        scheme.setSStatus(changedStatus);
                        //approveSchemeLoan method
                        schemeRepo.save(scheme);
                        sendStatusEmail(scheme);
                    }else {
                        return new ResponseEntity<>("Invalid Change in status",HttpStatus.I_AM_A_TEAPOT);
                    }
                }else
                    return new ResponseEntity<>("Applied Change in status doesn't match recorded status",HttpStatus.I_AM_A_TEAPOT);
            }
        }
        else
            return new ResponseEntity<>("Invalid Agent",HttpStatus.I_AM_A_TEAPOT);
        return null;
    }
    private void sendStatusEmail(Scheme scheme) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(scheme.getAccount().getUser().getEmail());
        mailMessage.setSubject("Change in Scheme Status");
        mailMessage.setText("Hello User," + scheme.getAccount().getUser().getUserName() + ",\n\n Your Scheme Status has been changed to" + scheme.getSStatus() + "Please confirm so with your respective agent.");
        javaMailSender.send(mailMessage);
    }

}
