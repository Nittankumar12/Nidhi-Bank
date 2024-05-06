package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.entity.User;
import org.springframework.stereotype.Service;


@Service
public interface UserService {
     User getByEmail(String email);

    //prince
    boolean authenticate(String username, String password);
}







