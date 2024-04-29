package com.RWI.Nidhi.agent.serviceImplementation;

import com.RWI.Nidhi.agent.serviceInterface.AgentServiceInterface;
import com.RWI.Nidhi.dto.AddUserDto;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.otpSendAndVerify.OtpServiceImplementation;
import com.RWI.Nidhi.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class AgentServiceImplementation implements AgentServiceInterface {

    @Autowired
    UserRepo userRepo;
    @Autowired
    OtpServiceImplementation otpServiceImplementation;

    @Override
    public User addUser(AddUserDto addUserDto) throws Exception{

        //check if user already exists
        if(userRepo.existsByEmail(addUserDto.getEmail())){
            throw new Exception("user already exists");
        }
        //

        //creation of new user
        User newUser = new User();
        newUser.setUserName(addUserDto.getUserName());
        newUser.setEmail(addUserDto.getEmail());
        newUser.setPhoneNumber(addUserDto.getPhoneNumber());

        try {
            String tempPassword = otpServiceImplementation.generateOTP();
            String subject = "Your temporary password";
            String messageToSend = "Your temporary system generated password is: ";

            otpServiceImplementation.sendEmailOtp(newUser.getEmail(), subject, messageToSend,tempPassword);
            newUser.setPassword(tempPassword);
            userRepo.save(newUser);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return newUser;
    }

    @Override
    public User updateUserName(int id, String userName) throws Exception{
        User currUser = userRepo.findById(id).orElseThrow(() -> {return new Exception("User not found");});

        currUser.setUserName(userName);
        try {
            userRepo.save(currUser);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return currUser;
    }

    @Override
    public User updateUserEmail(int id, String userEmail) throws Exception{
        User currUser = userRepo.findById(id).orElseThrow(() -> {return new Exception("User not found");});

        currUser.setEmail(userEmail);
        try {
            userRepo.save(currUser);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return currUser;
    }

    @Override
    public User updateUserPhoneNum(int id, String phoneNum) throws Exception {
        User currUser = userRepo.findById(id).orElseThrow(() -> {return new Exception("User not found");});

        currUser.setPhoneNumber(phoneNum);
        try {
            userRepo.save(currUser);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return currUser;
    }

    @Override
    public User updateUserPassword(String email, String password) throws Exception {
        User currUser = userRepo.findByEmail(email);

        currUser.setPassword(password);
        try {
            userRepo.save(currUser);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return currUser;
    }

    @Override
    public boolean deleteUserById(int id) throws Exception {
        try{
            userRepo.deleteById(id);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User findUserById(int id) throws Exception{
        return userRepo.findById(id).orElseThrow(() -> {return new Exception("User not found");});
    }

    @Override
    public ResponseEntity<String> forgetPasswordSendVerificationCode(String email) throws Exception {
        //check if user already exists
        if(!userRepo.existsByEmail(email)){
            throw new Exception("This email is not registered with us");
        }
        //
        try {
            String otp = otpServiceImplementation.generateOTP();
            String subject = "Forgot password attempted";
            String messageToSend = "Your verification OTP is: ";
            otpServiceImplementation.sendEmailOtp(email, subject, messageToSend, otp);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }

        return new ResponseEntity("OTP send", HttpStatus.OK );
    }

    @Override
    public ResponseEntity<String> forgetPasswordVerifyVerificationCode(String email, String enteredOtp) throws Exception {
        return otpServiceImplementation.verifyEmailOtp(email, enteredOtp);
    }
}
