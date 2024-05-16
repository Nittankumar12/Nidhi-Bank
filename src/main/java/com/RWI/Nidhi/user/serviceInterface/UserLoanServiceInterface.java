package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.*;
import com.RWI.Nidhi.enums.LoanType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface UserLoanServiceInterface {
    double maxApplicableLoan(String email);

    Boolean checkForExistingLoan(String email);

    Boolean checkForLoanBound(String email, double principalLoanAmount);

    double calculateFirstPayableAmount(LoanCalcDto loanCalcDto);

    double calculateEMI(LoanCalcDto loanCalcDto);

    void applyLoan(LoanApplyDto loanApplyDto);
    MonthlyEmiDto payEMI(String email);

    LocalDate firstDateOfNextMonth(LocalDate date);
    LocalDate calcFirstEMIDate(LocalDate startDate);
    ResponseEntity<?> getLoanInfo(String email);

    ResponseEntity<?> getLoanClosureDetails(String email);

    ResponseEntity<?> applyForLoanClosure(String email);
    ResponseEntity<?> findRateByLoanType(LoanType loanType);

    //Prince
    List<LoanHistoryDto> getLoansByLoanType(String loanType);

    List<LoanHistoryDto> getLoansByLoanStatus(String status);

}
