package com.RWI.Nidhi.user.controller;
import com.RWI.Nidhi.dto.LoanApplyDto;
import com.RWI.Nidhi.dto.LoanClosureDto;
import com.RWI.Nidhi.dto.LoanInfoDto;
import com.RWI.Nidhi.dto.MonthlyEmiDto;
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
    public ResponseEntity<String> applyLoan(@RequestBody LoanApplyDto loanApplyDto){
        if(userLoanService.checkForExistingLoan(loanApplyDto.getEmail())==Boolean.TRUE)
            if(userLoanService.checkForLoanBound(loanApplyDto.getEmail(),loanApplyDto.getPrincipalLoanAmount()) == Boolean.TRUE) {
                userLoanService.applyLoan(loanApplyDto);
                return new ResponseEntity<>("Loan Has been successfully requested",HttpStatus.ACCEPTED);
            }
            else
                return new ResponseEntity<>("Loan Amount Request exceed allowed amount", HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>("You have another active loan",HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/getLoanInfo/{email}")
    public ResponseEntity<LoanInfoDto> getLoanInfo(@PathVariable String email){
        if (userLoanService.checkForExistingLoan(email) == Boolean.FALSE){
            return new ResponseEntity<>(userLoanService.getLoanInfo(email),HttpStatus.FOUND);
        }
        else {
            System.out.println("loan does not exist");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/payEMI/{email}")
    ResponseEntity<MonthlyEmiDto> payEMI(String email){
        return new ResponseEntity<>(userLoanService.payEMI(email),HttpStatus.ACCEPTED);
    }
    @GetMapping("/getloanClosureDetails/{email}")
    ResponseEntity<LoanClosureDto> getLoanClosureDetails(String email){
        return new ResponseEntity<>(userLoanService.getLoanClosureDetails(email),HttpStatus.FOUND);
    }
    @PutMapping("/applyloanClosureDetails/{email}")
    ResponseEntity<String> applyloanClosureDetails(String email){
        return new ResponseEntity<>(HttpStatus.valueOf(userLoanService.applyForLoanClosure(email)));
    }

    // Loan calc acc to scheme - > during the duration of scheme - > get for info , post for apply

}//loan closure