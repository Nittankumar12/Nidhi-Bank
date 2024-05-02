package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.SchLoanCalcDto;

public interface UserSchemeLoanServiceInterface {
    double schemeLoan(String email);//maxLoan for scheme
    void applySchemeLoan(String email); // apply for schemeLoan
    double calculatePayableSchLoanAmount(SchLoanCalcDto schLoanCalcDto);
    double calculateSchLoanEMI(SchLoanCalcDto schLoanCalcDto);
    Boolean checkForExistingLoan(String email);
}
