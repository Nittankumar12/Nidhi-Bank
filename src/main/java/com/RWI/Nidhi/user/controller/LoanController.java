package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.LoanDto;
import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.user.serviceInterface.UserLoanServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loan")
public class LoanController {
    @Autowired
    UserLoanServiceInterface userLoanService;

    @GetMapping("/maxLoan/{email}")
    public double maxLoan(@PathVariable String email){
        if(userLoanService.checkForExistingLoan(email)==Boolean.TRUE)
            return userLoanService.maxApplicableLoan(email);
        else
            return 0;
    }
    @PostMapping("/applyLoan")
    public void applyLoan(@RequestBody LoanDto loanDto){
        if(userLoanService.checkForExistingLoan(loanDto.getEmail())==Boolean.TRUE)
            if(userLoanService.checkForLoanBound(loanDto.getEmail(),loanDto.getPrincipalLoanAmount()) == Boolean.TRUE)
                userLoanService.applyLoan(loanDto);
            else
                System.out.println("Loan Amount Request exceed allowed amount");
        else
            System.out.println("You have another active loan");
    }

    @GetMapping("/getLoanInfo/{email}")
    public LoanDto getLoanInfo(@PathVariable String email){
        if (userLoanService.checkForExistingLoan(email) == Boolean.FALSE){
            return userLoanService.getLoanInfo(email);
        }
        else
            System.out.println("loan does not exist");
        return null;
    }

}
