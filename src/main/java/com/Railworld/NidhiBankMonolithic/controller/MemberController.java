package com.Railworld.NidhiBankMonolithic.controller;

import com.Railworld.NidhiBankMonolithic.dto.LoanDto;
import com.Railworld.NidhiBankMonolithic.model.Loan;
import com.Railworld.NidhiBankMonolithic.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("member")
public class MemberController {

    @Autowired
    MemberService memberService;

    @PostMapping("payLoan")
    public ResponseEntity<String> payLoan(@RequestBody Long accountNo, @RequestBody Double amount){
        memberService.payLoan(accountNo,amount);
        return new ResponseEntity<>("done", HttpStatus.OK);
    }

    @PostMapping("applyLoan")
    public ResponseEntity<String> applyLoan(@RequestBody LoanDto loanDto){
        memberService.applyLoan(loanDto);
        return new ResponseEntity<>("application submitted",HttpStatus.CREATED);
    }

}
