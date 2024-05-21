package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.LoanApplyDto;
import com.RWI.Nidhi.entity.Agent;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.LoanType;
import com.RWI.Nidhi.user.serviceInterface.UserLoanServiceInterface;
import com.RWI.Nidhi.user.serviceInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/loan")
public class LoanController {
    @Autowired
    UserService userService;
    @Autowired
    UserLoanServiceInterface userLoanService;
    @GetMapping("/maxLoan/{email}")
    public ResponseEntity<Double> maxLoan(@PathVariable("email") String email) {
        if (userLoanService.checkForExistingLoan(email) == Boolean.TRUE)
            return new ResponseEntity<>(userLoanService.maxApplicableLoan(email), HttpStatus.FOUND);
        else
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }
    @GetMapping("/getInfoByLoanType")
    public ResponseEntity<?> getLoanInfoByLoanType(@RequestParam LoanType loanType,@RequestParam double principalAmount,@RequestParam int rePaymentTerm){
        return userLoanService.getLoanInfoByLoanType(loanType,principalAmount,rePaymentTerm);
    }
    @PostMapping("/applyLoan")
    public ResponseEntity<?> applyLoan(@RequestBody LoanApplyDto loanApplyDto) {
        User user = userService.getByEmail(loanApplyDto.getUserEmail());
        Agent agent = user.getAgent();
        if (!agent.getAgentEmail().equals(loanApplyDto.getAgentEmail())) {
            return new ResponseEntity<>("This user is not in current agent's list", HttpStatus.NOT_FOUND);
        } else {
            if (userLoanService.checkForExistingLoan(loanApplyDto.getUserEmail()) == Boolean.TRUE) {
                if (userLoanService.checkForLoanBound(loanApplyDto.getUserEmail(), loanApplyDto.getPrincipalLoanAmount()) == Boolean.TRUE) {
                    userLoanService.applyLoan(loanApplyDto);
                    return new ResponseEntity<>(userLoanService.getLoanInfo(loanApplyDto.getUserEmail()), HttpStatus.ACCEPTED);
                } else
                    return new ResponseEntity<>("Loan Amount Request exceed allowed amount", HttpStatus.BAD_REQUEST);
            } else
                return new ResponseEntity<>("You have another active loan", HttpStatus.I_AM_A_TEAPOT);
        }
    }


    @GetMapping("/getLoanInfo/{email}")
    public ResponseEntity<?> getLoanInfo(@PathVariable String email) {
        return userLoanService.getLoanInfo(email);
    }

    @PutMapping("/payEMI/{email}")
    ResponseEntity<?> payEMI(@PathVariable String email) {
        if (userLoanService.checkForExistingLoan(email) != Boolean.FALSE) {
            return new ResponseEntity<>("No Loan currently recorded", HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>(userLoanService.payEMI(email), HttpStatus.ACCEPTED);
    }
    @GetMapping("/findInterest/")
    public ResponseEntity<?> findRateByLoanType(@RequestParam LoanType loanType){
        return userLoanService.findRateByLoanType(loanType);
    }

    @GetMapping("/getLoanClosureDetails/{email}")
    ResponseEntity<?> getLoanClosureDetails(@PathVariable String email) {
        return userLoanService.getLoanClosureDetails(email);
    }

    @PutMapping("/applyLoanClosureDetails/{email}")
    ResponseEntity<?> applyLoanClosureDetails(@PathVariable String email) {
        return userLoanService.applyForLoanClosure(email);
    }
}