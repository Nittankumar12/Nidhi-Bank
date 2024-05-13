package com.RWI.Nidhi.user.serviceInterface;


import com.RWI.Nidhi.dto.SchemeApplyDTO;
import org.springframework.http.ResponseEntity;

public interface SchemeServiceInterface {
    int findSchemeRemainingDays(int schemeId);

    double findLoanOnSchemeBasis(int schemeId);
    ResponseEntity<?> addScheme(SchemeApplyDTO schemeApplyDTO);
    ResponseEntity<?> depositMonthlySchPay(String email);
}
