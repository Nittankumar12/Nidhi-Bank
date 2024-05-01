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
    public ResponseEntity<Double> maxLoan(@PathVariable String email){
        if(userLoanService.checkForExistingLoan(email)==Boolean.TRUE)
            return new ResponseEntity<>( userLoanService.maxApplicableLoan(email),HttpStatus.FOUND);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Loan calc acc to scheme - > during the duration of scheme - > get for info , post for apply
    // Loan emi calc
}