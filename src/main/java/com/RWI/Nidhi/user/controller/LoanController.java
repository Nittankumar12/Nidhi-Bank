package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.LoanApplyDto;
import com.RWI.Nidhi.dto.LoanInfoDto;
import com.RWI.Nidhi.enums.LoanType;
import com.RWI.Nidhi.user.serviceInterface.AccountsServiceInterface;
import com.RWI.Nidhi.user.serviceInterface.UserLoanServiceInterface;
import com.RWI.Nidhi.user.serviceInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


@RestController
@RequestMapping("/loan")
public class LoanController {
    @Autowired
    AccountsServiceInterface accountsService;
    @Autowired
    UserLoanServiceInterface userLoanService;

    @GetMapping("/maxLoan/{email}")
    public ResponseEntity<?> maxLoan(@PathVariable("email") String email) {
        if (userLoanService.isLoanNotOpen(email) == Boolean.TRUE)
            return new ResponseEntity<>(userLoanService.maxApplicableLoan(email), HttpStatus.OK);
        else
            return new ResponseEntity<>("Another Loan active", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getInfoByLoanType")
    public ResponseEntity<?> getLoanInfoByLoanType(@RequestParam LoanType loanType, @RequestParam double principalAmount, @RequestParam int rePaymentTerm,@RequestParam Optional<Double> discount){
        if(loanType.equals(LoanType.Other))return userLoanService.getLoanInfoForOtherLoanType(discount.orElseThrow(), principalAmount,rePaymentTerm);
        return userLoanService.getLoanInfoByLoanType(loanType,principalAmount,rePaymentTerm);
    }

    @PostMapping("/applyLoan")
    public ResponseEntity<?> applyLoan(@RequestParam MultipartFile sign, @RequestParam MultipartFile signVideo, @RequestBody LoanApplyDto loanApplyDto) {
        LoanApplyDto loanApplyDto1 = loanApplyDto;
        loanApplyDto1.setSign(sign);
        loanApplyDto1.setSignVideo(signVideo);
        ResponseEntity<?> acco = accountsService.checkAccount(loanApplyDto1.getUserEmail());
        if(!acco.getStatusCode().equals(HttpStatus.OK)) return acco;
        if (userLoanService.isLoanNotOpen(loanApplyDto1.getUserEmail()) == Boolean.TRUE) {
            if (userLoanService.checkForLoanBound(loanApplyDto1.getUserEmail(), loanApplyDto1.getPrincipalLoanAmount()) == Boolean.TRUE) {
                LoanInfoDto loanInfoDto = userLoanService.applyLoan(loanApplyDto1);
                if (loanInfoDto == null)
                    return new ResponseEntity<>("Either user account closed or error while creating Loan", HttpStatus.NOT_ACCEPTABLE);
                return new ResponseEntity<>(loanInfoDto, HttpStatus.OK);
            } else
                return new ResponseEntity<>("Loan Amount Request exceed allowed amount", HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>("You have another active loan", HttpStatus.NOT_ACCEPTABLE);
    }


    @GetMapping("/getLoanInfo/{email}")
    public ResponseEntity<?> getLoanInfo(@PathVariable String email) {
        return userLoanService.getLoanInfo(email);
    }

    @PutMapping("/payEMI/{email}")
    ResponseEntity<?> payEMI(@PathVariable String email) {
        if (userLoanService.isLoanNotOpen(email) != Boolean.FALSE) {
            return new ResponseEntity<>("No Loan currently recorded", HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>(userLoanService.payEMI(email), HttpStatus.OK);
    }

    @GetMapping("/findInterest/")
    public ResponseEntity<?> findRateByLoanType(@RequestParam LoanType loanType) {
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