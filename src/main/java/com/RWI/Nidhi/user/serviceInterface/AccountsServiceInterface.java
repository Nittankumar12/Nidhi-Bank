package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.AccountResponseDTO;

import com.RWI.Nidhi.dto.BankRequestDTO;

import com.RWI.Nidhi.enums.Status;
import org.springframework.http.ResponseEntity;

public interface AccountsServiceInterface {

	ResponseEntity<?> openAccount(String email);
	double amountCalc(double commissionRate, double amount);

	Status checkAccountStatus(String accountNumber);
	Boolean CheckAccStatus(String userEmail);

	double checkAccountBalanceByNumber(String accountNumber);

	public String generateRandomAccountPIN();

	public void updateAccountPIN(String accountNumber, String newPIN);

	public void fundTransfer(String sourceAccountNumber, String destinationAccountNumber, double amount);

	public void addBankUserDetails(BankRequestDTO bankDetails, String emailId);

	public void addBalance(String accountNumber, double amount);

	Status checkAccountStatus(int accountId);

}
