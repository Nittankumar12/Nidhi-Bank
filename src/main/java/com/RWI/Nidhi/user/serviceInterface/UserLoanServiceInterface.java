package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface UserLoanServiceInterface {
    double maxApplicableLoan(String email);

    Boolean checkForExistingLoan(String email);

    Boolean checkForLoanBound(String email, double principalLoanAmount);

    double calculateFirstPayableAmount(LoanCalcDto loanCalcDto);

    double calculateEMI(LoanCalcDto loanCalcDto);

    void applyLoan(LoanApplyDto loanApplyDto);

    LoanInfoDto getLoanInfo(String email);

    MonthlyEmiDto payEMI(String email);

    LoanClosureDto getLoanClosureDetails(String email);

    LocalDate firstDateOfNextMonth(LocalDate date);

    String applyForLoanClosure(String email);

    LocalDate calcFirstEMIDate(LocalDate startDate);

    //Prince
    List<LoanHIstoryDTO> getLoansByLoanType(String loanType);

    List<LoanHIstoryDTO> getLoansByLoanStatus(String status);
}
