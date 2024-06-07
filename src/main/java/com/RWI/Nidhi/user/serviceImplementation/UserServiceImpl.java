package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.Security.models.Credentials;
import com.RWI.Nidhi.Security.repository.CredentialsRepo;
import com.RWI.Nidhi.dto.AddUserDto;
import com.RWI.Nidhi.dto.UpdateUserDTO;
import com.RWI.Nidhi.dto.UserResponseDto;
import com.RWI.Nidhi.dto.UserTransactionsHistoryDto;
import com.RWI.Nidhi.entity.*;
import com.RWI.Nidhi.otpSendAndVerify.OtpServiceImplementation;
import com.RWI.Nidhi.repository.*;
import com.RWI.Nidhi.user.serviceInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    AddressRepo addressRepo;
    @Autowired
    KycDetailsRepo kycDetailsRepo;
    @Autowired
    OtpServiceImplementation otpServiceImplementation;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    AgentRepo agentRepo;
    @Autowired
    private CredentialsRepo credRepo;
    @Autowired
    TransactionRepo transactionRepo;

    @Override
    public User getByEmail(String email) {
        return userRepo.findByEmail(email);
    }

//    @Override
//    public boolean authenticate(String email, String password) {
//
//        User user = userRepo.findByEmail(email);
//        if (user != null && user.getAccounts().getAccountStatus().equals("ACTIVE")) {
//            return (user.getPassword().equals(getEncryptedPassword(password)))
//                    && email.equals(user.getEmail());
//        } else {
//            return false;
//        }
//    }

//    private String getEncryptedPassword(String password) {
//        // String encryptedPassword = "";
//        try {
//            BigInteger number = new BigInteger(1, getSHA(password));
//            return number.toString(16);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    private byte[] getSHA(String input) {
//        try {
//            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
//            return messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    @Override
    public UserResponseDto updateUserName(String email, String userName) {

        User user = userRepo.findByEmail(email);

        if(user == null || user.getUserName().equals(userName) || agentRepo.existsByAgentName(userName) || userRepo.existsByUserName(userName)){
            return null;
        }
        Optional<Credentials> credObj=credRepo.findByEmail(email);
        if(credObj.isPresent()) {
            credObj.get().setUsername(userName);

            credRepo.save(credObj.get());
            userRepo.save(user);
        }

            user.setUserName(userName);

            UserResponseDto userResponseDto = new UserResponseDto();
            userResponseDto.setUserName(user.getUserName());
            userResponseDto.setEmail(user.getEmail());
            userResponseDto.setPhoneNumber(user.getPhoneNumber());
            return userResponseDto;
    }

    @Override
    public UserResponseDto updateUserEmail(String email, String userEmail){
        User user = userRepo.findByEmail(email);
        if(user == null || user.getEmail().equals(userEmail) || agentRepo.existsByAgentEmail(userEmail) || userRepo.existsByPhoneNumber(userEmail)){
            return null;
        }
        Optional<Credentials> credObj=credRepo.findByEmail(email);
        if(credObj.isPresent()) {
            credObj.get().setEmail(userEmail);

            credRepo.save(credObj.get());
            userRepo.save(user);
        }
        user.setEmail(userEmail);
        userRepo.save(user);
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUserName(user.getUserName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setPhoneNumber(user.getPhoneNumber());
        return userResponseDto;
    }//exit

     @Override
    public UserResponseDto updateUserPhoneNum(String email, String phoneNum){
        User user = userRepo.findByEmail(email);
        if(user == null || user.getPhoneNumber().equals(phoneNum) ||agentRepo.existsByAgentPhoneNum(phoneNum) || userRepo.existsByPhoneNumber(phoneNum)){
            return null;
        }
         Optional<Credentials> credObj=credRepo.findByEmail(email);
         if(credObj.isPresent()) {
             credObj.get().setPhoneNumber(phoneNum);

             credRepo.save(credObj.get());
             userRepo.save(user);
         }
        user.setPhoneNumber(phoneNum);
        userRepo.save(user);
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUserName(user.getUserName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setPhoneNumber(user.getPhoneNumber());
        return userResponseDto;
    }

    @Override
    public ResponseEntity<String> userForgetPasswordSendVerificationCode(String email) throws Exception {
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
    public ResponseEntity<String> userForgetPasswordVerifyVerificationCode(String email, String enteredOtp) throws Exception {
        try {
            otpServiceImplementation.verifyEmailOtp(email, enteredOtp);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return new ResponseEntity("Email Verify Successfully", HttpStatus.OK);
    }

    @Override
    public AddUserDto updateUserPassword(String email, String password) throws Exception {
        User currUser = userRepo.findByEmail(email);
        AddUserDto newUser = new AddUserDto();

        currUser.setPassword(encoder.encode(password));
        try {
            userRepo.save(currUser);
            newUser.setUserName(currUser.getUserName());
            newUser.setEmail(currUser.getEmail());
            newUser.setPhoneNumber(currUser.getPhoneNumber());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return newUser;
    }
    @Override
    public ResponseEntity<?> updateUser(UpdateUserDTO updateUserDTO){
        User user = userRepo.findByEmail(updateUserDTO.getOldEmail());
        if(user==null)return new ResponseEntity("User not found",HttpStatus.NOT_FOUND);
        KycDetails kycDetails = user.getKycDetails();
        if(kycDetails==null) return  new ResponseEntity("No Kyc details found",HttpStatus.NOT_FOUND);
        kycDetails.setFirstName(updateUserDTO.getFirstName());
        kycDetails.setLastName(updateUserDTO.getLastName());
        kycDetails.setPhnNo(updateUserDTO.getAlternatePhnNo());
        kycDetails.setMartialStatus(updateUserDTO.getMartialStatus());

        Address address = kycDetails.getAddress();
        address.setAddress(updateUserDTO.getPermanentAddress().getAddress());
        address.setDistrict(updateUserDTO.getPermanentAddress().getDistrict());
        address.setState(updateUserDTO.getPermanentAddress().getState());
//        address.setKycDetails(kycDetails);
        addressRepo.save(address);

        kycDetails.setEducation(updateUserDTO.getEducation());

        kycDetailsRepo.save(kycDetails);
        userRepo.save(user);
        return new ResponseEntity<>("User updated",HttpStatus.OK);
    }

    @Override
    public List<UserTransactionsHistoryDto> getTransactionsBetweenDateByUserEmail(String userEmail, LocalDate startDate, LocalDate endDate) {
        User currUser = userRepo.findByEmail(userEmail);

        int accountId = currUser.getAccounts().getAccountId();

        List<Transactions> currTransaction = transactionRepo.findByAccountAccountIdAndTransactionDateBetween(accountId, startDate, endDate);
        List<UserTransactionsHistoryDto> transactionsHistoryDtoList = new ArrayList<>();

        for(Transactions t : currTransaction){
            UserTransactionsHistoryDto temp = new UserTransactionsHistoryDto();
            temp.setTransactionId(t.getTransactionId());
            temp.setDate(t.getTransactionDate());
            temp.setAmount(t.getTransactionAmount());
            temp.setTransactionStatus(t.getTransactionStatus());
            if(t.getMis() != null){
                temp.setTransactionCause("MIS");
            } else if (t.getFd() != null) {
                temp.setTransactionCause("FD");
            } else if (t.getRd()!=null) {
                temp.setTransactionCause("RD");
            } else if(t.getScheme()!=null){
                temp.setTransactionCause("SCHEME");
            } else if(t.getLoan()!=null) {
                temp.setTransactionCause("LOAN");
            }
            transactionsHistoryDtoList.add(temp);
        }
        return transactionsHistoryDtoList;
    }
}

