package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.LoanClosureDto;
import com.RWI.Nidhi.dto.LoanInfoDto;
import com.RWI.Nidhi.dto.MonthlyEmiDto;
import com.RWI.Nidhi.user.serviceInterface.UserSchemeLoanServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scheme/loan")
public class UserSchemeLoanController {
    @Autowired
    UserSchemeLoanServiceInterface userSchemeLoanService;
    @GetMapping("/schemeLoan/{email}")
    public ResponseEntity<Double> maxLoan(@PathVariable String email){
        if(userSchemeLoanService.checkForExistingLoan(email)==Boolean.TRUE)
            return new ResponseEntity<>( userSchemeLoanService.schemeLoan(email), HttpStatus.FOUND);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/applySchLoan/{email}")
    public ResponseEntity<String> applyLoan(@PathVariable String email){
        if(userSchemeLoanService.checkForExistingLoan(email)==Boolean.TRUE) {
            userSchemeLoanService.applySchemeLoan(email);
            return new ResponseEntity<>("Loan Has been successfully requested", HttpStatus.ACCEPTED);
        }
        else
            return new ResponseEntity<>("You have another active loan",HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/getLoanInfo/{email}")
    public ResponseEntity<LoanInfoDto> getLoanInfo(@PathVariable String email){
        if (userSchemeLoanService.checkForExistingLoan(email) == Boolean.FALSE){
            return new ResponseEntity<>(userSchemeLoanService.getLoanInfo(email),HttpStatus.FOUND);
        }
        else {
            System.out.println("loan does not exist");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/payEMI/{email}")
    ResponseEntity<MonthlyEmiDto> payEMI(String email){
        return new ResponseEntity<>(userSchemeLoanService.payEMI(email),HttpStatus.ACCEPTED);
    }
    @GetMapping("/getloanClosureDetails/{email}")
    ResponseEntity<LoanClosureDto> getLoanClosureDetails(String email){
        return new ResponseEntity<>(userSchemeLoanService.getLoanClosureDetails(email),HttpStatus.FOUND);
    }
    @PutMapping("/applyloanClosureDetails/{email}")
    ResponseEntity<String> applyloanClosureDetails(String email){
        return new ResponseEntity<>(HttpStatus.valueOf(userSchemeLoanService.applyForLoanClosure(email)));
    }

}
