package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.enums.Status;

public interface AccountsService {
    Boolean schemeRunning(int accountId);

    Accounts openAccount();

    Status getAccountStatus(int accountId);

    double checkAccountBalanceByNumber(String accountNumber);


}
