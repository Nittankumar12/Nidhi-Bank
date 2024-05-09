package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.AddUserDto;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.otpSendAndVerify.OtpServiceImplementation;
import com.RWI.Nidhi.repository.UserRepo;
import com.RWI.Nidhi.user.serviceInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    UserService userService;
    @Autowired
    OtpServiceImplementation otpServiceImplementation;

    @Override
    public User getByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public boolean authenticate(String email, String password) {

        User user = userRepo.findByEmail(email);
        if (user != null && user.getAccounts().getAccountStatus().equals("ACTIVE")) {
            return (user.getPassword().equals(getEncryptedPassword(password)))
                    && email.equals(user.getEmail());
        } else {
            return false;
        }
    }

    private String getEncryptedPassword(String password) {
        // String encryptedPassword = "";
        try {
            BigInteger number = new BigInteger(1, getSHA(password));
            return number.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
    public AddUserDto updateUserPassword(String email, String password) throws Exception {
        User currUser = userRepo.findByEmail(email);
        AddUserDto newUser = new AddUserDto();

        currUser.setPassword(getEncryptedPassword(password));
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
}

