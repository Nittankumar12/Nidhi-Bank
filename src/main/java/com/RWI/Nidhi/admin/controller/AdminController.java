package com.RWI.Nidhi.admin.controller;

import com.RWI.Nidhi.Security.payload.request.SignupRequest;
import com.RWI.Nidhi.Security.payload.response.MessageResponse;
import com.RWI.Nidhi.admin.ResponseDto.AdminViewsAgentDto;
import com.RWI.Nidhi.admin.ResponseDto.AgentMinimalDto;
import com.RWI.Nidhi.admin.adminServiceImplementation.AdminServiceImplementation;
import com.RWI.Nidhi.dto.AddAgentDto;
import com.RWI.Nidhi.dto.TransactionsHistoryDto;
import com.RWI.Nidhi.entity.*;
import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.dto.LoginReq;
import com.RWI.Nidhi.entity.Admin;
import com.RWI.Nidhi.entity.Agent;
import com.RWI.Nidhi.repository.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    AdminServiceImplementation adminService;
    @Autowired
    AdminRepo adminRepo;

    @PostMapping("/addAgent")
    public SignupRequest addAgent(@RequestBody SignupRequest signupRequest) throws Exception {
        return adminService.addAgent(signupRequest);
    }
    @PostMapping("/addAdmin")
    public ResponseEntity<?> addAdmin(@RequestBody SignupRequest signupRequest) throws Exception {
        adminService.addAdmin(signupRequest);
        return ResponseEntity.ok(new MessageResponse("Driver registered successfully!"));
    }

    @PutMapping("/updateAgentName")
    public AddAgentDto updateAgentName(@RequestParam("agentEmail") String agentEmail, @RequestParam("agentName") String agentName) throws Exception {
        return adminService.updateAgentName(agentEmail, agentName);
    }

    @PutMapping("/updateAgentAddress")
    public AddAgentDto updateAgentAddress(@RequestParam("agentEmail") String agentEmail, @RequestParam("agentAddress") String agentAddress) throws Exception {
        return adminService.updateAgentAddress(agentEmail, agentAddress);
    }

    @PutMapping("/updateAgentEmail")
    public AddAgentDto updateAgentEmail(@RequestParam("agentEmail") String id, @RequestParam("agentEmail") String agentEmail) throws Exception {
        return adminService.updateAgentEmail(agentEmail, agentEmail);
    }

    @PutMapping("/updateAgentPhone")
    public AddAgentDto updateAgentPhoneNum(String agentEmail, String phoneNum) throws Exception {
        return adminService.updateAgentPhoneNum(agentEmail, phoneNum);
    }

    @DeleteMapping("/deleteAgentById")
    public boolean deleteAgentById(@RequestParam("id") int id) throws Exception {
        return adminService.deleteAgentById(id);
    }

    @GetMapping("/getAllAgentsIdentifierDetails")
    public List<AgentMinimalDto> getAllAgents() {
        return adminService.getAllAgents();
    }

    @GetMapping("/findAgentById/{id}")
    public AdminViewsAgentDto findAgentById(@PathVariable int id) throws Exception {
        return adminService.getAgentById(id);
    }
    @GetMapping("/getLoanByStatus")
    public List<Loan> findByStatus(@RequestParam("status") LoanStatus status){
        return adminService.findByStatus(status);
    }
    @GetMapping("/transactionOfCurrentMonth")
    public List<TransactionsHistoryDto> getTransactionForCurrentMonth() {
        return adminService.getTransactionForCurrentMonth();
    }
    @GetMapping("/transactionOfToday")
    public List<TransactionsHistoryDto> getTransactionForToday() {
        return adminService.getTransactionForToday();
    }
    @GetMapping("/transactionOfWeek")
    public List<TransactionsHistoryDto> getTransactionForCurrentWeek() {
        return adminService.getTransactionForCurrentWeek();
    }
    @GetMapping("/transactionOfDate")
    public List<TransactionsHistoryDto> getTransactionBetweenDates(@RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate){
        return adminService.getTransactionBetweenDates(startDate, endDate);
    }
    @PostMapping("/login-admin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginReq loginReq) {
        Admin admin = adminRepo.findByAdminName(loginReq.getUsername());
        try {
            if (admin == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin with given username does not exist");
            }
            if (admin.getPassword().equals(encoder.encode(loginReq.getPassword()))) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("login successful");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
        } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Exception");
        }
    }
}
