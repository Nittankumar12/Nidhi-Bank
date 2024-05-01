package com.RWI.Nidhi.user.controller;
import com.RWI.Nidhi.dto.LoanDto;
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
    public ResponseEntity<Double> maxLoan(@PathVariable String email){
        if(userLoanService.checkForExistingLoan(email)==Boolean.TRUE)
            return new ResponseEntity<>( userLoanService.maxApplicableLoan(email),HttpStatus.FOUND);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/applyLoan")
    public ResponseEntity<String> applyLoan(@RequestBody LoanDto loanDto){
        if(userLoanService.checkForExistingLoan(loanDto.getEmail())==Boolean.TRUE)
            if(userLoanService.checkForLoanBound(loanDto.getEmail(),loanDto.getPrincipalLoanAmount()) == Boolean.TRUE) {
                userLoanService.applyLoan(loanDto);
                return new ResponseEntity<>("Loan Has been successfully requested",HttpStatus.ACCEPTED);
            }
            else
                return new ResponseEntity<>("Loan Amount Request exceed allowed amount", HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>("You have another active loan",HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getLoanInfo/{email}")
    public ResponseEntity<LoanDto> getLoanInfo(@PathVariable String email){
        if (userLoanService.checkForExistingLoan(email) == Boolean.FALSE){
            return new ResponseEntity<>(userLoanService.getLoanInfo(email),HttpStatus.FOUND);
        }
        else {
            System.out.println("loan does not exist");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/payEMI/{email}/payedAmount")
    ResponseEntity<String> payEMI(String email, double payedAmount){
        Boolean b = userLoanService.payEMI(email,payedAmount);
        if(b)
            return new ResponseEntity<>("EMI paid for the month",HttpStatus.ACCEPTED);
        else
            return new ResponseEntity<>("EMI amount doesn't match",HttpStatus.NOT_ACCEPTABLE);
    }


    // Loan calc acc to scheme - > during the duration of scheme - > get for info , post for apply
    // Loan emi calc
}