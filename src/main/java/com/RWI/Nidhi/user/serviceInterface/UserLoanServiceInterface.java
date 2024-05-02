package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.*;

import java.time.LocalDate;

public interface UserLoanServiceInterface {
    double maxApplicableLoan(String email);
    Boolean checkForExistingLoan(String email);
    Boolean checkForLoanBound(String email, double principalLoanAmount);
    LoanInfoDto getLoanInfo(String email);
    double calculatePayableAmount(LoanCalcDto loanCalcDto);
    double calculateEMI(LoanCalcDto loanCalcDto);
    void applyLoan(LoanApplyDto loanApplyDto);
    MonthlyEmiDto payEMI(String email);
    LoanClosureDto getLoanClosureDetails(String email);
    LocalDate firstDateOfNextMonth(LocalDate date);
    String applyForLoanClosure(String email);
}
