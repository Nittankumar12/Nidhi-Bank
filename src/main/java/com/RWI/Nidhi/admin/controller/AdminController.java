package com.RWI.Nidhi.admin.controller;

import com.RWI.Nidhi.Security.payload.request.SignupRequest;
import com.RWI.Nidhi.admin.adminServiceImplementation.AdminServiceImplementation;
import com.RWI.Nidhi.dto.KycDetailsDto;
import com.RWI.Nidhi.dto.LoanHistoryDto;
import com.RWI.Nidhi.enums.KycStatus;
import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.enums.LoanType;
import com.RWI.Nidhi.enums.SchemeStatus;
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
    public ResponseEntity<?> addAgent(@RequestBody SignupRequest signupRequest){
        return adminService.addAgent(signupRequest);
    }
    @CrossOrigin(origins = "http://localhost:5173/")
    @PostMapping("/addAdmin")
    public ResponseEntity<?> addAdmin(@RequestBody SignupRequest signupRequest,@RequestParam("adminPassword") String adminPassword){
        return adminService.addAdmin(signupRequest, adminPassword);
    }

    @PutMapping("/updateAgentName")
    public ResponseEntity<?> updateAgentName(@RequestParam("agentEmail") String agentEmail, @RequestParam("agentName") String agentName) throws Exception {
        return adminService.updateAgentName(agentEmail, agentName);
    }

    @PutMapping("/updateAgentAddress")
    public ResponseEntity<?> updateAgentAddress(@RequestParam("agentEmail") String agentEmail, @RequestParam("agentAddress") String agentAddress) throws Exception {
        return adminService.updateAgentAddress(agentEmail, agentAddress);
    }

    @PutMapping("/updateAgentEmail")
    public ResponseEntity<?> updateAgentEmail(@RequestParam("agentEmail") String id, @RequestParam("agentEmail") String agentEmail) throws Exception {
        return adminService.updateAgentEmail(agentEmail, agentEmail);
    }

    @PutMapping("/updateAgentPhone")
    public ResponseEntity<?> updateAgentPhoneNum(String agentEmail, String phoneNum) throws Exception {
        return adminService.updateAgentPhoneNum(agentEmail, phoneNum);
    }

    @DeleteMapping("/deleteAgentById")
    public ResponseEntity<?> deleteAgentById(@RequestParam("id") int id) throws Exception {
        return adminService.deleteAgentById(id);
    }

    @GetMapping("/getAllAgentsIdentifierDetails")
    public ResponseEntity<?> getAllAgents() {
        return adminService.getAllAgents();
    }

    @GetMapping("/findAgentById/{id}")
    public ResponseEntity<?> findAgentById(@PathVariable int id) throws Exception {
        return adminService.getAgentById(id);
    }
    @GetMapping("/getLoanByStatus")
    public ResponseEntity<?> findByStatus(@RequestParam("status") LoanStatus status){
        return adminService.findByStatus(status);
    }
    @GetMapping("/transactionOfCurrentMonth")
    public ResponseEntity<?> getTransactionForCurrentMonth() {
        return adminService.getTransactionForCurrentMonth();
    }
    @GetMapping("/transactionOfToday")
    public ResponseEntity<?> getTransactionForToday() {
        return adminService.getTransactionForToday();
    }
    @GetMapping("/transactionOfWeek")
    public ResponseEntity<?> getTransactionForCurrentWeek() {
        return adminService.getTransactionForCurrentWeek();
    }
    @GetMapping("/transactionOfDate")
    public ResponseEntity<?> getTransactionBetweenDates(@RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate){
        return adminService.getTransactionBetweenDates(startDate, endDate);
    }
    @GetMapping("/getLoanByType")
    public ResponseEntity<Object> getByLoanType(@RequestParam LoanType loanType) {
        List<LoanHistoryDto> loanHistoryDtos = adminService.getLoansByLoanType(loanType);
        return new ResponseEntity<>(loanHistoryDtos, HttpStatus.OK);
    }
    @GetMapping("/loanStatus")
    public ResponseEntity<Object> getLoansByStatus(@RequestParam LoanStatus loanStatus) {
        List<LoanHistoryDto> loanHistoryDtos =adminService.getLoansByLoanStatus(loanStatus);
        return new ResponseEntity<>(loanHistoryDtos,HttpStatus.OK);
    }

    ResponseEntity<?> addBalanceToAccount(double amount){
        return adminService.addBalanceToAccount(amount);
    }

    ResponseEntity<?> deductBalanceToAccount(double amount){
        return adminService.deductBalanceToAccount(amount);
    }

    @PutMapping("/deactivateAccount")
    public ResponseEntity<?> deactivateAccount(@RequestParam("accountNumber") String accountNumber, @RequestParam("agentEmail") String agentEmail) {
        return adminService.deactivateAccount(accountNumber, agentEmail);
    }

//Shifted methods from Agent to Admin

    @PutMapping("/closeAccount")
    public ResponseEntity<?> closeAccount(@RequestParam("accountNumber") String accountNumber, @RequestParam("agentEmail") String agentEmail) throws Exception {
        return adminService.closeAccount(accountNumber, agentEmail);
    }
    @PutMapping("/ChangeLoanStatus")
    public ResponseEntity<?> changeLoanStatus(@RequestParam("agentEmail") String agentEmail, @RequestParam("userEmail") String userEmail, LoanStatus changedStatus, LoanStatus previousStatus) {
        return adminService.changeLoanStatus(userEmail, agentEmail, changedStatus, previousStatus);
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestBody SignupRequest signupRequest, @RequestParam("agentRefferalCode") String agentRefferalCode) throws Exception {
        return adminService.addUser(signupRequest, agentRefferalCode);
    }
    @PutMapping("/ChangeSchemeStatus/{email}")
    public ResponseEntity<?> ChangeSchemeStatus(@RequestParam("email") String agentEmail, @RequestBody String userEmail, SchemeStatus changedStatus, SchemeStatus previousStatus) {
        return adminService.ChangeSchemeStatus(userEmail, agentEmail, changedStatus, previousStatus);
    }

    @DeleteMapping("/deleteScheme/{email}")
    public String deleteScheme(@PathVariable String email) {
        return adminService.deleteScheme(email);
    }

    @DeleteMapping("deleteUserById")
    public ResponseEntity<?> deleteUserById(@RequestParam("userEmail") String userEmail, @RequestParam("agentEmail") String agentEmail) throws Exception {
        return adminService.deleteUserById(userEmail, agentEmail);
    }
    @PutMapping("/ChangeKycStatus/{userEmail}")
    public ResponseEntity<?> ChangeSchemeStatus(@RequestParam("userEmail") String userEmail, @RequestBody KycStatus newStatus) {
        return adminService.ChangeKycStatus(userEmail,newStatus);
    }
    @GetMapping("/getKycDetails/{userEmail}")
    public ResponseEntity<?> getKycDetails(String userEmail){
        return adminService.getKycDetails(userEmail);
    }

//    @PostMapping("/login-admin")
//    public ResponseEntity<?> authenticateUser(@RequestBody LoginReq loginReq) {
//        Admin admin = adminRepo.findByAdminName(loginReq.getUsername());
//        try {
//            if (admin == null) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin with given username does not exist");
//            }
//            if (admin.getPassword().equals(encoder.encode(loginReq.getPassword()))) {
//                return ResponseEntity.status(HttpStatus.ACCEPTED).body("login successful");
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//            }
//        } catch (Exception e) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Exception");
//        }
//    }
}
