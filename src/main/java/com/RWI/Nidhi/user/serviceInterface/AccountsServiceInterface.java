package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.BankDetailsDTO;
import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.enums.Status;

public interface AccountsServiceInterface {
    Boolean schemeRunning(int accountId);

    Accounts openAccount(String email);

    Status getAccountStatus(int accountId);

    double checkAccountBalanceByNumber(String accountNumber);

    String generateRandomAccountPIN();

    void updateAccountPIN(String accountNumber, String newPIN);

    void fundTransfer(String sourceAccountNumber, String destinationAccountNumber, double amount);

    void addBankUserDetails(BankDetailsDTO bankDetails, String emailId);




}
