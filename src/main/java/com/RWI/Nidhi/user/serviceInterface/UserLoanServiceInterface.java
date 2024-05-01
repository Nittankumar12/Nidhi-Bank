package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.LoanApplyDto;
import com.RWI.Nidhi.dto.LoanCalcDto;
import com.RWI.Nidhi.dto.LoanInfoDto;
import com.RWI.Nidhi.dto.MonthlyEmiDto;

public interface UserLoanServiceInterface {
    double maxApplicableLoan(String email);
    Boolean checkForExistingLoan(String email);
    Boolean checkForLoanBound(String email, double principalLoanAmount);
    LoanInfoDto getLoanInfo(String email);
    double calculatePayableAmount(LoanCalcDto loanCalcDto);
    double calculateEMI(LoanCalcDto loanCalcDto);
    void applyLoan(LoanApplyDto loanApplyDto);
    MonthlyEmiDto payEMI(String email);
}
