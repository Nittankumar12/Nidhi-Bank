package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.UserResponse;
import com.RWI.Nidhi.entity.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

public interface UserDetailsService {
//    UserResponse getUserDetailsByUserId(int userId);
    Loan getLoanDetailsByUserId(int userId) throws UserPrincipalNotFoundException;
    List<Transactions> getTransactionsDetailsByUserId(int userId) throws UserPrincipalNotFoundException;
    List<FixedDeposit> getFixedDepositDetailsByUserId(int userId) throws UserPrincipalNotFoundException;
    List<RecurringDeposit> getRecurringDepositDetailsByUserId(int userId) throws UserPrincipalNotFoundException;
    List<MIS> getMisByDetailsUserId(int userId) throws UserPrincipalNotFoundException;
    List<Scheme> getSchemeByDetailsUserId(int userId) throws UserPrincipalNotFoundException;
}
