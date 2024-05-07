package com.RWI.Nidhi.admin.controller;

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
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminServiceImplementation adminService;
    @Autowired
    AdminRepo adminRepo;

    @PostMapping("/addAgent")
    public Agent addAgent(@RequestBody AddAgentDto addAgentDto) throws Exception {
        return adminService.addAgent(addAgentDto);
    }

    @PutMapping("/updateAgentName")
    public Agent updateAgentName(@RequestParam("id") int id, @RequestParam("agentName") String agentName) throws Exception {
        return adminService.updateAgentName(id, agentName);
    }

    @PutMapping("/updateAgentAddress")
    public Agent updateAgentAddress(@RequestParam("id") int id, @RequestParam("agentAddress") String agentAddress) throws Exception {
        return adminService.updateAgentAddress(id, agentAddress);
    }

    @PutMapping("/updateAgentEmail")
    public Agent updateAgentEmail(@RequestParam("id") int id, @RequestParam("agentEmail") String agentEmail) throws Exception {
        return adminService.updateAgentEmail(id, agentEmail);
    }

    @PutMapping("/updateAgentPhone")
    public Agent updateAgentPhoneNum(int id, String phoneNum) throws Exception {
        return adminService.updateAgentPhoneNum(id, phoneNum);
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
    public List<TransactionsHistoryDto> getTransactionForCurrentMonth(TransactionsHistoryDto transactionsHistoryDto) {
        return adminService.getTransactionForCurrentMonth(transactionsHistoryDto);
    }
    @GetMapping("/transactionOfToday")
    public List<TransactionsHistoryDto> getTransactionForToday(TransactionsHistoryDto transactionsHistoryDto) {
        return adminService.getTransactionForToday(transactionsHistoryDto);
    }
    @GetMapping("/transactionOfWeek")
    public List<TransactionsHistoryDto> getTransactionForCurrentWeek(TransactionsHistoryDto transactionsHistoryDto) {
        return adminService.getTransactionForCurrentWeek(transactionsHistoryDto);
    }
    @PostMapping("/login-admin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginReq loginReq) {
        Admin admin = adminRepo.findByAdminName(loginReq.getUsername());
        try {
            if (admin == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin with given username does not exist");
            }
            if (admin.getPassword().equals(loginReq.getPassword())) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("login successful");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");

            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Exception");
        }
    }
}
