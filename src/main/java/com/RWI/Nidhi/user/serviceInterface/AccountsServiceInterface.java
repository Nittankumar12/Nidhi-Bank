package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.BankRequestDTO;
import com.RWI.Nidhi.enums.Status;
import org.springframework.http.ResponseEntity;

public interface AccountsServiceInterface {
    ResponseEntity<?> checkAccount(String email);

    ResponseEntity<?> openAccount(String email);

    double amountCalc(double commissionRate, double amount);

    Status checkAccountStatus(String accountNumber);

    Boolean CheckAccStatus(String userEmail);

    double checkAccountBalanceByNumber(String accountNumber);

    String generateRandomAccountPIN();

    void updateAccountPIN(String accountNumber, String newPIN);

    void fundTransfer(String sourceAccountNumber, String destinationAccountNumber, double amount);

    void addBankUserDetails(BankRequestDTO bankDetails, String emailId);

    void addBalance(String accountNumber, double amount);

    Status checkAccountStatus(int accountId);

}
