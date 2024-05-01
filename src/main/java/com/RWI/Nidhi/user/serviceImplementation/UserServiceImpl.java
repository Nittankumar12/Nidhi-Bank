package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.repository.UserRepo;
import com.RWI.Nidhi.user.serviceInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepo userRepo;
    @Override
    public User getByEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
