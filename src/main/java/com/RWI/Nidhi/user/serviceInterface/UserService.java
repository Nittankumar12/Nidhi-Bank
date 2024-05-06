package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.entity.User;
import org.springframework.stereotype.Service;


@Service
public interface UserService {

     User getByEmail(String email);

    //prince

    User getByEmail(String email);
    

    boolean authenticate(String username, String password);
}







