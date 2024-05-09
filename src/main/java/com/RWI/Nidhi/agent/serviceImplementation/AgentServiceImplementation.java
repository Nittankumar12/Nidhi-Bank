package com.RWI.Nidhi.agent.serviceImplementation;

import com.RWI.Nidhi.agent.serviceInterface.AgentServiceInterface;
import com.RWI.Nidhi.dto.AddUserDto;
import com.RWI.Nidhi.entity.*;
import com.RWI.Nidhi.enums.Status;
import com.RWI.Nidhi.otpSendAndVerify.OtpServiceImplementation;
import com.RWI.Nidhi.repository.AccountsRepo;
import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.repository.AgentRepo;
import com.RWI.Nidhi.repository.LoanRepo;
import com.RWI.Nidhi.repository.UserRepo;
import com.RWI.Nidhi.user.serviceImplementation.UserLoanServiceImplementation;
import com.RWI.Nidhi.user.serviceInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

@Service
public class AgentServiceImplementation implements AgentServiceInterface {

    @Autowired
    UserRepo userRepo;
    @Autowired
    OtpServiceImplementation otpServiceImplementation;
    @Autowired
    AccountsRepo accountsRepo;
    UserService userService;
    @Autowired
    UserLoanServiceImplementation userLoanService;
    @Autowired
    LoanRepo loanRepo;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    AgentRepo agentRepo;

    @Override
    public User addUser(AddUserDto addUserDto, String agentEmail) throws Exception {

        //check if user already exists
        if (userRepo.existsByEmail(addUserDto.getEmail())) {
            throw new Exception("User already exists");
        }

        //Getting the agent from repo by email
        Agent agent = agentRepo.findByAgentEmail(agentEmail);

        //Check if agent exists or not
        if (agent == null) {
            throw new Exception("Agent does not exists");
        }
        //creation of new user
        User newUser = new User();
        newUser.setUserName(addUserDto.getUserName());
        newUser.setEmail(addUserDto.getEmail());
        newUser.setPhoneNumber(addUserDto.getPhoneNumber());
        newUser.setAgent(agent);
        agent.getUserList().add(newUser);
        agentRepo.save(agent);

        try {
            String tempPassword = otpServiceImplementation.generateOTP();
            String subject = "Your temporary password";
            String messageToSend = "Your temporary system generated password is: ";

            otpServiceImplementation.sendEmailOtp(newUser.getEmail(), subject, messageToSend, tempPassword);
            newUser.setPassword(getEncryptedPassword(tempPassword));
            userRepo.save(newUser);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return newUser;
    }

    @Override
    public User updateUserName(int id, String userName) throws Exception {
        User currUser = userRepo.findById(id).orElseThrow(() -> {
            return new Exception("User not found");
        });

        currUser.setUserName(userName);
        try {
            userRepo.save(currUser);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return currUser;
    }

    @Override
    public User updateUserEmail(int id, String userEmail) throws Exception {
        User currUser = userRepo.findById(id).orElseThrow(() -> {
            return new Exception("User not found");
        });

        currUser.setEmail(userEmail);
        try {
            userRepo.save(currUser);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return currUser;
    }

    @Override
    public User updateUserPhoneNum(int id, String phoneNum) throws Exception {
        User currUser = userRepo.findById(id).orElseThrow(() -> {
            return new Exception("User not found");
        });

        currUser.setPhoneNumber(phoneNum);
        try {
            userRepo.save(currUser);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return currUser;
    }

    @Override
    public User updateUserPassword(String email, String password) throws Exception {
        User currUser = userRepo.findByEmail(email);

        currUser.setPassword(getEncryptedPassword(password));
        try {
            userRepo.save(currUser);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return currUser;
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
    public ResponseEntity<String> forgetPasswordSendVerificationCode(String email) throws Exception {
        //check if user already exists
        if (!userRepo.existsByEmail(email)) {
            throw new Exception("This email is not registered with us");
        }
        //
        try {
            String otp = otpServiceImplementation.generateOTP();
            String subject = "Forgot password attempted";
            String messageToSend = "Your verification OTP is: ";
            otpServiceImplementation.sendEmailOtp(email, subject, messageToSend, otp);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return new ResponseEntity("OTP send", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> forgetPasswordVerifyVerificationCode(String email, String enteredOtp) throws Exception {
        try {
            otpServiceImplementation.verifyEmailOtp(email, enteredOtp);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return new ResponseEntity("Email Verify Successfully", HttpStatus.OK);
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
