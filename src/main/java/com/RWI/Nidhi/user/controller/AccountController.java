package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.enums.Status;
import com.RWI.Nidhi.user.serviceInterface.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {
	@Autowired
	private AccountsService accountsService;

	// End point to open a new account
	@PostMapping("/open")
	public Accounts openAccount() {
		return accountsService.openAccount();
	}

	// End point to get account status
	@GetMapping("/status/{accountId}")
	public Status getAccountStatus(@PathVariable int accountId) {
		return accountsService.getAccountStatus(accountId);
	}

	// End point to get account balance
	@GetMapping("/balance/{accountNumber}")
	public double checkAccountBalanceByNumber(@PathVariable String accountNumber) {
		return accountsService.checkAccountBalanceByNumber(accountNumber);
	}

	@PutMapping("")
	public ResponseEntity<String> updateAccountPin(@RequestParam String accountNumber, @RequestParam String newPin) {
		accountsService.updateAccountPIN(accountNumber, newPin);
		return ResponseEntity.ok("Account PIN updated successfully");
	}

}
