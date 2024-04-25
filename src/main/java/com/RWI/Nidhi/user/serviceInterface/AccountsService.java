package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.enums.Status;

public interface AccountsService {
    Boolean schemeRunning(int accountId);
    public  Accounts openAccount();

	public  Status getAccountStatus(int accountId);

	public double checkAccountBalanceByNumber(String accountNumber);
}
