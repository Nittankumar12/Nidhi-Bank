package com.RWI.Nidhi.agent.controller;

import com.RWI.Nidhi.Security.payload.request.SignupRequest;
import com.RWI.Nidhi.agent.serviceImplementation.AgentServiceImplementation;
import com.RWI.Nidhi.dto.AddUserDto;
import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.dto.LoanInfoDto;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.LoanStatus;
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
    public ResponseEntity<?> addUser(@RequestBody SignupRequest signupRequest, @RequestParam("agentEmail") String agentEmail) throws Exception{
        return agentService.addUser(signupRequest, agentEmail);
    }
    @DeleteMapping("deleteUserById")
    public ResponseEntity<?> deleteUserById(@RequestParam("userEmail") String userEmail,@RequestParam("agentEmail") String agentEmail ) throws Exception{
        return agentService.deleteUserById(userEmail,agentEmail);
    }
    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers(@RequestParam("agentEmail") String agentEmail){
        return new ResponseEntity<>(agentService.getAllUsers(agentEmail),HttpStatus.OK);

    }
    @GetMapping("findUserById")
    public ResponseEntity<?> findUserById(@RequestParam("id") int id, @RequestParam("agentEmail") String agentEmail) throws Exception{
        return agentService.findUserById(id,agentEmail);
    }
    @PutMapping("/deactivateAccount")
    public ResponseEntity<?> deactivateAccount(@RequestParam("accountNumber") String accountNumber, @RequestParam("agentEmail") String agentEmail){
        return agentService.deactivateAccount(accountNumber,agentEmail);
    }
    @PutMapping("/closeAccount")
    public ResponseEntity<?> closeAccount(@RequestParam("accountNumber") String accountNumber,  @RequestParam("agentEmail") String agentEmail) throws Exception{
        return agentService.closeAccount(accountNumber,agentEmail);
    }
    @PutMapping("/ChangeLoanStatus/{email}")
    public ResponseEntity<?> ChangeLoanStatus(@RequestParam("email")String agentEmail, @RequestBody String userEmail, LoanStatus changedStatus, LoanStatus previousStatus){
        return agentService.changeLoanStatus(userEmail,agentEmail,changedStatus,previousStatus);
    } // DTO?
}
