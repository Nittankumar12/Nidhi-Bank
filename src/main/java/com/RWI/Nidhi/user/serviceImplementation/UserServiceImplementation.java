package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.user.serviceInterface.UserServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserServiceInterface {

    //in scope of Mr Piyush and Mr Prince
    private String sentOtp;

    //implemented by Mr Piyush
    @Override
    public ResponseEntity<String> sendEmailOtp(String email) throws Exception {
        return null;
    }
    //implemented by Mr Piyush
    @Override
    public ResponseEntity<String> verifyEmailOtp(String email, String sentOtp, String enteredOtp) throws Exception {
        return null;
    }
    //implemented by Mr Prince
    @Override
    public ResponseEntity<String> sendPhoneOtp(String phoneNumber) throws Exception {
        return null;
    }
    //implemented by Mr Prince
    @Override
    public ResponseEntity<String> verifyPhoneOtp(String phoneNumber, String sentOtp, String enteredOtp) throws Exception {
        return null;
    }
}
