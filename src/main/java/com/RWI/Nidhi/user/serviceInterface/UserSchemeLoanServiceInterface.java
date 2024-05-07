package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.LoanClosureDto;
import com.RWI.Nidhi.dto.LoanInfoDto;
import com.RWI.Nidhi.dto.MonthlyEmiDto;
import com.RWI.Nidhi.dto.SchLoanCalcDto;

import java.time.LocalDate;

public interface UserSchemeLoanServiceInterface {
    double schemeLoan(String email);//maxLoan for scheme

    Boolean checkForExistingLoan(String email);

    void applySchemeLoan(String email); // apply for schemeLoan

    double calculateFirstPayableSchLoanAmount(SchLoanCalcDto schLoanCalcDto);

    double calculateSchLoanEMI(SchLoanCalcDto schLoanCalcDto);

    LoanInfoDto getLoanInfo(String email);

    MonthlyEmiDto payEMI(String email);

    LoanClosureDto getLoanClosureDetails(String email);

    LocalDate firstDateOfNextMonth(LocalDate date);

    String applyForLoanClosure(String email);

    LocalDate calcFirstEMIDate(LocalDate startDate);
}