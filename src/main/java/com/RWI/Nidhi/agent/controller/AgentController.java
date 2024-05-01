package com.RWI.Nidhi.agent.controller;

import com.RWI.Nidhi.agent.serviceImplementation.AgentServiceImplementation;
import com.RWI.Nidhi.dto.AddUserDto;
import com.RWI.Nidhi.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/agent")
public class AgentController {

    @Autowired
    AgentServiceImplementation agentService;

    @PostMapping("/addUser")
    public User addUser(@RequestBody AddUserDto addUserDto) throws Exception{
        return agentService.addUser(addUserDto);
    }
    @PutMapping("updateName")
    public User updateUserName(@RequestParam("id") int id,@RequestParam("userName") String userName) throws Exception{
        return agentService.updateUserName(id, userName);
    }
    @PutMapping("updateEmail")
    public User updateUserEmail(@RequestParam("id") int id,@RequestParam("userEmail") String userEmail) throws Exception{
        return agentService.updateUserEmail(id, userEmail);
    }
    @PutMapping("updatePhoneNumber")
    public User updateUserPhoneNum(@RequestParam("id") int id,@RequestParam("phoneNum") String phoneNum) throws Exception{
        return agentService.updateUserPhoneNum(id, phoneNum);
    }
    @DeleteMapping("deleteUserById")
    public boolean deleteUserById(@RequestParam("id") int id) throws Exception{
        return agentService.deleteUserById(id);
    }
    @GetMapping("getAllUsers")
    public List<User> getAllUsers(){
        return agentService.getAllUsers();
    }
    @GetMapping("findUserById")
    public User findUserById(@RequestParam("id") int id) throws Exception{
        return agentService.findUserById(id);
    }
    @PostMapping("/forgetPassword")
    public ResponseEntity<String> forgetPassword(@RequestParam String email) throws Exception {
        agentService.forgetPasswordSendVerificationCode(email);
        return ResponseEntity.ok("OTP send to provided Email");
    }

    @PostMapping("/verifiedPassword")
    public ResponseEntity<String> verifiedPassword(@RequestParam String email, @RequestParam String enteredOtp ) throws Exception {
        agentService.forgetPasswordVerifyVerificationCode(email, enteredOtp);
        return ResponseEntity.ok("Password Verified");
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestParam String email, @RequestParam String password ) throws Exception {
        agentService.updateUserPassword(email, password);
        return ResponseEntity.ok("Update Successfully");
    }
}
