package com.RWI.Nidhi.user.controller;
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
    public ResponseEntity<?> maxLoan(@PathVariable String email) {
        if (userSchemeLoanService.checkForExistingLoan(email) == Boolean.TRUE)
            return userSchemeLoanService.schemeLoan(email);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/applySchLoan/{email}")
    public ResponseEntity<?> applyLoan(@PathVariable String email) {
        if (userSchemeLoanService.checkForExistingLoan(email) == Boolean.TRUE) {

            return userSchemeLoanService.applySchemeLoan(email);
        } else
            return new ResponseEntity<>("You have another active loan", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getLoanInfo/{email}")
    public ResponseEntity<?> getLoanInfo(@PathVariable String email) {
        if (userSchemeLoanService.checkForExistingLoan(email) == Boolean.FALSE) {
            return userSchemeLoanService.getLoanInfo(email);
        } else {
            System.out.println("scheme loan does not exist");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/payEMI/{email}")
    ResponseEntity<?> payEMI(String email) {
        MonthlyEmiDto monthlyEmiDto = userSchemeLoanService.payEMI(email);
        if(monthlyEmiDto == null){
            return new ResponseEntity<>("either scheme is null or user is inactive", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(monthlyEmiDto, HttpStatus.OK);
    }

    @GetMapping("/getLoanClosureDetails/{email}")
    ResponseEntity<?> getLoanClosureDetails(String email) {
        return userSchemeLoanService.getLoanClosureDetails(email);
    }

    @PutMapping("/applyLoanClosureDetails/{email}")
    ResponseEntity<?> applyLoanClosureDetails(String email) {
        return userSchemeLoanService.applyForLoanClosure(email);
    }

}