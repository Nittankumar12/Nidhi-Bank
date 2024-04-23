package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.enums.LoanStatus;

public interface UserLoanServiceInterface {
    void applyLoan(Loan loan);
    LoanStatus checkLoanStatus(int loanId);
    int checkCurrentEMI(int loanId);
}
