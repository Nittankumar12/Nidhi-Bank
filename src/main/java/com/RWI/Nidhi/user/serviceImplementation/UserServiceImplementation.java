package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.user.serviceInterface.UserServiceInterface;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserServiceInterface {
    @Override
    public boolean verifyMobileOtp(String mobNo) {
        return false;
    }

    @Override
    public boolean verifyEmailOtp(String mobNo) {
        return false;
    }
}
