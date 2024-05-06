package com.RWI.Nidhi.user.controller;

import com.RWI.Nidhi.dto.BankDetailsDTO;
import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.enums.Status;
import com.RWI.Nidhi.user.serviceInterface.AccountsServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {
	@Autowired
	private AccountsServiceInterface accountsServiceInterface;

	// End point to open a new account
	@GetMapping("/open")
	public ResponseEntity<Accounts> openAccount(@RequestParam String email) {

		Accounts newAccount = accountsServiceInterface.openAccount(email);
		return ResponseEntity.ok(newAccount);

	}

	// End point to get account status
	@GetMapping("/status/{accountId}")
	public Status getAccountStatus(@PathVariable int accountId) {
		return accountsServiceInterface.getAccountStatus(accountId);
	}

	// End point to get account balance
	@GetMapping("/balance/{accountNumber}")
	public double checkAccountBalanceByNumber(@PathVariable String accountNumber) {
		return accountsServiceInterface.checkAccountBalanceByNumber(accountNumber);
	}

	@PutMapping("")
	public ResponseEntity<String> updateAccountPin(@RequestParam String accountNumber, @RequestParam String newPin) {
		accountsServiceInterface.updateAccountPIN(accountNumber, newPin);
		return ResponseEntity.ok("Account PIN updated successfully");
	}

	@PutMapping("/")
	public String fundTransfer(@RequestParam String sourceAccountNumber, @RequestParam String destinationAccountNumber,
			double amount) {
		accountsServiceInterface.fundTransfer(sourceAccountNumber, destinationAccountNumber, amount);
		return "Money Transfer Done";
	}

	@PostMapping("/addBankDetails")
	public ResponseEntity<String> addBankUserDetails(@RequestBody BankDetailsDTO bankDto,
													 @RequestParam String emailId) {
		try {
			accountsServiceInterface.addBankUserDetails(bankDto, emailId);
			return ResponseEntity.ok("Bank details added successfully");
		} catch (Exception e) {
			return ResponseEntity.ok("Bank details not  added");
		}
	}

}
