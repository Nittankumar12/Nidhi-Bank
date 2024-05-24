package com.RWI.Nidhi.user.serviceInterface;
import com.RWI.Nidhi.dto.MonthlyEmiDto;
import com.RWI.Nidhi.dto.SchLoanCalcDto;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface UserSchemeLoanServiceInterface {
    ResponseEntity<?> schemeLoan(String email);//maxLoan for scheme

    Boolean checkForExistingLoan(String email);

    ResponseEntity<?> applySchemeLoan(String email); // apply for schemeLoan

    double calculateFirstPayableSchLoanAmount(SchLoanCalcDto schLoanCalcDto);

    double calculateSchLoanEMI(SchLoanCalcDto schLoanCalcDto);
    MonthlyEmiDto payEMI(String email);
    LocalDate firstDateOfNextMonth(LocalDate date);
    LocalDate calcFirstEMIDate(LocalDate startDate);

    ResponseEntity<?> getLoanInfo(String email);
    ResponseEntity<?> getLoanClosureDetails(String email);
    ResponseEntity<?> applyForLoanClosure(String email);
}