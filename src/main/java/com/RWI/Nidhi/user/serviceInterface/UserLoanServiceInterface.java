package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.LoanDto;
import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.enums.LoanStatus;

public interface UserLoanServiceInterface {
    // int maxApplicableLoan(int accNo);
    void applyLoan(LoanDto loanDto);
    Boolean checkForLoan(String email);
}
