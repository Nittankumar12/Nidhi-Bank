package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.LoanDto;
import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.user.serviceInterface.UserLoanServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/loan")
public class UserLoanController {

    @Autowired
    UserLoanServiceInterface userLoanService;

    @GetMapping("/max-applicable-loan/{accountId}")
    public double maxApplicableLoan(@PathVariable int accountId){
        return userLoanService.maxApplicableLoan(accountId);
    }
    @PostMapping("/apply-loan")
    public void applyLoan(@RequestBody LoanDto loanDto){
        System.out.println("In loan contro");
        userLoanService.applyLoan(loanDto);
    }
    @GetMapping("/check-loan-status/{loanId}")
    public LoanStatus checkLoanStatus(@PathVariable int loanId){
        return userLoanService.checkLoanStatus(loanId);
    }
    @GetMapping("/check-current-status/{loanId}")
    public int checkCurrentEMI(@PathVariable int loanId){
        return userLoanService.checkCurrentEMI(loanId);

    }
    @PutMapping("/pay-emi/{loanId}")
    public String payEMI(@PathVariable int loanId,@RequestParam int payedEMI){
        return userLoanService.payEMI(loanId,payedEMI);

    }
    @PutMapping("/apply-loan-closure/{loanId}")
    public void applyLoanClosure(@PathVariable int loanId) {
        userLoanService.applyLoanClosure(loanId);
    }
}