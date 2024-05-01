package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.LoanDto;

public interface UserLoanServiceInterface {
    double maxApplicableLoan(String email);
    void applyLoan(LoanDto loanDto);
    Boolean checkForExistingLoan(String email);
    Boolean checkForLoanBound(String email, double principalLoanAmount);
    LoanDto getLoanInfo(String email);
    double calculatePayableAmount(LoanDto loanDto);
    double calculateEMI(LoanDto loanDto);
    Boolean payEMI(String email, double payedAmount);
}
