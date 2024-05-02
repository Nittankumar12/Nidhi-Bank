package com.RWI.Nidhi.user.controller;

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
}
