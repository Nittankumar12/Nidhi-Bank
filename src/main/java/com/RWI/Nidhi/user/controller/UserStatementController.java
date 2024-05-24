package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.*;
import com.RWI.Nidhi.entity.Scheme;
import com.RWI.Nidhi.entity.Transactions;
import com.RWI.Nidhi.user.serviceImplementation.UserStatementServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Statement")
public class UserStatementController {
    @Autowired
    private UserStatementServiceImpl service;


    @GetMapping("/getFdDetails/{email}")
    public ResponseEntity<?> getFdDetails(@PathVariable("email") String email) {
        List<FdRequestDto> fdRequestDtoList =  service.getFixedDepositDetailsByEmail(email);
        if(fdRequestDtoList == null) return new ResponseEntity<>("User account is inactive", HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(fdRequestDtoList,HttpStatus.OK);
    }

    @GetMapping("/getRdDetails/{email}")
    public ResponseEntity<?>  getRdDetails(@PathVariable("email") String email) {
        List<RdRequestDto> rdRequestDtoList = service.getRecurringDepositDetailsByEmail(email);
        if(rdRequestDtoList == null) return new ResponseEntity<>("user inactive list not found", HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(rdRequestDtoList,HttpStatus.OK);
    }

    @GetMapping("/getMisDetails/{email}")
    public ResponseEntity<?>  getMisDetails(@PathVariable("email") String email) {
        List<MisRequestDto> misRequestDtoList = service.getMisDetailsByEmail(email);
        if(misRequestDtoList == null) return new ResponseEntity<>("user inactive list not found", HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(misRequestDtoList,HttpStatus.OK);

    }

    @GetMapping("/getTransactionDetails/{email}")
    public ResponseEntity<?>  getTransactionDetails(@PathVariable("email") String email) {
        List<Transactions> transactions = service.getTransactionsDetailsByEmail(email);
        if(transactions == null) return new ResponseEntity<>("user inactive list not found", HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(transactions,HttpStatus.OK);

    }

    @GetMapping("/getLoanDetails/{email}")
    public ResponseEntity<?>  getLoanDetails(@PathVariable("email") String email) {
        List<LoanHistoryDto> loanHistoryDtos  = service.getLoanDetailsByEmail(email);
        if(loanHistoryDtos == null) return new ResponseEntity<>("user inactive list not found", HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(loanHistoryDtos,HttpStatus.OK);
    }

    @GetMapping("/getSchemeDetails/{email}")
    public ResponseEntity<?>  getSchemeDetails(@PathVariable("email") String email) {
        SchemeDto scheme  = service.getSchemeDetailsByEmail(email);
        if(scheme == null) return new ResponseEntity<>("user inactive list not found", HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(scheme, HttpStatus.OK);
    }
}
