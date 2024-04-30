package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.LoanDto;
import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.enums.LoanStatus;

public interface UserLoanServiceInterface {
    double maxApplicableLoan(String email);
    void applyLoan(LoanDto loanDto);
    Boolean checkForExistingLoan(String email);
    public Boolean checkForLoanBound(String email, int principalLoanAmount);
    LoanDto getLoanInfo(String email);
}
