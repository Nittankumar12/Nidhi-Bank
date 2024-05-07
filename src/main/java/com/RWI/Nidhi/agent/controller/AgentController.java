package com.RWI.Nidhi.agent.controller;

import com.RWI.Nidhi.agent.serviceImplementation.AgentServiceImplementation;
import com.RWI.Nidhi.dto.AddUserDto;
import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.dto.LoanInfoDto;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.user.serviceImplementation.AccountsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/agent")
public class AgentController {

    @Autowired
    AgentServiceImplementation agentService;
    @Autowired
    AccountsServiceImplementation accountsService;

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
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users= agentService.getAllUsers();
        return new ResponseEntity<>(users,HttpStatus.OK);

    }
    @GetMapping("findUserById")
    public User findUserById(@RequestParam("id") int id) throws Exception{
        return agentService.findUserById(id);
    }
    @PostMapping("/verifyEmail")
    public ResponseEntity<String> verifyEmail(@RequestParam("email") String email) throws Exception {
        return agentService.forgetPasswordSendVerificationCode(email);
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<String> verifyOtp(@RequestParam("email") String email, @RequestParam("enteredOtp") String enteredOtp ) throws Exception {
        return agentService.forgetPasswordVerifyVerificationCode(email, enteredOtp);
    }

    @PostMapping("/updatePassword")
    public User updatePassword(@RequestParam("email") String email, @RequestParam("password") String password ) throws Exception {
       return agentService.updateUserPassword(email, password);
    }
    @PutMapping("/deactivateAccount")
    public Accounts deactivateAccount(@RequestParam("accountNumber") String accountNumber) throws Exception{
        return agentService.deactivateAccount(accountNumber);
    }
    @PutMapping("/closeAccount")
    public Accounts closeAccount(@RequestParam("accountNumber") String accountNumber) throws Exception{
        return agentService.closeAccount(accountNumber);
    }
    @PutMapping("/LoanApproved/{email}")
    public ResponseEntity<LoanInfoDto> LoanApproved(@RequestParam("email")String email){
        return new ResponseEntity<>(agentService.LoanApproved(email), HttpStatus.OK);
    }
}
