package com.RWI.Nidhi.agent.serviceImplementation;

import com.RWI.Nidhi.agent.serviceInterface.AgentServiceInterface;
import com.RWI.Nidhi.dto.AddUserDto;
import com.RWI.Nidhi.entity.*;
import com.RWI.Nidhi.enums.Status;
import com.RWI.Nidhi.otpSendAndVerify.OtpServiceImplementation;
import com.RWI.Nidhi.repository.AccountsRepo;
import com.RWI.Nidhi.dto.LoanInfoDto;
import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.otpSendAndVerify.OtpServiceImplementation;
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
    @Autowired
    UserService userService;
    @Autowired
    UserLoanServiceImplementation userLoanService;
    @Autowired
    LoanRepo loanRepo;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private AgentRepo agentRepo;

    @Override
    public User addUser(AddUserDto addUserDto, String agentEmail) throws Exception {

        //check if user already exists
        if (userRepo.existsByEmail(addUserDto.getEmail())) {
            throw new Exception("User already exists");
        }

        //Getting the agent from repo by email
        Agent agent = agentRepo.findByAgentEmail(agentEmail);

        //Check if agent exists or not
        if(agent == null){
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

            @Override
            public LoanInfoDto LoanApproved (String email){//must check for loan existence in controller
                User user = userService.getByEmail(email);
                Accounts accounts = user.getAccounts();
                List<Loan> loanList = accounts.getLoanList();
                for (Loan loan : loanList) {
                    if (loan.getStatus() == LoanStatus.APPLIED) {
                        loan.setStatus(LoanStatus.APPROVED);
                        loanRepo.save(loan);
                        sendStatusEmail(loan);
                    } else
                        return null;
                }
                return userLoanService.getLoanInfo(email);
            }

            private void sendApprovalEmail (Loan loan){
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(loan.getUser().getEmail());
                mailMessage.setSubject("Loan Approval - " + loan.getAccount().getAccountNumber());
                mailMessage.setText("Dear " + loan.getUser().getUserName() + ",\n\nYour loan has been approved. Please find the details below:\n\nLoan Number: " + loan.getLoanId() + "\nLoan Amount: " + loan.getPrincipalLoanAmount() + "\n\nBest regards,\n[Your Bank Name]");

                javaMailSender.send(mailMessage);
            }
            private void sendStatusEmail (Loan loan){
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(loan.getUser().getEmail());
                mailMessage.setSubject("Change in Loan Status");
                mailMessage.setText("Hello User," + loan.getUser().getUserName() + ",\n\n Your loan has been" + loan.getStatus() + "Please confirm so with your respective agent.");
            }

            @Override
            public LoanInfoDto LoanOnSanction (String email){
                User user = userService.getByEmail(email);
                Accounts accounts = user.getAccounts();
                List<Loan> loanList = accounts.getLoanList();
                for (Loan loan : loanList) {
                    if (loan.getStatus() == LoanStatus.APPROVED) {
                        loan.setStatus(LoanStatus.SANCTIONED);
                        loanRepo.save(loan);

                    } else
                        return null;
                }
                return userLoanService.getLoanInfo(email);
            }

            @Override
            public LoanInfoDto LoanOnPending (String email){
                User user = userService.getByEmail(email);
                Accounts accounts = user.getAccounts();
                List<Loan> loanList = accounts.getLoanList();
                for (Loan loan : loanList) {
                    if (loan.getStatus() == LoanStatus.APPLIED) {
                        loan.setStatus(LoanStatus.PENDING);
                        loanRepo.save(loan);
                    } else
                        return null;
                }
                return userLoanService.getLoanInfo(email);
            }

            @Override
            public LoanInfoDto LoanRejected (String email){
                User user = userService.getByEmail(email);
                Accounts accounts = user.getAccounts();
                List<Loan> loanList = accounts.getLoanList();
                for (Loan loan : loanList) {
                    if (loan.getStatus() == LoanStatus.APPLIED) {
                        loan.setStatus(LoanStatus.REJECTED);
                        loanRepo.save(loan);
                    } else
                        return null;
                }
                return userLoanService.getLoanInfo(email);
            }

            @Override
            public LoanInfoDto LoanForeclosed (String email){
                User user = userService.getByEmail(email);
                Accounts accounts = user.getAccounts();
                List<Loan> loanList = accounts.getLoanList();
                for (Loan loan : loanList) {
                    if (loan.getStatus() == LoanStatus.REQUESTEDFORFORECLOSURE) {
                        loan.setStatus(LoanStatus.FORECLOSED);
                        loanRepo.save(loan);
                    } else
                        return null;
                }
                return userLoanService.getLoanInfo(email);
            }
            @Override
            public LoanInfoDto LoanClosed (String email){
                User user = userService.getByEmail(email);
                Accounts accounts = user.getAccounts();
                List<Loan> loanList = accounts.getLoanList();
                for (Loan loan : loanList) {
                    if (loan.getStatus() == LoanStatus.SANCTIONED) {
                        loan.setStatus(LoanStatus.CLOSED);
                        loanRepo.save(loan);
                    } else
                        return null;
                }
                return userLoanService.getLoanInfo(email);
            }
        }
