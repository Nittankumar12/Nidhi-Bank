package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.LoanApplyDto;
import com.RWI.Nidhi.user.serviceInterface.UserLoanServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/loan")
public class LoanController {
    @Autowired
    UserLoanServiceInterface userLoanService;

    @GetMapping("/maxLoan/{email}")
    public ResponseEntity<Double> maxLoan(@PathVariable("email") String email) {
        if (userLoanService.checkForExistingLoan(email) == Boolean.TRUE)
            return new ResponseEntity<>(userLoanService.maxApplicableLoan(email), HttpStatus.FOUND);
        else
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }

    @PostMapping("/applyLoan")
    public ResponseEntity<?> applyLoan(@RequestBody LoanApplyDto loanApplyDto) {
        if (userLoanService.checkForExistingLoan(loanApplyDto.getEmail()) == Boolean.TRUE) {
            if (userLoanService.checkForLoanBound(loanApplyDto.getEmail(), loanApplyDto.getPrincipalLoanAmount()) == Boolean.TRUE) {
                userLoanService.applyLoan(loanApplyDto);
                return new ResponseEntity<>(userLoanService.getLoanInfo(loanApplyDto.getEmail()), HttpStatus.ACCEPTED);
            } else
                return new ResponseEntity<>("Loan Amount Request exceed allowed amount", HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>("You have another active loan", HttpStatus.I_AM_A_TEAPOT);
    }


    @GetMapping("/getLoanInfo/{email}")
    public ResponseEntity<?> getLoanInfo(@PathVariable String email) {
        return userLoanService.getLoanInfo(email);
    }

    @PutMapping("/payEMI/{email}")
    ResponseEntity<?> payEMI(String email) {
        if (userLoanService.checkForExistingLoan(email) != Boolean.FALSE) {
            return new ResponseEntity<>("No Loan currently recorded", HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>(userLoanService.payEMI(email), HttpStatus.ACCEPTED);
    }

    @GetMapping("/getLoanClosureDetails/{email}")
    ResponseEntity<?> getLoanClosureDetails(String email) {
        return userLoanService.getLoanClosureDetails(email);
    }

    @PutMapping("/applyLoanClosureDetails/{email}")
    ResponseEntity<?> applyLoanClosureDetails(String email) {
        return userLoanService.applyForLoanClosure(email);
    }
}
// Loan Service - > Done, Testing remains
// Info, LoanClosure-> Info, Apply
// Scheme Info, LoanClosure-> Info, Apply

// AGENT LOAN CONTROLLER - > Done, Testing remains
// sanction, approve, etc. --> update loan status(user email, agent email(with verification), status to & status from)

// SCHEME LOAN CONTROLLER
// Scheme Loan - common problem of working for any loan and check for scheme running
// apply prob - in conditions
// help rahul with apply scheme - email, tenure, amount