package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.entity.User;

public interface UserService {
    User getByEmail(String email);
    
    boolean authenticate(String username, String password);
}
