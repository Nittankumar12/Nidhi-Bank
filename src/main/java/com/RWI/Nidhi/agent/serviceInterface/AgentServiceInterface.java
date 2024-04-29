package com.RWI.Nidhi.agent.serviceInterface;

import com.RWI.Nidhi.dto.AddUserDto;
import com.RWI.Nidhi.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AgentServiceInterface {
    User addUser(AddUserDto addUserDto) throws Exception;
    User updateUserName(int id,String userName) throws Exception;
    User updateUserEmail(int id,String userEmail) throws Exception;
    User updateUserPhoneNum(int id,String phoneNum) throws Exception;
    boolean deleteUserById(int id) throws Exception;
    List<User> getAllUsers();
    User findUserById(int id) throws Exception;
    ResponseEntity<String> forgetPassword(String email)throws Exception;
}

