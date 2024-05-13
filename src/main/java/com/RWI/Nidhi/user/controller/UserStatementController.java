package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.*;
import com.RWI.Nidhi.entity.Transactions;
import com.RWI.Nidhi.user.serviceImplementation.UserStatementServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<FdRequestDto> getFdDetails(@PathVariable("email") String email) {
        return service.getFixedDepositDetailsByEmail(email);
    }

    @GetMapping("/getRdDetails/{email}")
    public List<RdRequestDto> getRdDetails(@PathVariable("email") String email) {
        return service.getRecurringDepositDetailsByEmail(email);
    }

    @GetMapping("/getMisDetails/{email}")
    public List<MisRequestDto> getMisDetails(@PathVariable("email") String email) {
        return service.getMisDetailsByEmail(email);
    }

    @GetMapping("/getTransactionDetails/{email}")
    public List<Transactions> getTransactionDetails(@PathVariable("email") String email) {
        return service.getTransactionsDetailsByEmail(email);
    }

    @GetMapping("/getLoanDetails/{email}")
    public List<LoanHistoryDto> getLoanDetails(@PathVariable("email") String email) {
        return service.getLoanDetailsByEmail(email);
    }

    @GetMapping("/getSchemeDetails/{email}")
    public List<SchemeDto> getSchemeDetails(@PathVariable("email") String email) {
        return service.getSchemeDetailsByEmail(email);
    }
}
