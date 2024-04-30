package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.LoanDto;
import com.RWI.Nidhi.user.serviceInterface.UserLoanServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan")
public class LoanController {
    @Autowired
    UserLoanServiceInterface userLoanService;
    @PostMapping("/applyLoan")
    public void applyLoan(@RequestBody LoanDto loanDto){
        if(userLoanService.checkForLoan(loanDto.getEmail())==Boolean.TRUE)
            userLoanService.applyLoan(loanDto);
        else
            System.out.println("Not Applicable for loan");
    }

}
