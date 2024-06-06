package com.RWI.Nidhi.user.serviceInterface;


import com.RWI.Nidhi.dto.LoanSchApplyDto;
import com.RWI.Nidhi.dto.SchemeApplyDTO;
import org.springframework.http.ResponseEntity;

public interface SchemeServiceInterface {
    int findSchemeRemainingDays(int schemeId);

    double findLoanOnSchemeBasis(int schemeId);

    ResponseEntity<?> addScheme(SchemeApplyDTO schemeApplyDTO);

    ResponseEntity<?> getSchemeInfo(String email);

    ResponseEntity<?> monthlyDeposit(String email);

    ResponseEntity<?> getSchemeLoanInfo(String email);

    ResponseEntity<?> applyForSchemeLoan(LoanSchApplyDto loanSchApplyDto);
}
