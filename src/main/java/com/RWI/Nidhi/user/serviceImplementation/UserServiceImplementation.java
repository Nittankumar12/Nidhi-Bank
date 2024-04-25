package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.user.repository.UserRepo;
import com.RWI.Nidhi.user.serviceInterface.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserServiceInterface {
    @Autowired
    UserRepo userRepo;
    @Override
    public String getEmail(int userId) {
        return userRepo.findEmailByUserId(userId);
    }

    @Override
    public int getAccountIdByUserEmail(String email) {
        return userRepo.findAccountIdByEmail(email);
    }
}