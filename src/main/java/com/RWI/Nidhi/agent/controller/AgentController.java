package com.RWI.Nidhi.agent.controller;

import com.RWI.Nidhi.Security.payload.request.SignupRequest;
import com.RWI.Nidhi.agent.serviceImplementation.AgentServiceImplementation;
import com.RWI.Nidhi.dto.Agentforgetpassword;
import com.RWI.Nidhi.enums.CommissionType;
import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.enums.SchemeStatus;
import com.RWI.Nidhi.repository.AgentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agent")
public class AgentController {

    @Autowired
    AgentServiceImplementation agentService;
    @Autowired
    AgentRepo agentRepo;



    @DeleteMapping("deleteUserById")
    public ResponseEntity<?> deleteUserById(@RequestParam("userEmail") String userEmail, @RequestParam("agentEmail") String agentEmail) throws Exception {
        return agentService.deleteUserById(userEmail, agentEmail);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers(@RequestParam("agentEmail") String agentEmail) {
        return new ResponseEntity<>(agentService.getAllUsers(agentEmail), HttpStatus.OK);

    }

    @GetMapping("findUserById")
    public ResponseEntity<?> findUserById(@RequestParam("id") int id, @RequestParam("agentEmail") String agentEmail) throws Exception {
        return agentService.findUserById(id, agentEmail);
    }

//    @PutMapping("/deactivateAccount")
//    public ResponseEntity<?> deactivateAccount(@RequestParam("accountNumber") String accountNumber, @RequestParam("agentEmail") String agentEmail) {
//        return agentService.deactivateAccount(accountNumber, agentEmail);
//    }
//
//    @PutMapping("/closeAccount")
//    public ResponseEntity<?> closeAccount(@RequestParam("accountNumber") String accountNumber, @RequestParam("agentEmail") String agentEmail) throws Exception {
//        return agentService.closeAccount(accountNumber, agentEmail);
//    }

    @PostMapping("/forget/verifyEmail")
    public ResponseEntity<String> verifyEmail(@RequestParam("agentEmail") String agentEmail) throws Exception {
        return agentService.agentForgetPasswordSendVerificationCode(agentEmail);
    }

    @PostMapping("/forget/verifyOtp")
    public ResponseEntity<String> verifyOtp(@RequestParam("agentEmail") String agentEmail, @RequestParam("enteredOtp") String enteredOtp) throws Exception {
        return agentService.agentForgetPasswordVerifyVerificationCode(agentEmail, enteredOtp);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/updateAgentPassword")
    public ResponseEntity<?> updateAgentPassword(
            @RequestBody Agentforgetpassword agentforgetpassword
            ) {
        try {
            return agentService.updateAgentPassword(agentforgetpassword);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getCommissionList/{agentEmail}")
    public ResponseEntity<?> getCommissionList(@RequestParam ("agentEmail")String agentEmail){
        if(agentRepo.existsByAgentEmail(agentEmail))
            return new ResponseEntity<>(agentService.getCommissionList(agentEmail),HttpStatus.FOUND);
        else
            return null;
    }
    @GetMapping("/getCommissionList/{agentEmail}/{CommissionType}")
    public ResponseEntity<?> getCommissionListByType(@RequestParam("agentEmail") String agentEmail,@RequestParam("commissionType") CommissionType commissionType){
        if(agentRepo.existsByAgentEmail(agentEmail))
            return new ResponseEntity<>(agentService.getCommissionList(agentEmail,commissionType),HttpStatus.FOUND);
        else
            return null;
    }
//    @PutMapping("/ChangeLoanStatus/{email}")
//    public ResponseEntity<?> ChangeLoanStatus(@RequestParam("email") String agentEmail, @RequestBody String userEmail, LoanStatus changedStatus, LoanStatus previousStatus) {
//        return agentService.ChangeLoanStatus(userEmail, agentEmail, changedStatus, previousStatus);
//    }
//
//    @PutMapping("/ChangeSchemeStatus/{email}")
//    public ResponseEntity<?> ChangeSchemeStatus(@RequestParam("email") String agentEmail, @RequestBody String userEmail, SchemeStatus changedStatus, SchemeStatus previousStatus) {
//        return agentService.ChangeSchemeStatus(userEmail, agentEmail, changedStatus, previousStatus);
//    }
//
//    @DeleteMapping("/deleteScheme/{email}")
//    public String deleteScheme(@PathVariable String email) {
//        return agentService.deleteScheme(email);
//    }
}
