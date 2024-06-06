package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.BankRequestDTO;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.Status;
import com.RWI.Nidhi.exception.AccountIdNotFoundException;
import com.RWI.Nidhi.user.serviceInterface.AccountsServiceInterface;
import com.RWI.Nidhi.user.serviceInterface.UserService;
import com.twilio.rest.api.v2010.Account;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountsServiceInterface accountsServiceInterface;

    // End point to open a new account
    @GetMapping("/open-account")
    public ResponseEntity<?> openAccount(@RequestParam String email) {
        return ResponseEntity.ok(accountsServiceInterface.openAccount(email));
    }

    // End point to get account status
    @GetMapping("/accountStatus/{accountNumber}")
    public ResponseEntity<Status> checkAccountStatus(@PathVariable String accountNumber) {
        try {
            Status status = accountsServiceInterface.checkAccountStatus(accountNumber);
            return ResponseEntity.ok(status);
        } catch (AccountIdNotFoundException e) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).build();
        }
    }

    // End point to get account balance
    @GetMapping("/balance/{accountNumber}")
    public double checkAccountBalanceByNumber(@PathVariable String accountNumber) {
        return accountsServiceInterface.checkAccountBalanceByNumber(accountNumber);
    }

    @PutMapping("/updateAccountPin")
    public ResponseEntity<String> updateAccountPin(@RequestParam String accountNumber, @RequestParam String newPin) {
        accountsServiceInterface.updateAccountPIN(accountNumber, newPin);
        return ResponseEntity.ok("Account PIN updated successfully");
    }

    @PutMapping("/fundTransfer")
    public String fundTransfer(@RequestParam String sourceAccountNumber, @RequestParam String destinationAccountNumber,
                               double amount) {
        accountsServiceInterface.fundTransfer(sourceAccountNumber, destinationAccountNumber, amount);
        return "Money Transfer Done";
    }

    @PostMapping("/addBankDetails")
    public ResponseEntity<String> addBankUserDetails(@RequestBody BankRequestDTO bankDto,
                                                     @RequestParam String emailId) {
        try {
            accountsServiceInterface.addBankUserDetails(bankDto, emailId);
            return ResponseEntity.ok("Bank details added successfully");
        } catch (Exception e) {
            return ResponseEntity.ok("Bank details not  added");
        }
    }

    @PostMapping("/addBalance")
    public ResponseEntity<String> addBalance(@RequestParam String accountNumber, @RequestParam double amount) {
        try {
            accountsServiceInterface.addBalance(accountNumber, amount);
            return ResponseEntity.ok("Balance added successfully");
        } catch (AccountIdNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error occurred while adding balance to the account");
        }
    }
}
