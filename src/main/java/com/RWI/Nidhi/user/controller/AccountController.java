package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.enums.Status;
import com.RWI.Nidhi.user.serviceInterface.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountsService accountsService;

    // Endpoint to open a new account
    @PostMapping("/open")
    public Accounts openAccount() {
        return accountsService.openAccount();
    }

    // Endpoint to get account status
    @GetMapping("/status/{accountId}")
    public Status getAccountStatus(@PathVariable int accountId) {
        return accountsService.getAccountStatus(accountId);
    }
    // Endpoint to get account balance
    @GetMapping("/balance/{accountNumber}")
    public double checkAccountBalanceByNumber(@PathVariable String accountNumber) {
        return accountsService.checkAccountBalanceByNumber(accountNumber);
    }
}
